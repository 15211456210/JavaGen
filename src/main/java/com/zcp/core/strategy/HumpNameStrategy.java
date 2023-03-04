package com.zcp.core.strategy;

import cn.hutool.core.util.StrUtil;
import com.zcp.core.engine.metas.MetaData;

/**
 * @author ZCP
 * @title: HumpNameStrategy
 * @projectName JavaGen
 * @description: 驼峰命名处理器
 * @date 2023/1/15 12:41
 */
public class HumpNameStrategy extends NameStrategy {

    /**
     * 首字母大写？
     */
    private boolean capitalizeTheFirstLetter = true;

    public HumpNameStrategy() {
        super();
    }

    public HumpNameStrategy(boolean capitalizeTheFirstLetter) {
        super();
        this.capitalizeTheFirstLetter = capitalizeTheFirstLetter;
    }

    /**
     * cus_module_t   ->   CusModuleT
     *
     * @param name
     * @return
     */
    @Override
    public String execute(String name) {
        int idx = 0;
        String nameCopy = name;
        // 处理首字母（大写）
        if (capitalizeTheFirstLetter) {
            char firstChar = nameCopy.charAt(0);
            if (firstChar >= 'a' && firstChar <= 'z') {
                nameCopy = (char) (firstChar - 32) + nameCopy.substring(1);
            }
        }
        while (idx < nameCopy.length()) {
            if (nameCopy.charAt(idx) == '_') {
                nameCopy = nameCopy.substring(0, idx).concat(idx + 1 < nameCopy.length() ? nameCopy.substring(idx + 1) : "");
                if (idx < nameCopy.length()) {
                    char c = nameCopy.charAt(idx);
                    if (c >= 'a' && c <= 'z') {
                        nameCopy = nameCopy.substring(0, idx) + (char) (c - 32) + (idx + 1 < nameCopy.length() ? nameCopy.substring(idx + 1) : "");
                    }
                }
            }
            idx++;
        }
        return nameCopy;
    }

}
