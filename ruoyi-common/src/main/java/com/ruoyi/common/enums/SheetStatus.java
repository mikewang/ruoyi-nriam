package com.ruoyi.common.enums;

/**
 * 状态
 *
 NoPass = 2,             //审批不通过
 XiangMuShenPi = 3,             //项目负责人审批
 BuMenShenPi = 4,              //部门审批
 ChuShenPi = 5,              //分管处审批
 FenGuanSuoShenPi = 6,         //分管所审批
 SuoZhangShenPi = 7,              //所长审批
 ShenPiWanCheng = 8,           //审批完成
 YiZuoFei = 9,                   //已作废
 XinJianZhong = 17,               //新建中
 YiQianDing = 30,               //已签订
 FuKuanWanCheng = 31,               //付款完成
 ShenQingZuoFei = 34            //申请作废中

 */
public enum SheetStatus
{
    NoPass(2, "审批不通过")
    ,XiangMuShenPi(3, "项目负责人审批")
    ,BuMenShenPi(4, "部门审批")
    ,ChuShenPi(5, "分管处审批")
    ,FenGuanSuoShenPi(6, "分管所审批")
    ,SuoZhangShenPi(7, "所长审批")
    ,ShenPiWanCheng(8, "审批完成")
    ,YiZuoFei(9, "已作废")
    ,XinJianZhong(17, "新建中")
    ,YiQianDing(30, "已签订")
    ,FuKuanWanCheng(31, "付款完成")
    ,ShenQingZuoFei(34, "申请作废中")
    ;

    private final Integer code;
    private final String info;

    SheetStatus(Integer code, String info)
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
