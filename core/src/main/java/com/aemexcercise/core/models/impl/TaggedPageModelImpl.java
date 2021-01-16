package com.aemexcercise.core.models.impl;

import com.aemexcercise.core.constants.MetadataConstants;
import com.aemexcercise.core.models.TaggedPage;
import com.aemexcercise.core.models.TaggedPageModel;
import com.aemexcercise.core.utils.AppUtils;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import org.apache.commons.lang3.StringUtils;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.day.cq.wcm.api.NameConstants.NT_PAGE;

@Model(adaptables = {Resource.class}, adapters = {TaggedPageModel.class}, resourceType = TaggedPageModelImpl.RESOURCE_TYPE)
public class TaggedPageModelImpl implements TaggedPageModel {

    protected static final String RESOURCE_TYPE = "aemexcercise/components/tagpicker";
    private static final String PATH_TO_SEARCH = "/";
    private static final String LIMIT = "p.limit";
    private static final String TITLE_PATH = "jcr:content/jcr:title";
    private final Logger log = LoggerFactory.getLogger(getClass());
    private List<TaggedPage> taggedPageList;

    private Session session;

    @Self
    private Resource resource;

    @Inject
    private QueryBuilder queryBuilder;

    @ValueMapValue
    @Optional
    @Named("tags")
    private String[] tags;

    @ValueMapValue
    @Named("searchPath")
    @Default(values = TaggedPageModelImpl.PATH_TO_SEARCH)
    private String searchPath;

    @OSGiService
    private ResourceResolverFactory resourceResolverFactory;

    @PostConstruct
    protected void init() throws RepositoryException {
        log.debug("Inside TaggedPageModel");

        ResourceResolver resourceResolver = null;

        try {
            resourceResolver = AppUtils.getServiceUserResourceResolver(resourceResolverFactory, MetadataConstants.METADATA_READER_SERVICE);
            this.session = resourceResolver.adaptTo(Session.class);

            log.debug("Tags configured for component: {}", tags);
            if (tags != null && tags.length > 0) {
                this.taggedPageList = getTaggedPagesList();
            } else {
                this.taggedPageList = new ArrayList<>();
            }
        } catch (LoginException e) {
            log.error("Error getting resourceResolver for Tagged Page: {}", e.getMessage());
        } finally {
            AppUtils.closeResourceResolver(resourceResolver);
        }


    }

    private Map<String, String> getQueryMap() {
        log.debug("Inside getQueryMap");
        Map<String, String> queryParamsMap = new HashMap<>();
        int i = 1;

        final String pathToSearch = getSearchPath();
        queryParamsMap.put("type", NT_PAGE);
        queryParamsMap.put("path", pathToSearch);
        queryParamsMap.put("path.self", "true");
        queryParamsMap.put("1_property", "jcr:content/cq:tags");
        for (String tag : tags) {
            log.debug("tag id: {}", tag);
            String property = "1_property." + i++ + "_value";
            queryParamsMap.put(property, tag);
        }

        queryParamsMap.put("orderby.sort", "desc");
        queryParamsMap.put("p.hits", "full");
        queryParamsMap.put(LIMIT, "-1");
        log.debug("Query map returned: {}", queryParamsMap);
        return queryParamsMap;
    }


    private List<TaggedPage> getTaggedPagesList() throws RepositoryException {

        log.debug("Inside getTaggedPagesList");
        Query query = queryBuilder.createQuery(PredicateGroup.create(getQueryMap()), this.session);
        SearchResult result = query.getResult();
        List<TaggedPage> taggedPages = new ArrayList<>();

        log.debug("Result Count: {}", result.getHits().size());

        for (Hit hit : result.getHits()) {
            Node node = hit.getNode();
            TaggedPage bean = new TaggedPage();
            bean.setPath(node.getPath());
            log.debug("Tagged Page path: {}", bean.getPath());
            bean.setName(node.getName());
            log.debug("Tagged Page name: {}", bean.getName());
            bean.setTitle(getTitle(node));
            log.debug("Tagged Page title: {}", bean.getTitle());
            taggedPages.add(bean);
        }
        return taggedPages;
    }

    private String getSearchPath() {
        return (searchPath == null || searchPath.equals(StringUtils.EMPTY)) ? PATH_TO_SEARCH : searchPath;
    }

    private String getTitle(Node node) throws RepositoryException {
        return node.hasProperty(TITLE_PATH) ?
                node.getProperty(TITLE_PATH).getValue().getString() :
                StringUtils.EMPTY;
    }

    public List<TaggedPage> getTaggedPageList() {
        return taggedPageList;
    }
}
