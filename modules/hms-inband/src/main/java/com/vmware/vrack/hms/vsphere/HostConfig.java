/* ********************************************************************************
 * HostConfig.java
 * 
 * Copyright (C) 2014-2016 VMware, Inc. - All rights reserved.
 *
 * *******************************************************************************/
package com.vmware.vrack.hms.vsphere;

/**
 * Created by Jeffrey Wang on 3/14/14.
 */
public class HostConfig
{
    private HostCredential hostCredential;

    private NetworkConfig vsanConfig;

    private NetworkConfig vMotionConfig;

    public HostConfig()
    {
    }

    public HostConfig( HostCredential hostCredential, NetworkConfig vsanConfig, NetworkConfig vMotionConfig )
    {
        this.hostCredential = hostCredential;
        this.vsanConfig = vsanConfig;
        this.vMotionConfig = vMotionConfig;
    }

    public HostCredential getHostCredential()
    {
        return hostCredential;
    }

    public void setHostCredential( HostCredential hostCredential )
    {
        this.hostCredential = hostCredential;
    }

    public NetworkConfig getVsanConfig()
    {
        return vsanConfig;
    }

    public void setVsanConfig( NetworkConfig vsanConfig )
    {
        this.vsanConfig = vsanConfig;
    }

    public NetworkConfig getVMotionConfig()
    {
        return vMotionConfig;
    }

    public void setVMotionConfig( NetworkConfig vMotionConfig )
    {
        this.vMotionConfig = vMotionConfig;
    }

    public static class NetworkConfig
    {
        private String name;

        private int vlandId;

        private boolean forVMotion;

        private boolean forFTLogging;

        private boolean forTrafficManagement;

        private IpAllocation ipAllocation;

        public NetworkConfig()
        {
        }

        public NetworkConfig( String name, int vlandId, boolean forVMotion, boolean forFTLogging,
                              boolean forTrafficManagement, IpAllocation ipAllocation )
        {
            this.name = name;
            this.vlandId = vlandId;
            this.forVMotion = forVMotion;
            this.forFTLogging = forFTLogging;
            this.forTrafficManagement = forTrafficManagement;
            this.ipAllocation = ipAllocation;
        }

        public String getName()
        {
            return name;
        }

        public void setName( String name )
        {
            this.name = name;
        }

        public int getVlandId()
        {
            return vlandId;
        }

        public void setVlandId( int vlandId )
        {
            this.vlandId = vlandId;
        }

        public boolean isForVMotion()
        {
            return forVMotion;
        }

        public void setForVMotion( boolean forVMotion )
        {
            this.forVMotion = forVMotion;
        }

        public boolean isForFTLogging()
        {
            return forFTLogging;
        }

        public void setForFTLogging( boolean forFTLogging )
        {
            this.forFTLogging = forFTLogging;
        }

        public boolean isForTrafficManagement()
        {
            return forTrafficManagement;
        }

        public void setForTrafficManagement( boolean forTrafficManagement )
        {
            this.forTrafficManagement = forTrafficManagement;
        }

        public IpAllocation getIpAllocation()
        {
            return ipAllocation;
        }

        public void setIpAllocation( IpAllocation ipAllocation )
        {
            this.ipAllocation = ipAllocation;
        }
    }
}