[#-- Assigns: Page's model & definition, based on the rendering hierarchy and not the node hierarchy --]
[#assign site = sitefn.site()!]
[#assign theme = sitefn.theme(site)!]
[#-- Rendering --]
<!DOCTYPE html>
<!--[if lt IE 7]><html class="no-js lt-ie9 lt-ie8 lt-ie7" xml:lang="${cmsfn.language()}" lang="${cmsfn.language()}"><![endif]-->
<!--[if IE 7]><html class="no-js lt-ie9 lt-ie8" xml:lang="${cmsfn.language()}" lang="${cmsfn.language()}"><![endif]-->
<!--[if IE 8]><html class="no-js lt-ie9" xml:lang="${cmsfn.language()}" lang="${cmsfn.language()}"><![endif]-->
<!--[if gt IE 8]><!--><html class="no-js" xml:lang="${cmsfn.language()}" lang="${cmsfn.language()}"><!--<![endif]-->
<head>
    [@cms.init /]

    <title>${content.title!}</title>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <link rel="icon" href="${ctx.contextPath}/.resources/travel-demo/favicon.ico">

    [#list theme.cssFiles as cssFile]
        [#if cssFile.conditionalComment?has_content]<!--[if ${cssFile.conditionalComment}]>[/#if]
            <link rel="stylesheet" type="text/css" href="${cssFile.link}" media="${cssFile.media}" />
        [#if cssFile.conditionalComment?has_content]<![endif]-->[/#if]
    [/#list]
    [#if def.cssFiles??]
        [#list def.cssFiles as cssFile]
            <link rel="stylesheet" type="text/css" href="${cssFile.link}" media="${cssFile.media}" />
        [/#list]
    [/#if]

    [#-- jsFiles from the theme are here --]
    [#list theme.jsFiles as jsFile]
        <script src="${jsFile.link}"></script>
    [/#list]

    <!-- TODO, can these come after all of the css -->
    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <script src="//html5shiv.googlecode.com/svn/trunk/html5.js"></script>
    <script>window.html5 || document.write('<script src="${ctx.contextPath}/resources/travel-demo-theme/js/html5shiv.js"><\/script>')</script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>
<body>
<!--[if lt IE 7]>
<p class="browsehappy">You are using an <strong>outdated</strong> browser. Please <a href="http://browsehappy.com/">upgrade your browser</a> to improve your experience.</p>
<![endif]-->

[@cms.area name="content"/]

<script>window.jQuery || document.write('<script src="${ctx.contextPath}/resources/travel-demo-theme/js/jquery-1.10.2.min.js"><\/script>')</script>

[#-- We're using the prototype's jsFiles to be rendered at the bottom of the page --]
[#if def.jsFiles??]
    [#list def.jsFiles as jsFile]
        <script src="${jsFile.link}"></script>
    [/#list]
[/#if]
</body>
</html>