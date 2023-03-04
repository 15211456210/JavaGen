package com.zcp.core.engine.query;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author ZCP
 * @title: QueryFieldInfo
 * @projectName JavaGen
 * @description: 查询字段信息
 * @date 2023/1/22 19:34
 */
@Data
@AllArgsConstructor
public class QueryFieldInfo {

    private String columnName;

    private String queryType;

}
