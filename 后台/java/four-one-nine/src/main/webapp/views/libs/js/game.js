var number= 5;

if(window.innerWidth > 992){
    number= 5;
}else if(window.innerWidth > 768){
    number= 3;
}else{
    number= 1;
}

var mySwiper = new Swiper('.swiper-container', {
    slidesPerView : number,
    spaceBetween : 20,
    pagination: {
        el: '.swiper-pagination',
    },
})