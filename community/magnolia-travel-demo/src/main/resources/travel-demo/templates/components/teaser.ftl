[#-------------- DEFAULT INCLUDE AND ASSIGN PART --------------]
[#include "/mtk/templates/includes/init.ftl"]

[#assign contentHighlight = content.highlight!false]
[#if contentHighlight]
    [#assign divClass = "${divClass} highlight"]
[/#if]

[#assign hideTeaserImage = content.hideTeaserImage!false]
[#if hideTeaserImage]
    [#assign divClass = "${divClass} no-img"]
[/#if]

[#assign title = content.teaserTitle!]
[#assign abstract = content.teaserAbstract!]
[#assign resolveError = false]
[#assign linkTarget = ""]

[#-- Set the imageLink / divClass when image should be displayed --]
[#if !hideTeaserImage && content.image?has_content]
    [#assign imageLink = damfn.getRendition(content.image, "original").link]
    [#if !imageLink?has_content]
        [#assign divClass = "${divClass} no-img"]
    [/#if]
[/#if]

[#assign teaserLink = content.link!]

[#-------------- TEASER SPECIFIC LOGIC --------------]
[#if teaserLink == "page"]
    [#include "/mtk/templates/includes/pageTeaser.ftl"]
[#elseif teaserLink == "external"]
    [#include "/mtk/templates/includes/externalTeaser.ftl"]
[#elseif teaserLink == "download"]
    [#include "/mtk/templates/includes/downloadTeaser.ftl"]
[#else]
    [#assign resolveError = true]
[/#if]

[#if resolveError && cmsfn.editMode]
    [#assign divClass = "${divClass} note-for-editor"]
[/#if]

[#-------------- RENDERING PART --------------]
<div class="${divClass}"${divID}>

[#if cmsfn.editMode && resolveError]

    <${headlineLevel}>${i18n["reference.resolveError"]}</${headlineLevel}>

[#else]

    <${headlineLevel}><a href="${link!}" ${linkTarget!}>${title!}</a></${headlineLevel}>
    [#if imageLink?has_content]
        [#include "/travel-demo/templates/macros/imageResponsive.ftl"]
        [#assign imageClass = "content-image-below"]
        [#assign imageHtml][@imageResponsive imageLink content imageClass false def.parameters /][/#assign]

        <a href="${link}"${linkTarget!}>${imageHtml}</a>
    [/#if]
    [#if abstract?has_content]<p>${abstract}</p>[/#if]

[/#if]

</div><!-- end ${divClass} -->
