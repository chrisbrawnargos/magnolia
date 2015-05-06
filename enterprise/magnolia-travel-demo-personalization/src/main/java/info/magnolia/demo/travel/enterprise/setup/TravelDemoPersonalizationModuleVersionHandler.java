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
package info.magnolia.demo.travel.enterprise.setup;

import info.magnolia.module.DefaultModuleVersionHandler;
import info.magnolia.module.InstallContext;
import info.magnolia.module.delta.OrderNodeToFirstPositionTask;
import info.magnolia.module.delta.Task;
import info.magnolia.personalization.variant.VariantManager;
import info.magnolia.repository.RepositoryConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Install TravelDemoPersonalization.
 */
public class TravelDemoPersonalizationModuleVersionHandler extends DefaultModuleVersionHandler {

    @Override
    protected List<Task> getExtraInstallTasks(InstallContext installContext) {
        final List<Task> tasks = new ArrayList<Task>();

        tasks.addAll(super.getExtraInstallTasks(installContext));

        // personalization
        // transform travel page into a page with personalization variants.
        tasks.add(new AddMixinTask("/travel", RepositoryConstants.WEBSITE, VariantManager.HAS_VARIANT_MIXIN));
        tasks.add(new OrderNodeToFirstPositionTask("Order travel page variants to first position.", "", RepositoryConstants.WEBSITE, "travel/variants"));

        return tasks;
    }

}