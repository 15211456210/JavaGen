package com.zcp.core.engine.metas;

/**
 * @author ZCP
 * @title: PosMetaData
 * @projectName JavaGen
 * @description: 位置元数据
 * @date 2023/1/16 13:05
 */
public class PosMetaData extends MetaData<String, Integer> {

    /**
     * 获取标签开始位置
     *
     * @return
     */
    public int getHeadBeginIdx() {
        return get("headBeginIdx");
    }

    /**
     * 获取标签结束位置
     *
     * @return
     */
    public int getHeadEndIdx() {
        return get("headEndIdx");
    }

    /**
     * 获取内容开始位置
     *
     * @return
     */
    public int getContentBeginIdx() {
        return get("contentBeginIdx");
    }

    /**
     * 获取内容结束位置
     *
     * @return
     */
    public int getContentEndIdx() {
        return get("contentEndIdx");
    }

    /**
     * 获取结束开始位置
     *
     * @return
     */
    public int getFootBeginIdx() {
        return get("footBeginIdx");
    }

    /**
     * 获取结束结束位置
     *
     * @return
     */
    public int getFootEndIdx() {
        return get("footEndIdx");
    }
}
