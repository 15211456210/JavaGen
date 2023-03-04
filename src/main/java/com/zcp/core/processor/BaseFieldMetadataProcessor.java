package com.zcp.core.processor;

import com.zcp.core.engine.metas.FieldMetaData;
import com.zcp.core.engine.metas.MetaData;
import com.zcp.core.utils.DbUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * @author ZCP
 * @title: BaseFieldMetadataProcessor
 * @projectName JavaGen
 * @description: Field基础数据
 * @date 2023/1/22 19:12
 */
public class BaseFieldMetadataProcessor extends MetadataProcessor {


    public BaseFieldMetadataProcessor(MetadataProcessor nextProcessor) {
        super(nextProcessor);
    }

    @Override
    public void process(MetaData metaData) {
        super.process(metaData);
    }
}
