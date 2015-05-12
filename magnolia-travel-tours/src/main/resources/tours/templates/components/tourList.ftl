[#-- Uses ContentListModel --]

[#-------------- ASSIGNMENTS --------------]
[#include "/tours/templates/macros/tourTeaser.ftl"]
[#include "/tours/templates/macros/editorAlert.ftl" /]

[#-- TODO Change this into a method too. --]
[#if content.referenceId?has_content]
    [#assign categoryId = content.referenceId]
    [#assign category = cmsfn.contentById(categoryId, catfn.getCategorizationRepository())];
[#else]
    [#assign routeParameter = "category"]
    [#assign category = contentfn.getContentByParameter(routeParameter, catfn.getCategorizationRepository(), "active")!]
[/#if]

[#assign tours = model.getItemsFancy(def.parameters.referenceProperty, category.@id, "")]

[#assign title = content.title!i18n.get('tour.all.tours', [category.name!""])]

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

    [@editorAlert i18n.get('note.for.editors.assign.category', [category.name!""]) /]
</div>

