[#-------------- ASSIGNMENTS --------------]
[#include "/tours/templates/macros/relatedTours.ftl"]

[#-- Uses definition parameters: workspace, requestParameter, requestFallback --]
[#assign tourItem = model.getContentByParameter()!]
[#assign tourNode = cmsfn.asJCRNode(tourItem)]

[#assign categoryNodes = catfn.getCategories(tourNode, "tourTypes")]
[#assign categories = cmsfn.asContentMapList(categoryNodes)]

[#-------------- RENDERING --------------]
<!-- Tour Detail - Related Tours -->
[#list categories as category]

    [#-- Uses definition parameters: listWorkspace, listReferenceProperty, listQuery --]
    [#assign tours = model.getContentListByReference(category.@id)]
    
    [@relatedTours category.name tours /]

[/#list]
