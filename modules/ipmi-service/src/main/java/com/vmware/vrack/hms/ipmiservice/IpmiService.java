/* ********************************************************************************
 * IpmiService.java
 * 
 * Copyright (C) 2014-2016 VMware, Inc. - All rights reserved.
 *
 * *******************************************************************************/
package com.vmware.vrack.hms.ipmiservice;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import com.vmware.vrack.hms.common.boardvendorservice.resource.ServiceHmsNode;
import com.vmware.vrack.hms.common.boardvendorservice.resource.ServiceServerNode;
import com.vmware.vrack.hms.common.resource.AcpiPowerState;
import com.vmware.vrack.hms.common.resource.BmcUser;
import com.vmware.vrack.hms.common.resource.SelfTestResults;
import com.vmware.vrack.hms.common.resource.SystemBootOptions;
import com.vmware.vrack.hms.common.resource.chassis.ChassisIdentifyOptions;
import com.vmware.vrack.hms.common.resource.fru.EntityId;
import com.vmware.vrack.hms.common.resource.fru.SensorType;
import com.vmware.vrack.hms.common.resource.sel.SelFetchDirection;
import com.vmware.vrack.hms.common.resource.sel.SelInfo;
import com.vmware.vrack.hms.common.resource.sel.SelRecord;
import com.vmware.vrack.hms.common.servernodes.api.ServerNodeInfo;
import com.vmware.vrack.hms.task.ipmi.IpmiTaskConnector;

/**
 * Interface for Ipmi Service that will service all ipmi requests to ipmi-service.
 * 
 * @author Vmware
 */
public interface IpmiService
{
    public boolean coldResetServer( ServiceHmsNode serviceHmsNode )
        throws Exception;

    public boolean coldResetServer( ServiceHmsNode serviceHmsNode, IpmiTaskConnector ipmiConnector )
        throws Exception;

    public boolean getServerPowerStatus( ServiceHmsNode serviceHmsNode )
        throws Exception;

    public boolean getServerPowerStatus( ServiceHmsNode serviceHmsNode, IpmiTaskConnector ipmiConnector )
        throws Exception;

    public boolean isHostAvailable( ServiceServerNode serviceHmsNode )
        throws Exception;

    public boolean isHostAvailable( ServiceHmsNode serviceHmsNode, IpmiTaskConnector ipmiConnector )
        throws Exception;

    public boolean powerCycleServer( ServiceHmsNode serviceHmsNode )
        throws Exception;

    public boolean powerCycleServer( ServiceHmsNode serviceHmsNode, IpmiTaskConnector ipmiConnector )
        throws Exception;

    public boolean powerDownServer( ServiceHmsNode serviceHmsNode )
        throws Exception;

    public boolean powerDownServer( ServiceHmsNode serviceHmsNode, IpmiTaskConnector ipmiConnector )
        throws Exception;

    public boolean powerResetServer( ServiceHmsNode serviceHmsNode )
        throws Exception;

    public boolean powerResetServer( ServiceHmsNode serviceHmsNode, IpmiTaskConnector ipmiConnector )
        throws Exception;

    public boolean powerUpServer( ServiceHmsNode serviceHmsNode )
        throws Exception;

    public boolean powerUpServer( ServiceHmsNode serviceHmsNode, IpmiTaskConnector ipmiConnector )
        throws Exception;

    public SelfTestResults selfTest( ServiceHmsNode serviceHmsNode )
        throws Exception;

    public SelfTestResults selfTest( ServiceHmsNode serviceHmsNode, IpmiTaskConnector ipmiConnector )
        throws Exception;

    public AcpiPowerState getAcpiPowerState( ServiceHmsNode serviceHmsNode )
        throws Exception;

    public AcpiPowerState getAcpiPowerState( ServiceHmsNode serviceHmsNode, IpmiTaskConnector ipmiConnector )
        throws Exception;

    public List<BmcUser> getBmcUsers( ServiceHmsNode serviceHmsNode )
        throws Exception;

    public List<BmcUser> getBmcUsers( ServiceHmsNode serviceHmsNode, IpmiTaskConnector ipmiConnector )
        throws Exception;

    public SystemBootOptions getBootOptions( ServiceHmsNode serviceHmsNode )
        throws Exception;

    public SystemBootOptions getBootOptions( ServiceHmsNode serviceHmsNode, IpmiTaskConnector ipmiConnector )
        throws Exception;

    public List<Object> getFruInfo( ServiceHmsNode serviceHmsNode )
        throws Exception;

    public List<Object> getFruInfo( ServiceHmsNode serviceHmsNode, Integer fruReadPacketSize )
        throws Exception;

    public List<Object> getFruInfo( ServiceHmsNode serviceHmsNode, IpmiTaskConnector ipmiConnector,
                                    Integer fruReadPacketSize )
                                        throws Exception;

    public String getMacAddress( ServiceHmsNode serviceHmsNode )
        throws Exception;

    public String getMacAddress( ServiceHmsNode serviceHmsNode, IpmiTaskConnector ipmiConnector )
        throws Exception;

    public boolean setBootOptions( ServiceHmsNode serviceHmsNode, SystemBootOptions bootOptions )
        throws Exception;

    public boolean setBootOptions( ServiceHmsNode serviceHmsNode, IpmiTaskConnector ipmiConnector,
                                   SystemBootOptions bootOptions )
                                       throws Exception;

    public List<Map<String, String>> getSensorData( ServiceHmsNode serviceHmsNode )
        throws Exception;

    public List<Map<String, String>> getSensorData( ServiceHmsNode serviceHmsNode, List<Integer> listSensorNumber )
        throws Exception;

    public List<Map<String, String>> getSensorDataForSensorTypeAndEntity( ServiceHmsNode node,
                                                                          List<SensorType> typeList,
                                                                          List<EntityId> entityList )
                                                                              throws Exception;
    // public List<Map<String, String>> getSensorData(ServiceHmsNode serviceHmsNode, Integer headerSize, Integer
    // initialChunkSize, Integer chunkSize) throws Exception;

    // public List<Map<String, String>> getSensorData(ServiceHmsNode serviceHmsNode, IpmiTaskConnector connector,
    // Integer headerSize, Integer initialChunkSize, Integer chunkSize) throws Exception;
    public List<Map<String, String>> getSensorData( ServiceHmsNode serviceHmsNode, Integer headerSize,
                                                    Integer initialChunkSize, Integer chunkSize,
                                                    List<Integer> listSensorNumber )
                                                        throws Exception;

    public List<Map<String, String>> getSensorData( ServiceHmsNode serviceHmsNode, IpmiTaskConnector connector,
                                                    Integer headerSize, Integer initialChunkSize, Integer chunkSize,
                                                    List<Integer> listSensorNumber )
                                                        throws Exception;

    public ServerNodeInfo getServerInfo( ServiceHmsNode serviceHmsNode )
        throws Exception;

    public ServerNodeInfo getServerInfo( ServiceHmsNode serviceHmsNode, Integer fruReadPacketSize )
        throws Exception;

    public ServerNodeInfo getServerInfo( ServiceHmsNode serviceHmsNode, Integer fruReadPacketSize,
                                         ArrayList<Integer> fruList )
                                             throws Exception;

    public ServerNodeInfo getServerInfo( ServiceHmsNode serviceHmsNode, IpmiTaskConnector connector,
                                         Integer fruReadPacketSize )
                                             throws Exception;

    public ServerNodeInfo getServerInfo( ServiceHmsNode serviceHmsNode, IpmiTaskConnector connector,
                                         Integer fruReadPacketSize, ArrayList<Integer> fruList )
                                             throws Exception;

    public boolean performChassisIdentification( ServiceHmsNode serviceHmsNode,
                                                 ChassisIdentifyOptions chassisIdentifyOptions )
                                                     throws Exception;

    public boolean performChassisIdentification( ServiceHmsNode serviceHmsNode, IpmiTaskConnector ipmiConnector,
                                                 ChassisIdentifyOptions chassisIdentifyOptions )
                                                     throws Exception;

    public SelInfo getSelInfo( ServiceHmsNode serviceHmsNode )
        throws Exception;

    public SelInfo getSelInfo( ServiceHmsNode serviceHmsNode, IpmiTaskConnector ipmiConnector )
        throws Exception;

    public SelInfo getSelDetails( ServiceHmsNode serviceHmsNode )
        throws Exception;

    public SelInfo getSelDetails( ServiceHmsNode serviceHmsNode, List<SelRecord> selFilters )
        throws Exception;

    public SelInfo getSelDetails( ServiceHmsNode serviceHmsNode, SelFetchDirection direction,
                                  List<SelRecord> selFilters )
                                      throws Exception;

    public SelInfo getSelDetails( ServiceHmsNode node, Integer recordCount, SelFetchDirection direction )
        throws Exception;

    public SelInfo getSelDetails( ServiceHmsNode serviceHmsNode, Integer recordCount, SelFetchDirection direction,
                                  List<SelRecord> selFilters )
                                      throws Exception;

    public SelInfo getSelDetails( ServiceHmsNode serviceHmsNode, IpmiTaskConnector ipmiConnector, Integer recordCount,
                                  SelFetchDirection direction, List<SelRecord> selFilters )
                                      throws Exception;
    // public SelInfo getSelDetails(ServiceHmsNode serviceHmsNode, Integer startOffset, Integer endOffset) throws
    // Exception;
    // public SelInfo getSelDetails(ServiceHmsNode serviceHmsNode, IpmiTaskConnector ipmiConnector, Integer startOffset,
    // Integer endOffset) throws Exception;
}
