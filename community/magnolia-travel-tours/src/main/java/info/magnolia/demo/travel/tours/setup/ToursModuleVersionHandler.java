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
package info.magnolia.demo.travel.tours.setup;

import info.magnolia.demo.travel.tours.TourTemplatingFunctions;
import info.magnolia.module.DefaultModuleVersionHandler;
import info.magnolia.module.InstallContext;
import info.magnolia.module.delta.AddRoleToUserTask;
import info.magnolia.module.delta.ModuleDependencyBootstrapTask;
import info.magnolia.module.delta.OrderNodeBeforeTask;
import info.magnolia.module.delta.Task;
import info.magnolia.rendering.module.setup.InstallRendererContextAttributeTask;
import info.magnolia.repository.RepositoryConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link DefaultModuleVersionHandler} of the {@link info.magnolia.demo.travel.tours.ToursModule}.
 */
public class ToursModuleVersionHandler extends DefaultModuleVersionHandler {

    @Override
    protected List<Task> getExtraInstallTasks(InstallContext installContext) {
        final List<Task> tasks = new ArrayList<Task>();

        tasks.addAll(super.getExtraInstallTasks(installContext));

        tasks.add(new InstallRendererContextAttributeTask("rendering", "freemarker", "tourfn", TourTemplatingFunctions.class.getName()));
        tasks.add(new InstallRendererContextAttributeTask("site", "site", "tourfn", TourTemplatingFunctions.class.getName()));

        /* Order bootstrapped pages accordingly */
        tasks.add(new OrderNodeBeforeTask("", "", RepositoryConstants.WEBSITE, "/travel/tourType", "about"));
        tasks.add(new OrderNodeBeforeTask("", "", RepositoryConstants.WEBSITE, "/travel/destination", "about"));
        tasks.add(new OrderNodeBeforeTask("", "", RepositoryConstants.WEBSITE, "/travel/tour", "about"));

        /* Add travel-base role to user anonymous */
        tasks.add(new AddRoleToUserTask("Adds role 'travel-base' to user 'anonymous'", "anonymous", "travel-base"));

        tasks.add(new ModuleDependencyBootstrapTask("multisite"));

        return tasks;
    }

}