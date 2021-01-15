package com.aemexcercise.core.properties;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Designate(ocd = WorkflowCustomMetadataPropertyConfig.class)
@Component(service = WorkflowCustomMetadataProperty.class, immediate = true)
public class WorkflowCustomMetadataProperty {

    private static final Logger log = LoggerFactory.getLogger(WorkflowCustomMetadataProperty.class);

    private WorkflowCustomMetadataPropertyConfig workflowCustomMetadataPropertyConfig;

    @Activate
    protected void activate(final WorkflowCustomMetadataPropertyConfig workflowCustomMetadataPropertyConfig) {
        this.workflowCustomMetadataPropertyConfig = workflowCustomMetadataPropertyConfig;
        log.debug("config.propertyValue(): {}", workflowCustomMetadataPropertyConfig.propertyValue());
        log.debug("config.path(): {}", workflowCustomMetadataPropertyConfig.path());
        log.debug("config.location(): {}", workflowCustomMetadataPropertyConfig.location());
    }

    public String getValue() {
        return workflowCustomMetadataPropertyConfig.propertyValue();
    }

    public String getPath() {
        return workflowCustomMetadataPropertyConfig.path();
    }

    public String getLocation() {
        return workflowCustomMetadataPropertyConfig.location();
    }
}
