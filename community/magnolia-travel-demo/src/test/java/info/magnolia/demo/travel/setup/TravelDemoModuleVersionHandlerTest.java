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

import static info.magnolia.test.hamcrest.NodeMatchers.hasProperty;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import info.magnolia.cms.security.MgnlRoleManager;
import info.magnolia.cms.security.RoleManager;
import info.magnolia.cms.security.SecuritySupport;
import info.magnolia.cms.security.SecuritySupportImpl;
import info.magnolia.cms.security.operations.VoterBasedConfiguredAccessDefinition;
import info.magnolia.context.MgnlContext;
import info.magnolia.module.ModuleVersionHandler;
import info.magnolia.module.ModuleVersionHandlerTestCase;
import info.magnolia.module.model.Version;
import info.magnolia.repository.RepositoryConstants;
import info.magnolia.test.ComponentsTestUtil;
import info.magnolia.voting.voters.RoleBaseVoter;

import java.util.Collections;
import java.util.List;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link info.magnolia.demo.travel.setup.TravelDemoModuleVersionHandler}.
 */
public class TravelDemoModuleVersionHandlerTest extends ModuleVersionHandlerTestCase {

    private static final String PERMISSIONS_NODE_PATH = "/permissions";
    private static final String PERMISSIONS_VOTERS_DENIED_ROLES_NODE_PATH = PERMISSIONS_NODE_PATH.concat("/voters/deniedRoles");
    private static final String PERMISSIONS_VOTERS_DENIED_ROLES_ROLES_NODE_PATH = PERMISSIONS_VOTERS_DENIED_ROLES_NODE_PATH.concat("/roles");
    private static final String TRAVEL_DEMO_ADMINCENTRAL_ROLE = "travel-demo-admincentral";
    private static final String CONTACTS_APPS_CONTACTS_NODE_PATH = "/modules/contacts/apps/contacts";
    private static final String UIADMINCENTRAL_CONFIG_APPLAUNCH_GROUPS_STK_NODE_PATH = "/modules/ui-admincentral/config/appLauncherLayout/groups/stk";
    private static final String UIADMINCENTRAL_CONFIG_APPLAUNCH_GROUPS_MANAGE_NODE_PATH = "/modules/ui-admincentral/config/appLauncherLayout/groups/manage";

    private Session session;

    @Override
    protected String getModuleDescriptorPath() {
        return "/META-INF/magnolia/travel-demo.xml";
    }

    @Override
    protected ModuleVersionHandler newModuleVersionHandlerForTests() {
        return new TravelDemoModuleVersionHandler();
    }

    @Override
    protected List<String> getModuleDescriptorPathsForTests() {
        return Collections.singletonList("/META-INF/magnolia/core.xml");
    }

    /**
     * When finding the default site in site module (doesn't have any sub nodes nor properties), the demo should add
     * and set the extends property pointing to the STK site.
     */
    @Test
    public void updateTo08CreatesExtendsPropertyInSiteNodeWhenNodeIsEmpty() throws Exception {
        // GIVEN
        setupConfigNode("/modules/site/config/site");

        // WHEN
        executeUpdatesAsIfTheCurrentlyInstalledVersionWas(Version.parseVersion("0.7"));

        // THEN
        assertTrue(session.propertyExists("/modules/site/config/site/extends"));
        assertThat(session.getProperty("/modules/site/config/site/extends").getString(), is("/modules/travel-demo/config/travel"));
    }

    @Override
    protected String[] getExtraWorkspaces() {
        return new String[]{"dam"};
    }

    @Override
    protected String getExtraNodeTypes() {
        return "/mgnl-nodetypes/magnolia-dam-nodetypes.xml";
    }

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        addSupportForSetupModuleRepositoriesTask(null);
        SecuritySupportImpl securitySupport = new SecuritySupportImpl();
        RoleManager roleManager = new MgnlRoleManager();
        securitySupport.setRoleManager(roleManager);
        ComponentsTestUtil.setInstance(SecuritySupport.class, securitySupport);
        roleManager.createRole("superuser");
        session = MgnlContext.getJCRSession(RepositoryConstants.CONFIG);
        setupConfigNode(CONTACTS_APPS_CONTACTS_NODE_PATH);
        setupConfigNode(UIADMINCENTRAL_CONFIG_APPLAUNCH_GROUPS_STK_NODE_PATH);
        setupConfigNode(UIADMINCENTRAL_CONFIG_APPLAUNCH_GROUPS_MANAGE_NODE_PATH);
    }

    @Test
    public void demoRolesCanAccessPagesApp() throws Exception {
        // GIVEN
        setupConfigNode("/modules/pages/apps/pages");

        // WHEN
        executeUpdatesAsIfTheCurrentlyInstalledVersionWas(Version.parseVersion("0.7"));

        // THEN
        assertThat(session.getNode(SetupDemoRolesAndGroupsTask.PAGES_PERMISSIONS_ROLES), hasProperty(SetupDemoRolesAndGroupsTask.TRAVEL_DEMO_EDITOR_ROLE, SetupDemoRolesAndGroupsTask.TRAVEL_DEMO_EDITOR_ROLE));
        assertThat(session.getNode(SetupDemoRolesAndGroupsTask.PAGES_PERMISSIONS_ROLES), hasProperty(SetupDemoRolesAndGroupsTask.TRAVEL_DEMO_PUBLISHER_ROLE, SetupDemoRolesAndGroupsTask.TRAVEL_DEMO_PUBLISHER_ROLE));
    }

    @Test
    public void demoRoleCanAccessDamApp() throws Exception {
        // GIVEN
        setupConfigNode(SetupDemoRolesAndGroupsTask.DAM_PERMISSIONS_ROLES);

        // WHEN
        executeUpdatesAsIfTheCurrentlyInstalledVersionWas(Version.parseVersion("0.7"));

        // THEN
        assertThat(session.getNode(SetupDemoRolesAndGroupsTask.DAM_PERMISSIONS_ROLES), hasProperty(SetupDemoRolesAndGroupsTask.TRAVEL_DEMO_TOUR_EDITOR_ROLE, SetupDemoRolesAndGroupsTask.TRAVEL_DEMO_TOUR_EDITOR_ROLE));
    }

    @Test
    public void denyAccessPermissionsAfterCleanInstall() throws Exception {
        // GIVEN

        // WHEN
        executeUpdatesAsIfTheCurrentlyInstalledVersionWas(null);

        // THEN
        assertThatAccessPermissionsAreConfigured(CONTACTS_APPS_CONTACTS_NODE_PATH, TRAVEL_DEMO_ADMINCENTRAL_ROLE, true);
        assertThatAccessPermissionsAreConfigured(UIADMINCENTRAL_CONFIG_APPLAUNCH_GROUPS_STK_NODE_PATH, TRAVEL_DEMO_ADMINCENTRAL_ROLE, true);
        assertThatAccessPermissionsAreConfigured(UIADMINCENTRAL_CONFIG_APPLAUNCH_GROUPS_MANAGE_NODE_PATH, TRAVEL_DEMO_ADMINCENTRAL_ROLE, true);
    }

    @Test
    public void denyAccessPermissionsAfterUpdate() throws Exception {
        // GIVEN

        // WHEN
        executeUpdatesAsIfTheCurrentlyInstalledVersionWas(Version.parseVersion("0.7"));

        // THEN
        assertThatAccessPermissionsAreConfigured(CONTACTS_APPS_CONTACTS_NODE_PATH, TRAVEL_DEMO_ADMINCENTRAL_ROLE, true);
        assertThatAccessPermissionsAreConfigured(UIADMINCENTRAL_CONFIG_APPLAUNCH_GROUPS_STK_NODE_PATH, TRAVEL_DEMO_ADMINCENTRAL_ROLE, true);
        assertThatAccessPermissionsAreConfigured(UIADMINCENTRAL_CONFIG_APPLAUNCH_GROUPS_MANAGE_NODE_PATH, TRAVEL_DEMO_ADMINCENTRAL_ROLE, true);
    }

    private void assertThatAccessPermissionsAreConfigured(String path, String role, boolean not) throws RepositoryException {

        assertThat(session.getNode(path.concat(PERMISSIONS_NODE_PATH)), hasProperty("class", VoterBasedConfiguredAccessDefinition.class.getName()));
        assertThat(session.getNode(path.concat(PERMISSIONS_VOTERS_DENIED_ROLES_NODE_PATH)), hasProperty("class", RoleBaseVoter.class.getName()));
        assertThat(session.getNode(path.concat(PERMISSIONS_VOTERS_DENIED_ROLES_NODE_PATH)), hasProperty("not", Boolean.valueOf(not).toString()));
        assertThat(session.getNode(path.concat(PERMISSIONS_VOTERS_DENIED_ROLES_ROLES_NODE_PATH)), hasProperty(role, role));
    }

}
