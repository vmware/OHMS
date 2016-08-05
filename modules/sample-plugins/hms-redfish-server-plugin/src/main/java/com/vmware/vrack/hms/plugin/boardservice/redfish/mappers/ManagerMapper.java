/*
 * Copyright (c) 2016 Intel Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.vmware.vrack.hms.plugin.boardservice.redfish.mappers;

import com.vmware.vrack.hms.plugin.boardservice.redfish.resources.ManagerResource;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class ManagerMapper
{

    public ManagerResource getManagementController( List<ManagerResource> managers )
        throws MappingException
    {
        List<ManagerResource> detectedManagementControllers = new ArrayList<>();
        for ( ManagerResource manager : managers )
        {
            if ( Objects.equals( manager.getManagerType(), ManagerResource.ManagerType.ManagementController ) )
            {
                detectedManagementControllers.add( manager );
            }
        }

        if ( detectedManagementControllers.size() != 1 )
        {
            throw new MappingException(
                "No Management Controller or multiple Management Controllers detected, unable to determine single MAC Address" );
        }
        return detectedManagementControllers.get( 0 );
    }
}
