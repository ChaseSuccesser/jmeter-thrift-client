package com.ligx.thrift;

import com.alibaba.fastjson.JSON;
import com.facebook.nifty.client.FramedClientConnector;
import com.facebook.swift.service.ThriftClientManager;
import com.google.common.base.Throwables;
import com.google.common.net.HostAndPort;
import com.ligx.annotation.ThriftClient;
import com.ligx.constants.Constants;
import com.ligx.enums.MiddleStatus;
import org.apache.commons.lang3.StringUtils;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

import java.lang.reflect.Field;

/**
 * Author: ligongxing.
 * Date: 2017年12月13日.
 */
public abstract class AbstractThriftSwiftClient extends AbstractJavaSamplerClient implements IThriftClient {


    @Override
    public void setupTest(JavaSamplerContext context) {
        super.setupTest(context);

        String ip = context.getParameter("ip");
        String port = context.getParameter("port");
        if (StringUtils.isBlank(ip) || StringUtils.isBlank(port)) {
            middleResult.setCode(MiddleStatus.FAIL.getCode());
            middleResult.setMsg("'ip' or 'port' param is null");
            return;
        }

        ThriftClientManager thriftClientManager = new ThriftClientManager();

        try {
            Class clazz = this.getClass();
            Object instance = clazz.newInstance();

            Field[] fields = clazz.getDeclaredFields();
            if (fields != null && fields.length > 0) {
                for (Field field : fields) {
                    ThriftClient thriftClient = field.getDeclaredAnnotation(ThriftClient.class);
                    if (thriftClient == null) {
                        continue;
                    }
                    Class<?> fieldClass = field.getType();

                    Object obj = thriftClientManager.createClient(
                            new FramedClientConnector(HostAndPort.fromParts(ip, Integer.parseInt(port))),
                            fieldClass)
                            .get();

                    field.setAccessible(true);
                    field.set(instance, obj);
                }
            }
        } catch (Exception e) {
            middleResult.setCode(MiddleStatus.FAIL.getCode());
            middleResult.setMsg(Throwables.getStackTraceAsString(e));
            e.printStackTrace();
        }
    }

    @Override
    public SampleResult runTest(JavaSamplerContext javaSamplerContext) {
        SampleResult sampleResult = new SampleResult();

        if (middleResult.getCode() != MiddleStatus.SUCCESS.getCode()) {
            sampleResult.setResponseData(middleResult.getMsg(), Constants.ENCODING);
            sampleResult.setSuccessful(false);
            return sampleResult;
        }

        try {
            Object result = doTest();
            sampleResult.setResponseData(result == null ? "ok" : JSON.toJSONString(result), Constants.ENCODING);
            sampleResult.setSuccessful(true);
        } catch (Exception e) {
            e.printStackTrace();
            sampleResult.setResponseData(Throwables.getStackTraceAsString(e), Constants.ENCODING);
            sampleResult.setSuccessful(false);
        }

        return sampleResult;
    }

    @Override
    public void teardownTest(JavaSamplerContext context) {
        super.teardownTest(context);
    }
}
