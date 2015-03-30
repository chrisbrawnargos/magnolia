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
package info.magnolia.demo.travel.model;

import info.magnolia.jcr.predicate.NodeTypePredicate;
import info.magnolia.jcr.util.NodeTypes;
import info.magnolia.jcr.util.NodeUtil;
import info.magnolia.jcr.util.PropertyUtil;
import info.magnolia.rendering.model.RenderingModel;
import info.magnolia.rendering.model.RenderingModelImpl;
import info.magnolia.rendering.template.AreaDefinition;
import info.magnolia.rendering.template.type.DefaultTemplateTypes;
import info.magnolia.templating.functions.TemplatingFunctions;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Model for creating the navigation bar for the travel demo.
 */
public class NavigationAreaModel extends RenderingModelImpl<AreaDefinition> {

    private static final Logger log = LoggerFactory.getLogger(NavigationAreaModel.class);

    private static final String CURRENT_ACTIVE_CSS_CLASS = "active";
    private static final String CHILD_ACTIVE_CSS_CLASS = "child-active";

    private final TemplatingFunctions templatingFunctions;

    @Inject
    public NavigationAreaModel(Node content, AreaDefinition definition, RenderingModel<?> parent, TemplatingFunctions templatingFunctions) {
        super(content, definition, parent);

        this.templatingFunctions = templatingFunctions;
    }

    public List<Page> getRootPages() {
        try {
            Node pageNode = templatingFunctions.page(content);
            Node home = templatingFunctions.findParentWithTemplateType(pageNode, DefaultTemplateTypes.HOME);
            return getChildPages(home);
        } catch (RepositoryException e) {
            log.error("Could not retrieve pages for navigation.", e);
        }
        return Collections.emptyList();
    }

    public List<Page> getChildPages() {
        try {
            Node currentPage = NodeUtil.getNearestAncestorOfType(content, NodeTypes.Page.NAME);
            int depth = currentPage.getDepth();

            if (depth == 2) {
                return getChildPages(currentPage);
            } else if (depth > 2) {
                Node parent = currentPage;
                while (depth > 2) {
                    parent = parent.getParent();
                    depth--;
                }
                return getChildPages(parent);
            }
        } catch (RepositoryException e) {
            log.error("Could not retrieve pages for navigation.", e);
        }
        return Collections.emptyList();
    }

    private List<Page> getChildPages(Node parent) throws RepositoryException {
        List<Page> pages = new LinkedList<Page>();

        Iterator<Node> it = NodeUtil.getNodes(parent, NodeTypes.Page.NAME).iterator();
        while (it.hasNext()) {
            Node pageNode = it.next();
            boolean hide = PropertyUtil.getBoolean(pageNode, Page.PROPERTY_NAME_HIDE_PAGE, false);
            if (!hide) {
                try {
                    Page page = getPage(pageNode);
                    pages.add(page);
                } catch (RepositoryException e) {
                    log.error("Could not create page object from node.", e);
                }
            }
        }
        return pages;
    }

    private Page getPage(Node node) throws RepositoryException {
        Page page = new Page();
        String title = PropertyUtil.getString(node, Page.PROPERTY_NAME_TITLE, node.getName());
        page.setName(title);
        if (isActive(node)) {
            page.setCssClass(CURRENT_ACTIVE_CSS_CLASS);
        } else if (isChildActive(node)) {
            page.setCssClass(CHILD_ACTIVE_CSS_CLASS);
        }
        page.setLink(templatingFunctions.link(node));
        return page;
    }

    private boolean isChildActive(Node node) throws RepositoryException {
        Node currentPage = NodeUtil.getNearestAncestorOfType(content, NodeTypes.Page.NAME);
        Iterator<Node> it = NodeUtil.collectAllChildren(node, new NodeTypePredicate(NodeTypes.Page.NAME)).iterator();
        while (it.hasNext()) {
            Node child = it.next();
            if (currentPage.getPath().equals(child.getPath())) {
                return true;
            }
        }
        return false;
    }

    private boolean isActive(Node pageNode) throws RepositoryException {
        Node currentPage = NodeUtil.getNearestAncestorOfType(content, NodeTypes.Page.NAME);

        return pageNode.getPath().equals(currentPage.getPath());
    }

    /**
     * Simple Pojo for page nodes.
     */
    public class Page {

        public static final String PROPERTY_NAME_HIDE_PAGE = "hideInNav";
        public static final String PROPERTY_NAME_TITLE = "title";

        private String name;
        private String link;
        private String cssClass;

        public String getName() {
            return name;
        }

        public String getLink() {
            return link;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public void setCssClass(String cssClass) {
            this.cssClass = cssClass;
        }

        public String getCssClass() {
            return cssClass;
        }
    }
}
