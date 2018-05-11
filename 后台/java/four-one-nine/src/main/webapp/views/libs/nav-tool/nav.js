$(function(){
    function menu_show($_this, $_ul){
        $_ul.eq(0).slideDown(300);
        $_this.find(".right").eq(0).css({
            transform: "rotate(90deg)",
            "-moz-transition": "rotate(90deg)",
            "-webkit-transition": "rotate(90deg)",
            "-o-transition": "rotate(90deg)",
        })
    }
    function menu_hide($_this, $_ul){
        $_ul.eq(0).slideUp(300);
        $_this.find(".right").eq(0).css({
            transform: "rotate(0deg)",
            "-moz-transition": "rotate(0deg)",
            "-webkit-transition": "rotate(0deg)",
            "-o-transition": "rotate(0deg)",
        })
    }
    $(".nav-item").click(function(e){
        var $_this= $(this), $_ul= $_this.find("ul");
        if($_ul[0]){//存在子菜单则 打开 或 关闭 子菜单
            $_ul.css("display") == "block" ? menu_hide($_this, $_ul) : menu_show($_this, $_ul);
        }
        else{//不存在子菜单则激活当前菜单
            //关闭激活的的菜单
            $(".nav-show").removeClass("nav-show");
            //激活当前菜单
            $_this.addClass("nav-show");
        }
        e.stopPropagation();
    })
})
