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

import info.magnolia.module.DefaultModuleVersionHandler;
import info.magnolia.module.InstallContext;
import info.magnolia.module.delta.ArrayDelegateTask;
import info.magnolia.module.delta.IsAuthorInstanceDelegateTask;
import info.magnolia.module.delta.SetPropertyTask;
import info.magnolia.module.delta.Task;
import info.magnolia.module.inplacetemplating.setup.TemplatesInstallTask;
import info.magnolia.module.model.Version;
import info.magnolia.module.resources.ResourceTypes;
import info.magnolia.module.resources.setup.InstallBinaryResourcesTask;
import info.magnolia.module.resources.setup.InstallTextResourceTask;
import info.magnolia.module.resources.setup.InstallTextResourcesTask;
import info.magnolia.repository.RepositoryConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is optional and lets you manager the versions of your module,
 * by registering "deltas" to maintain the module's configuration, or other type of content.
 * If you don't need this, simply remove the reference to this class in the module descriptor xml.
 */
public class TemplatingSampleProjectVersionHandler extends DefaultModuleVersionHandler {

    private static final String DEFAULT_URI_NODEPATH = "/modules/ui-admincentral/virtualURIMapping/default";
    private static final String DEFAULT_URI = "redirect:/travel.html";

    private static final String THEME_NAME = "travel-demo-theme";
    private static final String DEFAULT_THEME_PATH_PATTERN = "/%s/%s/.*";

    private static final String MTE_FTL_PATTERN = "/templates/.*\\.ftl";
    private static final TemplatesInstallTask TEMPLATES_INSTALL_TASK = new TemplatesInstallTask(MTE_FTL_PATTERN, true);

    @Override
    protected List<Task> getBasicInstallTasks(InstallContext installContext) {
        final List<Task> tasks = new ArrayList<Task>();
        tasks.addAll(super.getBasicInstallTasks(installContext));

        // Custom tasks that will install our theme
        tasks.add(new ArrayDelegateTask(String.format("Install '%s' theme", THEME_NAME), String.format("Installs '%s' theme with all its resources", THEME_NAME),
                new InstallTextResourcesTask("", "", InstallTextResourceTask.DEFAULT_ENCODING, String.format(DEFAULT_THEME_PATH_PATTERN, THEME_NAME, ResourceTypes.CSS_SUFFIX), ResourceTypes.PROCESSED_CSS, true, null, false, false),
                new InstallTextResourcesTask("", "", InstallTextResourceTask.DEFAULT_ENCODING, String.format(DEFAULT_THEME_PATH_PATTERN, THEME_NAME, ResourceTypes.JS_SUFFIX), ResourceTypes.PROCESSED_JS, true, null, false, false),
                // Twitter bootstrap
                new InstallBinaryResourcesTask("", "", String.format(DEFAULT_THEME_PATH_PATTERN, THEME_NAME, "libs/twitterbootstrap/fonts"), false, false),
                new InstallTextResourcesTask("", "", InstallTextResourceTask.DEFAULT_ENCODING, String.format(DEFAULT_THEME_PATH_PATTERN, THEME_NAME, "libs/twitterbootstrap/" + ResourceTypes.CSS_SUFFIX), ResourceTypes.PROCESSED_CSS, true, null, false, false),
                new InstallTextResourcesTask("", "", InstallTextResourceTask.DEFAULT_ENCODING, String.format(DEFAULT_THEME_PATH_PATTERN, THEME_NAME, "libs/twitterbootstrap/" + ResourceTypes.JS_SUFFIX), ResourceTypes.PROCESSED_JS, true, null, false, false),
                // Twitter bootstrap extras
                new InstallTextResourcesTask("", "", InstallTextResourceTask.DEFAULT_ENCODING, String.format(DEFAULT_THEME_PATH_PATTERN, THEME_NAME, "libs/twitterbootstrap-extras"), ResourceTypes.PROCESSED_JS, true, null, false, false)
        ));

        return tasks;
    }

    @Override
    protected List<Task> getExtraInstallTasks(InstallContext installContext) {
        final List<Task> tasks = new ArrayList<Task>();
        tasks.addAll(super.getExtraInstallTasks(installContext));
        tasks.add(TEMPLATES_INSTALL_TASK);
        tasks.add(new IsAuthorInstanceDelegateTask("Set default URI to home page", String.format("Sets default URI to point to '%s'", DEFAULT_URI), null,
                new SetPropertyTask(RepositoryConstants.CONFIG, DEFAULT_URI_NODEPATH, "toURI", DEFAULT_URI)));
        return tasks;
    }

    @Override
    protected List<Task> getDefaultUpdateTasks(Version forVersion) {
        final List<Task> tasks = new ArrayList<Task>();
        tasks.addAll(super.getDefaultUpdateTasks(forVersion));
        tasks.add(TEMPLATES_INSTALL_TASK);
        return tasks;
    }

}