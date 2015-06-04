[#macro tourTypeIcon tourTypeName themeName ctx]
    <img class="img-responsive absolute-center" src="${ctx.contextPath}/.resources/${themeName}/img/tour-types/${(tourTypeName?lower_case)!}.svg" alt="${tourTypeName!}" title="${tourTypeName!}">
[/#macro]