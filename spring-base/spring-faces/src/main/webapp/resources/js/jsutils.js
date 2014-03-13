function previous( el ) {
    if( el.previousElementSibling ) {
        return el.previousElementSibling;
    } else {
        while( el = el.previousSibling ) {
            if( el.nodeType === 1 ) return el;
        }
    }
}

function firstChild(el){
    var firstChild = el.firstChild;
    while(firstChild != null && firstChild.nodeType == 3){ // skip TextNodes
        firstChild = firstChild.nextSibling;
    }
    return firstChild;
}

function hasClass(el,className){
    if (el.classList)
        return el.classList.contains(className);
    else
        return new RegExp('(^| )' + className + '( |$)', 'gi').test(el.className);
}

function addClass(el,className){
    if (el.classList){
        el.classList.add(className);
        return;
    }
    else {
        el.className += ' ' + className;
        return;
    }

}

function removeClass(el,className){
    if (el.classList){
        el.classList.remove(className);
        return;
    }
    else{
        el.className = el.className.replace(new RegExp('(^|\\b)' + className.split(' ').join('|') + '(\\b|$)', 'gi'), ' ');
        return;
    }
}