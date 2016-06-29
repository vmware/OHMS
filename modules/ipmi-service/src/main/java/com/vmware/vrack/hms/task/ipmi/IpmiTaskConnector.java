/* ********************************************************************************
 * IpmiTaskConnector.java
 * 
 * Copyright (C) 2014-2016 VMware, Inc. - All rights reserved.
 *
 * *******************************************************************************/
package com.vmware.vrack.hms.task.ipmi;

import java.io.IOException;
import java.net.InetAddress;

import org.apache.log4j.Logger;

import com.veraxsystems.vxipmi.api.async.ConnectionHandle;
import com.veraxsystems.vxipmi.api.sync.IpmiConnector;
import com.veraxsystems.vxipmi.coding.commands.IpmiVersion;
import com.veraxsystems.vxipmi.coding.commands.PrivilegeLevel;
import com.veraxsystems.vxipmi.coding.commands.chassis.GetChassisStatus;
import com.veraxsystems.vxipmi.coding.commands.chassis.GetChassisStatusResponseData;
import com.veraxsystems.vxipmi.coding.commands.session.GetChannelAuthenticationCapabilities;
import com.veraxsystems.vxipmi.coding.commands.session.GetChannelAuthenticationCapabilitiesResponseData;
import com.veraxsystems.vxipmi.coding.commands.session.SessionCustomPayload;
import com.veraxsystems.vxipmi.coding.commands.session.SetSessionPrivilegeLevel;
import com.veraxsystems.vxipmi.coding.protocol.AuthenticationType;
import com.veraxsystems.vxipmi.coding.security.CipherSuite;
import com.veraxsystems.vxipmi.connection.ConnectionException;
import com.vmware.vrack.hms.ipmiservice.exception.IpmiServiceConnectionException;

/**
 * Ipmi Task Connector class to facilitate creation, destruction of IPMI connectors and session objects
 * 
 * @author Vmware
 */
public class IpmiTaskConnector
{
    public static Logger logger = Logger.getLogger( IpmiTaskConnector.class );

    private IpmiConnector connector = null;

    private String user = null;

    private String password = null;

    private ConnectionHandle handle = null;

    private CipherSuite cipherSuite = null;

    private String ipAddress = null;

    private int UDP_PORT = 0;

    private boolean encryptData = true;

    private SessionCustomPayload customSessionMessage = null;

    public IpmiTaskConnector( String ipAddress, String user, String password )
    {
        this.ipAddress = ipAddress;
        this.user = user;
        this.password = password;
    }

    public IpmiTaskConnector( String ipAddress, String user, String password, int UDP_PORT )
    {
        this( ipAddress, user, password );
        this.UDP_PORT = UDP_PORT;
    }

    public IpmiTaskConnector( String ipAddress, String user, String password, CipherSuite cipherSuite,
                              boolean encryptData, SessionCustomPayload customSessionMessage )
    {
        this( ipAddress, user, password );
        this.cipherSuite = cipherSuite;
        this.encryptData = encryptData;
        this.customSessionMessage = customSessionMessage;
    }

    public IpmiConnector getConnector()
    {
        return connector;
    }

    public ConnectionHandle getHandle()
    {
        return handle;
    }

    public CipherSuite getCipherSuite()
    {
        return cipherSuite;
    }

    public void createConnection( int cipherSuiteIndex )
        throws IOException, Exception, IpmiServiceConnectionException
    {
        logger.debug( "IpmiTaskConnector createConnection request received" );
        try
        {
            connector = new IpmiConnector( UDP_PORT );
            handle = connector.createConnection( InetAddress.getByName( ipAddress ) );
            connector.setConnectionEncryption( handle, encryptData, customSessionMessage );
            handle.setPrivilegeLevel( PrivilegeLevel.Administrator );
            if ( encryptData )
                cipherSuite = connector.getAvailableCipherSuites( handle ).get( cipherSuiteIndex );
            connector.getChannelAuthenticationCapabilities( handle, cipherSuite, PrivilegeLevel.Administrator );
            connector.openSession( handle, user, password, null );
        }
        catch ( ConnectionException e )
        {
            logger.error( "Exception while executing the Ipmi Task Connector...create connection failed" );
            logger.debug( e.getMessage() );
            destroy();
            throw new IpmiServiceConnectionException( e.getMessage() );
        }
    }

    /**
     * Check if session is Valid.
     *
     * @return true if session state is valid No heart-beat request is sent to BMC on this call, only Current CLient
     *         state is checked.
     */
    public boolean isSessionValid()
        throws Exception
    {
        return connector.isSessionValid( handle );
    }

    /**
     * Check if session is Responsive.
     *
     * @return true if session is responding to requests Power status check is performed to validate if session is
     *         responsive
     */
    public boolean isConnectionResponsive()
        throws Exception
    {
        if ( connector.isSessionValid( handle ) )
        {
            GetChassisStatusResponseData response =
                (GetChassisStatusResponseData) connector.sendMessage( handle,
                                                                      new GetChassisStatus( IpmiVersion.V20,
                                                                                            this.cipherSuite,
                                                                                            AuthenticationType.RMCPPlus ) );
            logger.debug( "response for isConnectionResponsive chassis PowerState is "
                + response.getCurrentPowerState() );
            return true;
        }
        else
            return false;
    }

    public void createSession()
        throws Exception
    {
        logger.debug( "Session valid [ " + connector.isSessionValid( handle ) + " ] for IP [ " + ipAddress
            + " ]. Thread [ " + Thread.currentThread().getName() + " ]" );
        if ( !connector.isSessionValid( handle ) )
        {
            connector.openSession( handle, user, password, null );
        }
        setSessionPrivilege();
    }

    /**
     * Set session Privilege level to Administrator.
     */
    public void setSessionPrivilege()
        throws Exception
    {
        connector.sendMessage( this.handle,
                               new SetSessionPrivilegeLevel( IpmiVersion.V20, this.cipherSuite,
                                                             AuthenticationType.RMCPPlus,
                                                             PrivilegeLevel.Administrator ) );
    }

    public void closeSession()
        throws IOException, Exception
    {
        if ( connector != null )
            connector.closeSession( handle );
    }

    public void destroy()
        throws Exception
    {
        if ( connector != null )
        {
            // Putting things in the explicit try blocks because, want to make sure that we attempt to teardown
            // connector
            try
            {
                connector.closeSession( handle );
                connector.closeConnection( handle );
            }
            catch ( Exception e )
            {
                logger.error( "Error closing session for ConnectionHandle [ " + handle + " ]", e );
            }
            finally
            {
                connector.tearDown();
            }
            connector = null;
        }
    }
}