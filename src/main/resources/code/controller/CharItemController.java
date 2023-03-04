package com.cc.zcp.myworld.http.controller;

import com.cc.zcp.myworld.http.entity.CharItem;
import com.cc.zcp.myworld.http.service.ICharItemService;
import com.cc.zcp.myworld.http.query.CharItemQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.lang.Integer;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  CharItem前端控制器
 * </p>
 *
 * @author zcp
 * @since 2022-04-22
 */

@RestController
@RequestMapping("/myworld/CharItem")
public class CharItemController {
    @Autowired
    private ICharItemService iCharItemService;

    @PostMapping("/page")
    @ApiOperation(value = "分页查询",notes = "page")
    public Page<CharItem> page(@RequestBody CharItemQuery query){
        return iCharItemService.page(query.getPage(),query.getWrapper());
    }

    @PostMapping("/list")
    @ApiOperation(value = "列表查询",notes = "list")
    public List<CharItem> list(@RequestBody(required = false)CharItemQuery query){
        return iCharItemService.list(query.getWrapper());
    }

    @GetMapping("/detail")
    @ApiOperation(value = "查看详情",notes = "detail")
    public CharItem getCharItemById(Integer id){
        return iCharItemService.getById(id);
    }

    @PostMapping("/save")
    @ApiOperation(value = "新增",notes = "save")
    public boolean saveCharItem(@RequestBody CharItem charItem){
        return iCharItemService.save(charItem);
    }

    @PostMapping("/saveOrUpdate")
    @ApiOperation(value = "新增/修改",notes = "saveOrUpdate")
    public boolean saveOrUpdate(@RequestBody CharItem charItem){
        return iCharItemService.saveOrUpdate(charItem);
    }

    @PostMapping("/update")
    @ApiOperation(value = "修改",notes = "update")
    public boolean update(@RequestBody CharItem charItem){
        return iCharItemService.updateById(charItem);
    }

    @PostMapping("/delete")
    @ApiOperation(value = "删除",notes = "delete")
    public boolean delete(Integer id){
        return iCharItemService.removeById(id);
    }

}
