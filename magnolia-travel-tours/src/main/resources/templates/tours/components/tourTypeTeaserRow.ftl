[#include "/templates/tours/macros/image.ftl" /]

[#assign tours = model.tours]

<!-- TourType Teaser Row -->
<div class="container">
    <div class="row">

        <h1>${content.title!}</h1>
        <p>${content.body!}</p>

        [#list tours as tour]
            [#assign rendition = damfn.getRendition(tour.image, "large-16x9")!]

            <div class="col-md-4 category-card">
                [#if rendition?has_content]
                    <span class="card-teaser-image clearfix">
                        [@tourImage rendition "" tour.name /]
                    </span>
                [/#if]

                <h3><span>${tour.name!}</span></h3>

                <div class="category-card-content">
                    <p>${tour.description!}</p>

                    <p class="card-button">
                        <a class="btn btn-primary" href="${tour.link!"#"}">${i18n.get('tour.view.named', [tour.name!""])}</a>
                    </p>
                </div>
            </div>
        [/#list]
    </div>
</div>
