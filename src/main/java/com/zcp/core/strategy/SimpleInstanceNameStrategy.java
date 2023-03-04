package com.zcp.core.strategy;

import cn.hutool.core.util.StrUtil;
import com.zcp.core.strategy.NameStrategy;

/**
 * @author ZCP
 * @title: SimpleInstanceNameStrategy
 * @projectName JavaGen
 * @description: 简单实例名 策略
 * @date 2023/1/19 20:38
 */
public class SimpleInstanceNameStrategy extends NameStrategy {
    /**
     * CustomerContract -> customerContract
     *
     * @param name
     * @return
     */
    @Override
    public String execute(String name) {
        if (StrUtil.isBlank(name)) {
            return name;
        }
        char firstChar = name.charAt(0);

        if (firstChar >= 'A' && firstChar <= 'Z') {
            name = (char) (firstChar + 32) + name.substring(1);
        }
        return name;
    }
}
