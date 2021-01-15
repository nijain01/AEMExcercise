package com.aemexcercise.core.properties;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "AEM Exercise: Config to get Repository Path & Value to add Custom Metadata via Handler",
        description = "Custom Config for Repository Path to add Custom Page Metadata via Handler")
public @interface ListenerCustomMetadataConfig {
    @AttributeDefinition(name = "Path", description = "Repository Path to add Custom Page Metadata")
    String path();

    @AttributeDefinition(name = "Property Value", description = "Value for the custom property 'Company' to be assigned at the time of page Creation")
    String propertyValue();

}