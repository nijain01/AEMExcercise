package com.aemexcercise.core.models;

import com.day.cq.wcm.api.Page;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
public class BreadcrumbModelTest {

    private final AemContext ctx = new AemContext();

    private Page page;
    private Resource resource;

    private BreadcrumbModel model;

    @BeforeEach
    public void setUp() {
        page = ctx.create().page("/content/appleassignment/us/en/Iphone/iphone-se");
        resource = ctx.create().resource(page, "breadcrumb",
                "sling:resourceType", "appleassignment/components/breadcrumb");

        model = resource.adaptTo(BreadcrumbModel.class);
    }

    @Test
    @DisplayName("Get the BreadCrumb")
    public void getBreadcrumb() {
        Breadcrumb breadcrumb = model.getBreadcrumb();
        assertNotNull(breadcrumb.getBreadcrumbList());
    }
}