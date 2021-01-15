package com.aemexcercise.core.workflow;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.aemexcercise.core.properties.WorkflowCustomMetadataProperty;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import static com.aemexcercise.core.constants.MetadataConstants.PROCESS_ARGS;
import static com.aemexcercise.core.constants.MetadataConstants.PROPERTY_NAME;
import static com.aemexcercise.core.constants.MetadataConstants.PROPERTY_VALUE;
import static com.aemexcercise.core.constants.MetadataConstants.TYPE_JCR_PATH;


@Component(service = WorkflowProcess.class,
        property = {"process.label=Add Metadata to Page"})
public class AddPageMetadata implements WorkflowProcess {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Reference
    private WorkflowCustomMetadataProperty workflowCustomMetadataProperty;

    @Override
    public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap metaDataMap)
            throws WorkflowException {

        log.debug("inside execute for AddPageMetadata");

        WorkflowData workflowData = workItem.getWorkflowData();

        if (workflowData.getPayloadType().equals(TYPE_JCR_PATH)) {
            String path = workflowData.getPayload().toString() + "/jcr:content";
            log.debug(path);

            Session jcrSession;
            if (workflowSession != null) {
                try {
                    jcrSession = workflowSession.adaptTo(Session.class);

                    String propertyValue = getPropertyValue(metaDataMap);

                    if (jcrSession.itemExists(path) && path.startsWith(workflowCustomMetadataProperty.getPath())) {
                        log.debug("path [{}] is present", path);
                        Node node = (Node) jcrSession.getItem(path);
                        Property property = node.setProperty(PROPERTY_NAME, propertyValue);
                        node.getSession().save();
                        log.debug("Property {} successfully created with value:{}", property.getName(),
                                property.getValue());
                    } else {
                        log.debug("Metadata could not be added due to either path [{}] is not present" +
                                " ;or [{}] is not in scope of configured path [{}]", path, path, workflowCustomMetadataProperty.getPath());
                    }
                } catch (RepositoryException e) {
                    log.error(e.getMessage());
                }
            }
        }

    }

    private String getPropertyValue(MetaDataMap metaDataMap) {
        String propertyValue;
        if (workflowCustomMetadataProperty.getLocation().equals("OSGi")) {
            propertyValue = workflowCustomMetadataProperty.getValue();
        } else if (workflowCustomMetadataProperty.getLocation().equals("Workflow") && metaDataMap.containsKey(PROCESS_ARGS)) {
            log.debug("Workflow metadata for key PROCESS_ARGS and value {}", metaDataMap.get(PROCESS_ARGS, "string"));
            propertyValue = metaDataMap.get(PROCESS_ARGS, "string");
        } else {
            propertyValue = PROPERTY_VALUE;
        }
        return propertyValue;
    }
}
