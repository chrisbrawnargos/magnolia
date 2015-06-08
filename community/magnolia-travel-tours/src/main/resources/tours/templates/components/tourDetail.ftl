[#-------------- ASSIGNMENTS --------------]
[#include "/tours/templates/macros/image.ftl" /]
[#include "/tours/templates/macros/tourTypeIcon.ftl" /]
[#include "/tours/templates/macros/sampleTourText.ftl" /]

[#assign site = sitefn.site()!]
[#assign theme = sitefn.theme(site)!]

[#assign tour = model.tour]
[#assign asset = tour.image!]
[#if asset?exists]
    [#assign assetCredit = asset.caption!]
    [#assign renditionDetail = damfn.getRendition(asset, "xxlarge")!]
[/#if]

[#if def.parameters.showTourTypes?? && def.parameters.showTourTypes == false]
    [#assign showTourTypes = false]
[#else]
    [#assign showTourTypes = true]
    [#assign relatedTourTypes = tour.tourTypes!]
[/#if]

[#if def.parameters.showDestinations?? && def.parameters.showDestinations == false]
    [#assign showDestinations = false]
[#else]
    [#assign showDestinations = true]
    [#assign relatedDestinations = tour.destinations!]
[/#if]


[#-------------- RENDERING --------------]
<!-- TourDetail -->
<div class="product-header"${backgroundImage(renditionDetail)}>
    <div class="lead-caption">
        <h1>${tour.name}</h1>

        [#if showTourTypes]
            <div class="category-icons">
                [#list relatedTourTypes as tourType]
                    <div class="category-icon absolute-center-container">
                        <a href="${tourfn.getTourTypeLink(content, tourType.nodeName)!'#'}">[@tourTypeIcon tourType.nodeName theme.name ctx /]</a>
                    </div>
                [/#list]
            </div>
        [/#if]
    </div>
</div>


<div class="container after-product-header after-product-header-2">

    <div class="row product-info product-summary">

            <div class="product-location">
            [#if showDestinations]
                [#list relatedDestinations as destination]
                    [#assign rendition = damfn.getRendition(destination.image, "small-square")!]
                    <a href="${tourfn.getDestinationLink(content, destination.nodeName)!'#'}">
                        [@tourImage rendition "" destination.name "img-circle img-responsive" /]
                    </a>
                [/#list]
            [/#if]
            </div>

        <div class="col-xs-10 col-xs-push-1">
            <div class="product-property">
                <div class="property-label">${i18n.get('tour.property.startCity')}</div>
                <div class="property-value">${tour.location!}</div>
            </div>
            <div class="product-property">
                <div class="property-label">${i18n.get('tour.property.duration')}</div>
                <div class="property-value">${i18n.get('tour.duration', [tour.duration!])}</div>
            </div>
            <div class="product-property">
                <div class="property-label">${i18n.get('tour.property.operator')}</div>
                <div class="property-value">${tour.author!}</div>
            </div>
            <div class="product-property ">
            [#if showTourTypes]
                <div class="property-label">${i18n.get('tour.property.tourTypes')}</div>
                <div class="property-value product-categories">
                    <div class="category-icons">
                        [#list relatedTourTypes as tourType]
                            <div class="category-icon absolute-center-container">
                                <a href="${tourfn.getTourTypeLink(content, tourType.nodeName)!'#'}">[@tourTypeIcon tourType.nodeName theme.name ctx /]</a>
                            </div>
                        [/#list]
                    </div>
                </div>
            [/#if]
            </div>

        </div>

        <div class="product-action">
            <button type="button" class="btn btn-primary btn-lg book-button" data-toggle="modal" data-target=".book-tour-not-implemented">${i18n['tour.book']}</button>
        </div>
    </div>

    <div class="row product-info">
        <div class="col-xs-10 col-xs-push-1">
            <p class="summary">${tour.description}</p>

            [#if tour.body?has_content]
                <div class="body">${tour.body!}</div>
            [/#if]
        </div>
    </div>

    <!-- Additional sample text for the purposes of demonstrating what a full page could look like -->
    <div class="row product-info">
        <div class="col-xs-10 col-xs-push-1">
            <hr style="margin-top:0px;"/>
            <div class="body">[@sampleTourText /]</div>
        </div>
    </div>

    [#if assetCredit?has_content]
        <div class="row product-info ">
            <div class="col-xs-10 col-xs-push-1 product-image-credit">
                <hr style="margin-top:0px;">
                <div class="body">${i18n['credit.leadImage']} ${assetCredit}
                    [#assign license=asset.copyright!]
                    [#if license?has_content]
                        &nbsp;<a target="_blank" href="https://creativecommons.org/licenses/${license}">${license}</a>
                    [/#if]
                </div>
            </div>
        </div>
    [/#if]

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