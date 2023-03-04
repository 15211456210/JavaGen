package com.zcp.core.processor;

import cn.hutool.core.util.StrUtil;
import com.zcp.core.CodeGenerator;
import com.zcp.core.engine.Context;
import com.zcp.core.engine.metas.FieldMetaData;
import com.zcp.core.engine.metas.MetaData;
import com.zcp.core.engine.query.QueryFieldInfo;

import java.util.List;

/**
 * @author ZCP
 * @title: QueryFieldMetaDataProcessor
 * @projectName JavaGen
 * @description: 查询字段处理
 * @date 2023/1/22 19:26
 */
public class QueryFieldMetaDataProcessor extends MetadataProcessor {

    public QueryFieldMetaDataProcessor(Context context) {
        super(context);
    }

    @Override
    public void process(MetaData metaData) {
        FieldMetaData fieldMetaData = (FieldMetaData) metaData;
        String columnName = fieldMetaData.getColumnName();
        // 获取查询字段 信息
        CodeGenerator codeGenerator = (CodeGenerator) getContext().get("codeGenerator");
        List<QueryFieldInfo> queryFieldInfos = codeGenerator.getQueryFieldInfoList();
        if(queryFieldInfos != null && queryFieldInfos.size() > 0){
            for (int i = 0; i < queryFieldInfos.size(); i++) {
                QueryFieldInfo queryFieldInfo = queryFieldInfos.get(i);
                if (StrUtil.equals(queryFieldInfo.getColumnName(), columnName)) {
                    // 是查询字段
                    fieldMetaData.setIsQueryField(Boolean.valueOf(true));
                    fieldMetaData.setQueryType(queryFieldInfo.getQueryType());
                }
            }
        }

        super.process(metaData);
    }
}
