package com.ligx.thrift;

import com.alibaba.fastjson.JSON;
import com.facebook.nifty.client.FramedClientConnector;
import com.facebook.swift.service.ThriftClientManager;
import com.google.common.net.HostAndPort;
import com.ligx.annotation.ThriftClient;
import org.apache.commons.lang3.StringUtils;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

import java.lang.reflect.Field;

/**
 * Author: ligongxing.
 * Date: 2017年12月13日.
 */
public abstract class AbstractThriftSwiftClient extends AbstractJavaSamplerClient {


    @Override
    public void setupTest(JavaSamplerContext context) {
        super.setupTest(context);

        String ip = context.getParameter("ip");
        String port = context.getParameter("port");
        if (StringUtils.isBlank(ip) || StringUtils.isBlank(port)) {
            return;
        }

        ThriftClientManager thriftClientManager = new ThriftClientManager();

        try {
            Class clazz = this.getClass();
            Object instance = clazz.newInstance();
            Field[] fields = clazz.getFields();
            if (fields != null && fields.length > 0) {
                for (Field field : fields) {
                    ThriftClient thriftClient = field.getDeclaredAnnotation(ThriftClient.class);
                    if (thriftClient == null) {
                        continue;
                    }
                    Class<?> fieldClazz = field.getType();

                    Object obj = thriftClientManager.createClient(
                            new FramedClientConnector(HostAndPort.fromParts(
                                    StringUtils.isBlank(ip) ? thriftClient.ip() : ip,
                                    Integer.parseInt(port))
                            ), fieldClazz).get();

                    field.set(instance, obj);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public SampleResult runTest(JavaSamplerContext javaSamplerContext) {

        SampleResult sampleResult = new SampleResult();

        try {
            Object result = doTest();
            sampleResult.setResponseData(JSON.toJSONString(result), "utf-8");
            sampleResult.setSuccessful(true);
        } catch (Exception e) {
            e.printStackTrace();
            sampleResult.setSuccessful(false);
        }

        return sampleResult;
    }

    @Override
    public void teardownTest(JavaSamplerContext context) {
        super.teardownTest(context);
    }


    /**
     * 实际的测试逻辑写在这里面
     *
     * 测试正常情况：返回响应
     * 测试异常情况：抛出异常
     *
     * @return
     * @throws Exception
     */
    public abstract Object doTest() throws Exception;
}
