package com.zcp.core;

import com.zcp.core.engine.query.QueryFieldInfo;
import com.zcp.core.strategy.AppendNameStrategy;
import com.zcp.core.strategy.HumpNameStrategy;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author ZCP
 * @title: CodeGeneratorTest
 * @projectName JavaGen
 * @description: test
 * @date 2023/1/16 15:05
 */
@Slf4j
public class CodeGeneratorTest {

    /**
     * 驼峰命名处理器测试类
     */
    @Test
    public void genTest() {
        CodeGenerator codeGenerator = new CodeGenerator();
        codeGenerator.setDburl("jdbc:mysql://124.220.30.168:3308/my_world");
        codeGenerator.setDbuser("root");
        codeGenerator.setDbpassword("root");
        codeGenerator.setDbDirverClass("com.mysql.cj.jdbc.Driver");


        codeGenerator.setTableName("t_bd_char_item");
        codeGenerator.setPkg("com.cc.zcp.myworld.http");
        codeGenerator.setRequestPath("myworld");
        codeGenerator.setTablePrefix("t_bd");
//        codeGenerator.addQueryFieldInfo("module_name","like")
//                .addQueryFieldInfo("url","like");
        // 实体类名称生成策略
//        codeGenerator.addEntityNameStrategy(new AppendNameStrategy(0, "Entity"), false);
        // 设置实体类模板
//        codeGenerator.setEntityTempleteName("entity.mdl");
        codeGenerator.generateCode();
    }


}