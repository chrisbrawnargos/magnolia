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
import info.magnolia.demo.travel.tours.ToursModule;
import info.magnolia.demo.travel.tours.service.TourServices;
import info.magnolia.templating.functions.TemplatingFunctions;
import info.magnolia.templating.models.navigation.Navigation;
import info.magnolia.templating.models.navigation.NavigationConfig;

import javax.inject.Provider;
import javax.jcr.Node;
import javax.jcr.RepositoryException;

/**
 * A {@link info.magnolia.templating.models.navigation.NavigationItem} dedicated to tours types.
 */
public class TourTypeNavigationItem extends AbstractNavigationItem {

    public TourTypeNavigationItem(Node content, Navigation parent, NavigationConfig navigationConfig,
                                  TemplatingFunctions templatingFunctions,
                                  Provider<AggregationState> aggregationStateProvider,
                                  ToursModule toursModule, TourServices tourServices) throws RepositoryException {
        super(content, parent, navigationConfig, templatingFunctions, aggregationStateProvider, toursModule, tourServices);
    }

    @Override
    String getCategoryRootNode() {
        return toursModule.getTourTypeRootNode();
    }

    @Override
    String getTemplateSubType() {
        return ToursModule.TEMPLATE_SUB_TYPE_TOUR_OVERVIEW;
    }

}
