/* ********************************************************************************
 * ColdResetBmcTask.java
 * 
 * Copyright (C) 2014-2016 VMware, Inc. - All rights reserved.
 *
 * *******************************************************************************/
package com.vmware.vrack.hms.task.oob.ipmi;

import org.apache.log4j.Logger;

import com.vmware.vrack.hms.boardservice.BoardServiceProvider;
import com.vmware.vrack.hms.boardservice.HmsPluginServiceCallWrapper;
import com.vmware.vrack.hms.common.boardvendorservice.api.IBoardService;
import com.vmware.vrack.hms.common.boardvendorservice.resource.ServiceServerNode;
import com.vmware.vrack.hms.common.exception.HmsException;
import com.vmware.vrack.hms.common.exception.HmsResourceBusyException;
import com.vmware.vrack.hms.common.notification.TaskResponse;
import com.vmware.vrack.hms.common.resource.PowerOperationAction;
import com.vmware.vrack.hms.common.servernodes.api.ServerNode;

/**
 * Cold Resets the BMC node.
 * 
 * @author Yagnesh Chawda
 */
public class ColdResetBmcTask
    extends IpmiTask
{
    private static Logger logger = Logger.getLogger( ColdResetBmcTask.class );

    public ServerNode node;

    public TaskResponse response;

    public ColdResetBmcTask( TaskResponse response )
    {
        this.response = response;
        this.node = (ServerNode) response.getNode();
    }

    @Override
    public void executeTask()
        throws Exception
    {
        try
        {
            ServiceServerNode serviceServerNode = (ServiceServerNode) node.getServiceObject();
            IBoardService boardService = BoardServiceProvider.getBoardService( serviceServerNode );
            if ( boardService != null )
            {
                // boolean status = boardService.coldResetServer(serviceServerNode);
                Object[] paramsArray = new Object[] { serviceServerNode, PowerOperationAction.COLDRESET };
                boolean status = HmsPluginServiceCallWrapper.invokeHmsPluginService( boardService, serviceServerNode,
                                                                                     "powerOperations", paramsArray );
            }
            else
            {
                throw new Exception( "Board Service is NULL for node:" + node.getNodeID() );
            }
        }
        catch ( HmsResourceBusyException e )
        {
            String error =
                String.format( "HMS Resource is Busy for the node [%s]. Please try after some time", node.getNodeID() );
            logger.debug( error, e );
            throw e;
        }
        catch ( Exception e )
        {
            logger.error( "Error while Cold Resetting Node:" + node.getNodeID(), e );
            throw new HmsException( "Error while Cold Resetting for Node:" + node.getNodeID(), e );
        }
    }
}