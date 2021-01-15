package com.aemexcercise.core.listeners;

import com.aemexcercise.core.properties.ListenerCustomMetadataProperty;
import com.day.cq.wcm.api.PageEvent;
import com.day.cq.wcm.api.PageModification;
import org.apache.sling.event.jobs.JobManager;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.aemexcercise.core.constants.MetadataConstants.TOPIC;


@Component(immediate = true, service = EventHandler.class,
        property = {
                EventConstants.EVENT_TOPIC + "=" + PageEvent.EVENT_TOPIC
        })
public class PageCreationHandler implements EventHandler {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Reference
    private ListenerCustomMetadataProperty listenerCustomMetadataProperty;

    @Reference
    private JobManager jobManager;

    @Override
    public void handleEvent(Event event) {
        log.debug("inside PageCreationHandler");
        PageEvent pageEvent = PageEvent.fromEvent(event);

        Iterator<PageModification> pageModificationIterator = pageEvent.getModifications();
        while (pageModificationIterator.hasNext()) {
            PageModification modification = pageModificationIterator.next();
            log.debug("PageCreationHandler:: Page modification type: {}", modification.getType());
            if (PageModification.ModificationType.CREATED.equals(modification.getType()) &&
                    modification.getPath().startsWith(listenerCustomMetadataProperty.getPath())) {
                Map<String, Object> jobProps = new HashMap<>();
                jobProps.put("pageEvent", pageEvent);
                jobManager.addJob(TOPIC, jobProps);
            } else {
                log.debug("Metadata could not be added due to either Page modification type: {}" +
                                " ;or [{}] is not in scope of configured path [{}]", modification.getType(),
                        modification.getPath(), listenerCustomMetadataProperty.getPath());
            }
        }
    }
}