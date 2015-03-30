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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

import info.magnolia.cms.core.AggregationState;
import info.magnolia.cms.i18n.I18nContentSupport;
import info.magnolia.dam.api.Asset;
import info.magnolia.dam.templating.functions.DamTemplatingFunctions;
import info.magnolia.i18nsystem.SimpleTranslator;
import info.magnolia.rendering.template.configured.ConfiguredTemplateDefinition;
import info.magnolia.rendering.template.type.TemplateTypeHelper;
import info.magnolia.repository.RepositoryConstants;
import info.magnolia.templating.functions.TemplatingFunctions;
import info.magnolia.test.ComponentsTestUtil;
import info.magnolia.test.mock.MockUtil;
import info.magnolia.test.mock.jcr.MockSession;
import info.magnolia.test.mock.jcr.SessionTestUtil;

import java.io.IOException;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.google.inject.Provider;

/**
 * Tests for {@link LinkModel} focusing on returning the proper title.
 */
public class LinkModelTest {

    private DamTemplatingFunctions damTemplatingFunctions;
    private TemplatingFunctions templatingFunctions;
    private SimpleTranslator i18n;
    private Session session;
    private Node rootNode;

    @Before
    public void setUp() throws Exception {
        this.damTemplatingFunctions = mock(DamTemplatingFunctions.class);
        this.session = new MockSession(RepositoryConstants.WEBSITE);
        this.rootNode = session.getRootNode();
        this.i18n = mock(SimpleTranslator.class);

        ComponentsTestUtil.setInstance(I18nContentSupport.class, mock(I18nContentSupport.class));
        MockUtil.initMockContext();
        MockUtil.setSessionAndHierarchyManager(session);

        this.templatingFunctions = new TemplatingFunctions(new Provider<AggregationState>() {
            @Override
            public AggregationState get() {
                return mock(AggregationState.class);
            }
        }, mock(TemplateTypeHelper.class));
    }

    @Test
    public void testExternalLinkWithTitle() throws RepositoryException, IOException {
        // GIVEN
        final String testTitle = "ABC";
        final String testUrl = "http://magestic.com";
        final Session session = SessionTestUtil.createSession("test", "/testLink.linkType=external");
        final Node content = session.getNode("/testLink");

        content.setProperty("title", testTitle);
        content.setProperty(LinkModel.PROPERTY_NAME_EXTERNAL, testUrl);

        // WHEN
        LinkModel<ConfiguredTemplateDefinition> linkModel = new LinkModel(content, null, null, templatingFunctions, damTemplatingFunctions, i18n);

        String title = linkModel.getTitle();
        String link = linkModel.getLink();

        // THEN
        assertThat(link, is(testUrl));
        assertThat(title, is(testTitle));
    }

    @Test
    public void testExternalLinkWithoutTitle() throws RepositoryException, IOException {
        // GIVEN
        final String testUrl = "http://magestic.com";
        final Session session = SessionTestUtil.createSession("test", "/testLink.linkType=external");
        final Node content = session.getNode("/testLink");

        content.setProperty(LinkModel.PROPERTY_NAME_EXTERNAL, testUrl);

        // WHEN
        LinkModel<ConfiguredTemplateDefinition> linkModel = new LinkModel(content, null, null, templatingFunctions, damTemplatingFunctions, i18n);

        String title = linkModel.getTitle();
        String link = linkModel.getLink();

        // THEN
        assertThat(link, is(testUrl));
        assertThat(title, is(testUrl));
    }

    @Test
    public void testInternalTitleWithTitle() throws RepositoryException, IOException {
        // GIVEN
        final String testTitle = "ABC";
        final Session session = SessionTestUtil.createSession("test", "/testLink.linkType=internal", "/testLink.title=" + testTitle);
        final Node content = session.getNode("/testLink");

        // WHEN
        LinkModel<ConfiguredTemplateDefinition> linkModel = new LinkModel(content, null, null, templatingFunctions, damTemplatingFunctions, i18n);

        String title = linkModel.getTitle();

        // THEN
        assertThat(title, is(testTitle));
    }

    @Test
    public void testInternalTitleWithoutTitle() throws RepositoryException, IOException {
        // GIVEN
        this.templatingFunctions = spy(templatingFunctions);

        when(templatingFunctions.encode(Mockito.any(Node.class))).thenAnswer(new Answer<Node>() {
            @Override
            public Node answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                return (Node) args[0];
            }
        });

        when(templatingFunctions.wrapForI18n(Mockito.any(Node.class))).thenAnswer(new Answer<Node>() {
            @Override
            public Node answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                return (Node) args[0];
            }
        });

        final String linkedTitle = "LinkedOne";
        final Node content = rootNode.addNode("testLink");
        final Node linkedNode = rootNode.addNode("testLinked");

        content.setProperty(LinkModel.PROPERTY_NAME_LINK_TYPE, LinkModel.INTERNAL);
        content.setProperty(LinkModel.PROPERTY_NAME_INTERNAL, linkedNode.getIdentifier());

        linkedNode.setProperty(LinkModel.PROPERTY_NAME_TITLE, linkedTitle);

        LinkModel<ConfiguredTemplateDefinition> linkModel = new LinkModel(content, null, null, templatingFunctions, damTemplatingFunctions, i18n);


        // WHEN
        String title = linkModel.getTitle();

        // THEN
        assertThat(title, is(linkedTitle));
    }

    @Test
    public void testInternalTitleWithoutTitleLinkWithoutTitle() throws RepositoryException, IOException {
        // GIVEN
        this.templatingFunctions = spy(templatingFunctions);

        when(templatingFunctions.encode(Mockito.any(Node.class))).thenAnswer(new Answer<Node>() {
            @Override
            public Node answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                return (Node) args[0];
            }
        });

        when(templatingFunctions.wrapForI18n(Mockito.any(Node.class))).thenAnswer(new Answer<Node>() {
            @Override
            public Node answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                return (Node) args[0];
            }
        });

        final Node content = rootNode.addNode("testLink");
        final Node linkedNode = rootNode.addNode("testLinked");

        content.setProperty(LinkModel.PROPERTY_NAME_LINK_TYPE, LinkModel.INTERNAL);
        content.setProperty(LinkModel.PROPERTY_NAME_INTERNAL, linkedNode.getIdentifier());

        LinkModel<ConfiguredTemplateDefinition> linkModel = new LinkModel(content, null, null, templatingFunctions, damTemplatingFunctions, i18n);

        // WHEN
        String title = linkModel.getTitle();

        // THEN
        assertThat(title, is("testLinked"));
    }

    @Test
    public void testDownloadTitleWithTitle() throws RepositoryException, IOException {
        // GIVEN
        final String testTitle = "ABC";
        final Session session = SessionTestUtil.createSession("test", "/testLink.linkType=download", "/testLink.title=" + testTitle);
        final Node content = session.getNode("/testLink");

        LinkModel<ConfiguredTemplateDefinition> linkModel = new LinkModel(content, null, null, templatingFunctions, damTemplatingFunctions, i18n);

        // WHEN
        String title = linkModel.getTitle();

        // THEN
        assertThat(title, is(testTitle));
    }

    @Test
    public void testDownloadTitleWithoutTitle() throws RepositoryException, IOException {
        // GIVEN
        final String testTitle = "";
        final String linkedTitle = "LinkedOne";
        final String linkedUUID = "DAM-UUID-ZYXW";
        final Session session = SessionTestUtil.createSession("test", "/testLink.linkType=download", "/testLink.linkTypedownload=" + linkedUUID, "/testLink.title=" + testTitle);
        final Node content = session.getNode("/testLink");

        Asset asset = mock(Asset.class);
        when(asset.getTitle()).thenReturn(linkedTitle);
        when(damTemplatingFunctions.getAsset(eq(linkedUUID))).thenReturn(asset);

        // WHEN
        LinkModel<ConfiguredTemplateDefinition> linkModel = new LinkModel(content, null, null, templatingFunctions, damTemplatingFunctions, i18n);

        String title = linkModel.getTitle();

        // THEN
        assertThat(title, is(linkedTitle));
    }

}