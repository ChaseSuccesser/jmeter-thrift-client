package com.ligx.thrift;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Throwables;
import com.ligx.annotation.ThriftClient;
import com.ligx.constants.Constants;
import com.ligx.enums.MiddleStatus;
import org.apache.commons.lang3.StringUtils;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TNonblockingSocket;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public abstract class AbstractThriftIdlClient extends AbstractJavaSamplerClient implements IThriftClient {

    private TNonblockingSocket transport;

    /**
     * 读取通过JMeter设置的参数;
     * 利用反射创建thrift client实例
     *
     * @param context
     */
    @Override
    public void setupTest(JavaSamplerContext context) {
        super.setupTest(context);

        String ip = context.getParameter("ip");
        String port = context.getParameter("port");
        if (StringUtils.isBlank(ip) || StringUtils.isBlank(port)) {
            middleResult.setCode(MiddleStatus.FAIL.getCode());
            middleResult.setMsg(String.format("'ip' or 'port' param is null ip=%s, port=%s", ip, port));
            return;
        }

        try {
            transport = new TNonblockingSocket(ip, Integer.parseInt(port));
            TProtocol protocol = new TBinaryProtocol(transport);

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

                    Constructor fieldClassConstructor = fieldClass.getConstructor(TProtocol.class);
                    Object fieldClassInstance = fieldClassConstructor.newInstance(protocol);

                    field.setAccessible(true);
                    field.set(instance, fieldClassInstance);
                }
            }
        } catch (Exception e) {
            middleResult.setCode(MiddleStatus.FAIL.getCode());
            middleResult.setMsg(Throwables.getStackTraceAsString(e));
            e.printStackTrace();
        }
    }

    /**
     * JMeter单元测试
     *
     * @param javaSamplerContext
     * @return
     */
    @Override
    public SampleResult runTest(JavaSamplerContext javaSamplerContext) {
        SampleResult sampleResult = new SampleResult();

        if (middleResult.getCode() != MiddleStatus.SUCCESS.getCode()) {
            sampleResult.setResponseData(middleResult.getMsg(), Constants.ENCODING);
            sampleResult.setSuccessful(false);
            return sampleResult;
        }

        try {
            transport.open();

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
        transport.close();
    }
}
