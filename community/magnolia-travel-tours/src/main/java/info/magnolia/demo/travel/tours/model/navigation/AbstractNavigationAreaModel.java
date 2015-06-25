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

import info.magnolia.context.MgnlContext;
import info.magnolia.demo.travel.tours.ToursModule;
import info.magnolia.demo.travel.tours.service.Category;
import info.magnolia.demo.travel.tours.service.TourServices;
import info.magnolia.jcr.predicate.NodeTypePredicate;
import info.magnolia.jcr.util.NodeUtil;
import info.magnolia.module.categorization.CategorizationModule;
import info.magnolia.module.categorization.CategorizationNodeTypes;
import info.magnolia.rendering.model.RenderingModel;
import info.magnolia.rendering.model.RenderingModelImpl;
import info.magnolia.rendering.template.AreaDefinition;

import java.util.LinkedList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Model for creating the navigation bar for the travel demo. Uses Categories for TourTypes and Destination.
 */
public abstract class AbstractNavigationAreaModel extends RenderingModelImpl<AreaDefinition> {

    private static final Logger log = LoggerFactory.getLogger(AbstractNavigationAreaModel.class);

    protected final ToursModule toursModule;
    protected final TourServices tourServices;

    public AbstractNavigationAreaModel(Node content, AreaDefinition definition, RenderingModel<?> parent, TourServices tourServices, ToursModule toursModule) {
        super(content, definition, parent);

        this.toursModule = toursModule;
        this.tourServices = tourServices;
    }

    public abstract List<Category> getCategories();

    /**
     * @return The i18n key for the i18n property file.
     */
    public abstract String getTitleI18nKey();

    /**
     * Gets all child categories of the passed parent node. Used to retrieve all categories under e.g. 'Destinations'
     * or 'TourTypes'.
     *
     * @return a list {@link info.magnolia.demo.travel.tours.service.Category} Pojos.
     */
    protected List<Category> getCategories(String parent, String featureSubType) {
        try {
            final Session session = MgnlContext.getJCRSession(CategorizationModule.CATEGORIZATION_WORKSPACE);
            final Node categoryRoot = session.getRootNode().getNode(parent);
            final Iterable<Node> categoryNodes = NodeUtil.getNodes(categoryRoot, new NodeTypePredicate(CategorizationNodeTypes.Category.NAME));

            return tourServices.marshallCategoryNodes(NodeUtil.asList(categoryNodes), content, featureSubType);
        } catch (RepositoryException e) {
            log.error("Could not retrieve the categories under '{}' parent.", parent, e);
        }

        return new LinkedList<>();
    }

}
