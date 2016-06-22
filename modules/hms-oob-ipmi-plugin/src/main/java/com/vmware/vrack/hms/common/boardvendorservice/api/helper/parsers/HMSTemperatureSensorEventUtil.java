/* ********************************************************************************
 * HMSTemperatureSensorEventUtil.java
 * 
 * Copyright (C) 2014-2016 VMware, Inc. - All rights reserved.
 *
 * *******************************************************************************/
package com.vmware.vrack.hms.common.boardvendorservice.api.helper.parsers;

import com.veraxsystems.vxipmi.coding.commands.sdr.SensorState;
import com.vmware.vrack.hms.common.resource.fru.EntityId;
import com.vmware.vrack.hms.common.resource.fru.SensorType;
import com.vmware.vrack.hms.common.servernodes.api.event.NodeEvent;

/**
 * Represents state of threshold-based sensor.
 */
public class HMSTemperatureSensorEventUtil
{
    public static NodeEvent getHMSEvent( SensorState state, EntityId entity )
    {
        switch ( entity )
        {
            case Processor:
                return getProcessorThresholdEvents( state );
            case MemoryDevice:
            case MemoryModule:
                return getMemoryThresholdEvents( state );
            case Fan:
                return getFanThresholdEvents( state );
            default:
                return null;
        }
    }

    private static NodeEvent getProcessorThresholdEvents( SensorState state )
    {
        switch ( state )
        {
            case AboveUpperCritical:
            case AboveUpperNonRecoverable:
                return NodeEvent.CPU_TEMP_ABOVE_THRESHHOLD;
            case BelowLowerCritical:
            case BelowLowerNonRecoverable:
                return NodeEvent.CPU_TEMP_BELOW_THRESHHOLD;
            default:
                return null;
        }
    }

    private static NodeEvent getMemoryThresholdEvents( SensorState state )
    {
        switch ( state )
        {
            case AboveUpperCritical:
            case AboveUpperNonRecoverable:
                return NodeEvent.MEMORY_TEMP_ABOVE_THRESHOLD;
            default:
                return null;
        }
    }

    private static NodeEvent getFanThresholdEvents( SensorState state )
    {
        switch ( state )
        {
            case AboveUpperCritical:
            case BelowLowerCritical:
            case AboveUpperNonCritical:
            case BelowLowerNonCritical:
                return NodeEvent.FAN_SPEED_THRESHHOLD;
            case AboveUpperNonRecoverable:
            case BelowLowerNonRecoverable:
                return NodeEvent.FAN_STATUS_NON_RECOVERABLE;
            default:
                return null;
        }
    }

    public static NodeEvent getTemperatureReadingEvents( EntityId entity, SensorType sensorType )
    {
        if ( sensorType.equals( SensorType.Fan ) )
            return NodeEvent.FAN_SPEED;
        switch ( entity )
        {
            case Processor:
                return NodeEvent.CPU_TEMPERATURE;
            case MemoryDevice:
            case MemoryModule:
                return NodeEvent.MEMORY_TEMPERATURE;
            default:
                return null;
        }
    }
}
