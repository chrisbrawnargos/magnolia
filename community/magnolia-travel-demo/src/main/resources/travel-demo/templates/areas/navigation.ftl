[#-------------- ASSIGNMENTS --------------]
[#include "/travel-demo/templates/macros/searchForm.ftl"]

[#assign homeLink = cmsfn.link(cmsfn.siteRoot(content))!"/" /]
[#assign pages = model.rootPages! /]
[#assign childPages = model.childPages! /]

[#assign spaceClass = "navbar-spacer" /]
[#if childPages?has_content]
    [#assign spaceClass = "navbar-spacer-children"]
[/#if]

[#assign searchProperty = cmsfn.siteRoot(content).searchResultPage! /]
[#if searchProperty?has_content]
    [#assign searchResultPage = cmsfn.link(cmsfn.contentById(searchProperty)) /]
[/#if]

[#assign site = sitefn.site()!]
[#assign theme = sitefn.theme(site)!]

[#-------------- RENDERING --------------]
<div class="navbar-wrapper">

    <div id="masthead">
        <span id="masthead-logo">
            <a class="home" href="${homeLink}">
                <img src="${ctx.contextPath}/.resources/${theme.name}/img/logo-white.png"/>
            </a>
            <span class="tagline">${i18n['navigation.tagline']}</span>
        </span>
        <div class="phone-hours hidden-xs">
            <div class="hours">${i18n['navigation.call.to.action']}
                <a class="telephone" href="callto:${i18n['navigation.telephone']}">${i18n['navigation.telephone']}</a>
            </div>
        </div>
    </div>

    <nav id="sticky-nav" class="navbar navbar-inverse navbar-static-top" role="navigation" style="clear:both;">
        <div class="container">

            <div class="navbar-header">
              <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
                <span class="sr-only">${i18n['navigation.toggle']}</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
              </button>
              <a id="nav-logo" class="navbar-brand" href="${homeLink}"><img  id="nav-logo-icon" src="${ctx.contextPath}/.resources/${theme.name}/img/logo-icon-white.png"/></a>
              <a class="telephone-small visible-xs-inline" href="callto:${i18n['navigation.telephone']}">${i18n['navigation.telephone']}</a>
            </div>

            <div id="navbar" class="navbar-collapse collapse">
                <ul class="nav navbar-nav">

                [@cms.area name="tours" /]
                [@cms.area name="destinations" /]

                [#list pages as page]
                    <li class="${page.cssClass!}"><a href="${page.link!}">${page.name!}</a></li>
                [/#list]

                </ul>

                [#-- Only when the search result page was set should the form be displayed --]
                [#if searchResultPage?exists]
                    [@searchForm action=searchResultPage! /]
                [/#if]
            </div>

            [#if childPages?has_content]
                <div id="navbar-secondary" class="navbar-collapse collapse">
                    <ul class="nav navbar-nav">
                        [#list childPages as childPage]
                            <li class="${childPage.cssClass!}"><a href="${childPage.link}">${childPage.name}</a></li>
                        [/#list]
                    </ul>
                </div>
            [/#if]
        </div>
    </nav>

</div>


<div class="${spaceClass}"></div>
