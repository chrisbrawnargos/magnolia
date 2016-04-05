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
package info.magnolia.demo.travel.marketingtags.setup;

import info.magnolia.module.DefaultModuleVersionHandler;
import info.magnolia.module.InstallContext;
import info.magnolia.module.delta.ArrayDelegateTask;
import info.magnolia.module.delta.BootstrapSingleResource;
import info.magnolia.module.delta.DeltaBuilder;
import info.magnolia.module.delta.IsModuleInstalledOrRegistered;
import info.magnolia.module.delta.PropertyValueDelegateTask;
import info.magnolia.module.delta.SetPropertyTask;
import info.magnolia.module.delta.Task;
import info.magnolia.module.delta.WarnTask;
import info.magnolia.repository.RepositoryConstants;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.ImportUUIDBehavior;

/**
 * Install the travel-demo-marketing-tags module.
 */
public class TravelDemoMarketingTagsModuleVersionHandler extends DefaultModuleVersionHandler {

    protected static final String MULTISITE_PROTOTYPE = "/modules/multisite/config/sites/travel/templates/prototype";
    protected static final String DEFAULT_MAIN_LOCATION = "/travel-demo/templates/pages/main.ftl";

    /**
     * Updates the multi site prototype to point to travel-demo-marketing-tags supplied main.ftl which includes the
     * areas required for script insertion.
     */
    private final Task updateMultiSiteDefinition = new PropertyValueDelegateTask("Replace Travel Demo prototype's main.ftl with an ftl that supports the marketing tags script insertion.", "", RepositoryConstants.CONFIG, MULTISITE_PROTOTYPE, "templateScript", DEFAULT_MAIN_LOCATION, true,
            new SetPropertyTask(RepositoryConstants.CONFIG, MULTISITE_PROTOTYPE, "templateScript", "/travel-demo-marketing-tags/templates/pages/main-marketing-tags.ftl"),
            new WarnTask("Marketing Tags compatible main template is not active.", "The multisite default prototype was not updated to reference the template provided by the Travel Demo Marketing Tags module because the prototype does not reference the expected '" + DEFAULT_MAIN_LOCATION + "' default template."));

    private final Task bootstrapAndUpdateMultiSiteDefinition = new IsModuleInstalledOrRegistered("Bootstrap scripts areas and replace Travel Demo prototype's main.ftl with an ftl that supports the marketing tags script insertion.", "multisite",
            new ArrayDelegateTask("Bootstrap bodyBeginScripts, bodyEndScripts and headerScripts areas into /config/modules/multisite/config/sites/travel/templates/prototype/areas.",
                    new BootstrapSingleResource("", "", "/info/magnolia/module/travel-demo-marketing-tags/setup/multisite/config.modules.multisite.config.sites.travel.templates.prototype.areas.bodyBeginScripts.xml", ImportUUIDBehavior.IMPORT_UUID_COLLISION_REPLACE_EXISTING),
                    new BootstrapSingleResource("", "", "/info/magnolia/module/travel-demo-marketing-tags/setup/multisite/config.modules.multisite.config.sites.travel.templates.prototype.areas.bodyEndScripts.xml", ImportUUIDBehavior.IMPORT_UUID_COLLISION_REPLACE_EXISTING),
                    new BootstrapSingleResource("", "", "/info/magnolia/module/travel-demo-marketing-tags/setup/multisite/config.modules.multisite.config.sites.travel.templates.prototype.areas.headerScripts.xml", ImportUUIDBehavior.IMPORT_UUID_COLLISION_REPLACE_EXISTING),
                    updateMultiSiteDefinition));

    public TravelDemoMarketingTagsModuleVersionHandler() {
        // We re-bootstrap every config/content item upon update
        register(DeltaBuilder.update("0.12", "")
                .addTask(bootstrapAndUpdateMultiSiteDefinition)
                .addTask(new BootstrapSingleResource("", "", "/mgnl-bootstrap-samples/travel-demo-marketing-tags/tags.Clicky-for-Travel-Demo.xml", ImportUUIDBehavior.IMPORT_UUID_COLLISION_REPLACE_EXISTING))
                .addTask(new BootstrapSingleResource("", "", "/mgnl-bootstrap-samples/travel-demo-marketing-tags/tags.Google-Analytics-for-Travel-Demo.xml", ImportUUIDBehavior.IMPORT_UUID_COLLISION_REPLACE_EXISTING))
        );
    }

    @Override
    protected List<Task> getExtraInstallTasks(InstallContext installContext) {
        final List<Task> tasks = new ArrayList<>();
        tasks.addAll(super.getExtraInstallTasks(installContext));
        tasks.add(bootstrapAndUpdateMultiSiteDefinition);
        return tasks;
    }

}