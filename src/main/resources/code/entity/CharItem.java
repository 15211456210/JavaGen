package com.cc.zcp.myworld.http.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.lang.Integer;

@ApiModel(value = "角色道具表", description = "角色道具表")
@TableName("t_bd_char_item")
@Data
public class CharItem implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户角色id
     */
    @ApiModelProperty(value = "用户角色id")
    @TableField(value = "uc_id")
    @TableId(type = IdType.AUTO)
    private Integer ucId;

    /**
     * 道具id
     */
    @ApiModelProperty(value = "道具id")
    @TableField(value = "item_id")
    @TableId(type = IdType.AUTO)
    private Integer itemId;

    /**
     * 数量
     */
    @ApiModelProperty(value = "数量")
    private Integer count;


}