[#include "/tours/templates/macros/tourTeaser.ftl"]
[#include "/tours/templates/macros/editorAlert.ftl" /]

[#assign category = model.categoryByUrl!]
[#assign tours = model.getToursByCategory(category.identifier)]

<!-- Tour List -->
<div class="container">

    <h2>${i18n.get('tour.all.tours', [category.name!""])}</h2>

    <div class="row">
        [#list tours as tour]
            [#assign link = tourfn.getTourLink(tour)!"#"]
            [#assign asset = damfn.getAsset(tour.img)!]
            [@tourTeaser tour.name tour.description link asset /]
        [/#list]
    </div>

    [@editorAlert i18n.get('note.for.editors.assign.category', [category.name!""]) /]
</div>

