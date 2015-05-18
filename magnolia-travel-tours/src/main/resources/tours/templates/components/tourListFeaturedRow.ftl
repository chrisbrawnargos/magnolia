[#-- Renders a row of items, that belong to the category, and are featured. The category is retrieved from a url parameter. --]
[#-------------- ASSIGNMENTS --------------]
[#-- Uses definition parameters: workspace, requestParameter, requestFallback --]
[#assign category = model.getContentByParameter()!]

[#-------------- RENDERING --------------]
[#if category?has_content]
    <!-- Tour List - Featured Row -->
    [#include "/tours/templates/macros/relatedTours.ftl"]
    [#-- Uses definition parameters: listWorkspace, listReferenceProperty, listQuery --]
    [#assign tours = model.getContentListByReference(category.@id!)]
    
    [@relatedTours category.displayName tours /]

    <hr class="featurette-divider"/>
[/#if]
