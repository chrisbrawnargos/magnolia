/**
 * This file Copyright (c) 2015-2016 Magnolia International
 * Ltd.  (http://www.magnolia-cms.com). All rights reserved.
 *
 *
 * This program and the accompanying materials are made
 * available under the terms of the Magnolia Network Agreement
 * which accompanies this distribution, and is available at
 * http://www.magnolia-cms.com/mna.html
 *
 * Any modifications to this file must keep this entire header
 * intact.
 *
 */
package info.magnolia.demo.travel.multisite.setup;

import info.magnolia.module.DefaultModuleVersionHandler;
import info.magnolia.module.InstallContext;
import info.magnolia.module.delta.ArrayDelegateTask;
import info.magnolia.module.delta.BootstrapConditionally;
import info.magnolia.module.delta.CheckAndModifyPropertyValueTask;
import info.magnolia.module.delta.DeltaBuilder;
import info.magnolia.module.delta.NodeExistsDelegateTask;
import info.magnolia.module.delta.RemoveNodeTask;
import info.magnolia.module.delta.SetPropertyTask;
import info.magnolia.module.delta.Task;
import info.magnolia.repository.RepositoryConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Default {@link info.magnolia.module.ModuleVersionHandler} for travel-demo multi site example.
 */
public class TravelDemoMultiSiteModuleVersionHandler extends DefaultModuleVersionHandler {

    private final Task mappingAndDomainConfigurationTask = new ArrayDelegateTask("Add domain and mapping configuration to travel site definition in multisite", "",
            new BootstrapConditionally("Add domain configuration to travel site definition in multisite", "/info/magnolia/demo/travel/multisite/setup/config.modules.multisite.config.sites.travel.domains.xml"),
            new BootstrapConditionally("Add mapping configuration to travel site definition in multisite", "/info/magnolia/demo/travel/multisite/setup/config.modules.multisite.config.sites.travel.mappings.xml"));

    public TravelDemoMultiSiteModuleVersionHandler() {
        register(DeltaBuilder.update("1.1", "")
                .addTask(new NodeExistsDelegateTask("Update travel-related sites (travel & sportstation)", "/modules/multisite/config/sites/travel",
                        new ArrayDelegateTask("", "",
                                new CheckAndModifyPropertyValueTask("/modules/multisite/config/sites/sportstation", "extends", "../default", "../travel"),
                                mappingAndDomainConfigurationTask)))
                .addTask(new NodeExistsDelegateTask("Remove any existing prototype from sportstation", "/modules/multisite/config/sites/sportstation/templates/prototype",
                        new ArrayDelegateTask("",
                                new RemoveNodeTask("", "/modules/multisite/config/sites/sportstation/templates/prototype"),
                                new SetPropertyTask(RepositoryConstants.CONFIG, "/modules/multisite/config/sites/sportstation/templates", "prototypeId", "sportstation:pages/prototype"))))
        );
    }

    @Override
    protected List<Task> getExtraInstallTasks(InstallContext installContext) {
        final List<Task> tasks = new ArrayList<>();
        tasks.addAll(super.getExtraInstallTasks(installContext));
        tasks.add(new NodeExistsDelegateTask("Add mapping and domain configuration to travel site definition in multisite", "/modules/multisite/config/sites/travel", mappingAndDomainConfigurationTask));
        return tasks;
    }

}
