package com.ruoyi.common.enums;

public enum ProjectColor
{
    Red(-1, "红色，截止天数小于90天，或新建审核不通过，或验收审核不通过"), Green(1, "新建中，或验收待审核，或新建待审核"), Default(0, "正常");

    private final Integer code;
    private final String info;

    ProjectColor(Integer code, String info)
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
