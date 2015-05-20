$(document).ready(function(){
    $(window).bind('scroll', function() {
        var navHeight = 90; // custom nav height
        ($(window).scrollTop() > navHeight) ? $('#sticky-nav').addClass('sticky') : $('#sticky-nav').removeClass('sticky');
    });
});