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

import info.magnolia.dam.api.AssetRendition;
import info.magnolia.dam.templating.functions.DamTemplatingFunctions;
import info.magnolia.imaging.ImagingSupport;
import info.magnolia.jcr.util.PropertyUtil;
import info.magnolia.rendering.model.RenderingModel;
import info.magnolia.rendering.model.RenderingModelImpl;
import info.magnolia.rendering.template.RenderableDefinition;

import javax.inject.Inject;
import javax.jcr.Node;

import org.apache.commons.lang3.StringUtils;

/**
 * Simple image model.
 *
 * @param <RD> The {@link RenderableDefinition} of the model.
 */
public class ImageModel<RD extends RenderableDefinition> extends RenderingModelImpl<RD> {

    private static final String PROPERTY_NAME_IMAGE_VARIATION = "imageVariation";

    private String PROPERTY_NAME_IMAGE = "image";

    private final DamTemplatingFunctions damTemplatingFunctions;

    @Inject
    public ImageModel(Node content, RD definition, RenderingModel<?> parent, DamTemplatingFunctions damTemplatingFunctions) {
        super(content, definition, parent);
        this.damTemplatingFunctions = damTemplatingFunctions;
    }

    public AssetRendition getImage() {
        return getImage(getImageVariationName());
    }

    public AssetRendition getImage(String variationName) {
        final String assetIdentifier = PropertyUtil.getString(content, getImageName());

        if (StringUtils.isNotBlank(assetIdentifier)) {
            return damTemplatingFunctions.getRendition(assetIdentifier, variationName);
        }

        return null;
    }

    public String getImageVariationName() {
        final String variationName = (String) definition.getParameters().get(PROPERTY_NAME_IMAGE_VARIATION);
        return StringUtils.defaultIfBlank(variationName, ImagingSupport.VARIATION_ORIGINAL);
    }

    public String getImageName() {
        return PROPERTY_NAME_IMAGE;
    }

}
