package com.zcp.core;

import cn.hutool.core.util.StrUtil;
import com.zcp.core.engine.Context;
import com.zcp.core.engine.metas.FieldMetaData;
import com.zcp.core.engine.metas.MetaData;
import com.zcp.core.engine.TempleteEngine;
import com.zcp.core.engine.query.QueryFieldInfo;
import com.zcp.core.processor.MetadataProcessor;
import com.zcp.core.processor.QueryFieldMetaDataProcessor;
import com.zcp.core.strategy.*;
import com.zcp.core.utils.DbUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * @author ZCP
 * @title: CodeGenerator
 * @projectName JavaGen
 * @description: CodeGenerator 代码逆向生成类
 * @date 2023/1/14 20:32
 */
@Slf4j
@Data
public class CodeGenerator {

    /**
     * 代码生成路径
     */
    private static final String CODE_DIR = "src\\main\\resources\\code";

    /**
     * 模板路径
     */
    private static final String TEMPLETE_DIR = "src\\main\\resources\\templetes";

    /**
     * 包路径
     */
    private String pkg;

    /**
     * 数据库驱动类名
     */
    private String dbDirverClass;

    /**
     * 数据库url
     */
    private String dburl;

    /**
     * 数据库用户
     */
    private String dbuser;

    /**
     * 数据库密码
     */
    private String dbpassword;

    /**
     * 代码生成目录
     */
    private String codeDir;

    /**
     * 模板目录
     */
    private String templeteDir;

    /**
     * 实体目录
     */
    private String entityDir;

    /**
     * mapper目录
     */
    private String mapperDir;

    /**
     * serive目录
     */
    private String serviceDir;

    /**
     * seriveImpl目录
     */
    private String serviceImplDir;

    /**
     * controller目录
     */
    private String controllerDir;

    /**
     * query目录
     */
    private String queryDir;

    /**
     * 应用的请求路径
     */
    private String requestPath;

    /**
     * 表名称
     */
    private String tableName;

    /**
     * 表前缀
     */
    private String tablePrefix;

    /**
     * 字段前缀
     */
    private String fieldPrefix;

    /**
     * 实体模板文件名
     */
    private String entityTempleteName;

    /**
     * mapper模板文件名
     */
    private String mapperTempleteName;

    /**
     * service模板文件名
     */
    private String serviceTempleteName;

    /**
     * serviceImpl模板文件名
     */
    private String serviceImplTempleteName;

    /**
     * controller模板文件名
     */
    private String controllerTempleteName;

    /**
     * query模板名称
     */
    private String queryTempleteName;

    /**
     * 实体类名称策略
     * 默认下划线转驼峰命名
     */
    private List<NameStrategy> entityClassNameStrategies = new ArrayList<>();

    /**
     * 实体字段名称策略
     * 默认下划线转驼峰名称
     */
    private List<NameStrategy> entityFieldNameStrategies = new ArrayList<>();

    /**
     * 类型策略
     */
    private NameStrategy classTypeNameStrategy = new SimpleClassNameStrategy();

    /**
     * mappername策略
     */
    private List<NameStrategy> mapperClassNameStrategies = new ArrayList<>();

    /**
     * service命名策略
     */
    private List<NameStrategy> serviceClassNameStrategies = new ArrayList<>();

    /**
     * serviceImpl命名策略
     */
    private List<NameStrategy> serviceImplClassNameStrategies = new ArrayList<>();

    /**
     * controller命名策略
     */
    private List<NameStrategy> controllerClassNameStrategies = new ArrayList<>();

    /**
     * service 实例名命名策略
     */
    private List<NameStrategy> serviceInstanceNameStrategies = new ArrayList<>();

    /**
     * entity 实例命名策略
     */
    private List<NameStrategy> entityInstanceNameStrategies = new ArrayList<>();

    /**
     * entity 实例命名策略
     */
    private List<NameStrategy> queryClassNameStrategies = new ArrayList<>();

    /**
     * 元数据处理器
     */
    private MetadataProcessor fieldMetaDataProcessorsChain;

    /**
     * 查询字段名
     */
    private List<QueryFieldInfo> queryFieldInfoList = new ArrayList<>();


    public CodeGenerator() {
        init();
    }

    private void init() {
        // 创建代码生成目录
        createGenCodeDir();
        initTempletes();
        initStrategies();
        initProcessors();
    }

    /**
     * 初始化fieldMetaDataProcessorsChain
     */
    private void initProcessors() {
        Context context = new Context();
        context.put("codeGenerator", this);
        fieldMetaDataProcessorsChain = new QueryFieldMetaDataProcessor(context);
    }

    /**
     * 初始化策略
     */
    private void initStrategies() {
        entityClassNameStrategies.add(new HumpNameStrategy(true));
        entityFieldNameStrategies.add(new HumpNameStrategy(false));
        // 默认mapper名称  EntityMapper
        mapperClassNameStrategies.add(new AppendNameStrategy(0, "Mapper"));
        // 默认service名称  IEntityService
        serviceClassNameStrategies.add(new AppendNameStrategy(1, "I"));
        serviceClassNameStrategies.add(new AppendNameStrategy(0, "Service"));
        // 默认serviceImpl名称 EntityServiceImpl
        serviceImplClassNameStrategies.add(new AppendNameStrategy(0, "ServiceImpl"));
        // 默认iEntitySerivce 实例名
        serviceInstanceNameStrategies.add(new AppendNameStrategy(1, "i"));
        serviceInstanceNameStrategies.add(new AppendNameStrategy(0, "Service"));
        // 默认EntityController 控制类名
        controllerClassNameStrategies.add(new AppendNameStrategy(0, "Controller"));
        // 默认 entity
        entityInstanceNameStrategies.add(new SimpleInstanceNameStrategy());
        // 默认 EntityQuery
        queryClassNameStrategies.add(new AppendNameStrategy(0, "Query"));
    }

    /**
     * 添加查询列
     *
     * @param queryFieldInfo
     * @return
     */
    public CodeGenerator addQueryFieldInfo(QueryFieldInfo queryFieldInfo) {
        queryFieldInfoList.add(queryFieldInfo);
        return this;
    }

    /**
     * 添加查询列
     * @param columnName
     * @param queryType
     * @return
     */
    public CodeGenerator addQueryFieldInfo(String columnName, String queryType) {
        return addQueryFieldInfo(new QueryFieldInfo(columnName, queryType));
    }

    /**
     * 添加字段名称生成策略
     *
     * @param strategy
     * @param reset    是否重置
     * @return
     */
    public List<NameStrategy> addEntityFieldNameStrategy(NameStrategy strategy, boolean reset) {
        if (reset) {
            this.entityFieldNameStrategies.clear();
        }
        this.entityFieldNameStrategies.add(strategy);
        return this.entityFieldNameStrategies;
    }

    /**
     * 添加实体名称生成策略
     *
     * @param strategy
     * @param reset    是否重置
     * @return
     */
    public List<NameStrategy> addEntityNameStrategy(NameStrategy strategy, boolean reset) {
        if (reset) {
            this.entityClassNameStrategies.clear();
        }
        this.entityClassNameStrategies.add(strategy);
        return this.entityClassNameStrategies;
    }

    /**
     * 初始化模板信息
     */
    private void initTempletes() {
        String workPath = System.getProperty("user.dir");
        this.templeteDir = workPath.concat(File.separator).concat(TEMPLETE_DIR);
        this.setEntityTempleteName("entity.mdl");
        this.setMapperTempleteName("mapper.mdl");
        this.setServiceTempleteName("service.mdl");
        this.setServiceImplTempleteName("serviceImpl.mdl");
        this.setControllerTempleteName("controller.mdl");
        this.setQueryTempleteName("query.mdl");
    }

    /**
     * 生成代码
     */
    public void generateCode() {
        genCode();
    }

    /**
     * 生成代码生成目录
     */
    private void createGenCodeDir() {
        String workPath = System.getProperty("user.dir");
        this.codeDir = workPath.concat(File.separator).concat(CODE_DIR);

        File file = new File(codeDir);
        if (!file.exists()) {
            file.mkdir();
        }

        this.entityDir = codeDir.concat(File.separator).concat("entity");
        file = new File(entityDir);
        if (!file.exists()) {
            file.mkdir();
        }

        this.mapperDir = codeDir.concat(File.separator).concat("mapper");
        file = new File(mapperDir);
        if (!file.exists()) {
            file.mkdir();
        }

        this.serviceDir = codeDir.concat(File.separator).concat("service");
        file = new File(serviceDir);
        if (!file.exists()) {
            file.mkdir();
        }

        this.serviceImplDir = serviceDir.concat(File.separator).concat("impl");
        file = new File(serviceImplDir);
        if (!file.exists()) {
            file.mkdir();
        }

        this.controllerDir = codeDir.concat(File.separator).concat("controller");
        file = new File(controllerDir);
        if (!file.exists()) {
            file.mkdir();
        }

        this.queryDir = codeDir.concat(File.separator).concat("query");
        file = new File(queryDir);
        if (!file.exists()) {
            file.mkdir();
        }
    }

    /**
     * 生成代码
     */
    private void genCode() {
        if (StrUtil.isNotBlank(this.tableName)) {
            executeGenCode(tableName);
        } else {
            List<String> tableList = getTableList();
            for (int i = 0; i < tableList.size(); i++) {
                executeGenCode(tableList.get(i));
            }
        }

    }

    /**
     * 获取所有表
     *
     * @return
     */
    private List<String> getTableList() {
        ArrayList<String> tableList = new ArrayList<>();
        Connection conn = null;
        Statement statement = null;
        ResultSet rs = null;
        try {
            Class.forName(dbDirverClass);
            conn = DriverManager.getConnection(dburl, dbuser, dbpassword);

            statement = conn.createStatement();
            rs = statement.executeQuery("show tables");
            log.info("tables:");
            while (rs.next()) {
                log.info(rs.getString(1));
                tableList.add(rs.getString(1));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
                statement.close();
                conn.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        }
        return tableList;
    }

    /**
     * 根据表生成代码
     *
     * @param tableName
     */
    private void executeGenCode(String tableName) {
        // 获取元数据
        MetaData metaData = new MetaData<String, Object>();
        metaData.put("tableName", tableName);
        initMetaData(metaData);
        // 生成实体类
        genEntityCode(metaData);
        // 生成query
        genQueryCode(metaData);
        // 生成mapper
        genMapperCode(metaData);
        // 生成service
        genServiceCode(metaData);
        genServiceImplCode(metaData);
        // 生成controller
        genControllerCode(metaData);
    }

    /**
     * 生成query代码
     *
     * @param metaData
     */
    private void genQueryCode(MetaData metaData) {
        String queryClassName = getQueryClassName(metaData);
        // 生成的java类文件
        File codeFile = new File(queryDir.concat(File.separator).concat(queryClassName).concat(".java"));
        // .mdl模板文件
        File mdlFile = new File(templeteDir.concat(File.separator).concat(queryTempleteName));
        // 生成文件
        genCodeByMdl(metaData, codeFile, mdlFile);
    }

    /**
     * 获取query类名称
     *
     * @param metaData
     * @return
     */
    private String getQueryClassName(MetaData metaData) {
        String queryClassName = (String) metaData.get("entityClassName");
        for (int i = 0; i < queryClassNameStrategies.size(); i++) {
            queryClassName = queryClassNameStrategies.get(i).execute(queryClassName);
        }
        metaData.put("queryClassName", queryClassName);
        return queryClassName;
    }

    /**
     * 生成controller代码
     *
     * @param metaData
     */
    private void genControllerCode(MetaData metaData) {
        String controllerClassName = getControllerClassName(metaData);
        // 生成的java类文件
        File codeFile = new File(controllerDir.concat(File.separator).concat(controllerClassName).concat(".java"));
        // .mdl模板文件
        File mdlFile = new File(templeteDir.concat(File.separator).concat(controllerTempleteName));
        // 生成文件
        genCodeByMdl(metaData, codeFile, mdlFile);

    }

    /**
     * 获取controller类名
     *
     * @param metaData
     * @return
     */
    private String getControllerClassName(MetaData metaData) {
        String controllerClassName = (String) metaData.get("entityClassName");
        for (int i = 0; i < controllerClassNameStrategies.size(); i++) {
            controllerClassName = controllerClassNameStrategies.get(i).execute(controllerClassName);
        }
        metaData.put("controllerClassName", controllerClassName);
        return controllerClassName;
    }

    /**
     * 生成serviceImpl类
     *
     * @param metaData
     */
    private void genServiceImplCode(MetaData metaData) {
        String serviceImplClassName = getServiceImplClassName(metaData);
        // 生成的java类文件
        File codeFile = new File(serviceImplDir.concat(File.separator).concat(serviceImplClassName).concat(".java"));
        // .mdl模板文件
        File mdlFile = new File(templeteDir.concat(File.separator).concat(serviceImplTempleteName));
        // 生成文件
        genCodeByMdl(metaData, codeFile, mdlFile);
    }

    /**
     * 获取serviceImpl类名
     *
     * @param metaData
     * @return
     */
    private String getServiceImplClassName(MetaData metaData) {
        String serviceImplClassName = (String) metaData.get("entityClassName");
        for (int i = 0; i < serviceImplClassNameStrategies.size(); i++) {
            serviceImplClassName = serviceImplClassNameStrategies.get(i).execute(serviceImplClassName);
        }
        metaData.put("serviceImplClassName", serviceImplClassName);
        return serviceImplClassName;
    }

    /**
     * 生成service代码
     *
     * @param metaData
     */
    private void genServiceCode(MetaData metaData) {
        String serviceClassName = getServiceClassName(metaData);
        getServiceInstanceName(metaData);
        // 生成的java类文件
        File codeFile = new File(serviceDir.concat(File.separator).concat(serviceClassName).concat(".java"));
        // .mdl模板文件
        File mdlFile = new File(templeteDir.concat(File.separator).concat(serviceTempleteName));
        // 生成文件
        genCodeByMdl(metaData, codeFile, mdlFile);
    }

    /**
     * 获取service示例名称
     *
     * @param metaData
     * @return
     */
    private String getServiceInstanceName(MetaData metaData) {
        String serviceInstanceName = (String) metaData.get("entityClassName");
        for (int i = 0; i < serviceInstanceNameStrategies.size(); i++) {
            serviceInstanceName = serviceInstanceNameStrategies.get(i).execute(serviceInstanceName);
        }
        metaData.put("serviceInstanceName", serviceInstanceName);
        return serviceInstanceName;
    }

    /**
     * 根据模板生成代码
     *
     * @param metaData 元数据
     * @param codeFile 最终生成的代码文件
     * @param mdlFile  模板文件
     */
    private void genCodeByMdl(MetaData metaData, File codeFile, File mdlFile) {
        FileReader mapperMdlFileReader = null;
        FileWriter mapperFileWriter = null;
        try {
            if (!mdlFile.exists()) {
                return;
            }
            TempleteEngine templeteEngine = new TempleteEngine();
            mapperMdlFileReader = new FileReader(mdlFile);
            codeFile.createNewFile();
            mapperFileWriter = new FileWriter(codeFile);
            char[] chars = new char[1024 * 1024];
            int read = 0;
            while ((read = mapperMdlFileReader.read(chars)) != -1) {
                log.info("read:{}", read);
                String mdlData = new String(chars, 0, read);
//                log.info(mdlData);
                StringBuffer appendData = new StringBuffer();
                templeteEngine.execute(metaData, mdlData, appendData);
                mapperFileWriter.write(appendData.toString());
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                mapperMdlFileReader.close();
                mapperFileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取servicename
     *
     * @param metaData
     * @return
     */
    private String getServiceClassName(MetaData metaData) {
        String serviceClassName = (String) metaData.get("entityClassName");
        for (int i = 0; i < serviceClassNameStrategies.size(); i++) {
            serviceClassName = serviceClassNameStrategies.get(i).execute(serviceClassName);
        }
        metaData.put("serviceClassName", serviceClassName);
        return serviceClassName;
    }

    /**
     * 生成mapper类
     *
     * @param metaData
     */
    private void genMapperCode(MetaData metaData) {
        String mapperClassName = getMapperClassName(metaData);
        // 生成的java类文件
        File codeFile = new File(mapperDir.concat(File.separator).concat(mapperClassName).concat(".java"));
        // .mdl模板文件
        File mdlFile = new File(templeteDir.concat(File.separator).concat(mapperTempleteName));
        // 生成文件
        genCodeByMdl(metaData, codeFile, mdlFile);
    }

    /**
     * 获取mappername
     *
     * @param metaData
     * @return
     */
    private String getMapperClassName(MetaData metaData) {
        String mapperClassName = (String) metaData.get("entityClassName");
        for (int i = 0; i < mapperClassNameStrategies.size(); i++) {
            mapperClassName = mapperClassNameStrategies.get(i).execute(mapperClassName);
        }
        metaData.put("mapperClassName", mapperClassName);
        return mapperClassName;
    }

    /**
     * 生成实体类
     *
     * @param metaData
     */
    private void genEntityCode(MetaData metaData) {
        String entityClassName = getEntityClassName(metaData);
        getEntityInstanceName(metaData);
        // 生成的java类文件
        File codeFile = new File(entityDir.concat(File.separator).concat(entityClassName).concat(".java"));
        // .mdl模板文件
        File mdlFile = new File(templeteDir.concat(File.separator).concat(entityTempleteName));
        // 生成文件
        genCodeByMdl(metaData, codeFile, mdlFile);

    }

    /**
     * 获取实体示例名称
     *
     * @param metaData
     * @return
     */
    private String getEntityInstanceName(MetaData metaData) {
        String entityInstanceName = (String) metaData.get("entityClassName");
        for (int i = 0; i < entityInstanceNameStrategies.size(); i++) {
            entityInstanceName = entityInstanceNameStrategies.get(i).execute(entityInstanceName);
        }
        metaData.put("entityInstanceName", entityInstanceName);
        return entityInstanceName;
    }

    /**
     * 获取元数据
     *
     * @param metaData
     * @return
     */
    private MetaData initMetaData(MetaData metaData) {
        String tableName = (String) metaData.get("tableName");
        metaData.put("package", pkg);
        metaData.put("requestPath", requestPath);
        getEntityClassName(metaData);
        Connection conn = null;
        Statement statement = null;
        ResultSet rs = null;
        ResultSet metaRs = null;
        try {
            log.info("start gen table : [{}]", tableName);
            conn = DbUtil.getConnection(dbDirverClass, dburl, dbuser, dbpassword);
            statement = conn.createStatement();
            // 获取表信息
            rs = statement.executeQuery(String.format("SHOW TABLE STATUS WHERE name='%s'", tableName));
            if (rs.next()) {
                metaData.put("tableComment", rs.getString("Comment"));
            }
            // 字段类型自动判断
            metaRs = statement.executeQuery(String.format("select * from %s where 1=2", tableName));
            ResultSetMetaData resultSetMetaData = metaRs.getMetaData();
            int columnCount = resultSetMetaData.getColumnCount();
            HashMap<String, String> fieldClassTypeMap = new HashMap<>();
            for (int i = 1; i <= columnCount; i++) {
                fieldClassTypeMap.put(resultSetMetaData.getColumnName(i), resultSetMetaData.getColumnClassName(i));
            }
            rs = statement.executeQuery(String.format("show full columns from %s", tableName));
            ArrayList<MetaData> fieldsMetaData = new ArrayList<>();
            metaData.put("fields", fieldsMetaData);
            ArrayList<MetaData> importMetaData = new ArrayList<>();
            metaData.put("imports", importMetaData);
            HashSet<String> importSet = new HashSet<>();

            while (rs.next()) {
                FieldMetaData fieldMetaData = new FieldMetaData();
                fieldMetaData.setColumnName(rs.getString("Field"));
                fieldMetaData.setName(getFieldName(rs.getString("Field")));
                fieldMetaData.setDbType(rs.getString("Type"));
                fieldMetaData.setIsPrimaryKey("PRI".equals(rs.getString("Key")));
                String fieldClassType = fieldClassTypeMap.get(rs.getString("Field"));
                fieldMetaData.setFullClassType(fieldClassType);
                fieldMetaData.setClassType(classTypeNameStrategy.execute(fieldClassType));
                fieldMetaData.setComment(rs.getString("Comment"));

                if (importSet.add(fieldClassType)) {
                    MetaData<String, String> importItem = new MetaData<>();
                    importItem.put("importClassName", fieldClassType);
                    importMetaData.add(importItem);
                }
                if (fieldMetaData.getIsPrimaryKey()) {
                    // 主键
                    metaData.put("primaryKeyClassType", fieldClassType);
                    metaData.put("simplePrimaryKeyClassType", fieldMetaData.getClassType());
                }
                // TODO chain 元数据处理链
                fieldMetaDataProcessorsChain.process(fieldMetaData);
                log.info("列名：[{}],列类型：[{}],java类型：[{}],", fieldMetaData.getColumnName(), fieldMetaData.getDbType(), fieldMetaData.getFullClassType());
                fieldsMetaData.add(fieldMetaData);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbUtil.cleanUp(conn, statement, rs, metaRs);
        }

        return metaData;
    }

    /**
     * 获取字段名称
     *
     * @param fieldName
     * @return
     */
    private String getFieldName(String fieldName) {
        if (StrUtil.isBlank(fieldName)) {
            return "";
        }
        if (StrUtil.isNotBlank(fieldPrefix) && fieldName.startsWith(fieldPrefix)) {
            fieldName = fieldName.replaceFirst(fieldPrefix, "");
        }
        // 处理驼峰命名策略
        for (int i = 0; i < entityFieldNameStrategies.size(); i++) {
            fieldName = entityFieldNameStrategies.get(i).execute(fieldName);
        }
        return fieldName;
    }

    /**
     * 获取实体名
     *
     * @param metaData
     * @return
     */
    private String getEntityClassName(MetaData metaData) {
        String entityClassName = (String) metaData.get("tableName");
        if (StrUtil.isBlank(entityClassName)) {
            return "";
        }
        if (StrUtil.isNotBlank(tablePrefix) && entityClassName.startsWith(tablePrefix)) {
            entityClassName = entityClassName.replaceFirst(tablePrefix, "");
        }
        // 处理驼峰命名策略
        for (int i = 0; i < entityClassNameStrategies.size(); i++) {
            entityClassName = entityClassNameStrategies.get(i).execute(entityClassName);
        }
        metaData.put("entityClassName", entityClassName);
        return entityClassName;
    }

}
