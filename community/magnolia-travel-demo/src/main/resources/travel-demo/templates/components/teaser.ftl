[#-------------- ASSIGNMENTS --------------]
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

[#-- Set the image / divClass when image should be displayed --]
[#if !hideTeaserImage && content.image?has_content]
    [#assign image = damfn.getRendition(content.image, "original")!]
    [#if !image?has_content]
        [#assign divClass = "${divClass} no-img"]
    [/#if]
[/#if]

[#assign teaserLinkType = content.linkType!]


[#-------------- TEASER SPECIFIC LOGIC --------------]
[#if teaserLinkType?has_content]
    [#include "/mtk/templates/includes/teaser${teaserLinkType?cap_first}.ftl"]
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
    [#if image?has_content]
        [#include "/travel-demo/templates/macros/imageResponsive.ftl"]
        [#assign imageClass = "content-image-below"]
        [#assign imageHtml][@imageResponsive image content imageClass false def.parameters /][/#assign]

        <a href="${link}"${linkTarget!}>${imageHtml}</a>
    [/#if]
    [#if abstract?has_content]<p>${abstract}</p>[/#if]

[/#if]

</div><!-- end ${divClass} -->
