/* ********************************************************************************
 * NodeRateLimitModel.java
 * 
 * Copyright © 2013 - 2016 VMware, Inc. All Rights Reserved.

 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, without warranties or
 * conditions of any kind, EITHER EXPRESS OR IMPLIED. see the License for the
 * specific language governing permissions and limitations under the License
 *
 * *******************************************************************************/
package com.vmware.vrack.hms.boardservice;

import java.util.concurrent.ScheduledExecutorService;

/**
 * This class is used to keep a ScheduledExecutorService and a ThreadPool for each node
 *
 * @author Vmware inc
 */
public class NodeRateLimitModel
{
    ThreadLimitExecuterServiceObjectPool threadLimitExecuterServiceObject;

    ScheduledExecutorService scheduledExecutorService;

    public ThreadLimitExecuterServiceObjectPool getThreadLimitExecuterServiceObject()
    {
        return threadLimitExecuterServiceObject;
    }

    public void setThreadLimitExecuterServiceObject( ThreadLimitExecuterServiceObjectPool threadLimitExecuterServiceObject )
    {
        this.threadLimitExecuterServiceObject = threadLimitExecuterServiceObject;
    }

    public ScheduledExecutorService getScheduledExecutorService()
    {
        return scheduledExecutorService;
    }

    public void setScheduledExecutorService( ScheduledExecutorService scheduledExecutorService )
    {
        this.scheduledExecutorService = scheduledExecutorService;
    }
}
