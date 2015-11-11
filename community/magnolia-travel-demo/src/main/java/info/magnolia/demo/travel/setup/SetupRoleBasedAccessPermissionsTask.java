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

import info.magnolia.cms.security.operations.VoterBasedConfiguredAccessDefinition;
import info.magnolia.jcr.util.NodeTypes;
import info.magnolia.jcr.util.NodeUtil;
import info.magnolia.module.InstallContext;
import info.magnolia.module.delta.AbstractRepositoryTask;
import info.magnolia.module.delta.TaskExecutionException;
import info.magnolia.voting.voters.RoleBaseVoter;

import java.util.List;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;

/**
 * Task that sets access definition with role based voter.
 */
public class SetupRoleBasedAccessPermissionsTask extends AbstractRepositoryTask {

    public static final String PERMISSIONS_NODE_PATH = "/permissions";
    public static final String VOTERS_DENIED_ROLES = PERMISSIONS_NODE_PATH + "/voters/deniedRoles";
    public static final String VOTERS_ALLOWED_ROLES = PERMISSIONS_NODE_PATH + "/voters/allowedRoles";

    private String[] paths;
    private List<String> roles = Lists.newArrayList();
    private boolean allow;

    public SetupRoleBasedAccessPermissionsTask(String name, String description, List<String> roles, boolean allow, String... paths) {
        super(name, description);
        this.roles = roles;
        this.allow = allow;
        this.paths = paths;
    }

    @Override
    protected void doExecute(InstallContext installContext) throws RepositoryException, TaskExecutionException {

        Node config = installContext.getConfigJCRSession().getRootNode();

        for (String path : paths) {
            String relPath = StringUtils.removeStart(path, "/");
            if (config.hasNode(relPath)) {
                createRoleBasedPermissionsConfig(config, relPath);
            }
        }
    }

    private void createRoleBasedPermissionsConfig(Node config, String relPath) throws RepositoryException {
        Node rolePermissions = null;

        if (allow) {
            rolePermissions = NodeUtil.createPath(config, relPath.concat(VOTERS_ALLOWED_ROLES), NodeTypes.ContentNode.NAME);
        } else {
            rolePermissions = NodeUtil.createPath(config, relPath.concat(VOTERS_DENIED_ROLES), NodeTypes.ContentNode.NAME);
            rolePermissions.setProperty("not", "true");
        }

        config.getNode(relPath.concat(PERMISSIONS_NODE_PATH)).setProperty("class", VoterBasedConfiguredAccessDefinition.class.getName());
        rolePermissions.setProperty("class", RoleBaseVoter.class.getName());
        Node rolesNode = rolePermissions.addNode("roles", NodeTypes.ContentNode.NAME);

        for (String role : roles) {
            rolesNode.setProperty(role, role);
        }
    }
}
