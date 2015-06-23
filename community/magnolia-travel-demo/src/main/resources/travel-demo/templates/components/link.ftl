[#-- This custom link component can include an image. --]
[#-------------- INCLUDE AND ASSIGN PART --------------]

[#include "/mte/templates/includes/init.ftl"]

[#-- Custom image macro for links only --]
[#-- Renders an image (asset) rendition --]
[#macro image image content imageClass="img-responsive" useOriginal=false]
    [#if image?has_content]
        [#-- Fallback text for alt text --]
        [#assign assetTitle=i18n['image.alt.unavailable']]
        [#if image.asset?? && image.asset.title?has_content]
            [#assign assetTitle=image.asset.title]
        [/#if]

        [#-- Alt text and title --]
        [#assign imageAlt=content.altText!content.title!i18n['image.alt.prefix']+": "+assetTitle]
        [#assign imageTitle=content.title!content.altText!i18n['image.alt.prefix']+": "+assetTitle]

        [#assign imageLink=image.link /]
        [#-- For PNGs it might be useful to use render the original asset and therefore bypassing imaging --]
        [#if useOriginal]
            [#assign imageLink=image.asset.link /]
        [/#if]

        [#-- Render the image --]
    <img src="${imageLink}" alt="${imageAlt}" title="${imageTitle}" class="${imageClass}"/>
    [/#if]
[/#macro]

[#assign hasImage=false]
[#if content.image?exists && content.image?has_content]
    [#assign hasImage=true]
[/#if]

[#if content.linkType?has_content]
    [#assign divClass = "${content.linkType} ${divClass}"]
[/#if]

[#assign linkText = model.title!]
[#assign linkHref = model.link!]

[#if model.isDownload()]
    [#assign asset = model.asset!]
    [#assign extension = cmsfn.fileExtension(asset.getFileName())]
    [#assign linkText =  "${linkText} <em>(" + extension?upper_case + ", " + cmsfn.readableFileSize(asset.fileSize) + ")</em>"]
[/#if]

[#-- Show resolve error in edit mode for internal/download links --]
[#if model.isInternal() || model.isDownload()]
    [#if !model.link?exists && cmsfn.editMode]
        [#assign linkText = "${i18n['reference.resolveError']}"]
        [#assign divClass = "${divClass} note-for-editor"]
    [/#if]
[/#if]

[#if model.isExternal() && !linkHref?starts_with("#")]
    [#assign linkTarget = " target=\"_blank\""]
[/#if]

[#-------------- RENDERING  --------------]
[#if linkHref?exists || cmsfn.editMode]
<span class="${divClass!}"${divID}><a href="${linkHref}"${linkTarget!}>
    [#if hasImage]
        [#assign assetRendition=damfn.getRendition(content.image, "original")]
        [@image assetRendition content "list-image" true /]
    [#else]
        ${linkText}
    [/#if]
</a></span>
[/#if]
