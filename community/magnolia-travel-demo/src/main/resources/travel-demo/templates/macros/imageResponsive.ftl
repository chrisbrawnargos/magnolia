[#-- Renders an image (asset) rendition --]
[#macro imageResponsive image content imageClass="content-image" useOriginal=false definitionParameters={}]
    [#if image?has_content]
    [#-- Fallback text for alt text --]
        [#assign assetTitle = i18n['image.no.alt']]
        [#if image.asset?? && image.asset.title?has_content]
            [#assign assetTitle = image.asset.title]
        [/#if]

    [#-- Alt text and title --]
        [#assign imageAlt = content.imageAltText!content.imageTitle!assetTitle!]
        [#assign imageTitle = content.imageTitle!content.imageAltText!assetTitle!]

        [#assign imageLink = image.link]
    [#-- For PNGs/GIFs it might be useful to use render the original asset and therefore bypassing imaging --]
        [#if useOriginal]
            [#assign imageLink = image.asset.link]
        [/#if]

    [#-- Image caption / credit; Falls back to asset's properties --]
        [#assign imageCaption = content.imageCaption!image.asset.caption!""]
        [#assign imageCredit = content.imageCredit!image.asset.copyright!""]

    [#-- CSS --]
    [#-- Image class is used from def.parameters, otherwise falls back to given parameter --]
        [#assign divWrapperClass = definitionParameters.imageWrapperClass!"content-image-wrapper"]
        [#assign imgClass = imageClass][#-- Using another variable here as the macro parameter cannot be re-assinged --]
        [#if definitionParameters.imageClass?has_content]
        [#-- Adding custom parameters as img class when specified --]
            [#assign imgClass = "${imgClass} ${definitionParameters.imageClass}"]
        [/#if]

    [#-------------- RENDERING  --------------]
    [#-- Using a wrapper to be able to position caption+credit nicely --]
        [#if imageCaption?has_content || imageCredit?has_content]
        <!-- image with caption/credit -->
        <div class="${divWrapperClass}">
        [/#if]

        [@responsiveImageTravel image.asset imageAlt imageTitle imgClass additional /]

        [#if imageCaption?has_content || imageCredit?has_content]
            [#if imageCaption?has_content]
                <span class="image-caption">${imageCaption}</span>
            [/#if]
            [#if imageCredit?has_content]
                <span class="image-credit">${imageCredit}</span>
            [/#if]
        </div><!-- end image with caption/credit -->
        [/#if]
    [/#if]
[/#macro]

[#-- Macro to render a responsive image with the variations configured in the theme. --]
[#macro responsiveImageTravel asset  alt="" title="" cssClass="" additional=""]
  [#if !(content.fixedHeight!false)]
    [#assign srcs = [
    {"name":"240", "width":"240"},
    {"name":"320", "width":"320"},
    {"name":"480", "width":"480"},
    {"name":"960", "width":"960"},
    {"name":"1366","width":"1366"},
    {"name":"1920","width":"1920"}]]

    [#assign fallback="240"]

    [@responsiveImageLazySizes asset alt title cssClass srcs fallback additional /]
  [#else]
        [#assign srcs = [
        {"name":"240x160", "width":"240", "height":"160"},
        {"name":"320x240", "width":"320", "height":"240"},
        {"name":"480x320", "width":"480", "height":"320"},
        {"name":"960x540", "width":"960", "height":"540"},
        {"name":"1366x768","width":"1366", "height":"768"},
        {"name":"1920x1200", "width":"1920", "height":"1200"}]]

        [#assign fallback="240x160"]

        [@responsiveImageLazySizes asset alt title cssClass srcs fallback additional /]
  [/#if]
[/#macro]

[#-- Macro to render responsive image using lazysizes javascript library.
        Use data-srcset attribute to only load the size of image that the current image width requires --]
[#macro responsiveImageLazySizes asset  alt="" title="" cssClass="" srcs="" fallbackName="" sizes="" additional="" ]
    [#if asset?exists]
        [#assign cssClass = cssClass + " lazyload"]
        [#assign rendition = damfn.getRendition(asset, fallbackName)!]
    <img data-sizes="auto" class="${cssClass} lazyload lazysizes" src="${rendition.link}" alt="${alt}" title="${title}"
         data-srcset="
        [#list srcs as src]
            [#assign rendition = damfn.getRendition(asset, src.name)!]
            [#if rendition?exists && rendition?has_content]
                ${rendition.link} ${src.width}w
            [/#if]
        [/#list]
        "

    ${additional} />
    [/#if]
[/#macro]
