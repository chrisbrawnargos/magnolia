[#-------------- ASSIGNMENTS --------------]
[#assign slideShowId = content.id!"slideshow"]

[#-------------- RENDERING --------------]

<!-- carousel start -->

[#-- We only want to render the javascript when not in edit mode --]
[#if !cmsfn.editMode]
<script type="text/javascript">
    $(document).ready(function () {
        $("#" + "${slideShowId}").slick({
            [#if content.slickConfig?has_content]
            ${content.slickConfig?replace("<br/>", " ")}
            [#else]
                dots: ${content.dots!"true"},
                arrows: ${content.arrows!"true"},
                fade: ${content.fade!"false"},
                variableWidth: ${content.variableWidth!"false"},
                slidesToShow: ${content.slidesToShow!"2"},
                autoplay: ${content.autoplay!"false"},
                autoplaySeconds: ${content.autoplaySeconds!"5"}
            [/#if]
        });
    });
</script>
[/#if]

<div id="${slideShowId}" class="component-carousel">
    [@cms.area name="carouselItems" /]
</div><!-- carousel end -->