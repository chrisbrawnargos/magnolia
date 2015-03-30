[#assign headingLevel=def.parameters.headingLevel!"h5"]

[#assign cssClass=""]
[#if content.inline?? && content.inline && !cmsfn.editMode]
    [#assign cssClass=' class="inline"']
[/#if]

<div${cssClass}>
    [#if content.subtitle?has_content]
        <${headingLevel}>${content.subtitle}</${headingLevel}>
    [/#if]

    [@cms.area name="linkList"/]
</div><!-- end links -->
