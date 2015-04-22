[#macro tourTeaser name description link image]

    [#include "/tours/templates/macros/image.ftl" /]

    [#assign rendition = damfn.getRendition(image, "large-square")! /]

    <!-- Tour Teaser -->
    <div class="row featurette">
        <div class="col-md-7">
            <h2 class="featurette-heading">${name}</h2>
            <p class="lead">${description}</p>
            <p class="card-button"><a class="btn btn-primary" href="${link!'#'}">${i18n['tour.view']}</a></p>
        </div>
        <div class="col-md-5">
            [@tourImage rendition "" name "featurette-image img-responsive" /]
        </div>
    </div>

[/#macro]