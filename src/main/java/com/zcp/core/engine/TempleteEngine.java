package com.zcp.core.engine;

import com.zcp.core.engine.metas.MetaData;
import com.zcp.core.engine.metas.PosMetaData;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.regex.Pattern;

/**
 * @author ZCP
 * @title: TempleteEngin
 * @projectName JavaGen
 * @description: 模板引擎
 * @date 2023/1/15 20:55
 */
@Slf4j
public class TempleteEngine {

    /**
     * 执行引擎替换
     *
     * @param metaData
     * @param templete
     */
    public void execute(MetaData<String, Object> metaData, String templete, StringBuffer result) {
        int idx = 0;
        while (idx < templete.length()) {
            char c = templete.charAt(idx);
            if (c == '$' && templete.charAt(idx + 1) == '{') {
                // 处理变量
                int endPos = templete.indexOf("}", idx + 1);
                String propertyName = templete.substring(idx + 2, endPos);
                result.append(metaData.get(propertyName));
                idx = endPos + 1;
            } else if (c == '<' && templete.charAt(idx + 1) == '#') {
                // 获取语法
                String grammar = templete.substring(idx + 2, templete.indexOf(' ', idx + 2));
                PosMetaData posMetaData = getPosMetaData(templete, idx);
                Object[] grammerData;
                switch (grammar) {
                    case "foreach":
                        grammerData = getGrammerData("foreach", templete, metaData, posMetaData);
                        analysisForeach(templete, (String) grammerData[0], (List<MetaData>) grammerData[1], posMetaData, result);
                        idx = posMetaData.getFootEndIdx() + 1;
                        break;
                    case "if":
                        grammerData = getGrammerData("if", templete, metaData, posMetaData);
                        analysisIf(templete, (String) grammerData[0], metaData, posMetaData, result);
                        idx = posMetaData.getFootEndIdx() + 1;
                        break;
                }
            } else {
                result.append(c);
                idx++;
            }

        }


    }

    /**
     * 获取标签开始结束位置
     * <#foreach field in fields>
     * private null null;
     *
     * </end>
     *
     * @param templete
     * @param idx
     * @return
     */
    private PosMetaData getPosMetaData(String templete, int idx) {
        PosMetaData metaData = new PosMetaData();
        int temIdx = idx;
        metaData.put("headBeginIdx", temIdx);
        while (temIdx < templete.length()) {
            if (templete.charAt(temIdx) == '>') {
                break;
            }
            temIdx++;
        }
        metaData.put("headEndIdx", temIdx);
        metaData.put("contentBeginIdx", temIdx + 1);
        temIdx = idx;
        int grammaticalCnt = 0;
        while (temIdx < templete.length()) {
            if (templete.charAt(temIdx) == '<' && "<#end>".equals(templete.substring(temIdx, temIdx + 6))) {
                if (--grammaticalCnt == 0) {
                    metaData.put("contentEndIdx", temIdx - 1);
                    metaData.put("footStartIdx", temIdx);
                    metaData.put("footEndIdx", temIdx + 5);
                    break;
                }
                temIdx = temIdx + 6;
                continue;
            }
            if (templete.charAt(temIdx) == '<' && templete.charAt(temIdx + 1) == '#') {
                grammaticalCnt++;
                temIdx++;
                continue;
            }
            temIdx++;
        }

        return metaData;
    }

    /**
     * 解析foreach语法
     *
     * @param templete
     * @param item
     * @param metaDataList
     * @param posMetaData
     * @param result
     */
    private void analysisForeach(String templete, String item, List<MetaData> metaDataList, PosMetaData posMetaData, StringBuffer result) {
        for (int i = 0; i < metaDataList.size(); i++) {
            MetaData metaData = metaDataList.get(i);
            metaData.setPrefix(item);
            execute(metaData, templete.substring(posMetaData.getContentBeginIdx(), posMetaData.getContentEndIdx() + 1), result);
        }
    }

    /**
     * 逻辑运算符
     */
    private static final String[] LOGICAL_OPERATORS = new String[]{"||", "&&", "!"};

    /**
     * 比较运算符
     */
    private static final String[] COMPARISON_OPERATORS = new String[]{"<", ">", "="};

    /**
     * 解析if语法
     * ${field.isPrimaryKey}='true[bool]'
     *
     * @param templete
     * @param condition
     * @param metaData
     * @param posMetaData
     * @param result
     */
    private void analysisIf(String templete, String condition, MetaData<String, Object> metaData, PosMetaData posMetaData, StringBuffer result) {
        if (calcCondition(condition, metaData)) {
            // test 条件满足
            execute(metaData, templete.substring(posMetaData.getContentBeginIdx(), posMetaData.getContentEndIdx() + 1), result);
        }
    }

    /**
     * 计算条件表达式
     * ${field.isPrimaryKey}='true[bool]' || ${field.name}='id[str]'
     *
     * @param condition
     * @param metaData
     * @return
     */
    private boolean calcCondition(String condition, MetaData<String, Object> metaData) {
        int idx = 0;
        StringBuffer leftCondition = new StringBuffer();
        boolean result = false;
        condition = condition.replaceAll(" ", "");
        int conditionLength = condition.length();
        if (condition.indexOf("&&") != -1 || condition.indexOf("||") != -1 || condition.indexOf("!") != -1) {
            // 逻辑表达式
            while (idx < conditionLength) {
                if (idx + 1 < conditionLength && condition.charAt(idx) == '&' && condition.charAt(idx + 1) == '&') {
                    return calcCondition(leftCondition.toString(), metaData) && calcCondition(condition.substring(idx + 2), metaData);
                } else if (idx + 1 < conditionLength && condition.charAt(idx) == '|' && condition.charAt(idx + 1) == '|') {
                    return calcCondition(leftCondition.toString(), metaData) || calcCondition(condition.substring(idx + 2), metaData);
                } else if (condition.charAt(idx) == '!') {
                    return !calcCondition(condition.substring(idx + 1), metaData);
                } else {
                    leftCondition.append(condition.charAt(idx));
                }
                idx++;
            }
        } else {
            // 比较逻辑
            int optIdx = condition.indexOf("=");
            Object leftValue = getsValueOfTheExpression(condition.substring(0, optIdx), metaData);
            Object rightValue = getsValueOfTheExpression(condition.substring(optIdx + 1), metaData);
            if (leftValue instanceof String) {
                return leftValue.equals(rightValue);
            } else if (leftValue instanceof Boolean) {
                return !((boolean) leftValue ^ (boolean) rightValue);
            }
        }
        return result;
    }

    /**
     * 获取表达式的值
     * ${field.name}='id[str]'
     *
     * @param expression
     * @param metaData
     * @return
     */
    public Object getsValueOfTheExpression(String expression, MetaData<String, Object> metaData) {
        // 判断是否${} 取值
        if (Pattern.matches("^\\$\\{.*\\}$", expression)) {
            String fieldName = expression.substring(expression.indexOf("{") + 1, expression.indexOf("}"));
            return metaData.get(fieldName);
        } else if (Pattern.matches("^'.*'$", expression)) {
            String value = expression.substring(1, expression.indexOf("["));
            String type = expression.substring(expression.indexOf("[") + 1, expression.indexOf("]"));
            if ("str".equals(type)) {
                return value;
            } else if ("bool".equals(type)) {
                return "true".equals(value);
            }
        }
        return null;
    }

    /**
     * 获取语法 元数据
     * <#foreach field in fields>
     * <#if test="${field.isPrimaryKey} = 'true[bool]'" >@TableId(type = IdType.AUTO)<#end>
     *
     * @param templete
     * @param metaData
     * @param posMetaData
     * @return
     */
    private Object[] getGrammerData(String grammer, String templete, MetaData<String, Object> metaData, PosMetaData posMetaData) {
        String syntaxTag = templete.substring(posMetaData.get("headBeginIdx"), posMetaData.get("headEndIdx"));
        Object[] res = null;
        String[] params;
        switch (grammer) {
            case "foreach":
                params = syntaxTag.split(" ");
                String itemName = params[1].trim();
                String metaName = params[3].trim();
                res = new Object[]{itemName, metaData.get(metaName)};
                break;
            case "if":
                String condition = syntaxTag.substring(syntaxTag.indexOf("\"") + 1, syntaxTag.lastIndexOf("\""));
                res = new Object[]{condition};
                break;
        }

        return res;
    }

}
