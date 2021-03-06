/*
 * Copyright (c) SiteWhere, LLC. All rights reserved. http://www.sitewhere.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.sitewhere.spi.microservice.spring;

/**
 * Spring bean names for device management configuration components.
 * 
 * @author Derek
 */
public class DeviceManagementBeans {

    /** Bean id for device mangement MongoDB client */
    public static final String BEAN_MONGODB_CLIENT = "mongoClient";

    /** Bean id for device management in server configuration */
    public static final String BEAN_DEVICE_MANAGEMENT = "deviceManagement";
}