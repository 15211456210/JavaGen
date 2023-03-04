package com.cc.zcp.myworld.http.query;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cc.zcp.myworld.http.entity.CharItem;

import java.lang.Integer;

/**
 * @author ZCP
 * @title:
 * @projectName JavaGen
 * @description: CharItem查询实体
 * @date 2022/4/28 15:10
 */
@Data
public class CharItemQuery{


    /**
     * 分页
     */
    private Page<CharItem> page;

    public Wrapper<CharItem> getWrapper() {

        QueryWrapper<CharItem> queryWrapper = new QueryWrapper();

        return queryWrapper;
    }


}
