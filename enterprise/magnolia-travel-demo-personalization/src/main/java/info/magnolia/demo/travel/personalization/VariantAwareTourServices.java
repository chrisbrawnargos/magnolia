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
package info.magnolia.demo.travel.personalization;

import info.magnolia.dam.templating.functions.DamTemplatingFunctions;
import info.magnolia.demo.travel.tours.ToursModule;
import info.magnolia.demo.travel.tours.service.TourServices;
import info.magnolia.jcr.util.NodeTypes;
import info.magnolia.jcr.util.NodeUtil;
import info.magnolia.module.categorization.functions.CategorizationTemplatingFunctions;
import info.magnolia.personalization.decoration.PersonalizationNodeWrapper;
import info.magnolia.personalization.variant.VariantManager;
import info.magnolia.rendering.template.type.TemplateTypeHelper;
import info.magnolia.templating.functions.TemplatingFunctions;

import javax.inject.Inject;
import javax.jcr.Node;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Resolves the original node in case the current content node is a variant.
 */
public class VariantAwareTourServices extends TourServices {

    private static Logger log = LoggerFactory.getLogger(TourServices.class);

    private final VariantManager variantManager;

    @Inject
    public VariantAwareTourServices(VariantManager variantManager, ToursModule toursModule, TemplateTypeHelper templateTypeHelper, TemplatingFunctions templatingFunctions, CategorizationTemplatingFunctions categorizationTemplatingFunctions, DamTemplatingFunctions damFunctions) {
        super(toursModule, templateTypeHelper, templatingFunctions, categorizationTemplatingFunctions, damFunctions);
        this.variantManager = variantManager;
    }

    /**
     * We should probably not unwrap the node here, but as we do not override the {@link PersonalizationNodeWrapper#getPath()}
     * we end up with the wrong path in {@link TemplateTypeHelper#getContentListByTemplateIds} and the category based navigation is broken in the demo
     * for all page variants.
     */
    @Override
    public String getCategoryLink(Node content, String categoryName, String featureSubType) {
        try {
            if (variantManager.isVariant(content)) {
                content = NodeUtil.getNearestAncestorOfType(NodeUtil.deepUnwrap(content, PersonalizationNodeWrapper.class), NodeTypes.Page.NAME);
            }
            return super.getCategoryLink(content, categoryName, featureSubType);

        } catch (Exception e) {
            log.warn("Failed to determine whether node is variant.", featureSubType, e);
        }
        return StringUtils.EMPTY;

    }
}

