package com.ruoyi.common.enums;

// <add key="CHString_Prize" value="获奖成果" />
//    <add key="CHString_Thesis" value="学术论文" />
//    <add key="CHString_Article" value="著作" />
//    <add key="CHString_Standard" value="标准" />
//    <add key="CHString_Patent" value="专利" />
//    <add key="CHString_Software" value="软件著作权" />
//    <add key="CHString_Product" value="农机新产品" />
//    <add key="CHString_Tech" value="农业部主推技术" />
//    <add key="CHString_Appraisal" value="鉴定（评价）成果" />

public enum AchieveType
{

    PATENT("专利", "patent"), THESIS("学术论文","thesis"), ARTICLE("著作","article")
    ,PRIZE("获奖成果","prize") , STANDARD("标准", "standard"), SOFTWARE("软件著作权", "software")
    ,PRODUCT("农机新产品", "product"),TECH("农业部主推技术", "tech"),APPRAISAL("鉴定（评价）成果", "appraisal") ;

    private final String info;
    private final String link;

    AchieveType(String info, String link)
    {
        this.info = info;
        this.link = link;
    }

    public String getInfo()
    {
        return info;
    }
    public String getLink() {return  link;}

}
