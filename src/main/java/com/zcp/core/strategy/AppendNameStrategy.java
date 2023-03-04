package com.zcp.core.strategy;

import com.zcp.core.engine.metas.MetaData;

/**
 * @author ZCP
 * @title: AppendNameStrategy
 * @projectName JavaGen
 * @description: 名称拼接策略
 * @date 2023/1/16 13:59
 */
public class AppendNameStrategy extends NameStrategy {

    /**
     * 附加 位置  1：开头  0：结尾
     */
    private int pos = 0;

    /**
     * 拼接字符串
     */
    private String appendStr;

    public AppendNameStrategy(int pos, String appendStr) {
        super();
        this.pos = pos;
        this.appendStr = appendStr;
    }

    @Override
    public String execute(String name) {
        return pos == 1 ? appendStr + name : name + appendStr;
    }
}
