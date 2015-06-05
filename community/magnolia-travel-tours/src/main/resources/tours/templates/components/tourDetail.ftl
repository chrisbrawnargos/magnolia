[#-------------- ASSIGNMENTS --------------]
[#include "/tours/templates/macros/image.ftl" /]
[#include "/tours/templates/macros/sampleTourText.ftl" /]

[#assign tour = model.tour]
[#assign asset = damfn.getAsset(tour.img)!]
[#if asset??]
    [#assign assetCredit = asset.caption!]
[/#if]

[#if def.parameters.showTourTypes?? && def.parameters.showTourTypes == false]
    [#assign showTourTypes = false]
[#else]
    [#assign showTourTypes = true]
    [#assign relatedTourTypes = model.getTourTypes(cmsfn.asJCRNode(tour)) ]
[/#if]

[#if def.parameters.showDestinations?? && def.parameters.showDestinations == false]
    [#assign showDestinations = false]
[#else]
    [#assign showDestinations = true]
    [#assign relatedDestinations = model.getDestinations(cmsfn.asJCRNode(tour))]
[/#if]


[#-------------- RENDERING --------------]
<!-- TourDetail -->
<div class="container">

    <div class="row">
        <div class="col-sm-11 product-detail">
            <h1>${tour.name}</h1>
        </div>
        <div class="col-sm-1 product-detail">
            <button type="button" class="btn btn-primary btn-lg book-button" data-toggle="modal" data-target=".book-tour-not-implemented">${i18n['tour.book']}</button>
        </div>
    </div>

    <div class="row">
        <div class="col-md-4 product-detail">

            <div class="description">${tour.description}</div>

            [#if showTourTypes]
                <ul class="product-categories list-unstyled">
                [#list relatedTourTypes as tourType]
                    <li><a href="${tourType.link!}">${tourType.name!}</a></li>
                [/#list]
                </ul>
            [/#if]

            <div class="duration">${i18n.get('tour.duration', [tour.duration!])}</div>
            <div class="locations">${tour.location!}</div>

            [#if showTourTypes]
                [#list relatedDestinations as destination]
                    [#assign rendition = damfn.getRendition(destination.image, "small-square")!]
                    <a href="${destination.link}">
                        <div class="destination-card">
                            [@tourImage rendition "" destination.name "img-circle img-responsive" /]
                            <h3>${destination.name!}</h3>
                        </div>
                    </a>
                [/#list]
            [/#if]
        </div>

        [#assign renditionDetail = damfn.getRendition(tour.img, "large-16x9")!]
        [#if renditionDetail?has_content]
            <div class="col-md-8 product-detail">
                <span class="lead-image clearfix">
                    [@tourImage renditionDetail assetCredit tour.name "img-responsive" /]
                </span>
            </div>
        [/#if]
    </div>

    <div class="row">
        <div class="col-lg-12">
            <h2>${i18n['tour.details']}</h2>
        </div>
    </div>

    [#if tour.body?has_content]
        <div class="row">
            <div class="col-md-8 ">
                <div class="body">${tour.body!}</div>
            </div>
        </div>
    [/#if]

    <!-- Additional sample text for the purposes of demonstrating what a full page could look like -->
    <div class="row">
        <div class="col-md-8 ">
            <hr>
        </div>
    </div>
    <div class="row">
        <div class="col-md-8 ">
            <span class="body">[@sampleTourText /]</span>
        </div>
    </div>

</div>

<!-- Book Tour Dialog -->
<div class="modal fade book-tour-not-implemented">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="${i18n['tour.book.notImplementedDialog.close']}"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title">${i18n['tour.book.notImplementedDialog.title']}</h4>
      </div>
      <div class="modal-body">
        <p>${i18n['tour.book.notImplementedDialog.body']}</p>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">${i18n['tour.book.notImplementedDialog.close']}</button>
      </div>
    </div>
  </div>
</div>
