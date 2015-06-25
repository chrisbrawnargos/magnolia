[#macro categoryNavigation categories title]
<li class="dropdown">
    <a href="#" class="dropdown-toggle" data-toggle="dropdown"  aria-expanded="false">${title} <span class="caret"></span></a>
    <ul class="dropdown-menu" role="menu">
        [#list categories as category]
            <li><a href="${category.link}">${category.name!}</a></li>
        [/#list]
    </ul>
</li>
[/#macro]

[#assign categories = model.categories /]
[#assign title = i18n[model.titleI18nKey] /]

[@categoryNavigation categories title /]