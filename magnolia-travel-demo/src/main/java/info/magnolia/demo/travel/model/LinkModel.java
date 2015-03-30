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

import info.magnolia.cms.util.PathUtil;
import info.magnolia.dam.api.Asset;
import info.magnolia.dam.templating.functions.DamTemplatingFunctions;
import info.magnolia.i18nsystem.SimpleTranslator;
import info.magnolia.jcr.util.PropertyUtil;
import info.magnolia.rendering.model.RenderingModel;
import info.magnolia.rendering.model.RenderingModelImpl;
import info.magnolia.rendering.template.RenderableDefinition;
import info.magnolia.repository.RepositoryConstants;
import info.magnolia.templating.functions.TemplatingFunctions;
import info.magnolia.util.EscapeUtil;

import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.apache.commons.lang3.StringUtils;

/**
 * Renderable Model definition dedicated to Links.
 *
 * @param <RD> Renderable definition.
 */
public class LinkModel<RD extends RenderableDefinition> extends RenderingModelImpl<RD> {

    public static final String INTERNAL = "internal";
    public static final String EXTERNAL = "external";
    public static final String DOWNLOAD = "download";
    public static final String PROPERTY_NAME_LINK_TYPE = "linkType";
    public static final String PROPERTY_NAME_TITLE = "title";
    public static final String PROPERTY_NAME_INTERNAL = PROPERTY_NAME_LINK_TYPE + INTERNAL;
    public static final String PROPERTY_NAME_EXTERNAL = PROPERTY_NAME_LINK_TYPE + EXTERNAL;
    public static final String PROPERTY_NAME_DOWNLOAD = PROPERTY_NAME_LINK_TYPE + DOWNLOAD;

    private final TemplatingFunctions templatingFunctions;
    private final DamTemplatingFunctions damFunctions;
    private final SimpleTranslator i18n;

    @Inject
    public LinkModel(Node content, RD definition, RenderingModel<?> parent, TemplatingFunctions templatingFunctions, DamTemplatingFunctions damFunctions, SimpleTranslator i18n) {
        super(content, definition, parent);
        this.templatingFunctions = templatingFunctions;
        this.damFunctions = damFunctions;
        this.i18n = i18n;
    }

    public String getLinkType() {
        return PropertyUtil.getString(content, PROPERTY_NAME_LINK_TYPE, "");
    }

    /**
     * Get the title for the link based on the linkType.
     */
    public String getTitle() {
        final String linkType = getLinkType();

        if (INTERNAL.equals(linkType)) {
            return getInternalTitle();
        } else if (DOWNLOAD.equals(linkType)) {
            return getDownloadTitle();
        } else if (EXTERNAL.equals(linkType)) {
            return getExternalTitle();
        }

        return "";
    }

    private String getDownloadTitle() {
        String title = PropertyUtil.getString(content, PROPERTY_NAME_TITLE, "");
        if (title.equals("")) {
            Asset asset = getAsset();
            title = asset.getTitle();
        }
        return title;
    }

    /**
     * Get the title for an internal link by returning the first of the
     * following items that are available: The Title field of the component. The
     * Title of the referenced page. The name of the referenced page.
     */
    private String getInternalTitle() {
        String title = PropertyUtil.getString(content, PROPERTY_NAME_TITLE, "");
        if (!title.equals("")) {
            return title;
        }
        Node linkNode = null;
        try {
            linkNode = templatingFunctions.getReferencedContent(content, PROPERTY_NAME_INTERNAL, RepositoryConstants.WEBSITE);
        } catch (RepositoryException e) {
            // continue.
        }
        String pageTitle = PropertyUtil.getString(linkNode, PROPERTY_NAME_TITLE, "");
        if (!pageTitle.equals("")) {
            return pageTitle;
        }

        String pageName = "";
        if (linkNode != null) {
            try {
                pageName = linkNode.getName();
            } catch (RepositoryException e) {
                // continue
            }
        }
        return pageName;
    }

    private String getExternalTitle() {
        String title = templatingFunctions.externalLinkTitle(content, PROPERTY_NAME_EXTERNAL, PROPERTY_NAME_TITLE);
        return EscapeUtil.escapeXss(title);
    }

    /**
     * Get the url for the link based on the linkType.
     */
    public String getLink() {
        final String linkType = getLinkType();

        if (INTERNAL.equals(linkType)) {
            return getInternalLink();
        } else if (DOWNLOAD.equals(linkType)) {
            return getDownloadLink();
        } else if (EXTERNAL.equals(linkType)) {
            return getExternalLink();
        }

        return "";
    }

    private String getInternalLink() {
        return templatingFunctions.link(RepositoryConstants.WEBSITE, PropertyUtil.getString(content, PROPERTY_NAME_INTERNAL));
    }

    private String getExternalLink() {
        final String linkRaw = PropertyUtil.getString(content, PROPERTY_NAME_EXTERNAL);
        if ("#".equals(linkRaw)) {
            return linkRaw;
        }
        final String link = templatingFunctions.externalLink(content, PROPERTY_NAME_EXTERNAL);
        return EscapeUtil.escapeXss(link);
    }

    private String getDownloadLink() {
        Asset asset = getAsset();
        if (asset != null) {
            return asset.getLink();
        }
        return null;
    }

    /**
     * Retrieve the Asset referenced by the identifier stored into the 'link'
     * properties.
     */
    public Asset getAsset() {
        String assetIdentifier = PropertyUtil.getString(content, PROPERTY_NAME_DOWNLOAD);
        if (StringUtils.isNotBlank(assetIdentifier)) {
            return damFunctions.getAsset(assetIdentifier);
        }
        return null;
    }

    public static String getFileExtension(String fileName) {
        return PathUtil.getExtension(fileName);
    }

    /**
     * Get a human friendly string for a file size.
     */
    public String getSizeString(long sizeBytes) {
        long sizeKB;
        sizeKB = (sizeBytes == 0) ? 0 : sizeBytes / 1024;

        if (sizeKB < (1024 * 3)) {
            return sizeKB + i18n.translate("downloadLink.kb");
        }

        long sizeMB = sizeKB / 1024;
        return sizeMB + i18n.translate("downloadLink.mb");
    }
}
