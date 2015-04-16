[#-- Shared setup for all components. --]
[#include "/travel-demo/templates/includes/init.inc.ftl"]
[#include "/travel-demo/templates/macros/image.ftl"]

[#if divClass?has_content]
    [#assign divClass = content.linkType + " " + divClass]
[#else]
    [#assign divClass = content.linkType]
[/#if]

[#assign linkText = model.title!]
[#assign linkHref = model.link!]

[#if content.linkType == "download"]
    [#assign asset = model.asset!]
    [#assign extention = model.getFileExtension(asset.getFileName())]
    [#assign linkText =  "${linkText} <em>(" + extention?upper_case + ", " + model.getSizeString(asset.fileSize) + ")</em>"]
[/#if]

[#if content.linkType == "internal" || content.linkType == "download"]
    [#if !model.link?exists && cmsfn.editMode]
        [#assign linkText = "${i18n['link.internal.resolveError']}"]
        [#assign linkHref = "javascript:alert('${i18n['link.internal.resolveError']?js_string}')"]
    [/#if]
[/#if]

[#if content.linkType == "external" && linkHref != "#"]
    [#assign target="target=\"_blank\""]
[/#if]

[#assign hasImage=false]
[#if content.image?? && content.image?has_content]
    [#assign hasImage=true]
[/#if]

[#-------------- RENDERING  --------------]

[#if linkHref?exists || cmsfn.editMode]
    <span class="${divClass!}"${divID}><a href="${linkHref}" ${target!}>
    [#if hasImage]
        [#assign assetRendition=damfn.getRendition(content.image, "original")]
        [@image assetRendition content "list-image" true /]
    [#else]
        ${linkText}
    [/#if]
    </a></span>
[/#if]
