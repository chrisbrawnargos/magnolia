[#-------------- ASSIGNMENTS --------------]
[#include "/tours/templates/macros/image.ftl" /]

[#assign category = tourfn.categoryByUrl]
[#assign rendition = damfn.getRendition(category.image, "1920") ]
[#assign assetCredit = category.image.caption!]

[#-------------- RENDERING --------------]
<!-- TourType Overview Header -->
<div class="category-header">
    <div class="navbar-spacer"></div>
    <div class="header-wrapper"${backgroundImage(rendition)}>
        <div class="lead-caption">
            <h1 class="category">${category.name!}</h1>

            [#if category.description?has_content]
                <div class="category-description">
                    ${category.description!}
                </div>
            [/#if]
        </div>
    </div>
</div>

<div class="container after-category-header-2"></div>
