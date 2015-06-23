/**
 * This file Copyright (c) 2015 Magnolia International
 * Ltd.  (http://www.magnolia-cms.com). All rights reserved.
 *
 *
 * This file is dual-licensed under both the Magnolia
 * Network Agreement and the GNU General Public License.
 * You may elect to use one or the other of these licenses.
 *
 * This file is distributed in the hope that it will be
 * useful, but AS-IS and WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE, TITLE, or NONINFRINGEMENT.
 * Redistribution, except as permitted by whichever of the GPL
 * or MNA you select, is prohibited.
 *
 * 1. For the GPL license (GPL), you can redistribute and/or
 * modify this file under the terms of the GNU General
 * Public License, Version 3, as published by the Free Software
 * Foundation.  You should have received a copy of the GNU
 * General Public License, Version 3 along with this program;
 * if not, write to the Free Software Foundation, Inc., 51
 * Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * 2. For the Magnolia Network Agreement (MNA), this file
 * and the accompanying materials are made available under the
 * terms of the MNA which accompanies this distribution, and
 * is available at http://www.magnolia-cms.com/mna.html
 *
 * Any modifications to this file must keep this entire header
 * intact.
 *
 */
package info.magnolia.demo.travel.tours.service;

import info.magnolia.cms.core.Path;
import info.magnolia.cms.util.QueryUtil;
import info.magnolia.cms.util.SelectorUtil;
import info.magnolia.context.MgnlContext;
import info.magnolia.dam.api.Asset;
import info.magnolia.dam.templating.functions.DamTemplatingFunctions;
import info.magnolia.demo.travel.tours.ToursModule;
import info.magnolia.jcr.util.ContentMap;
import info.magnolia.module.categorization.functions.CategorizationTemplatingFunctions;
import info.magnolia.rendering.template.type.DefaultTemplateTypes;
import info.magnolia.rendering.template.type.TemplateTypeHelper;
import info.magnolia.templating.functions.TemplatingFunctions;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.Query;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service class used by several model classes.
 */
@Singleton
public class TourServices {

    private static Logger log = LoggerFactory.getLogger(TourServices.class);

    public static final String TOUR_QUERY_PARAMETER = "tour";

    private final ToursModule toursModule;
    private final TemplateTypeHelper templateTypeHelper;
    private final TemplatingFunctions templatingFunctions;
    private final CategorizationTemplatingFunctions categorizationTemplatingFunctions;
    private final DamTemplatingFunctions damFunctions;

    @Inject
    public TourServices(ToursModule toursModule, TemplateTypeHelper templateTypeHelper, TemplatingFunctions templatingFunctions,
                        CategorizationTemplatingFunctions categorizationTemplatingFunctions, DamTemplatingFunctions damFunctions) {
        this.toursModule = toursModule;
        this.templateTypeHelper = templateTypeHelper;
        this.templatingFunctions = templatingFunctions;
        this.categorizationTemplatingFunctions = categorizationTemplatingFunctions;
        this.damFunctions = damFunctions;
    }

    /**
     * Tries to determine {@link Category} from passed URL selector (e.g. <code>/page~category_name~.html</code>).
     */
    public Category getCategoryByUrl() {
        final String categoryName = StringUtils.defaultIfBlank(SelectorUtil.getSelector(0), "active");
        return getCategoryByName(categoryName);
    }

    /**
     * Returns a {@link Category} object based on path or name of category.
     */
    public Category getCategoryByName(String categoryName) {
        final String categoryWorkspace = categorizationTemplatingFunctions.getCategorizationRepository();

        Category category = null;
        try {
            final Node categoryNode = getContentNodeByName(categoryName, categoryWorkspace);
            if (categoryNode != null) {
                category = marshallCategoryNode(categoryNode);
            }
        } catch (RepositoryException e) {
            log.debug("Could not find category with name [{}] in workspace [{}]", categoryName, categoryWorkspace);
        }

        return category;
    }

    /**
     * Creates a {@link Category} object from a {@link Node}.
     */
    public Category marshallCategoryNode(Node categoryNodeRaw) {
        Category category = null;

        if (categoryNodeRaw != null) {
            try {
                final Node categoryNode = templatingFunctions.wrapForI18n(categoryNodeRaw);
                String name = categoryNode.getName();
                if (categoryNode.hasProperty(Category.PROPERTY_NAME_DISPLAY_NAME)) {
                    name = categoryNode.getProperty(Category.PROPERTY_NAME_DISPLAY_NAME).getString();
                }

                category = new Category(name, categoryNode.getIdentifier());

                // We always require a slug, here using the node name as the name might have a nicer display name
                category.setNodeName(categoryNode.getName());

                if (categoryNode.hasProperty(Category.PROPERTY_NAME_DESCRIPTION)) {
                    category.setDescription(categoryNode.getProperty(Category.PROPERTY_NAME_DESCRIPTION).getString());
                }

                if (categoryNode.hasProperty(Category.PROPERTY_NAME_BODY)) {
                    category.setBody(categoryNode.getProperty(Category.PROPERTY_NAME_BODY).getString());
                }

                if (categoryNode.hasProperty(Category.PROPERTY_NAME_IMAGE)) {
                    Asset image = damFunctions.getAsset(categoryNode.getProperty(Category.PROPERTY_NAME_IMAGE).getString());
                    category.setImage(image);
                }
            } catch (RepositoryException e) {
                log.debug("Could not marshall category from node [{}]", categoryNodeRaw);
            }
        }

        return category;
    }

    /**
     * Returns a list of {@link Category}/ies.
     *
     * Uses the passed {@link Node} from the website repository to find feature template sub type to generate {@link Category} link.
     */
    public List<Category> marshallCategoryNodes(List<Node> categoryNodes, Node contentNode, String featureSubTypeName) {
        final List<Category> categories = new ArrayList<>();

        for (Node categoryNode : categoryNodes) {
            final Category category = marshallCategoryNode(categoryNode);
            if (category != null) {
                try {
                    final String link = getCategoryLink(contentNode, categoryNode.getName(), featureSubTypeName);
                    category.setLink(link);
                } catch (RepositoryException e) {
                    log.error("Could not get node name of category node [{}]", categoryNode, e);
                }

                categories.add(category);
            }
        }

        return categories;
    }

    /**
     * Creates a {@link Tour} from a {@link Node}.
     */
    public Tour marshallTourNode(Node tourNodeRaw) {
        Tour tour = null;

        if (tourNodeRaw != null) {
            final Node tourNode = templatingFunctions.wrapForI18n(tourNodeRaw);
            tour = new Tour();

            try {
                tour.setName(tourNode.getName());
                if (tourNode.hasProperty(Tour.PROPERTY_NAME_DISPLAY_NAME)) {
                    tour.setName(tourNode.getProperty(Tour.PROPERTY_NAME_DISPLAY_NAME).getString());
                }

                if (tourNode.hasProperty(Tour.PROPERTY_NAME_DESCRIPTION)) {
                    tour.setDescription(tourNode.getProperty(Tour.PROPERTY_NAME_DESCRIPTION).getString());
                }

                if (tourNode.hasProperty(Tour.PROPERTY_NAME_BODY)) {
                    tour.setBody(tourNode.getProperty(Tour.PROPERTY_NAME_BODY).getString());
                }

                if (tourNode.hasProperty(Tour.PROPERTY_NAME_AUTHOR)) {
                    tour.setAuthor(tourNode.getProperty(Tour.PROPERTY_NAME_AUTHOR).getString());
                }

                if (tourNode.hasProperty(Tour.PROPERTY_NAME_DURATION)) {
                    tour.setDuration(tourNode.getProperty(Tour.PROPERTY_NAME_DURATION).getString());
                }

                if (tourNode.hasProperty(Tour.PROPERTY_NAME_LOCATION)) {
                    tour.setLocation(tourNode.getProperty(Tour.PROPERTY_NAME_LOCATION).getString());
                }

                if (tourNode.hasProperty(Tour.PROPERTY_NAME_IMAGE)) {
                    tour.setImage(damFunctions.getAsset(tourNode.getProperty(Tour.PROPERTY_NAME_IMAGE).getString()));
                }

                if (tourNode.hasProperty(Tour.PROPERTY_NAME_TOUR_TYPES_CATEGORY)) {
                    final List<Category> tourTypes = getCategories(tourNode, Tour.PROPERTY_NAME_TOUR_TYPES_CATEGORY);
                    tour.setTourTypes(tourTypes);
                }

                if (tourNode.hasProperty(Tour.PROPERTY_NAME_DESTINATION)) {
                    final List<Category> destinations = getCategories(tourNode, Tour.PROPERTY_NAME_DESTINATION);
                    tour.setDestinations(destinations);
                }

                final String tourLink = getTourLink(tourNode);
                if (StringUtils.isNotBlank(tourLink)) {
                    tour.setLink(tourLink);
                }
            } catch (RepositoryException e) {
                log.debug("Could not marshall tour from node [{}]", tourNodeRaw);
            }
        }

        return tour;
    }

    /**
     * Get and marshall all categories of a {@link Node} stored under the given <code>categoryPropertyName</code>.
     */
    private List<Category> getCategories(Node node, String categoryPropertyName) {
        final List<Category> categories = new ArrayList<>();

        final List<Node> destinationNodes = categorizationTemplatingFunctions.getCategories(node, categoryPropertyName);
        for (Node tourTypeNode : destinationNodes) {
            final Category category = marshallCategoryNode(tourTypeNode);
            if (category != null) {
                categories.add(category);
            }
        }

        return categories;
    }

    public Node getTourNodeByParameter() throws RepositoryException {
        final String tourName = StringUtils.defaultIfBlank(MgnlContext.getParameter(TOUR_QUERY_PARAMETER), toursModule.getDefaultTourName());
        return getContentNodeByName(tourName, ToursModule.TOURS_REPOSITORY_NAME);
    }

    /**
     * Get the Link as String of the category of a specific page type If no
     * category found, return empty String.
     */
    public String getCategoryLink(Node content, String categoryName, String featureSubType) {
        try {
            Node siteRoot = templatingFunctions.siteRoot(content);
            Node categoryOverviewPage = categorizationTemplatingFunctions.getContentByTemplateCategorySubCategory(siteRoot, DefaultTemplateTypes.FEATURE, featureSubType);

            if (categoryOverviewPage != null) {
                return templatingFunctions.link(categoryOverviewPage).replace(".html", Path.SELECTOR_DELIMITER + categoryName + Path.SELECTOR_DELIMITER + ".html");
            }
        } catch (RepositoryException e) {
            log.warn("Can't get categoryOverview page link [subType={}]", featureSubType, e);
        }

        return StringUtils.EMPTY;
    }

    private Node getContentNodeByName(final String pathOrName, final String workspace) throws RepositoryException {
        if (pathOrName.startsWith("/")) {
            return MgnlContext.getJCRSession(workspace).getNode(StringUtils.substringBefore(pathOrName, "?"));
        } else {
            final String sql = String.format("SELECT * FROM [nt:base] AS content WHERE name(content)='%s'", pathOrName);
            final NodeIterator items = QueryUtil.search(workspace, sql, Query.JCR_SQL2, "mgnl:content");

            if (items != null && items.hasNext()) {
                return items.nextNode();
            }
        }

        log.warn("Could not find node from workspace [{}] based on slug [{}]", workspace, pathOrName);
        return null;
    }

    public List<Category> getRelatedCategoriesByParameter() {
        final List<Category> categories = new LinkedList<>();

        try {
            final Node node = getTourNodeByParameter();

            for (Node categoryNode : categorizationTemplatingFunctions.getCategories(node, Tour.PROPERTY_NAME_TOUR_TYPES_CATEGORY)) {
                final Category category = marshallCategoryNode(categoryNode);
                if (category != null) {
                    categories.add(category);
                }
            }

        } catch (RepositoryException e) {
            log.error("Could not retrieve related categories by tour parameter.", e);
        }

        return categories;
    }

    public List<ContentMap> getToursByCategory(String categoryPropertyName, String identifier) {
        return getToursByCategory(categoryPropertyName, identifier, false);
    }

    public List<ContentMap> getToursByCategory(String categoryPropertyName, String identifier, boolean featured) {
        final List<ContentMap> tours = new LinkedList<>();

        try {
            final Session session = MgnlContext.getJCRSession(ToursModule.TOURS_REPOSITORY_NAME);
            String query = String.format("%s LIKE '%%%s%%'", categoryPropertyName, identifier);
            if (featured) {
                query += " AND isFeatured = 'true'";
            }

            final List<Node> tourNodes = templateTypeHelper.getContentListByTemplateIds(session.getRootNode(), null, Integer.MAX_VALUE, query, null);
            for (Node tourNode : tourNodes) {
                tours.add(templatingFunctions.asContentMap(tourNode));
            }

        } catch (RepositoryException e) {
            log.error("Could not get related tours by category identifier [{}={}].", categoryPropertyName, identifier, e);
        }

        return tours;
    }

    /**
     * Create a link to a specific tour.
     */
    public String getTourLink(Node tourNode) {
        return templatingFunctions.link(tourNode);
    }

}
