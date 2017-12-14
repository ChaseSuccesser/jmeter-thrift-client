package com.ligx.demo.idl;

import com.ligx.annotation.ThriftClient;
import com.ligx.thrift.AbstractThriftIdlClient;

public class JmeterThriftIdlClient extends AbstractThriftIdlClient {

    @ThriftClient
    private HelloWorldService.Client client;

    @Override
    public Object doTest() throws Exception {
        String result = client.hello("ligx");
        return result;
    }
}
