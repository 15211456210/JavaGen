package com.zcp.core.strategy;

import cn.hutool.core.util.StrUtil;

/**
 * @author ZCP
 * @title: SimpleClassNameStrategy
 * @projectName JavaGen
 * @description: java.lang.String -> String
 * @date 2023/1/16 18:25
 */
public class SimpleClassNameStrategy extends NameStrategy {

    @Override
    public String execute(String name) {
        if (StrUtil.isBlank(name)) {
            return "";
        }
        return name.substring(name.lastIndexOf('.') + 1);
    }
}
