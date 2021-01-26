package com.ruoyi.common.enums;

/**
 * 状态
 *
 DaiQueRen = 40,             //待确认
 ZaiYan = 41,             //在研
 YiJieTi = 42,              //部门审批
 YiShanChu = 43,              //已删除
 BuTongGuo = 44,             //不通过
 JieTiDaiQueRen = 45,      //结题待确认
 JietiBuTongGuo = 46,          //结题不通过
 XinJianZhong = 48            //新建中
 */
public enum ProjectStatus
{
    DaiQueRen(40, "待确认"), ZaiYan(41, "在研"), YiJieTi(42, "部门审批"),
    YiShanChu(43, "已删除"), BuTongGuo(44, "不通过"), JieTiDaiQueRen(45, "结题不通过"),
    JietiBuTongGuo(46, "结题不通过"), XinJianZhong(48, "新建中");

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
