package com.ruoyi.common.enums;

/**
 * 状态
 *
 namespace FSLP.ZYSM.ZHGL.Entity.Enums
 {
 public enum AchieveStatus
 {
 DaiQueRen = 36,

 ZhengChang = 37,

 BuTongGuo = 38,

 YiShanChu = 39
 }
 }
 */
public enum AchieveStatus
{
    DaiQueRen(36, "待审核"), ZhengChang(37, "正常"),
    YiShanChu(39, "已删除"), BuTongGuo(38, "审核不通过");

    private final Integer code;
    private final String info;

    AchieveStatus(Integer code, String info)
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
