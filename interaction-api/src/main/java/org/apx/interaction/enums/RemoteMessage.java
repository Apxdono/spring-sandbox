package org.apx.interaction.enums;

/**
 * Created with IntelliJ IDEA.
 * User: oleg
 * Date: 14.04.14
 * Time: 15:13
 * To change this template use File | Settings | File Templates.
 */
public enum RemoteMessage {
    REGISTRY_NOT_FOUND("Remote registry is not found"),
    EMPTY_PARAMETER("One or more parameters is null"),
    INTERNAL_ERROR("Internal error"),
    NOT_A_REMOTE_OBJECT("Object cannot be treated as remote"),
    RESOURCE_NOT_FOUND("Resource not found"),
    OK("OK"),

    ;


    String message;

    RemoteMessage(String m){
        message = m;
    }

    public String getMessage(){
        return message;
    }

}
