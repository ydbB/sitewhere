/*
 * Copyright (c) SiteWhere, LLC. All rights reserved. http://www.sitewhere.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.sitewhere.web.configuration;

import com.sitewhere.spring.handler.DeviceCommunicationParser;
import com.sitewhere.spring.handler.EventSourcesParser;
import com.sitewhere.spring.handler.TenantConfigurationParser;
import com.sitewhere.spring.handler.TenantDatastoreParser;
import com.sitewhere.web.configuration.model.AttributeNode;
import com.sitewhere.web.configuration.model.AttributeType;
import com.sitewhere.web.configuration.model.ConfigurationModel;
import com.sitewhere.web.configuration.model.ElementNode;
import com.sitewhere.web.configuration.model.ElementRole;

/**
 * Specifies model used for tenant configuration user interface.
 * 
 * @author Derek
 */
public class TenantConfigurationModel extends ConfigurationModel {

	public TenantConfigurationModel() {
		setLocalName("tenant-configuration");
		setName("Tenant Configuration");
		setDescription("Provides a model for all aspects of tenant configuration.");
		getElements().add(createGlobals());
		getElements().add(createDataManagement());
		getElements().add(createDeviceCommunication());
		getElements().add(createInboundProcessingChain());
		getElements().add(createOutboundProcessingChain());
		getElements().add(createAssetManagement());
	}

	/**
	 * Create the container for global overrides information.
	 * 
	 * @return
	 */
	protected ElementNode createGlobals() {
		ElementNode.Builder builder =
				new ElementNode.Builder("Global Overrides",
						TenantConfigurationParser.Elements.Globals.getLocalName(), "cogs",
						ElementRole.Top_Globals);
		builder.setDescription("Allow tenant-specific changes to global configuration elements.");
		return builder.build();
	}

	/**
	 * Create the container for datastore information.
	 * 
	 * @return
	 */
	protected ElementNode createDataManagement() {
		ElementNode.Builder builder =
				new ElementNode.Builder("Data Management",
						TenantConfigurationParser.Elements.TenantDatastore.getLocalName(), "database",
						ElementRole.Top_DataManagement);
		builder.setDescription("Configure the datastore and related aspects such as caching and "
				+ "data model initialization.");
		builder.addElement(createMongoTenantDatastoreElement());
		builder.addElement(createHBaseTenantDatastoreElement());
		builder.addElement(createEHCacheElement());
		builder.addElement(createHazelcastCacheElement());
		builder.addElement(createDefaultDeviceModelInitializerElement());
		builder.addElement(createDefaultAssetModelInitializerElement());
		builder.addElement(createDefaultScheduleModelInitializerElement());
		return builder.build();
	}

	/**
	 * Create the container for device communication information.
	 * 
	 * @return
	 */
	protected ElementNode createDeviceCommunication() {
		ElementNode.Builder builder =
				new ElementNode.Builder("Device Communication",
						TenantConfigurationParser.Elements.DeviceCommunication.getLocalName(), "exchange",
						ElementRole.Top_DeviceCommunication);
		builder.setDescription("Configure how information is received from devices, how data is queued "
				+ "for processing, and how commands are sent to devices.");
		builder.addElement(createEventSourcesElement());
		builder.addElement(createInboundProcessingStrategyElement());
		builder.addElement(createRegistrationElement());
		builder.addElement(createBatchOperationsElement());
		builder.addElement(createCommandRoutingElement());
		builder.addElement(createCommandDestinationsElement());
		return builder.build();
	}

	/**
	 * Create the container for inbound processing chain configuration.
	 * 
	 * @return
	 */
	protected ElementNode createInboundProcessingChain() {
		ElementNode.Builder builder =
				new ElementNode.Builder("Inbound Processors",
						TenantConfigurationParser.Elements.InboundProcessingChain.getLocalName(), "sign-in",
						ElementRole.Top_InboundProcessingChain);
		builder.setDescription("Configure a chain of processing steps that are applied to inbound data.");
		return builder.build();
	}

	/**
	 * Create the container for outbound processing chain configuration.
	 * 
	 * @return
	 */
	protected ElementNode createOutboundProcessingChain() {
		ElementNode.Builder builder =
				new ElementNode.Builder("Outbound Processors",
						TenantConfigurationParser.Elements.OutboundProcessingChain.getLocalName(),
						"sign-out", ElementRole.Top_OutboundProcessingChain);
		builder.setDescription("Configure a chain of processing steps that are applied to outbound data.");
		return builder.build();
	}

	/**
	 * Create the container for asset management configuration.
	 * 
	 * @return
	 */
	protected ElementNode createAssetManagement() {
		ElementNode.Builder builder =
				new ElementNode.Builder("Asset Management",
						TenantConfigurationParser.Elements.AssetManagement.getLocalName(), "tag",
						ElementRole.Top_AssetManagement);
		builder.setDescription("Configure asset management features.");
		return builder.build();
	}

	/**
	 * Create element configuration for MonogoDB tenant datastore.
	 * 
	 * @return
	 */
	protected ElementNode createMongoTenantDatastoreElement() {
		ElementNode.Builder builder =
				new ElementNode.Builder("MongoDB Tenant Datastore",
						TenantDatastoreParser.Elements.MongoTenantDatastore.getLocalName(), "database",
						ElementRole.DataManagement_Datastore);

		builder.setDescription("Store tenant data using a MongoDB database.");
		builder.addAttribute((new AttributeNode.Builder("Use bulk inserts", "useBulkEventInserts",
				AttributeType.Boolean).setDescription("Use the MongoDB bulk insert API to add "
				+ "events in groups and improve performance.").build()));
		builder.addAttribute((new AttributeNode.Builder("Bulk insert max chunk size",
				"bulkInsertMaxChunkSize", AttributeType.Integer).setDescription("Maximum number of records to send "
				+ "in a single bulk insert (if bulk inserts are enabled).").build()));
		return builder.build();
	}

	/**
	 * Create element configuration for HBase tenant datastore.
	 * 
	 * @return
	 */
	protected ElementNode createHBaseTenantDatastoreElement() {
		ElementNode.Builder builder =
				new ElementNode.Builder("HBase Tenant Datastore",
						TenantDatastoreParser.Elements.HBaseTenantDatastore.getLocalName(), "database",
						ElementRole.DataManagement_Datastore);
		builder.setDescription("Store tenant data using tables in an HBase instance.");
		return builder.build();
	}

	/**
	 * Create element configuration for EHCache cache.
	 * 
	 * @return
	 */
	protected ElementNode createEHCacheElement() {
		ElementNode.Builder builder =
				new ElementNode.Builder("EHCache Cache Provider",
						TenantDatastoreParser.Elements.EHCacheDeviceManagementCache.getLocalName(),
						"folder-open-o", ElementRole.DataManagement_CacheProvider);
		builder.setDescription("Cache device management data using EHCache. Note that this "
				+ "cache is not intended for use on clustered installations.");
		return builder.build();
	}

	/**
	 * Create element configuration for Hazelcast cache.
	 * 
	 * @return
	 */
	protected ElementNode createHazelcastCacheElement() {
		ElementNode.Builder builder =
				new ElementNode.Builder("Hazelcast Distributed Cache Provider",
						TenantDatastoreParser.Elements.HazelcastCache.getLocalName(), "folder-open-o",
						ElementRole.DataManagement_CacheProvider);
		builder.setDescription("Cache device management data using Hazelcast distributed maps. "
				+ "This cache allows data to be shared between clustered SiteWhere instances.");
		return builder.build();
	}

	/**
	 * Create element configuration for device model initializer.
	 * 
	 * @return
	 */
	protected ElementNode createDefaultDeviceModelInitializerElement() {
		ElementNode.Builder builder =
				new ElementNode.Builder("Device Model Initializer",
						TenantDatastoreParser.Elements.DefaultDeviceModelInitializer.getLocalName(), "flash",
						ElementRole.DataManagement_DeviceModelInitializer);
		builder.setDescription("This component creates sample data when no existing device data "
				+ "is detected in the datastore. A site with device specifications, devices, "
				+ "assignments, events and other example data is created on instance startup.");
		return builder.build();
	}

	/**
	 * Create element configuration for device model initializer.
	 * 
	 * @return
	 */
	protected ElementNode createDefaultAssetModelInitializerElement() {
		ElementNode.Builder builder =
				new ElementNode.Builder("Asset Model Initializer",
						TenantDatastoreParser.Elements.DefaultAssetModelInitializer.getLocalName(), "flash",
						ElementRole.DataManagement_AssetModelInitializer);
		builder.setDescription("This component creates sample data when no existing asset data "
				+ "is detected in the datastore. If using the <strong>device model initializer</strong> "
				+ "this component should be used as well so that assets in the sample data can be resolved.");
		return builder.build();
	}

	/**
	 * Create element configuration for device model initializer.
	 * 
	 * @return
	 */
	protected ElementNode createDefaultScheduleModelInitializerElement() {
		ElementNode.Builder builder =
				new ElementNode.Builder("Schedule Model Initializer",
						TenantDatastoreParser.Elements.DefaultScheduleModelInitializer.getLocalName(),
						"flash", ElementRole.DataManagement_ScheduleModelInitializer);
		builder.setDescription("This component creates sample data when no existing schedule data "
				+ "is detected in the datastore. It provides examples of both simple and cron-based "
				+ "schedules that are commonly used.");
		return builder.build();
	}

	/**
	 * Create element configuration for event sources.
	 * 
	 * @return
	 */
	protected ElementNode createEventSourcesElement() {
		ElementNode.Builder builder =
				new ElementNode.Builder("Event Sources",
						DeviceCommunicationParser.Elements.EventSources.getLocalName(), "sign-in",
						ElementRole.DeviceCommunication_EventSources);

		builder.setDescription("Event sources are responsible for bringing data into SiteWhere. They "
				+ "listen for incoming messages, convert them to a unified format, then forward them "
				+ "to the inbound processing strategy implementation to be processed.");
		builder.addElement(createMqttEventSourceElement());
		return builder.build();
	}

	/**
	 * Create element configuration for MQTT event source.
	 * 
	 * @return
	 */
	protected ElementNode createMqttEventSourceElement() {
		ElementNode.Builder builder =
				new ElementNode.Builder("MQTT Event Source",
						EventSourcesParser.Elements.MqttEventSource.getLocalName(), "sign-in",
						ElementRole.EventSources_MqttEventSource);

		builder.setDescription("Listen for events on an MQTT topic.");
		builder.addAttribute((new AttributeNode.Builder("Source id", "sourceId", AttributeType.String).setDescription(
				"Unique id used for referencing this event source.").makeIndex().build()));
		builder.addAttribute((new AttributeNode.Builder("MQTT broker hostname", "hostname",
				AttributeType.String).setDescription("Hostname used for creating the MQTT broker"
				+ "connection.").build()));
		builder.addAttribute((new AttributeNode.Builder("MQTT broker port", "port", AttributeType.Integer).setDescription("Port number used for creating the MQTT broker connection.").build()));
		builder.addAttribute((new AttributeNode.Builder("MQTT topic", "topic", AttributeType.String).setDescription("MQTT topic event source uses for inbound messages.").build()));
		builder.addAttribute((new AttributeNode.Builder("Trust store path", "trustStorePath",
				AttributeType.String).setDescription("Fully-qualified path to trust store for secured connections.").build()));
		builder.addAttribute((new AttributeNode.Builder("Trust store password", "trustStorePassword",
				AttributeType.String).setDescription("Password used to authenticate with trust store.").build()));
		return builder.build();
	}

	/**
	 * Create element configuration for event sources.
	 * 
	 * @return
	 */
	protected ElementNode createInboundProcessingStrategyElement() {
		ElementNode.Builder builder =
				new ElementNode.Builder("Inbound Processing Strategy",
						DeviceCommunicationParser.Elements.InboundProcessingStrategy.getLocalName(), "cogs",
						ElementRole.DeviceCommunication_InboundProcessingStrategy);

		builder.setDescription("The inbound processing strategy is responsible for moving events from event "
				+ "sources into the inbound processing chain. It is responsible for handling threading and "
				+ "reliably delivering events for processing.");
		return builder.build();
	}

	/**
	 * Create element configuration for device registration.
	 * 
	 * @return
	 */
	protected ElementNode createRegistrationElement() {
		ElementNode.Builder builder =
				new ElementNode.Builder("Device Registration Management",
						DeviceCommunicationParser.Elements.Registration.getLocalName(), "key",
						ElementRole.DeviceCommunication_Registration);

		builder.setDescription("Manages how new devices are registered with the system.");
		return builder.build();
	}

	/**
	 * Create element configuration for batch operations.
	 * 
	 * @return
	 */
	protected ElementNode createBatchOperationsElement() {
		ElementNode.Builder builder =
				new ElementNode.Builder("Batch Operation Management",
						DeviceCommunicationParser.Elements.BatchOperations.getLocalName(), "server",
						ElementRole.DeviceCommunication_BatchOperations);

		builder.setDescription("Manages how batch operations are processed. Batch operations are "
				+ "actions that are executed asynchronously for many devices with the ability to monitor "
				+ "progress at both the batch and element level.");
		return builder.build();
	}

	/**
	 * Create element configuration for command routing.
	 * 
	 * @return
	 */
	protected ElementNode createCommandRoutingElement() {
		ElementNode.Builder builder =
				new ElementNode.Builder("Device Command Routing",
						DeviceCommunicationParser.Elements.CommandRouting.getLocalName(),
						"sitemap fa-rotate-270", ElementRole.DeviceCommunication_CommandRouting);

		builder.setDescription("Determines how commands are routed to command destinations.");
		return builder.build();
	}

	/**
	 * Create element configuration for command routing.
	 * 
	 * @return
	 */
	protected ElementNode createCommandDestinationsElement() {
		ElementNode.Builder builder =
				new ElementNode.Builder("Device Command Destinations",
						DeviceCommunicationParser.Elements.CommandDestinations.getLocalName(), "sign-out",
						ElementRole.DeviceCommunication_CommandDestinations);

		builder.setDescription("Command destinations provide the information SiteWhere needs "
				+ "to route commands to devices. This includes information about how to encode the "
				+ "command and how to deliver the command via the underlying transport.");
		return builder.build();
	}
}