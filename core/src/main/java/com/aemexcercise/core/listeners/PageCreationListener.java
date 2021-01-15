package com.aemexcercise.core.listeners;

import com.aemexcercise.core.constants.MetadataConstants;
import com.aemexcercise.core.properties.ListenerCustomMetadataProperty;
import com.aemexcercise.core.utils.AppleUtils;
import com.day.cq.wcm.api.PageEvent;
import com.day.cq.wcm.api.PageModification;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.Session;
import java.util.Iterator;


/**
 * This Consumer creates a new metadata for the for the page upon creation
 */
@Component(immediate = true, service = JobConsumer.class,
        property = {
                JobConsumer.PROPERTY_TOPICS + "=" + MetadataConstants.TOPIC
        })
public class PageCreationListener implements JobConsumer {

    private static final String PROPERTY_NAME = "companyFromEventListener";
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    @Reference
    private ListenerCustomMetadataProperty listenerCustomMetadataProperty;

    /**
     * Execute the Job
     */
    @Override
    public JobResult process(Job job) {
        log.debug("inside PageCreationListener");
        String path;
        ResourceResolver resourceResolver = null;
        Session session = null;
        try {
            resourceResolver = AppleUtils.getServiceUserResourceResolver(resourceResolverFactory, MetadataConstants.METADATA_WRITER_SERVICE);

            session = resourceResolver.adaptTo(Session.class);

            PageEvent pageEvent = (PageEvent) job.getProperty("pageEvent");
            if (null != pageEvent && pageEvent.isLocal()) {
                log.debug("got pageEvent");
                Iterator<PageModification> pageModificationIterator = pageEvent.getModifications();

                while (pageModificationIterator.hasNext()) {
                    PageModification pageModification = pageModificationIterator.next();
                    log.debug("Page modification type: {}", pageModification.getType());
                    if (PageModification.ModificationType.CREATED.equals(pageModification.getType())) {
                        path = pageModification.getPath() + "/jcr:content";
                        log.debug("PageCreated at path: {}", pageModification.getPath());
                        log.debug("jcr:content path: {}", path);

                        Node node = (Node) session.getItem(path);
                        Property property = node.setProperty(PROPERTY_NAME, listenerCustomMetadataProperty.getValue());
                        node.getSession().save();

                        if (node.hasProperty(PROPERTY_NAME)) {
                            log.debug("Property {} successfully created with value:{}", property.getName(), property.getValue());
                        } else {
                            log.debug("Property {} does not exists.", PROPERTY_NAME);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.debug("Exception in PageCreationListener: {}", e.getMessage());
        } finally {
            if (session != null && session.isLive()) {
                session.logout();
            }
            AppleUtils.closeResourceResolver(resourceResolver);
        }
        return JobResult.OK;
    }
}
