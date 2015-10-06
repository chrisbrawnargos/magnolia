[#macro tourTeaser tour]

    [#include "/tours/templates/macros/image.ftl" /]
    [#include "/tours/templates/macros/tourTypeIcon.ftl" /]

    [#assign rendition = damfn.getRendition(tour.image, "960")! /]

    <!-- Tour Teaser -->
    <div class="col-md-6 tour-card card" >
        <div class="tour-card-background"${backgroundImage(rendition)}></div>
        <a class="tour-card-anchor" href="${tour.link!}">
            <div class="tour-card-content-shader"></div>
            <div class="tour-card-content">
                <h3>${tour.name!}</h3>
                <div class="category-icons">
                    [#list tour.tourTypes as tourType]
                        <div class="category-icon absolute-center-container">
                            [@tourTypeIcon tourType.icon tourType.name "" /]
                        </div>
                    [/#list]
                </div>
                <div class="card-button">
                    <div class="btn btn-primary"}">${i18n['tour.view']}</div>
                </div>
            </div>
        </a>
    </div>

[/#macro]