package com.ruoyi.system.domain;

import com.ruoyi.common.core.domain.entity.SysMenu;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 用户和角色和菜单关联 sys_user_role_menu
 * 
 * @author ruoyi
 */
public class SysUserMenu extends SysMenu
{
    /** 用户ID */
    private Long userId;
    
    /** 角色ID */
    private Long roleId;

    private String roleName;

    private String roleKey;

    /** 菜单ID */
    private Long menuId;


    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getRoleId()
    {
        return roleId;
    }

    public void setRoleId(Long roleId)
    {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleKey() {
        return roleKey;
    }

    public void setRoleKey(String roleKey) {
        this.roleKey = roleKey;
    }

    @Override
    public Long getMenuId() {
        return menuId;
    }

    @Override
    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    @Override
    public String toString() {
        return "SysUserMenu{" +
                "userId=" + userId +
                ", roleId=" + roleId +
                ", menuId=" + menuId +
                '}';
    }
}
