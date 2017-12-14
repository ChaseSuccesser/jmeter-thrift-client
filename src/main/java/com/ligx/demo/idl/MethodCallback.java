package com.ligx.demo.idl;

import org.apache.thrift.async.AsyncMethodCallback;

/**
 * Created by ligongxing on 2017/3/9.
 */
public class MethodCallback implements AsyncMethodCallback {

    private Object result;

    public Object getResult(){
        return result;
    }

    @Override
    public void onComplete(Object response) {
        this.result = response;
    }

    @Override
    public void onError(Exception exception) {

    }
}
