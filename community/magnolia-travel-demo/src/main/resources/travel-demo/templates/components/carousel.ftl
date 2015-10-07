[#-------------- ASSIGNMENTS --------------]
[#assign slideShowId = content.id!"slideshow"]

<script type="text/javascript">
    $(document).ready(function () {
        $("#" + "${slideShowId}").slick({
            dots: ${content.dots!"true"},
            arrows: ${content.arrows!"true"},
            fade: ${content.fade!"false"},
            variableWidth: ${content.variableWidth!"false"},
            slidesToShow: ${content.slidesToShow!"2"},
            autoplay: ${content.autoplay!"false"},
            autoplaySeconds: ${content.autoplaySeconds!"5"}
        });
    });
</script>

[#-------------- RENDERING --------------]
<!-- Carousel -->
<div id=${slideShowId}>
    [@cms.area name="carouselItems" /]
</div>