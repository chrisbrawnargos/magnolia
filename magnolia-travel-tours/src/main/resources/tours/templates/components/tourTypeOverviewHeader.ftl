[#-- Renders the title, description and lead image for a TourType --]
[#-------------- ASSIGNMENTS --------------]
[#include "/tours/templates/macros/image.ftl" /]

[#assign categoryRaw = model.getContentByParameter()!]
[#assign resolverConfig = {"img":"dam"}] 
[#assign category = contentfn.resolveReferences(categoryRaw, resolverConfig)]

[#assign rendition = damfn.getRendition(category.img, "xlarge-16x9") ]
[#assign assetCredit = category.img.caption!]

<!-- TourType Overview Header -->
<div class="container tour-type-overview">

    <h1 class="category">${i18n.get('tour.typed', [category.displayName!""])}</h1>

    [#if category.description?? || category.body??]
        <div class="category-page-introduction">
            [#if category.description?has_content]
                <p>${category.description!}</p>
            [/#if]
            [#if category.body?has_content]
                <p>${category.body!}</p>
            [/#if]
        </div>
    [/#if]

    [#if rendition?has_content]
        <span class="lead-image clearfix">
            [@tourImage rendition assetCredit category.displayName "img-responsive zoomable" /]
        </span>
    [/#if]

</div>
