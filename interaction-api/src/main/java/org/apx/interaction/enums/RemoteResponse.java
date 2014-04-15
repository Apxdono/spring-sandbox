package org.apx.interaction.enums;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: oleg
 * Date: 14.04.14
 * Time: 18:48
 * To change this template use File | Settings | File Templates.
 */
public class RemoteResponse<K> implements Serializable {

    RemoteMessage _message;
    K _result;
    boolean responseComplete = false;

    public RemoteResponse<K> message(RemoteMessage m){
        if(!responseComplete && _message == null){
            _message = m;
        }
        lockResponse();
        return this;
    }

    public RemoteResponse<K> result(K r){
        if(!responseComplete && _result == null){
            _result = r;
        }
        lockResponse();
        return this;
    }

    protected void lockResponse(){
        if(_message != null && _result != null){
            responseComplete = true;
        }
    }

    public RemoteMessage getMessage() {
        return _message;
    }

    public K getResult() {
        return _result;
    }
}
