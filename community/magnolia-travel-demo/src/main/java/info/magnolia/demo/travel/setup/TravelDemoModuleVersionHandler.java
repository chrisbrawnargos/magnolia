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
import info.magnolia.module.delta.BootstrapSingleModuleResource;
import info.magnolia.module.delta.BootstrapSingleResource;
import info.magnolia.module.delta.CheckAndModifyPropertyValueTask;
import info.magnolia.module.delta.CreateNodePathTask;
import info.magnolia.module.delta.CreateNodeTask;
import info.magnolia.module.delta.DeltaBuilder;
import info.magnolia.module.delta.IsAuthorInstanceDelegateTask;
import info.magnolia.module.delta.IsInstallSamplesTask;
import info.magnolia.module.delta.IsModuleInstalledOrRegistered;
import info.magnolia.module.delta.NodeExistsDelegateTask;
import info.magnolia.module.delta.PropertyExistsDelegateTask;
import info.magnolia.module.delta.PropertyValueDelegateTask;
import info.magnolia.module.delta.SetPropertyTask;
import info.magnolia.module.delta.Task;
import info.magnolia.module.site.setup.DefaultSiteExistsDelegateTask;
import info.magnolia.repository.RepositoryConstants;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.ImportUUIDBehavior;

/**
 * {@link DefaultModuleVersionHandler} for travel demo module.
 */
public class TravelDemoModuleVersionHandler extends DefaultModuleVersionHandler {

    private static final String DEFAULT_URI_NODEPATH = "/modules/ui-admincentral/virtualURIMapping/default";
    private static final String DEFAULT_URI = "redirect:/travel.html";

    private final Task setDefaultUriOnPublicInstance = new PropertyValueDelegateTask("Set default URI to home travel page, when current site is travel site", "/modules/site/config/site", "extends", "/modules/travel-demo/config/travel", false,
            new IsAuthorInstanceDelegateTask("Set default URI to home page", String.format("Sets default URI to point to '%s'", DEFAULT_URI), null,
                    new SetPropertyTask(RepositoryConstants.CONFIG, DEFAULT_URI_NODEPATH, "toURI", DEFAULT_URI)));

        private final Task setupTravelSiteAsActiveSite = new NodeExistsDelegateTask("Set travel demo as an active site", "/modules/site/config/site",
            new PropertyExistsDelegateTask("Check extends property and update or create it", "/modules/site/config/site", "extends",
                    new CheckAndModifyPropertyValueTask("/modules/site/config/site", "extends", "/modules/standard-templating-kit/config/site", "/modules/travel-demo/config/travel"),
                    new DefaultSiteExistsDelegateTask("", "",
                            new SetPropertyTask(RepositoryConstants.CONFIG, "/modules/site/config/site", "extends", "/modules/travel-demo/config/travel"))),
            new ArrayDelegateTask("",
                    new CreateNodeTask("", "/modules/site/config", "site", NodeTypes.ContentNode.NAME),
                    new SetPropertyTask(RepositoryConstants.CONFIG, "/modules/site/config/site", "extends", "/modules/travel-demo/config/travel")
            ));

    private final Task copySiteToMultiSiteAndMakeItFallback = new CopySiteToMultiSiteAndMakeItFallback();

    private final Task setupAccessDefinitionToUseRoleBaseVoter = new SetupAccessDefinitionToUseRoleBaseVoter("Deny access permissions to apps", "Deny access permissions to Contacts app, Web Dev group, Set Up group for travel-demo-admincentral role",
            "travel-demo-admincentral", true, "/modules/contacts/apps/contacts", "/modules/ui-admincentral/config/appLauncherLayout/groups/stk", "/modules/ui-admincentral/config/appLauncherLayout/groups/manage");

    public TravelDemoModuleVersionHandler() {
        register(DeltaBuilder.update("0.8", "")
                .addTask(new IsInstallSamplesTask("Re-Bootstrap website content for travel pages", "Re-bootstrap website content to account for all changes",
                        new BootstrapSingleResource("", "", "/mgnl-bootstrap-samples/travel-demo/website.travel.xml", ImportUUIDBehavior.IMPORT_UUID_COLLISION_REPLACE_EXISTING)))
                .addTask(new BootstrapSingleModuleResource("config.modules.travel-demo.config.travel.xml", ImportUUIDBehavior.IMPORT_UUID_COLLISION_REMOVE_EXISTING))
                .addTask(new BootstrapSingleModuleResource("config.modules.travel-demo.config.travel.xml", ImportUUIDBehavior.IMPORT_UUID_COLLISION_THROW))
                .addTask(new BootstrapSingleModuleResource("config.modules.site.config.themes.travel-demo-theme.xml", ImportUUIDBehavior.IMPORT_UUID_COLLISION_REPLACE_EXISTING))
                .addTask(setupTravelSiteAsActiveSite)
                .addTask(setDefaultUriOnPublicInstance)
                .addTask(new IsModuleInstalledOrRegistered("Enable travel site in multisite configuration", "multisite",
                        new NodeExistsDelegateTask("Check whether multisite can be enabled for travel demo", "/modules/travel-demo/config/travel",
                                new NodeExistsDelegateTask("Check whether travel demo was already copied in a previous version", "/modules/multisite/config/sites/default",
                                        new IsModuleInstalledOrRegistered("", "tours", (Task) null, new ArrayDelegateTask("", "",
                                                new RemoveTravelDemoSiteFromMultiSite(),
                                                copySiteToMultiSiteAndMakeItFallback)),
                                        new NodeExistsDelegateTask("Check whether travel node in multisite does not exist.", "/modules/multisite/config/sites/travel", null, copySiteToMultiSiteAndMakeItFallback)))))
                .addTask(new NodeExistsDelegateTask("Configure permissions for access to Pages app", "/modules/pages/apps/pages",
                        new ArrayDelegateTask("Configure permissions for access to Pages app",
                                new CreateNodePathTask("", "", RepositoryConstants.CONFIG, "/modules/pages/apps/pages/permissions/roles", NodeTypes.ContentNode.NAME),
                                new SetPropertyTask(RepositoryConstants.CONFIG, SetupDemoRolesAndGroupsTask.PAGES_PERMISSIONS_ROLES, SetupDemoRolesAndGroupsTask.TRAVEL_DEMO_EDITOR_ROLE, SetupDemoRolesAndGroupsTask.TRAVEL_DEMO_EDITOR_ROLE),
                                new SetPropertyTask(RepositoryConstants.CONFIG, SetupDemoRolesAndGroupsTask.PAGES_PERMISSIONS_ROLES, SetupDemoRolesAndGroupsTask.TRAVEL_DEMO_PUBLISHER_ROLE, SetupDemoRolesAndGroupsTask.TRAVEL_DEMO_PUBLISHER_ROLE))))
                .addTask(new NodeExistsDelegateTask("Add permission for access to Dam app", SetupDemoRolesAndGroupsTask.DAM_PERMISSIONS_ROLES,
                        new SetPropertyTask(RepositoryConstants.CONFIG, SetupDemoRolesAndGroupsTask.DAM_PERMISSIONS_ROLES, SetupDemoRolesAndGroupsTask.TRAVEL_DEMO_TOUR_EDITOR_ROLE, SetupDemoRolesAndGroupsTask.TRAVEL_DEMO_TOUR_EDITOR_ROLE)))
                .addTask(setupAccessDefinitionToUseRoleBaseVoter)
        );
    }

    @Override
    protected List<Task> getExtraInstallTasks(InstallContext installContext) {
        final List<Task> tasks = new ArrayList<>();
        tasks.addAll(super.getExtraInstallTasks(installContext));
        tasks.add(setupTravelSiteAsActiveSite);
        tasks.add(setDefaultUriOnPublicInstance);
        tasks.add(new IsModuleInstalledOrRegistered("Enable travel site in multisite configuration", "multisite",
                new NodeExistsDelegateTask("Check whether multisite can be enabled for travel demo", "/modules/travel-demo/config/travel",
                        copySiteToMultiSiteAndMakeItFallback)));
        tasks.add(new SetupDemoRolesAndGroupsTask());
        tasks.add(setupAccessDefinitionToUseRoleBaseVoter);
        return tasks;
    }

}