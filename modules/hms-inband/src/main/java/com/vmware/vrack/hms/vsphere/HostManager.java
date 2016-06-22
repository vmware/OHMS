/* ********************************************************************************
 * HostManager.java
 * 
 * Copyright (C) 2014-2016 VMware, Inc. - All rights reserved.
 *
 * *******************************************************************************/
package com.vmware.vrack.hms.vsphere;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vmware.vim.binding.vim.HostSystem;
import com.vmware.vim.binding.vmodl.ManagedObjectReference;

/**
 * Author: Tao Ma Date: 2/23/14
 */
public class HostManager
{
    private static final Logger logger = LoggerFactory.getLogger( HostManager.class );

    /*
     * Re-factor hard coded String 2014-08-06
     */
    private final String httpsUrl = "https://";

    private final String sdkPath = "/sdk";

    private final String failedToCreateVsphereClient =
        "Can't create VsphereClient instance for ESXi host %s, username %s.";

    private final String hostSystemType = "HostSystem";

    private final String hostSystemIsNull = "HostSystem is null or target is not an ESXi host";

    private HostManager()
    {
    }

    private static class HostManagerHolder
    {
        private static HostManager hostManager = new HostManager();
    }

    public static HostManager getInstance()
    {
        return HostManagerHolder.hostManager;
    }

    public HostProxy connect( HostCredential hostCredential )
    {
        return connect( hostCredential.getIpAddress(), hostCredential.getUsername(), hostCredential.getPassword() );
    }

    public HostProxy connect( String ipAddress, String username, String password )
    {
        logger.info( "Connecting to standalone host {}", ipAddress );
        try
        {
            VsphereClient client = VsphereClient.connect( httpsUrl + ipAddress + sdkPath, username, password );
            HostSystem host = getHostSystem( client );
            return new HostProxy( ipAddress, client, host );
        }
        catch ( Exception e )
        {
            throw new RuntimeException( String.format( failedToCreateVsphereClient, ipAddress, username ), e );
        }
    }

    private HostSystem getHostSystem( VsphereClient client )
    {
        ManagedObjectReference[] hostRefs =
            InventoryService.getInstance().findAll( client.getPropertyCollector(),
                                                    client.getContainerView( hostSystemType ), hostSystemType );
        if ( hostRefs.length == 1 )
            return client.createStub( HostSystem.class, hostRefs[0] );
        throw new NullPointerException( hostSystemIsNull );
    }
}
