[#-- Renders a row of items, that belong to the category, and are featured. The category is retrieved from a url parameter. --]
[#-------------- ASSIGNMENTS --------------]
[#assign category = contentfn.getContentByParameter("category", catfn.getCategorizationRepository(), "active")!]

[#-------------- RENDERING --------------]
[#if category?has_content]
    <!-- Tour List - Featured Row -->
    [#include "/tours/templates/macros/relatedTours.ftl"]
    [#assign tours = model.getContentListByReference(def.parameters.listReferenceProperty, category.@id, def.parameters.listQuery!)]
    
    [@relatedTours category.displayName tours /]

    <hr class="featurette-divider"/>
[/#if]
