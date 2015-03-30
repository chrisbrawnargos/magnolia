[#macro tourImage assetRendition assetCredit="" altTitle="" cssClass="img-responsive"]
    [#if assetRendition?has_content]
        <img class="${cssClass}" src="${assetRendition.link}" alt="Image: ${altTitle}">
        [#if assetCredit?has_content]
            <span class="image-credit">${assetCredit}</span>
        [/#if]
    [/#if]
[/#macro]