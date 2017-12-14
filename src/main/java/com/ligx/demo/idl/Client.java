package com.ligx.demo.idl;

import org.apache.thrift.async.TAsyncClientManager;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.transport.TNonblockingSocket;
import org.apache.thrift.transport.TSocket;

import java.io.IOException;

/**
 * Created by Administrator on 2016/7/16.
 */
public class Client {

    public static void main(String[] args) throws IOException {
//        syncClientTest();

        asyncClientTest();
    }


    /**
     * thrift sync client
     */
    private static void syncClientTest() {
        TSocket transport = null;
        try {
            transport = new TSocket("127.0.0.1", 8080);

            TProtocol protocol = new TBinaryProtocol(transport);

            HelloWorldService.Client client = new HelloWorldService.Client(protocol);

            transport.open();

            String result = client.hello("ligx");
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (transport != null) {
                transport.close();
            }
        }
    }


    /**
     * thrift async client
     */
    private static void asyncClientTest() {
        TNonblockingSocket transport = null;
        try {
            transport = new TNonblockingSocket("127.0.0.1", 8080);

            TProtocolFactory protocol = new TBinaryProtocol.Factory();

            TAsyncClientManager clientManager = new TAsyncClientManager();
            MethodCallback callback = new MethodCallback();
            HelloWorldService.AsyncClient asyncClient = new HelloWorldService.AsyncClient(protocol, clientManager, transport);

            asyncClient.hello("ligx", callback);
            Object resp = callback.getResult();
            while(resp == null){
                resp = callback.getResult();
            }
            System.out.println(((HelloWorldService.AsyncClient.hello_call)resp).getResult());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (transport != null) {
                transport.close();
            }
        }
    }
}
