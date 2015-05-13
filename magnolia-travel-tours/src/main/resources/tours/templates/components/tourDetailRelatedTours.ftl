[#-- Uses ContentListModel --]
[#-------------- ASSIGNMENTS --------------]
[#include "/tours/templates/macros/relatedTours.ftl"]

[#-- TODO Change this into a method too. --]
[#assign routeParameter = "tour"]
[#assign tourItem = contentfn.getContentByParameter(routeParameter, def.parameters.workspace, "Inside-New-Delhi")!]

[#assign tourNode = cmsfn.asJCRNode(tourItem)]
[#assign categories = cmsfn.asContentMapList(catfn.getCategories(tourNode, "tourTypes"))]

[#-------------- RENDERING --------------]
<!-- Tour Detail - Related Tours -->
[#list categories as category]

    [#--assign tours = model.getRelatedToursByCategory(category.@id) --]
    [#assign tours = model.getItemsFancy(def.parameters.referenceProperty, category.@id, def.parameters.extraQuery!)]
    
    [@relatedTours category.name tours /]

[/#list]
