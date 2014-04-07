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

function sessionExpirationCheck(response,dialog){
    if(response.responseText){
        if(response.responseText.indexOf("sessionExpired")!=-1){
            dialog.show();
        }
    }
}

function errorCheck(response,url,dialog){
    if(response.status == 401){
        sessionExpirationCheck(response,dialog);
    }
    if(response.status == 500){
        location.href= url;
    }
}

function redirectButtonClicked(el,event){
    console.log(el,el.form,event);
    if(event.which == 2){
        el.formtarget = '_blank';
    } else {
        el.formtarget = '_self';
    }
}