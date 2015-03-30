[#assign category = model.categoryByUrl!]

[#if category?has_content]
    <!-- Tour List - Featured Row -->
    [#include "/templates/tours/macros/relatedTours.ftl"]
    [#assign tours = model.getRelatedToursByCategory(category.identifier)]
    [@relatedTours category.name tours /]

    <hr class="featurette-divider"/>
[/#if]
