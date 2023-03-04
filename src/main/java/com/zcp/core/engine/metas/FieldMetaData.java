package com.zcp.core.engine.metas;

import lombok.Data;

/**
 * @author ZCP
 * @title: FieldMetaData
 * @projectName JavaGen
 * @description: 字段元数据
 * @date 2023/1/16 16:56
 */
public class FieldMetaData extends MetaData<String, Object> {

    public Boolean getIsPrimaryKey() {
        return (Boolean) get("isPrimaryKey");
    }

    public void setIsPrimaryKey(Boolean isPrimaryKey) {
        put("isPrimaryKey", isPrimaryKey);
    }

    /**
     * 是否是查询字段
     *
     * @return
     */
    public Boolean getIsQueryField() {
        return (Boolean) get("isQueryField");
    }

    public void setIsQueryField(Boolean isQueryField) {
        put("isQueryField", isQueryField);
    }

    /**
     * 查询类型
     *
     * @param queryType
     */
    public void setQueryType(String queryType) {
        put("queryType", queryType);
    }

    public String getQueryType() {
        return (String) get("queryType");
    }

    /**
     * 数据库字段名
     *
     * @return
     */
    public String getColumnName() {
        return (String) get("columnName");
    }

    public void setColumnName(String columnName) {
        put("columnName", columnName);
    }

    /**
     * java实例名
     *
     * @return
     */
    public String getName() {
        return (String) get("name");
    }

    public void setName(String name) {
        put("name", name);
    }

    public String getDbType() {
        return (String) get("dbType");
    }

    public void setDbType(String dbType) {
        put("dbType", dbType);
    }

    public void setClassType(String classType) {
        put("className", classType);
    }

    public void setFullClassType(String fullClassType) {
        put("fullClassName", fullClassType);
    }

    public String getFullClassType() {
        return (String) get("FullClassType");
    }

    public String getClassType() {
        return (String) get("className");
    }

    public String getComment() {
        return (String) get("comment");
    }

    public void setComment(String comment) {
        put("comment", comment);
    }


}
