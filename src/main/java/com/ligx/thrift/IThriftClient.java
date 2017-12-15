package com.ligx.thrift;

import com.ligx.enums.MiddleStatus;
import com.ligx.model.MiddleResult;

public interface IThriftClient {

    MiddleResult middleResult = new MiddleResult(MiddleStatus.SUCCESS.getCode());

    /**
     * 实际的测试逻辑写在这里面
     *
     * 测试正常情况：返回响应
     * 测试异常情况：抛出异常
     *
     * @return
     * @throws Exception
     */
    Object doTest() throws Exception;
}
