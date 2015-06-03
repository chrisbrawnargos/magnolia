[#macro tourImage assetRendition assetCredit="" altTitle="" cssClass="img-responsive"]
    [#if assetRendition?has_content]
        <img class="${cssClass}" src="${assetRendition.link}" alt="Image: ${altTitle}">
        [#if assetCredit?has_content]
            <span class="image-credit">${assetCredit}&nbsp;
            [#assign license=assetRendition.asset.copyright!]
            [#if license?has_content]
                <a target="_blank" href="https://creativecommons.org/licenses/${license}">${license}</a>
            [/#if]
            </span>
        [/#if]
    [/#if]
[/#macro]