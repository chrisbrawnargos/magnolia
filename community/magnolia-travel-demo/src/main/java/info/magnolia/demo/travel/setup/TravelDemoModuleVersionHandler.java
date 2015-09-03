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
package info.magnolia.demo.travel.setup;

import info.magnolia.jcr.util.NodeTypes;
import info.magnolia.module.DefaultModuleVersionHandler;
import info.magnolia.module.InstallContext;
import info.magnolia.module.delta.ArrayDelegateTask;
import info.magnolia.module.delta.CheckAndModifyPropertyValueTask;
import info.magnolia.module.delta.CopyNodeTask;
import info.magnolia.module.delta.CreateNodeTask;
import info.magnolia.module.delta.DeltaBuilder;
import info.magnolia.module.delta.IsAuthorInstanceDelegateTask;
import info.magnolia.module.delta.IsModuleInstalledOrRegistered;
import info.magnolia.module.delta.NodeExistsDelegateTask;
import info.magnolia.module.delta.PartialBootstrapTask;
import info.magnolia.module.delta.PropertyExistsDelegateTask;
import info.magnolia.module.delta.SetPropertyTask;
import info.magnolia.module.delta.Task;
import info.magnolia.repository.RepositoryConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link DefaultModuleVersionHandler} for travel demo module.
 */
public class TravelDemoModuleVersionHandler extends DefaultModuleVersionHandler {

    private static final String DEFAULT_URI_NODEPATH = "/modules/ui-admincentral/virtualURIMapping/default";
    private static final String DEFAULT_URI = "redirect:/travel.html";

    public TravelDemoModuleVersionHandler () {
        register(DeltaBuilder.update("0.7", "")
            .addTask(new PartialBootstrapTask("Re-Bootstrap publish messageView", "/mgnl-bootstrap-samples/travel-demo/website.travel.xml", "/travel/contact/main/01"))
        );
    }

    @Override
    protected List<Task> getExtraInstallTasks(InstallContext installContext) {
        final List<Task> tasks = new ArrayList<Task>();
        tasks.addAll(super.getExtraInstallTasks(installContext));
        tasks.add(new NodeExistsDelegateTask("Set travel demo as an active site", "/modules/site/config/site",
                new CheckAndModifyPropertyValueTask("/modules/site/config/site", "extends", "/modules/standard-templating-kit/config/site", "/modules/travel-demo/config/travel"),
                new ArrayDelegateTask("",
                        new CreateNodeTask("", "/modules/site/config", "site", NodeTypes.ContentNode.NAME),
                        new SetPropertyTask(RepositoryConstants.CONFIG, "/modules/site/config/site", "extends", "/modules/travel-demo/config/travel"),
                        new IsAuthorInstanceDelegateTask("Set default URI to home page", String.format("Sets default URI to point to '%s'", DEFAULT_URI), null, new SetPropertyTask(RepositoryConstants.CONFIG, DEFAULT_URI_NODEPATH, "toURI", DEFAULT_URI))
                )));
        tasks.add(new IsModuleInstalledOrRegistered("Enable travel site in multisite configuration", "multisite",
                new NodeExistsDelegateTask("Check whether multisite can be enabled for travel demo", "/modules/travel-demo/config/travel",
                        new ArrayDelegateTask("", "",
                                new CopyNodeTask("Copy site definition to multisite", "/modules/travel-demo/config/travel", "/modules/multisite/config/sites/travel", false),
                                new PropertyExistsDelegateTask("Set travel demo as fallback site if possible", "/modules/multisite/config/sites/fallback", "extends",
                                        new CheckAndModifyPropertyValueTask("/modules/multisite/config/sites/fallback", "extends", "../default", "../travel"),
                                        new SetPropertyTask(RepositoryConstants.CONFIG, "/modules/multisite/config/sites/fallback", "extends", "../travel"))))));
        tasks.add(new SetupDemoRolesAndGroupsTask());
        return tasks;
    }

}