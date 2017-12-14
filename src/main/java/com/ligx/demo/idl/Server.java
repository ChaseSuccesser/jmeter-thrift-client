package com.ligx.demo.idl;

import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.server.TNonblockingServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TTransportException;

/**
 * Created by Administrator on 2016/7/16.
 */
public class Server {

    public static void main(String[] args) {
        try {
            TNonblockingServerSocket serverTransport = new TNonblockingServerSocket(8080);

            TProtocolFactory protocolFactory = new TBinaryProtocol.Factory();

            TProcessor processor = new HelloWorldService.Processor<>(new HelloWorldServiceImpl());

            TNonblockingServer.Args arg = new TNonblockingServer.Args(serverTransport);
            arg.processor(processor);
            arg.protocolFactory(protocolFactory);

            System.out.println("Start server on port 8080");
            TServer server = new TNonblockingServer(arg);
            server.serve();
        } catch (TTransportException e) {
            e.printStackTrace();
        }
    }

}

