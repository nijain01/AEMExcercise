package com.aemexcercise.core.utils;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Map;

/**
 * This class is Util class for repository specific changes
 */

public class AppleUtils {

    private static final Logger log = LoggerFactory.getLogger(AppleUtils.class);

    private AppleUtils() {
    }

    public static ResourceResolver getServiceUserResourceResolver(final ResourceResolverFactory resourceResolverFactory,
                                                                  final String serviceName) throws LoginException {
        final Map<String, Object> authInfo = Collections.singletonMap(ResourceResolverFactory.SUBSERVICE,
                serviceName);

        return resourceResolverFactory.getServiceResourceResolver(authInfo);

    }

    /**
     * This method close resourceResolver object once no longer required
     *
     * @param resourceResolver
     */
    public static void closeResourceResolver(final ResourceResolver resourceResolver) {
        if (resourceResolver != null && resourceResolver.isLive()) {
            resourceResolver.close();
        }
    }

}