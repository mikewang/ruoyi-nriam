package com.ruoyi.common.enums;

/**
 * 状态
 *
 40	项目状态	新建待审核	1
 41	项目状态	在研	2
 42	项目状态	已完成	3
 43	项目状态	已删除	4
 44	项目状态	新建审核不通过	5
 45	项目状态	验收待审核	6
 46	项目状态	验收审核不通过	7
 48	项目状态	新建中	8
 */
public enum ProjectStatus
{
    DaiQueRen(40, "新建待审核"), ZaiYan(41, "在研"), YiJieTi(42, "已完成"),
    YiShanChu(43, "已删除"), BuTongGuo(44, "新建审核不通过"), JieTiDaiQueRen(45, "验收待审核"),
    JietiBuTongGuo(46, "验收审核不通过"), XinJianZhong(48, "新建中");

    private final Integer code;
    private final String info;

    ProjectStatus(Integer code, String info)
    {
        this.code = code;
        this.info = info;
    }

    public Integer getCode()
    {
        return code;
    }

    public String getInfo()
    {
        return info;
    }
}
