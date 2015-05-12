[#-- Uses ContentListModel --]

[#-------------- ASSIGNMENTS --------------]
[#assign routeParameter = "category"]
[#assign category = contentfn.getContentByParameter(routeParameter, catfn.getCategorizationRepository(), "active")!]

[#-------------- RENDERING --------------]
[#if category?has_content]
    <!-- Tour List - Featured Row -->
    [#include "/tours/templates/macros/relatedTours.ftl"]
    [#assign tours = model.getItemsFancy(def.parameters.referenceProperty, category.@id, def.parameters.extraQuery!)]
    
    [@relatedTours category.name tours /]

    <hr class="featurette-divider"/>
[/#if]
