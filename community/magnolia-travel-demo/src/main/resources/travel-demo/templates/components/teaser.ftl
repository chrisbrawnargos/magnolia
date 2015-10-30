[#-------------- INCLUDE AND ASSIGN PART --------------]
[#include "/mte/templates/includes/init.ftl"]

[#if model.isHighlighted()]
    [#assign divClass = "${divClass} highlight"]
[/#if]

[#assign hideTeaserImage = model.isHideTeaserImage()]
[#if hideTeaserImage]
    [#assign divClass = "${divClass} no-img"]
[/#if]

[#if model.isTargetBlank()]
    [#assign linkTarget = " target=\"_blank\""]
[/#if]

[#-- Set the imageLink / divClass when image should be displayed --]
[#if !hideTeaserImage]
    [#assign imageLink = (model.image!).link!]

    [#if !imageLink?has_content]
        [#assign divClass = "${divClass} no-img"]
    [/#if]
[/#if]

[#assign resolveError = !model.teaserLink?has_content]

[#-- We want special handling for download teaser title: add filesize and filetype --]
[#-- But we do not want that HTML to the title of the image --]
[#assign title =  model.teaserTitle!]
[#if model.asset??]
    [#if model.asset?has_content]
        [#assign extension = cmsfn.fileExtension(model.asset.fileName)]
        [#assign imageTitle = title][#-- Using title without html below for the image --]
        [#assign title =  "${title} <em>(" + extension?upper_case + ", " + cmsfn.readableFileSize(model.asset.fileSize) + ")</em>"]
    [#else]
        [#assign resolveError = true]
    [/#if]
[/#if]

[#if resolveError && cmsfn.editMode]
    [#assign divClass = "${divClass} note-for-editor"]
[/#if]


[#-------------- RENDERING PART --------------]
<div class="${divClass}"${divID}>

    [#if cmsfn.editMode && resolveError]

        <${headlineLevel}>${i18n['reference.resolveError']}</${headlineLevel}>

    [#else]

        <${headlineLevel}><a href="${model.teaserLink!}"${linkTarget!}>${title!}</a></${headlineLevel}>
        [#if imageLink?has_content]
            [#include "/travel-demo/templates/macros/imageResponsive.ftl"]
            [#assign imageClass = "content-image-below"]
            [#assign imageHtml][@imageResponsive model.image content imageClass false def.parameters /][/#assign]
            <a href="${model.teaserLink}"${linkTarget!}>${imageHtml}</a>
        [/#if]
        [#if model.teaserText?has_content]<p>${model.teaserText}</p>[/#if]

    [/#if]

</div><!-- end ${divClass} -->
