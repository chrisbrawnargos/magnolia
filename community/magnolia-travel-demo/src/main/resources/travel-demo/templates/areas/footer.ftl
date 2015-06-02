[#-------------- ASSIGNMENTS --------------]
[#assign homeLink = cmsfn.link(cmsfn.siteRoot(content))!"/"]
[#assign site = sitefn.site()!]
[#assign theme = sitefn.theme(site)!]

[#-------------- RENDERING --------------]
<!-- FOOTER -->
<footer>

    <div class="footer-1">
        <div class="container">
            <div class="row">
            [#if def.parameters?? && def.parameters.columns?? && def.parameters.columns > 1]
                [#list 1..def.parameters.columns as columnIndex]
                    [@cms.area name="footer${columnIndex}"/]
                [/#list]
            [#else]
                [@cms.area name="footer1"/]
            [/#if]
            </div>
        </div>
    </div>

    <div class="container">
        <div class="row">
            <div class="col-lg-12">
                <p style="text-align:center;margin-top:40px; margin-bottom:30px;">
                    <a class="home" href="${homeLink}"><img
                            src="${ctx.contextPath}/.resources/${theme.name}/img/logo-white.png"
                            style="height:40px;"/></a>
                </p>

                <p style="text-align:center;margin-top:20px; margin-bottom:20px;">
                    &copy; 2015 Magnolia Travel, Inc. | <a href="${ctx.contextPath}/travel/meta/privacy">Privacy</a> |
                    <a href="${ctx.contextPath}/travel/meta/terms">Terms</a> | <a
                        href="${ctx.contextPath}/travel/meta/impressum">Impressum</a>
                </p>
            </div>

        </div>
    </div>

    <div class="footer-2"></div>

</footer>