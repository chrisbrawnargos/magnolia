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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import info.magnolia.context.MgnlContext;
import info.magnolia.module.ModuleVersionHandler;
import info.magnolia.module.ModuleVersionHandlerTestCase;
import info.magnolia.module.model.Version;
import info.magnolia.repository.RepositoryConstants;

import java.util.Collections;
import java.util.List;

import javax.jcr.Session;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link TravelDemoModuleVersionHandler}.
 */
public class TravelDemoModuleVersionHandlerTest extends ModuleVersionHandlerTestCase {

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

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        this.session = MgnlContext.getJCRSession(RepositoryConstants.CONFIG);
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

}