[#-------------- INCLUDE AND ASSIGN PART --------------]
[#include "/templates/travel-demo/includes/init.inc.ftl"]
[#assign divClass = def.parameters.divClass!"text-section"]

[#-------------- RENDERING PART --------------]
[#-- Rendering: Text Image item --]
<div class="${divClass!}" ${divID!}>

[#assign headingLevel = "h2"]
[#if content.headingLevel?has_content]
    [#assign headingLevel = content.headingLevel]
[/#if]

[#if content.headline?has_content]
    <${headingLevel}>${content.headline!}</${headingLevel}>
[/#if]

[#if content.text?has_content]
    ${cmsfn.decode(content).text!}
[/#if]

[#if model.image??]
    [#include "/templates/travel-demo/components/macros/image.ftl"/]
    [@image model.image content /]
[/#if]
</div>