package com.ligx.demo.idl;

import org.apache.thrift.TException;

/**
 * Created by Administrator on 2016/7/16.
 */
public class HelloWorldServiceImpl implements HelloWorldService.Iface {
    public String hello(String name) throws TException {
        return "hello world:" + name;
    }
}
