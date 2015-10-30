[#-- Renders a video with an HTML5 tag or by embedding a video service code--]
[#macro video content]
    [#if content.type?has_content]
        [#assign videoCaption = content.videoCaption!]
        [#assign videoCredit = content.videoCredit!]

        [#if content.type=='asset']
            [#assign video = damfn.getAsset(content.assetsource)]

            [#assign videoTitle = '']
            [#if video.title?has_content]
                [#assign videoTitle = video.title]
            [/#if]
            [#assign assetType = video.mimeType]

            [#assign videoSrc = video.link]

            [#-- Video caption / credit; Falls back to asset's properties --]
            [#if !videoCaption?has_content]
                [#assign videoCaption =video.caption!videoTitle!]
            [/#if]

            [#if !videoCredit?has_content]
                [#assign videoCredit = video.copyright!]
            [/#if]

            [#if content.assetposter?has_content]
                [#assign assetPoster = damfn.getAsset(content.assetposter)!]
                [#if assetPoster?has_content]
                    [#assign poster = "poster='${assetPoster.link}'"]
                [/#if]
            [/#if]

            [#if content.assetpreload?has_content]
                [#assign preload = "preload='${content.assetpreload}'"]
            [/#if]

            [#-------------- RENDERING  --------------]
            [#if content.scale?boolean]
            <div class="scalable-video">
            [/#if]
            <video ${content.assetautoplay?string('autoplay','')} ${content.assetloop?string('loop','')} ${content.assetmuted?string('muted','')} ${content.assetcontrols?string('controls','')} ${preload!} ${poster!}>
                <source src=${videoSrc} type=${assetType}>
            </video>
            [#if content.scale?boolean]
            </div>
            [/#if]
        [#else]
            [#if content.scale?boolean]
            <div class="scalable-video">
              ${cmsfn.decode(content).embed}
            </div>
            [#else]
              ${cmsfn.decode(content).embed}
            [/#if]
        [/#if]
        [#if videoCaption?has_content || videoCredit?has_content]
            <div id="captions">
                [#if videoCaption?has_content]
                    <span class="video-caption">${videoCaption}</span>
                [/#if]
                [#if videoCredit?has_content]
                    <span class="video-credit">${videoCredit}</span>
                [/#if]
            </div>
        [/#if]
    [/#if]
[/#macro]
