package com.example.music.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("user")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String username;
    private String password;
    private String nickname;
    private String email;      // 新增
    private String bio;        // 新增
    private Integer gender;
    private String hobby;
    private String avatar;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}