[#macro tourTypeIcon icon tourTypeName]
    [#assign iconRendition = damfn.getRendition(icon,"")!]
    <img class="img-responsive absolute-center" src="${iconRendition.link}" alt="${tourTypeName!}" title="${tourTypeName!}">
[/#macro]