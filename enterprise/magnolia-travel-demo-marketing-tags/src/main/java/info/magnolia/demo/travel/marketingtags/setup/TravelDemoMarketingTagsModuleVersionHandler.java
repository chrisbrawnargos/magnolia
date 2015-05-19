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
package info.magnolia.demo.travel.marketingtags.setup;

import info.magnolia.module.DefaultModuleVersionHandler;
import info.magnolia.module.InstallContext;
import info.magnolia.module.delta.PropertyValueDelegateTask;
import info.magnolia.module.delta.SetPropertyTask;
import info.magnolia.module.delta.Task;
import info.magnolia.module.delta.WarnTask;
import info.magnolia.repository.RepositoryConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Install the travel-demo-marketing-tags module.
 */
public class TravelDemoMarketingTagsModuleVersionHandler extends DefaultModuleVersionHandler {

    protected static final String MULTISITE_PROTOTYPE = "/modules/multisite/config/sites/default/templates/prototype";
    protected static final String DEFAULT_MAIN_LOCATION = "/travel-demo/templates/pages/main.ftl";

    @Override
    protected List<Task> getExtraInstallTasks(InstallContext installContext) {
        final List<Task> tasks = new ArrayList<Task>();

        tasks.addAll(super.getExtraInstallTasks(installContext));

        // Update the Multisite prototype to point to travel-demo-marketing-tags supplied main.ftl which includes the areas required for script insertion.
        tasks.add(new PropertyValueDelegateTask("Replace Travel Demo prototype's main.ftl with an ftl that supports the marketing tags script insertion.", "", RepositoryConstants.CONFIG, MULTISITE_PROTOTYPE, "templateScript", DEFAULT_MAIN_LOCATION, true,
                new SetPropertyTask(RepositoryConstants.CONFIG, MULTISITE_PROTOTYPE, "templateScript", "/travel-demo-marketing-tags/templates/pages/main-marketing-tags.ftl"),
                new WarnTask("Marketing Tags compatible main template is not active.", "The multisite default prototype was not updated to reference the template provided by the Travel Demo Marketing Tags module because the prototype does not reference the expected '" + DEFAULT_MAIN_LOCATION + "' default template.")
                ));

        return tasks;
    }

}