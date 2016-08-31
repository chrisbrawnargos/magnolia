[#-- Extends the basic template behavior by getting  featured tours by personalization cookie trait --]
[#-------------- ASSIGNMENTS --------------]
[#include "/travel-demo-component-personalization/templates/macros/detectCookie.ftl" /]

[#assign cookie = detectCookie("tourType")! /]

[#if cookie?has_content]
    [#assign category = model.getCategoryByName(cookie)!]
[#else]
    [#assign category = model.getCategoryByUrl()!]
[/#if]


[#-------------- RENDERING --------------]
[#if category?has_content]
<!-- Tour List - Featured Row -->
    [#include "/travel-demo-component-personalization/templates/macros/personalizedRelatedTours.ftl"]
    [#assign tours = model.getRelatedToursByCategory(category.identifier)]
    [@relatedTours category.name tours /]

<div class="container category-overview">
    [#if category.body?has_content]
        <div class="category-body">
        ${category.body}
        </div>
    [/#if]
</div>
[/#if]
