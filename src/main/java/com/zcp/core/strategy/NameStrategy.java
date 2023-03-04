package com.zcp.core.strategy;

import com.zcp.core.engine.metas.MetaData;

/**
 * @author ZCP
 * @title: NameStrategy
 * @projectName JavaGen
 * @description: 名称处理器
 * @date 2023/1/15 12:38
 */
public abstract class NameStrategy implements IStrategy<String, String> {

    public NameStrategy() {
    }

    @Override
    public abstract String execute(String name);
}
