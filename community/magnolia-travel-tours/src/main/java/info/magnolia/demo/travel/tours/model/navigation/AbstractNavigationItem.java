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
package info.magnolia.demo.travel.tours.model.navigation;

import info.magnolia.cms.core.AggregationState;
import info.magnolia.context.MgnlContext;
import info.magnolia.demo.travel.tours.ToursModule;
import info.magnolia.demo.travel.tours.service.Category;
import info.magnolia.demo.travel.tours.service.TourServices;
import info.magnolia.jcr.predicate.NodeTypePredicate;
import info.magnolia.jcr.util.NodeUtil;
import info.magnolia.jcr.util.PropertyUtil;
import info.magnolia.module.categorization.CategorizationModule;
import info.magnolia.module.categorization.CategorizationNodeTypes;
import info.magnolia.templating.functions.TemplatingFunctions;
import info.magnolia.templating.models.navigation.DefaultNavigationItem;
import info.magnolia.templating.models.navigation.Navigation;
import info.magnolia.templating.models.navigation.NavigationConfig;
import info.magnolia.templating.models.navigation.NavigationItem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Provider;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An abstract {@link NavigationItem} dedicated to tours and destinations.
 */
public abstract class AbstractNavigationItem extends DefaultNavigationItem {

    private static final Logger log = LoggerFactory.getLogger(AbstractNavigationItem.class);

    protected final Provider<AggregationState> aggregationStateProvider;
    protected final ToursModule toursModule;
    protected final TourServices tourServices;

    public AbstractNavigationItem(Node content, Navigation parent, NavigationConfig navigationConfig,
                                  TemplatingFunctions templatingFunctions,
                                  Provider<AggregationState> aggregationStateProvider,
                                  ToursModule toursModule, TourServices tourServices) throws RepositoryException {
        super(content, parent, navigationConfig, templatingFunctions, aggregationStateProvider);

        this.aggregationStateProvider = aggregationStateProvider;
        this.toursModule = toursModule;
        this.tourServices = tourServices;
    }

    /**
     * Returns the root node of the categories.
     */
    abstract String getCategoryRootNode();

    /**
     * Returns the template sub-type to link to.
     *
     * @see info.magnolia.rendering.template.type.TemplateTypeHelper
     * @see info.magnolia.rendering.template.type.DefaultTemplateTypes
     */
    abstract String getTemplateSubType();

    /**
     * Returns all children.
     */
    public List<NavigationItem> getItems() {
        final List<NavigationItem> items = new ArrayList();

        try {
            final Iterator<Node> categoryNodes = getNodes();

            while (categoryNodes.hasNext()) {
                final Node categoryNode = categoryNodes.next();

                items.add(new DefaultNavigationItem(categoryNode, this, navigationConfig, templatingFunctions, aggregationStateProvider) {
                    @Override
                    public String getLink() {
                        return tourServices.getCategoryLink(getParentPageNode(), NodeUtil.getName(getJCRNode()), getTemplateSubType());
                    }

                    @Override
                    public String getTitle() {
                        final Node node = getJCRNode();
                        return PropertyUtil.getString(node, Category.PROPERTY_NAME_DISPLAY_NAME, NodeUtil.getName(node));
                    }

                    @Override
                    public boolean isSelected() {
                        final String currentUri = aggregationStateProvider.get().getCurrentURI();
                        if (StringUtils.contains(currentUri, getLink())) {
                            return true;
                        }
                        return super.isSelected();
                    }
                });
            }
        } catch (RepositoryException e) {
            log.error("Could not retrieve the categories under '{}' parent.", getCategoryRootNode(), e);
        }

        return items;
    }

    /**
     * Returns all category nodes.
     */
    protected Iterator<Node> getNodes() throws RepositoryException {
        final Session session = MgnlContext.getJCRSession(CategorizationModule.CATEGORIZATION_WORKSPACE);
        final Node categoryRoot = session.getRootNode().getNode(getCategoryRootNode());

        return NodeUtil.getNodes(categoryRoot, new NodeTypePredicate(CategorizationNodeTypes.Category.NAME)).iterator();
    }

    /**
     * Returns the parent's page node (required in order to access the page node in inner sub items).
     */
    private Node getParentPageNode() {
        return this.getJCRNode();
    }

}
