[#-------------- INCLUDE AND ASSIGN PART --------------]
[#include "/mte/templates/includes/init.ftl"]
[#-- Image --]
[#-- Basic positioning of an image below or above the text --]
[#assign imagePosition = content.imagePosition!"below"]

[#-- CSS default --]
[#if !divClass?has_content]
    [#assign divClass = "text-section"]
[/#if]

[#-- Image css classes --]
[#assign hasImage = false]
[#assign imageHtml = ""]
[#if model.image?exists]
    [#assign hasImage = true]
    [#assign divClass = "${divClass} text-image-section"]
    [#assign imageClass = "content-image-${imagePosition}"]

    [#include "/travel-demo/templates/macros/imageResponsive.ftl"]
    [#assign imageHtml][@imageResponsive model.image content imageClass false def.parameters /][/#assign]
[/#if]


[#-------------- RENDERING PART --------------]
[#-- Rendering: Text/Image item --]
<div class="${divClass!}"${divID!}>

    [#-- Headline --]
    [#if content.headline?has_content]
        <${headlineLevel}>${content.headline!}</${headlineLevel}>
    [/#if]

    [#-- Image above text --]
    [#if hasImage && imagePosition == "above"]
        ${imageHtml}
    [/#if]

    [#-- Text --]
    [#if content.text?has_content]
        ${cmsfn.decode(content).text!}
    [/#if]

    [#-- Image below text --]
    [#if hasImage && imagePosition == "below"]
        ${imageHtml}
    [/#if]

</div><!-- end ${divClass} -->