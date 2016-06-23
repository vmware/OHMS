/* ********************************************************************************
 * SwitchInfoHelperUtil.java
 * 
 * Copyright (C) 2014-2016 VMware, Inc. - All rights reserved.
 *
 * *******************************************************************************/
package com.vmware.vrack.hms.common.util;

import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import com.vmware.vrack.hms.common.rest.model.SwitchInfo;
import com.vmware.vrack.hms.common.servernodes.api.ComponentIdentifier;
import com.vmware.vrack.hms.common.switches.GetSwitchResponse;
import com.vmware.vrack.hms.common.switches.api.SwitchHardwareInfo;

/*
* Helper class to convert the Switch Node object to the Switch Info object as per the FRU Model
* @author VMware Inc.
*/
public class SwitchInfoHelperUtil
{
    private static Logger logger = Logger.getLogger( SwitchInfoHelperUtil.class );

    public SwitchInfo convertSwitchNodeToSwitchInfo( GetSwitchResponse response )
    {
        SwitchInfo switchInfo = new SwitchInfo();
        SwitchHardwareInfo switchHardwareInfo = new SwitchHardwareInfo();
        Map<String, Object> osMap = new TreeMap<String, Object>();
        ComponentIdentifier switchComponentIdentifier = new ComponentIdentifier();
        FruIdGeneratorUtil fruIdGeneratorUtil = new FruIdGeneratorUtil();
        try
        {
            switchInfo.setOperational_status( String.valueOf( response.isPowered() ) );
            switchInfo.setDiscoverable( response.isDiscoverable() );
            switchInfo.setPowered( response.isPowered() );
            if ( switchInfo.isPowered() )
            {
                osMap = response.getNodeOsDetails();
                switchInfo.setFirmwareVersion( osMap.get( "firmwareVersion" ).toString() );
                switchInfo.setFirmwareName( osMap.get( "firmwareName" ).toString() );
                switchInfo.setOsName( osMap.get( "osName" ).toString() );
                switchInfo.setOsVersion( osMap.get( "osVersion" ).toString() );
                switchInfo.setLocation( response.getLocation() );
                switchHardwareInfo = response.getHardwareInfo();
                switchComponentIdentifier.setProduct( switchHardwareInfo.getModel() );
                switchComponentIdentifier.setSerialNumber( switchHardwareInfo.getChassisSerialId() );
                switchComponentIdentifier.setManufacturer( switchHardwareInfo.getManufacturer() );
                switchComponentIdentifier.setPartNumber( switchHardwareInfo.getPartNumber() );
                switchComponentIdentifier.setManufacturingDate( switchHardwareInfo.getManufactureDate() );
                switchInfo.setComponentIdentifier( switchComponentIdentifier );
                switchInfo.setFruId( String.valueOf( fruIdGeneratorUtil.generateFruIdHashCode( switchInfo.getComponentIdentifier(),
                                                                                               switchInfo.getLocation() ) ) );
                switchInfo.setManagementMacAddress( switchHardwareInfo.getManagementMacAddress() );
                switchInfo.setRole( response.getRole() );
                switchInfo.setIpAddress( response.getManagementIpAddress() );
                switchInfo.setMangementPort( response.getPort().toString() );
                switchInfo.setSwitchId( response.getSwitchId() );
                switchInfo.setSwitchPorts( response.getSwitchPortList() );
            }
            return switchInfo;
        }
        catch ( Exception e )
        {
            logger.error( " Error while converting the SwitchNode to SwitchInfo object for switch "
                + response.getSwitchId(), e );
        }
        return null;
    }
}
