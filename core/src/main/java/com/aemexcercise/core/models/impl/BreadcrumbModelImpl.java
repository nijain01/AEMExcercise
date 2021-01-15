package com.aemexcercise.core.models.impl;

import com.aemexcercise.core.constants.MetadataConstants;
import com.aemexcercise.core.models.Breadcrumb;
import com.aemexcercise.core.models.BreadcrumbModel;
import com.aemexcercise.core.models.Link;
import com.aemexcercise.core.utils.AppUtils;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Model(adaptables = {Resource.class}, adapters = {BreadcrumbModel.class}, resourceType = BreadcrumbModelImpl.RESOURCE_TYPE)
public class BreadcrumbModelImpl implements BreadcrumbModel {

    protected static final String RESOURCE_TYPE = "aemexcercise/components/breadcrumb";

    private final Logger log = LoggerFactory.getLogger(getClass());

    private Breadcrumb breadcrumb = null;

    @Self
    private Resource currentResource;

    @OSGiService
    private ResourceResolverFactory resourceResolverFactory;

    @ValueMapValue @Optional
    @Named("startLevel") @Default(intValues = 2)
    private int startLevel;

    @PostConstruct
    protected void init() {

        Page currentPage;

        log.debug("Inside BreadcrumbLink class");

        breadcrumb = new Breadcrumb();
        List<Link> linkList = new ArrayList<>();
        ResourceResolver resourceResolver = null;
        try {
            resourceResolver = AppUtils.getServiceUserResourceResolver(resourceResolverFactory, MetadataConstants.METADATA_READER_SERVICE);
            PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
            currentPage = pageManager.getContainingPage(currentResource);

            int currentDepth = currentPage.getDepth();
            log.debug("Depth for current page '{}': {}", currentPage.getPath(), currentDepth);
            log.debug("Depth value from component configuration: {}", startLevel);

            while (startLevel < currentDepth) {
                Page currPage = currentPage.getAbsoluteParent(startLevel);
                if (null != currPage && null != currPage.getContentResource()) {
                    log.debug("Depth for page '{}': {}", currPage.getPath(), startLevel);
                    Link link = new Link();
                    link.setPageLink(currPage.getPath());
                    link.setName(currPage.getName());
                    link.setTitle(getTitle(currPage));
                    linkList.add(link);
                }
                startLevel++;
            }
            breadcrumb.setBreadcrumbList(Collections.unmodifiableList(linkList));
        } catch (LoginException e) {
            log.error("Error getting resourceResolver for BreadCrumb: {}", e.getMessage());
        } finally {
            AppUtils.closeResourceResolver(resourceResolver);
        }
    }

    private String getTitle(@NotNull Page currPage) {
        String title = currPage.getNavigationTitle();
        if (null == title) {
            title = currPage.getPageTitle();
        }
        if (null == title) {
            title = currPage.getName();
        }
        return title;
    }

    public Breadcrumb getBreadcrumb() {
        return breadcrumb;
    }
}
