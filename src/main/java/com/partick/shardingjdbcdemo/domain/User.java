package com.partick.shardingjdbcdemo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @TableName user_0
 */
@Data
public class User implements Serializable {
    /**
     * 
     */
    @TableId(type=IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    private Long userId;

    /**
     * 
     */
    private String userName;

    /**
     * 
     */
    private Short isDeleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}