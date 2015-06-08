[#-------------- ASSIGNMENTS --------------]
[#include "/tours/templates/macros/image.ftl" /]

[#assign category = tourfn.categoryByUrl]
[#assign rendition = damfn.getRendition(category.image, "xxlarge") ]
[#assign assetCredit = category.image.caption!]


[#-------------- RENDERING --------------]
<!-- TourType Overview Header -->
<div class="category-header"${backgroundImage(rendition)}>
    <div class="lead-caption">
        <h1 class="category">${category.name!}</h1>

        [#if category.description?has_content]
            <div class="category-description">
                ${category.description!}
            </div>
        [/#if]
    </div>
</div>

<div class="container after-category-header-2"></div>
