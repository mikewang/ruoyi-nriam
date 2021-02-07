package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.SysUserMenu;

import java.util.List;

/**
 * 用户与角色和 菜单关联表 数据层
 * 
 *
 */
public interface SysUserMenuMapper
{
    public List<SysUserMenu> selectUserMenuList(SysUserMenu userMenu);
}
