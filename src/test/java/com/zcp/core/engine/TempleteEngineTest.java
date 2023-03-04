package com.zcp.core.engine;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author ZCP
 * @title: TempleteEngineTest
 * @projectName JavaGen
 * @description: test
 * @date 2023/1/17 18:04
 */
@Slf4j
public class TempleteEngineTest {

    @Test
    public void regularMatchingTest(){
//        String s = new TempleteEngine().getsValueOfTheExpression("'xxx'", null);
        log.info(!(true ^ false) ? "true": "false");
        log.info(!(true ^ true) ? "true": "false");
        log.info(!(false ^ false) ? "true": "false");
    }

}