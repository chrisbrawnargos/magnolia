[#-- This custom link component can include an image. --]
[#-------------- INCLUDE AND ASSIGN PART --------------]

[#include "/mte/templates/includes/init.ftl"]
[#include "/travel-demo/templates/macros/image.ftl"]


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
    [#assign extension = cmsfn.getFileExtension(asset.getFileName())]
    [#assign linkText =  "${linkText} <em>(" + extension?upper_case + ", " + cmsfn.getDisplayFileSize(asset.fileSize) + ")</em>"]
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
