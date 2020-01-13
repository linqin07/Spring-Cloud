package com.example.common;

import lombok.Data;

import java.util.List;

/**
 * @Description:
 * @author: LinQin
 * @date: 2020/01/08
 */
@Data
public class UserInfo {
    private long userId;
    private String username;
    private String alias;
    private String mobile;
    private String email;
    private int status;
    private List<Long> groupIds;
    private boolean superAdmin;
    private List<Long> resourceGroupIds;
}
