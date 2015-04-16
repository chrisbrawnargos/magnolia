[#-- Renders an image (asset) rendition --]
[#macro image image content imageClass="img-responsive" useOriginal=false]
    [#if image?has_content]
        [#-- Fallback text for alt text --]
        [#assign assetTitle=i18n['image.alt.unavailable']]
        [#if image.asset?? && image.asset.title?has_content]
            [#assign assetTitle=image.asset.title]
        [/#if]

        [#-- Alt text and title --]
        [#assign imageAlt=content.altText!content.title!i18n['image.alt.prefix']+": "+assetTitle]
        [#assign imageTitle=content.title!content.altText!i18n['image.alt.prefix']+": "+assetTitle]

        [#assign imageLink=image.link /]
        [#-- For PNGs it might be useful to use render the original asset and therefore bypassing imaging --]
        [#if useOriginal]
            [#assign imageLink=image.asset.link /]
        [/#if]

        [#-- Render the image --]
        <img src="${imageLink}" alt="${imageAlt}" title="${imageTitle}" class="${imageClass}" />
    [/#if]
[/#macro]
