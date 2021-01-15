package com.aemexcercise.core.properties;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.osgi.service.metatype.annotations.Option;

@ObjectClassDefinition(name = "AEM Exercise: Config for Custom Metadata for page",
        description = "OSGi Configuration for Custom Metadata for new Page creation")
public @interface WorkflowCustomMetadataPropertyConfig {

    @AttributeDefinition(name = "Path", description = "Repository Path to add Custom Page Metadata")
    String path();

    @AttributeDefinition(name = "Property Value", description = "Value for the custom property 'Company' to be assigned at the time of page Creation")
    String propertyValue();

    @AttributeDefinition(name = "Location", description = "Location to pick the custom metadata Value",
            options = {@Option(label = "OSGi Config", value = "OSGi"), @Option(label = "Workflow Model Arguments", value = "Workflow")})
    String location() default "Osgi";
}
