[#-- Render a list of tours. --]

[#-------------- ASSIGNMENTS --------------]
[#include "/tours/templates/macros/tourTeaser.ftl"]
[#include "/tours/templates/macros/editorAlert.ftl" /]

[#-- Uses definition parameters: workspace, dialogProperty, requestParameter, requestFallback --]
[#assign category = model.getContentByCascade()]

[#-- Uses definition parameters: listWorkspace, listReferenceProperty, listQuery --]
[#assign tours = model.getContentListByReference(category.@id)]

[#assign title = content.title!i18n.get('tour.all.tours', [category.displayName!""])]

[#-------------- RENDERING --------------]
<!-- Tour List -->
<div class="container">

    <h2>${title}</h2>

    <div class="row">
        [#list tours as tour]
            [#assign link = "tour?tour=" + tour.@name!"NO NAME/NO SLUG"]
            [#assign asset = damfn.getAsset(tour.img)!]
            [@tourTeaser tour.name tour.description link asset /]
        [/#list]
    </div>

    [@editorAlert i18n.get('note.for.editors.assign.category', [(category.name)!""]) /]
</div>

