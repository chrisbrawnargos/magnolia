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

import static info.magnolia.demo.travel.setup.SetupDemoRolesTask.*;
import static info.magnolia.test.hamcrest.NodeMatchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

import info.magnolia.cms.core.HierarchyManager;
import info.magnolia.jcr.util.NodeTypes;
import info.magnolia.jcr.util.NodeUtil;
import info.magnolia.module.InstallContext;
import info.magnolia.test.mock.jcr.MockSession;

import javax.jcr.Session;

import org.junit.Before;
import org.junit.Test;


public class SetupDemoRolesTaskTest {

    private Session session;
    private InstallContext ctx;
    private HierarchyManager hm;

    @Before
    public void setUp() throws Exception {
        session = new MockSession("config");
        ctx = mock(InstallContext.class);
        hm = mock(HierarchyManager.class);
        when(ctx.getJCRSession(eq("config"))).thenReturn(session);
        when(ctx.getHierarchyManager(eq("config"))).thenReturn(hm);
    }

    @Test
    public void demoRolesCanActivatePages() throws Exception {
        // GIVEN
        NodeUtil.createPath(session.getRootNode(), PAGES_ACTIVATE_ACCESS_ROLES, NodeTypes.ContentNode.NAME);

        // WHEN
        new SetupDemoRolesTask().execute(ctx);

        // THEN
        assertThat(session.getNode(PAGES_ACTIVATE_ACCESS_ROLES), hasProperty(TRAVEL_DEMO_EDITOR_ROLE, TRAVEL_DEMO_EDITOR_ROLE));
        assertThat(session.getNode(PAGES_ACTIVATE_ACCESS_ROLES), hasProperty(TRAVEL_DEMO_PUBLISHER_ROLE, TRAVEL_DEMO_PUBLISHER_ROLE));
    }

    @Test
    public void demoRolesCanActivateAssets() throws Exception {
        // GIVEN
        NodeUtil.createPath(session.getRootNode(), DAM_ACTIVATE_ACCESS_ROLES, NodeTypes.ContentNode.NAME);

        // WHEN
        new SetupDemoRolesTask().execute(ctx);

        // THEN
        assertThat(session.getNode(DAM_ACTIVATE_ACCESS_ROLES), hasProperty(TRAVEL_DEMO_EDITOR_ROLE, TRAVEL_DEMO_EDITOR_ROLE));
        assertThat(session.getNode(DAM_ACTIVATE_ACCESS_ROLES), hasProperty(TRAVEL_DEMO_PUBLISHER_ROLE, TRAVEL_DEMO_PUBLISHER_ROLE));
    }

    @Test
    public void demoRolesCanAccessDam() throws Exception {
        // GIVEN
        NodeUtil.createPath(session.getRootNode(), DAM_PERMISSIONS_ROLES, NodeTypes.ContentNode.NAME);

        // WHEN
        new SetupDemoRolesTask().execute(ctx);

        // THEN
        assertThat(session.getNode(DAM_PERMISSIONS_ROLES), hasProperty(TRAVEL_DEMO_EDITOR_ROLE, TRAVEL_DEMO_EDITOR_ROLE));
        assertThat(session.getNode(DAM_PERMISSIONS_ROLES), hasProperty(TRAVEL_DEMO_PUBLISHER_ROLE, TRAVEL_DEMO_PUBLISHER_ROLE));
    }

    @Test
    public void installTaskCreatesPathToAccessRolesIfNotExisting() throws Exception {
        // GIVEN

        // WHEN
        new SetupDemoRolesTask().execute(ctx);

        // THEN
        assertThat(session.nodeExists(PAGES_ACTIVATE_ACCESS_ROLES), is(true));
        assertThat(session.nodeExists(DAM_ACTIVATE_ACCESS_ROLES), is(true));
    }

    @Test
    public void travelDemoPublisherCanAccessWorkflowItems() throws Exception {
        // GIVEN
        NodeUtil.createPath(session.getRootNode(), WORKFLOW_JBPM_PUBLISH_GROUPS, NodeTypes.ContentNode.NAME);
        when(ctx.isModuleRegistered(eq("workflow-jbpm"))).thenReturn(true);
        when(hm.isExist(eq("/modules/workflow-jbpm/"))).thenReturn(true);

        // WHEN
        new SetupDemoRolesTask().execute(ctx);

        // THEN
        assertThat(session.getNode(WORKFLOW_JBPM_PUBLISH_GROUPS), hasProperty(TRAVEL_DEMO_PUBLISHERS_GROUP, TRAVEL_DEMO_PUBLISHERS_GROUP));
    }

    @Test
    public void doNotAddRoleForWorkflowIfWorkflowJbpmIsNotInstalled() throws Exception {
        // GIVEN
        when(ctx.isModuleRegistered(eq("workflow-jbpm"))).thenReturn(false);
        when(hm.isExist(eq("/modules/workflow-jbpm/"))).thenReturn(false);

        // WHEN
        new SetupDemoRolesTask().execute(ctx);

        // THEN
        assertThat(session.propertyExists(WORKFLOW_JBPM_PUBLISH_GROUPS + "/" + TRAVEL_DEMO_PUBLISHERS_GROUP), is(false));
    }

}