package com.zcp.core.processor;

import com.zcp.core.engine.metas.MetaData;

/**
 * @author ZCP
 * @title: Processor
 * @projectName JavaGen
 * @description: 处理器接口
 * @date 2023/1/22 17:54
 */
public interface IProcessor {


    /**
     * 处理方法
     */
    void process(MetaData metaData);

}
