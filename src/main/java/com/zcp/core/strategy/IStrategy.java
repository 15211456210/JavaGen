package com.zcp.core.strategy;

/**
 * @author ZCP
 * @title: IStrategy
 * @projectName JavaGen
 * @description: 策略接口
 * @date 2023/1/15 12:36
 */
public interface IStrategy<T,R> {

    /**
     * 处理方法
     *
     * @return
     */
    R execute(T t);

}
