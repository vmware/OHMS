/* ********************************************************************************
 * SelInfoTask.java
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
import com.vmware.vrack.hms.common.resource.sel.SelInfo;
import com.vmware.vrack.hms.common.resource.sel.SelOption;
import com.vmware.vrack.hms.common.resource.sel.SelTask;
import com.vmware.vrack.hms.common.servernodes.api.ServerNode;

/**
 * Task to Get System Event Logs
 * 
 * @author Yagnesh Chawda
 */
public class SelInfoTask
    extends IpmiTask
{
    private static Logger logger = Logger.getLogger( SelInfoTask.class );

    public ServerNode node;

    public TaskResponse response;

    private SelOption preparedParameter;

    public SelInfoTask( TaskResponse response, Object preparedParameter )
    {
        this.response = response;
        this.node = (ServerNode) response.getNode();
        this.preparedParameter = (SelOption) preparedParameter;
    }

    public void executeTask()
        throws Exception
    {
        try
        {
            ServiceServerNode serviceServerNode = (ServiceServerNode) node.getServiceObject();
            IBoardService boardService = BoardServiceProvider.getBoardService( serviceServerNode );
            if ( boardService != null )
            {
                if ( preparedParameter.getSelTask() == null )
                {
                    preparedParameter.setSelTask( SelTask.SelInfo );
                }
                SelInfo selInfo = null;
                switch ( preparedParameter.getSelTask() )
                {
                    case SelDetails:
                        Object[] paramsArray = new Object[] { serviceServerNode, preparedParameter.getRecordCount(),
                            preparedParameter.getDirection() };
                        selInfo = HmsPluginServiceCallWrapper.invokeHmsPluginService( boardService, serviceServerNode,
                                                                                      "getSelDetails", paramsArray );
                        break;
                    default:
                        break;
                }
                this.node.setSelInfo( selInfo );
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
            logger.error( "Error while getting SEL Info for Node:" + node.getNodeID(), e );
            throw new HmsException( "Error while getting SEL Info for Node:" + node.getNodeID(), e );
        }
    }
}
