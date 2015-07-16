[#-------------- ASSIGNMENTS --------------]
[#include "/tours/templates/macros/image.ftl" /]
[#include "/tours/templates/macros/tourTypeIcon.ftl" /]

[#assign tours = model.tours]
[#assign showTourTypes = content.showTourTypes!true]

[#-------------- RENDERING --------------]

<!-- Tour Carousel -->
<div id="myCarousel" class="carousel slide">
    <!-- Indicators -->
    <ol class="carousel-indicators">
    [#list tours as tour]
        [#assign activeClass=""]
        [#if tour_index == 0 ]
            [#assign activeClass="active"]
        [/#if]
        <li data-target="#myCarousel" data-slide-to="${tour_index}" class="${activeClass}"></li>
    [/#list]
    </ol>

    <!-- Actual Carousel List -->
    <div class="carousel-inner" role="listbox">
    [#list tours as tour]
        [#assign activeClass=""]
        [#if tour_index == 0 ]
            [#assign activeClass="active"]
        [/#if]
        [#assign assetCredit = tour.image.caption!]
        [#assign rendition = damfn.getRendition(tour.image, "xxlarge")!]
        [#assign iconRendition = damfn.getRendition(tour.icon)!]

        <div class="item ${activeClass}"${backgroundImage(rendition)}>
            <div class="container">
                <div class="carousel-caption">
                    <a href="${tour.link!}"><h1>${tour.name!}</h1></a>
                    [#if showTourTypes]
                        <div class="category-icons">
                            [#list tour.tourTypes as tourType]
                                <div class="category-icon absolute-center-container">
                                    <a href="${tourfn.getTourTypeLink(content, tourType.nodeName)!'#'}">[@tourTypeIcon tourType.icon tourType.name /]</a>
                                </div>
                            [/#list]
                        </div>
                    [/#if]
                    <a class="btn btn-lg btn-primary" href="${tour.link!}">${i18n['tour.view']}</a>
                </div>
            </div>
        </div>
    [/#list]
    </div>

    <!-- Carousel Controls -->
    <a class="left carousel-control" href="#myCarousel" data-slide="prev">
        <span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
        <span class="sr-only">${i18n['tour.previous']}</span>
    </a>
    <a class="right carousel-control" href="#myCarousel" data-slide="next">
        <span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
        <span class="sr-only">${i18n['tour.next']}</span>
    </a>
</div>
