/**
 * This file Copyright (c) 2015 Magnolia International
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

import static info.magnolia.jcr.nodebuilder.Ops.*;

import info.magnolia.jcr.nodebuilder.task.ErrorHandling;
import info.magnolia.jcr.nodebuilder.task.NodeBuilderTask;
import info.magnolia.jcr.util.NodeTypes;
import info.magnolia.module.DefaultModuleVersionHandler;
import info.magnolia.module.InstallContext;
import info.magnolia.module.delta.NodeExistsDelegateTask;
import info.magnolia.module.delta.Task;
import info.magnolia.repository.RepositoryConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Version handler.
 */
public class TravelDemoMultiSiteModuleVersionHandler extends DefaultModuleVersionHandler {

    private Task mappingAndDomainConfigurationTask = new NodeBuilderTask("", "", ErrorHandling.strict, RepositoryConstants.CONFIG, "/modules/multisite/config/sites",
            getNode("travel").then(
                    addNode("mappings", NodeTypes.ContentNode.NAME).then(
                            addNode("website", NodeTypes.ContentNode.NAME).then(
                                    addProperty("URIPrefix", ""),
                                    addProperty("handlePrefix", "/travel"),
                                    addProperty("repository", "website")
                            )
                    ),
                    addNode("domains", NodeTypes.ContentNode.NAME).then(
                            addNode("travel-demo", NodeTypes.ContentNode.NAME).then(
                                    addProperty("name", "travel-demo.magnolia-cms.com")
                            )
                    )
            ));

    @Override
    protected List<Task> getExtraInstallTasks(InstallContext installContext) {
        List<Task> tasks = new ArrayList<>(super.getExtraInstallTasks(installContext));
        tasks.add(new NodeExistsDelegateTask("", "/modules/multisite/config/sites/travel", mappingAndDomainConfigurationTask));
        return tasks;
    }

}
