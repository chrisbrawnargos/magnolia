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
<nav class="navbar navbar-default navbar-fixed-top " role="navigation" style="clear:both;">

    <div class="container">

        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar"
                    aria-expanded="false" aria-controls="navbar">
                <span class="sr-only">${i18n['navigation.toggle']}</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="${homeLink}">
                <img src="${ctx.contextPath}/.resources/${theme.name}/img/logo-white.png" alt=""/>
            </a>
        </div>

        <div id="navbar" class="navbar-collapse collapse">
            <div class="navbar-right">

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

<div class="${spaceClass}"></div>
