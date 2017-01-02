/**
 * This file Copyright (c) 2015-2017 Magnolia International
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

import info.magnolia.demo.travel.tours.ToursModule;
import info.magnolia.demo.travel.tours.service.Category;
import info.magnolia.demo.travel.tours.service.TourServices;
import info.magnolia.rendering.model.RenderingModel;
import info.magnolia.rendering.template.AreaDefinition;

import java.util.List;

import javax.inject.Inject;
import javax.jcr.Node;

/**
 * Model for creating the navigation bar for the travel demo. Uses Categories for TourTypes.
 */
public class TourTypesNavigationAreaModel extends AbstractNavigationAreaModel {

    @Inject
    public TourTypesNavigationAreaModel(Node content, AreaDefinition definition, RenderingModel<?> parent, TourServices tourServices, ToursModule toursModule) {
        super(content, definition, parent, tourServices, toursModule);
    }

    @Override
    public List<Category> getCategories() {
        return getCategories(toursModule.getTourTypeRootNode(), ToursModule.TEMPLATE_SUB_TYPE_TOUR_OVERVIEW);
    }

    @Override
    public String getTitleI18nKey() {
        return "navigation.tourTypes.title";
    }

}
