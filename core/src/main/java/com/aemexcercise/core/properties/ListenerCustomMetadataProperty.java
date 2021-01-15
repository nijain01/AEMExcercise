package com.aemexcercise.core.properties;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Designate(ocd = ListenerCustomMetadataConfig.class)
@Component(service = ListenerCustomMetadataProperty.class, immediate = true)
public class ListenerCustomMetadataProperty {

    private static final Logger log = LoggerFactory.getLogger(ListenerCustomMetadataProperty.class);

    private ListenerCustomMetadataConfig listenerCustomMetadataConfig;

    @Activate
    protected void activate(final ListenerCustomMetadataConfig listenerCustomMetadataConfig) {
        this.listenerCustomMetadataConfig = listenerCustomMetadataConfig;
        log.debug("config.propertyValue(): {}", listenerCustomMetadataConfig.propertyValue());
        log.debug("config.path(): {}", listenerCustomMetadataConfig.path());
    }

    public String getValue(){
        return listenerCustomMetadataConfig.propertyValue();
    }

    public String getPath(){
        return listenerCustomMetadataConfig.path();
    }


}
