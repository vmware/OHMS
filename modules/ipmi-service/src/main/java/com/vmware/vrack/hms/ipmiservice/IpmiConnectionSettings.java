/* ********************************************************************************
 * IpmiConnectionSettings.java
 * 
 * Copyright (C) 2014-2016 VMware, Inc. - All rights reserved.
 *
 * *******************************************************************************/
package com.vmware.vrack.hms.ipmiservice;

import com.veraxsystems.vxipmi.coding.commands.session.SessionCustomPayload;
import com.veraxsystems.vxipmi.coding.security.CipherSuite;
import com.vmware.vrack.hms.common.boardvendorservice.resource.ServiceServerNode;

public class IpmiConnectionSettings
{
    private ServiceServerNode node;

    private int cipherSuiteIndex;

    private boolean encryptData;

    private CipherSuite cipherSuite;

    private SessionCustomPayload sessionOpenPayload;

    public ServiceServerNode getNode()
    {
        return node;
    }

    public void setNode( ServiceServerNode node )
    {
        this.node = node;
    }

    public int getCipherSuiteIndex()
    {
        return cipherSuiteIndex;
    }

    public void setCipherSuiteIndex( int cipherSuiteIndex )
    {
        this.cipherSuiteIndex = cipherSuiteIndex;
    }

    public boolean isEncryptData()
    {
        return encryptData;
    }

    public void setEncryptData( boolean encryptData )
    {
        this.encryptData = encryptData;
    }

    public CipherSuite getCipherSuite()
    {
        return cipherSuite;
    }

    public void setCipherSuite( CipherSuite cipherSuite )
    {
        this.cipherSuite = cipherSuite;
    }

    public SessionCustomPayload getSessionOpenPayload()
    {
        return sessionOpenPayload;
    }

    public void setSessionOpenPayload( SessionCustomPayload sessionOpenPayload )
    {
        this.sessionOpenPayload = sessionOpenPayload;
    }

    @Override
    public int hashCode()
    {
        if ( node != null && node.getNodeID() != null )
            return node.getNodeID().hashCode();
        else
            return 0;
    }

    @Override
    public boolean equals( Object obj )
    {
        if ( obj == null )
            return false;
        if ( getClass() != obj.getClass() )
            return false;
        IpmiConnectionSettings other = (IpmiConnectionSettings) obj;
        if ( this.node.getNodeID().equals( other.node.getNodeID() ) )
            return true;
        else
            return false;
    }
}