var toggleMenu = function(elem){
    var menu = previous(elem.parentNode);
    var hideButton = firstChild(elem);
    var content = document.getElementById('content');
    if( hasClass(menu,'menu-hidden')){
        removeClass(menu , 'menu-hidden');
//        addClass(content,'content');
        removeClass(content,'content-wider');
        addClass(hideButton , 'arrow-left');
        removeClass(hideButton , 'arrow-right');
    } else {
        addClass(menu , 'menu-hidden');
        addClass(content,'content-wider');
//        removeClass(content,'content');
        addClass(hideButton , 'arrow-right');
        removeClass(hideButton , 'arrow-left');
    }
    Cookies.set('sideMenuVisible', !hasClass(menu,'menu-hidden'));
}