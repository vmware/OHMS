/* ********************************************************************************
 * TransactionContext.java
 * 
 * Copyright © 2013 - 2016 VMware, Inc. All Rights Reserved.

 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, without warranties or
 * conditions of any kind, EITHER EXPRESS OR IMPLIED. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * *******************************************************************************/
package com.vmware.vrack.hms.vsphere;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: Tao Ma Date: 3/13/14
 */
public class TransactionContext
{
    private ThreadLocal<Map<String, String>> context = new ThreadLocal<Map<String, String>>()
    {
        @Override
        protected Map<String, String> initialValue()
        {
            return new HashMap<String, String>();
        }
    };

    private TransactionContext()
    {
    }

    private static class TransactionContextHolder
    {
        private static TransactionContext transactionContext = new TransactionContext();
    }

    public static TransactionContext getInstance()
    {
        return TransactionContextHolder.transactionContext;
    }

    public String get( String key )
    {
        return context.get().get( key );
    }

    public String set( String key, String value )
    {
        return context.get().put( key, value );
    }

    public void remove( String key )
    {
        context.get().remove( key );
    }

    @Override
    public String toString()
    {
        return String.valueOf( context.get() );
    }
}