package com.zcp.core.strategy;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author ZCP
 * @title: HumpNameProcessorTest
 * @projectName JavaGen
 * @description: test
 * @date 2023/1/15 13:07
 */
@Slf4j
public class HumpNameProcessorTest {

    /**
     * 驼峰命名处理器测试类
     */
    @Test
    public void process() {

        HumpNameStrategy processor = new HumpNameStrategy(true);
        log.info(processor.execute("cus_module_t_"));
    }
}