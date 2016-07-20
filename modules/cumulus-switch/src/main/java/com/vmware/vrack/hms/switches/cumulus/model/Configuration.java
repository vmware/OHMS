package com.vmware.vrack.hms.switches.cumulus.model;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Provides basic implementation for the object Configuration.
 *
 * Created by sankala on 12/5/14.
 */
public class Configuration {
    /** Set of variables for Configuration object such as bridges, switch ports, and switchPortMap */
    private static Pattern ifacePattern = Pattern
            .compile("(auto|iface) (swp.*|lo|eth.*|br.*|bond.*|bd-.*|poc.*)( *.*)$");
    private static Pattern bondPattern = Pattern.compile("(bond.*|bd-.*|poc.*)");
    public List<SwitchPort> switchPorts = new ArrayList<>();
    public List<Bridge> bridges = new ArrayList<>();
    public List<Svi> svis = new ArrayList<Svi>();
    private HashMap<String, ConfigBlock> switchPortMap = new HashMap<>();

    private static Logger logger = Logger.getLogger(Configuration.class);
 
    /**
     * Get string - output to display current config block details.
     * @return Return string stating file generated by Evo-rack HMS.
     */
    public String getString() {
        String retString = "# This file generated by EVO-RACK HMS. Please do not edit by hand. \n" +
                "# System will not function appropriately if modified by hand\n\n" +
                "source /etc/network/interfaces.d/hms-port*\n\n";
        for( ConfigBlock configBlock : switchPorts ) {
            retString = retString + configBlock.getString() + "\n\n";
        }

        for( ConfigBlock configBlock : svis ) {
            retString = retString + configBlock.getString() + "\n\n";
        }

        for( ConfigBlock configBlock : bridges ) {
            retString = retString + configBlock.getString() + "\n\n";
        }

        //retString += "# Source all HMS-related artifacts\n" +
        //        "source /etc/network/interfaces.d/hms-*\n";
        return retString;
    }

    /**
     * Add config block - based on configBlock provided, add to list of switchPorts or list of bridges. Store in the SwitchPort Map.
     *
     * @param configBlock
     */
    public void addConfigBlock(ConfigBlock configBlock) {
        if ( configBlock instanceof SwitchPort) {
            switchPorts.add( (SwitchPort) configBlock );
        }  else if ( configBlock instanceof Bridge) {
            bridges.add((Bridge) configBlock);
        } else if ( configBlock instanceof Svi) {
            svis.add( (Svi) configBlock);
        }
        switchPortMap.put(configBlock.name, configBlock);
    }

    /**
     * Remove config block based on name provided.
     *
     * @param name
     */
    public void removeConfigBlock(String name) {
        ConfigBlock configBlock = switchPortMap.get(name);
        boolean found = false;

        if (configBlock != null) {
            logger.debug("Removing config block with the name " + name);

            if (configBlock instanceof SwitchPort) {
                found = switchPorts.remove(configBlock);
            } else if (configBlock instanceof Bridge) {
                found = bridges.remove(configBlock);
            } else if (configBlock instanceof Svi) {
                found = svis.remove(configBlock);
            }

            if (found)
                switchPortMap.remove(name);
        }

        if (!found)
            logger.warn("Couldn't locate config block with name " + name + " to be deleted.");
    }

    /**
     * Get config block from switchPort map based on provided name.
     *
     * @param name
     * @return Config Block.
     */
    public ConfigBlock getConfigBlock( String name ) {
        return switchPortMap.get( name );
    }

    /**
     * Parsing api - based on input parameter, parse to determine if string starts with auto, iface, bond. Creates a configuration object.
     *
     * @param is input stream object
     * @return
     */
    public static Configuration parse(InputStream is) {
        Configuration configuration = new Configuration();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String aLine;

            ConfigBlock previousBlock = null;
            while ((aLine = br.readLine()) != null) {
                aLine = aLine.replaceFirst("\\s+$", "");
                if ( aLine.startsWith("#") || aLine.length() == 0 ) continue;

                Matcher matcher = ifacePattern.matcher(aLine);
                if ( matcher.matches() ) {
                    int count = matcher.groupCount();

                    if ( count > 1 && matcher.group(2).startsWith("br") ) {
                        if ( matcher.group(1).startsWith("auto") ) {
                            if (matcher.group(2).matches(".*\\.[0-9]+$")) {
                                String tokens[] = matcher.group(2).split("\\.");
                                Svi svi = new Svi();

                                if (tokens.length != 2) {
                                    return configuration; /* And also throw exception ? */
                                }

                                svi.name = tokens[0];
                                svi.pvid = tokens[1];
                                previousBlock = svi;
                                configuration.addConfigBlock(svi);
                                svi.parentConfig = configuration;
                            }
                            else {
                                Bridge bridge = new Bridge();
                                bridge.name = matcher.group(2);
                                previousBlock = bridge;
                                configuration.addConfigBlock(bridge);
                                bridge.parentConfig = configuration;
                            }
                        } else if ( count == 3 ) {
                            previousBlock.portConfig = matcher.group(3);
                        }
                    } else if ( count > 1 && matcher.group(1).startsWith("iface") ) {
                        if ( count == 3 )
                            previousBlock.portConfig = matcher.group(3);
                    } else if ( count > 1 ) {
                        SwitchPort switchPort = new SwitchPort();
                        Matcher matcherBond = bondPattern.matcher(matcher.group(2));
                        if (matcherBond.matches() && matcherBond.groupCount() == 1) {
                            switchPort = new Bond();
                        }
                        switchPort.name = matcher.group(2);
                        previousBlock = switchPort;
                        configuration.addConfigBlock(switchPort);
                        switchPort.parentConfig = configuration;
                    }
                } else {
                    if ( previousBlock != null )
                        previousBlock.addOtherConfig( aLine, configuration );
                }
            }

            return configuration;
        } catch(IOException ioe) {
            logger.error("Error while parsing interfaces file", ioe);
        }
        return null;
    }

    /**
     * Join collection of objects.
     *
     * @param collection
     * @param delimiter
     * @return
     */
    public static String joinCollection(Collection<?> collection, String delimiter) {
        String retString = "";
        for(Object object : collection) {
            retString += "" + object + delimiter;
        }

        return retString;
    }

    /**
     * Substitute one interface for a list of interfaces in all VLAN and LAG definitions. This is
     * typically called when deleting a LAG for example.
     *
     * @param from
     * @param toList
     */
    public void substituteInterface(String from, List<String> toList) {
        Bridge vRackBridge = getVlanAwareBridge();

        if (from == null || toList == null || toList.isEmpty())
            return;

        ConfigBlock fromBlock = getConfigBlock(from);
        if (!(fromBlock instanceof SwitchPort))
            return;

        logger.debug("Substituting interface " + from + " with " + joinCollection(toList, " "));

        List<SwitchPort> toPorts = getSwitchPortsFromNames(toList);

        /* Substitute interface in VLAN configuration(s) */
        if (vRackBridge != null) {
            boolean existed = vRackBridge.members.remove(fromBlock);
            if (existed) {
                for (SwitchPort subif : toPorts) {
                    boolean subExists = vRackBridge.members.contains(subif);
                    if (!subExists) {
                        vRackBridge.members.add(subif);
                        subif.parentBridge = vRackBridge;
                    }
                }
            }
        }

        /* Substitute interface in LAG configuration(s) */
        for (SwitchPort sp : switchPorts) {
            if (sp instanceof Bond) {
                Bond bond = (Bond) sp;
                boolean existed = bond.slaves.remove(fromBlock);
                if (existed) {
                    for (SwitchPort subif : toPorts) {
                        boolean subExists = bond.slaves.contains(subif);
                        if (!subExists) {
                            bond.slaves.add(subif);
                        }
                    }
                }
            }
        }
    }

    /**
     * Get Vlan Aware bridge
     *
     * @return bridge that is vlan aware
     */
    private Bridge getVlanAwareBridge() {
        return (bridges != null && !bridges.isEmpty()) ? bridges.get(0) : null;
    }


    /**
     * Convert a list of names (either port or lag name) to a list of SwitchPort elements.
     * Additionally, if a port has a LAG defined on it, this routine will return the LAG instead
     * of the port.
     *
     * @param ports
     * @return
     */
    public List<SwitchPort> convertToSwitchPorts(Collection<String> ports) {
        Bridge defaultParentBridge = null;
        List<SwitchPort> returnSwitchPorts = new ArrayList<>();

        if (bridges != null && bridges.size() > 0)
            defaultParentBridge = bridges.get(0);

        for( String taggedPort : ports ) {
            SwitchPort taggedSwitchPort = null;
            for( SwitchPort switchPort : switchPorts ) {
                if ( switchPort instanceof Bond) {
                    Bond bond = (Bond) switchPort;
                    if (switchPort.name.equals(taggedPort)) {
                    	taggedSwitchPort = bond;
                        break;
                    }
                    else {
                    	for( SwitchPort slave : bond.slaves ) {
                            if (slave.name.equals(taggedPort)) {
                                taggedSwitchPort = bond;
                                break;
                            }
                        }
                    }
                    
                    if (taggedSwitchPort != null) break;
                } else {
                    if ( switchPort.name.equals(taggedPort) ) {
                        taggedSwitchPort = switchPort;
                        break;
                    }
                }
            }

            if ( taggedSwitchPort == null ) {
                taggedSwitchPort = new SwitchPort();
                taggedSwitchPort.setParentBridge(defaultParentBridge); // no
                                                                       // port
                                                                       // is
                                                                       // orphan
                taggedSwitchPort.name = taggedPort;
            }

            returnSwitchPorts.add(taggedSwitchPort);
        }

        return returnSwitchPorts;
    }


    /**
     * Convert a list of names (either port or lag name) to a list of SwitchPort elements.
     * No additional processing is done.
     *
     * @param ports
     * @return
     */
    public List<SwitchPort> getSwitchPortsFromNames(Collection<String> ports) {
        List<SwitchPort> returnSwitchPorts = new ArrayList<>();

        for (String portName : ports) {
            SwitchPort port = null;
            ConfigBlock block = getConfigBlock(portName);

            if (block != null) {
                if (block instanceof SwitchPort)
                    port = (SwitchPort) block;
            } else {
                port = new SwitchPort();
                port.name = portName;
                switchPorts.add(port); /* TODO: This should be moved to parse() function. */
            }

            returnSwitchPorts.add(port);
        }

        return returnSwitchPorts;
    }


}