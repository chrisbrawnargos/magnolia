[#include "/tours/templates/macros/image.ftl" /]

[#assign category = tfn.categoryByUrl]
[#assign rendition = damfn.getRendition(category.image, "xlarge-16x9") ]
[#assign assetCredit = category.image.caption!]

<!-- TourType Overview Header -->
<div class="container tour-type-overview">

    <h1 class="category">${i18n.get('tour.typed', [category.name!""])}</h1>

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
            [@tourImage rendition assetCredit category.name "img-responsive zoomable" /]
        </span>
    [/#if]

</div>
