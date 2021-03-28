package com.ruoyi.performance.service;

import com.ruoyi.common.constant.CHString;
import com.ruoyi.common.enums.ProjectStatus;
import com.ruoyi.performance.domain.*;
import com.ruoyi.performance.mapper.*;
import com.ruoyi.project.domain.AudProject;
import com.ruoyi.project.domain.PmTeam;
import com.ruoyi.project.mapper.AudProjectMapper;
import com.ruoyi.project.mapper.PmTeamMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class IncomereportService {

    private static final Logger log = LoggerFactory.getLogger(IncomereportService.class);

    @Resource
    PerIncomeMapper incomeMapper;

    @Resource
    PmTeamMapper teamMapper;

    @Resource
    PerTeamperformanceMapper teamperformanceMapper;


    @Resource
    PerRelationMapper relationMapper;


    @Resource
    PerIndicatorfundMapper indicatorfundMapper;


    public List<PerIncome> selectPerIncome(PerIncome record) {

        List<PerIncome> list = incomeMapper.selectPerIncome(record);

        return list;
    }

    public PerIncome selectPerIncomeById(Integer incomeid) {

        PerIncome income = incomeMapper.selectPerIncomeById(incomeid);

        return income;
    }




    @Transactional
    public Integer insertPerIncome(PerIncome record) {

        Integer  result = incomeMapper.insertPerIncome(record);

        return result;
    }

    @Transactional
    public Integer updatePerIncome(PerIncome record) {
        Integer  result = incomeMapper.updatePerIncome(record);

        return result;

    }

    @Transactional
    public Integer deletePerIncomeById(Integer incomeid) {


        //如果原状态是“已确认”，则删除后，还要重新计算团队绩效评分
//        if (status == "已确认")
//        {
//            string teamID = gdv_List.DataKeys[thisRow.RowIndex][1].ToString();
//            iteamper.AddRecord_FromIncome(teamID,YearAndTeam1.dpl_Year.SelectedValue);
//        }

        PerIncome record = incomeMapper.selectPerIncomeById(incomeid);

        Integer teamid = record.getTeamid();
        Integer year = record.getYear();

        addIncomeRecordByTeamidYear(teamid, year);

        Integer  result = incomeMapper.updatePerIncomeDeletedById(incomeid);
        return result;
    }

    private Integer addIncomeRecordByTeamidYear(Integer teamid, Integer year)
    {
        //先删除对应的旧评分记录
        PerTeamperformance teamperformance = new PerTeamperformance();
        teamperformance.setStatus("待确认");
        teamperformance.setPerformanceyear(year);
        teamperformance.setTeamid(teamid);
        teamperformance.setIndicatortype(CHString.IncomeDevelop);
        teamperformanceMapper.updatePerTeamperformanceDeleted(teamperformance);

        teamperformance.setIndicatortype(CHString.IncomeTransfer);
        teamperformanceMapper.updatePerTeamperformanceDeleted(teamperformance);

        // 计算某个团队某年的开发收入
        PerIncome incomereport = new PerIncome();
        incomereport.setTeamid(teamid);
        incomereport.setYear(year);
        incomereport.setType(CHString.IncomeDevelop);

        java.math.BigDecimal totalDevelop = incomeMapper.selectPerIncomeCaculateTotal(incomereport);
        log.debug("totalDevelop is " + totalDevelop.toString());
        addIncomeRecord(2, totalDevelop, CHString.IncomeDevelop, teamid, year);

        //计算该团队今年的转让收入
        incomereport.setType(CHString.IncomeTransfer);
        java.math.BigDecimal totalTransfer = incomeMapper.selectPerIncomeCaculateTotal(incomereport);
        addIncomeRecord(3, totalTransfer, CHString.IncomeTransfer, teamid, year);

        return 1;
    }


    private Integer addIncomeRecord(Integer indicatorfundid, java.math.BigDecimal total, String chIndicator,Integer teamid, Integer year) {

//            IIndicatorFundManager indifund = IndicatorFundManager.GetInstance();
//            DataSet pointsDS = indifund.Get(indicatorType);
//            int perMoney = (int)(pointsDS.Tables[0].Rows[0]["PerMoney"]);
//            decimal basepoints = (decimal)(pointsDS.Tables[0].Rows[0]["Points"]);
//            pointsDS.Dispose();
//
//            //计算分数
//            decimal count = Math.Floor(total / (perMoney * 10000));
//            decimal points = basepoints * count;
//
//            IIndicatorManager indicm = IndicatorManager.GetInstance();
//            IndicatorRelation rela = indicm.GetRelation(chIndicator);
//
//            //增加新的记录
//            AddNew_NoAuthor(teamID, curYear, chIndicator,
//                    "1", "1", chIndicator + "-每" + perMoney.ToString() + "万元-" + basepoints.ToString() + "分"
//                            + "-本年到账" + total.ToString() + "元",
//                    points, rela);

        //计算分数
        PerIndicatorfund indicatorfund = indicatorfundMapper.selectPerIndicatorfundById(indicatorfundid);

        log.debug("indicatorfund is " + indicatorfund.toString());
        BigDecimal p = new BigDecimal (indicatorfund.getPermoney());
        p = p.multiply(new BigDecimal(10000));
        BigDecimal count = total.divide(p);
        BigDecimal points = new BigDecimal(indicatorfund.getPoints().toString()).multiply(count);

        PerTeamperformance tp = new PerTeamperformance();
        tp.setTeamid(teamid);
        tp.setPerformanceyear(year);
        tp.setIndicatortype(chIndicator);
        tp.setIndicatorid(1);
        tp.setAchieveid(1);

        String description = chIndicator + "-每" + indicatorfund.getPermoney().toString() + "万元-" + indicatorfund.getPoints().toString() + "分" + "-本年到账" + total.toString() + "元";
        tp.setDescription(description);
        tp.setBasepoints(points);
        tp.setAuthoruserid(-1);
        tp.setAuthororder(-1);
        tp.setPoints(points);

        PerRelation relation = new PerRelation();
        relation.setIndicatortype(chIndicator);
        List<PerRelation> relationList = relationMapper.selectPerRelation(relation);

        if (relationList.size() > 0) {
            PerRelation rela = relationList.get(0);
            tp.setLevel1id(rela.getLevel1id());
            tp.setLevel1name(rela.getLevel1name());
            tp.setLevel2id(rela.getLevel2id());
            tp.setLevel2name(rela.getLevel2name());
        }

        teamperformanceMapper.insertPerTeamperformance(tp);


        return 1;
    }


    @Transactional
    public Integer confirmPerIncomeByIds(List<Integer> incomeids) {

        Integer  result = 0;
        for (Integer incomeid :incomeids) {

            PerIncome record = incomeMapper.selectPerIncomeById(incomeid);
            if (record.getStatus().equals("已确认")) {
                continue;
            }
            record.setStatus("已确认");
            result = incomeMapper.updatePerIncomeConfirmById(incomeid);

            //增加评分记录（从到账经费）(某团队)
            List<PmTeam> teamList = teamMapper.selectTeamList(new PmTeam());

            for (PmTeam team : teamList) {

                Integer teamid = team.getTeamid();
                Integer year = record.getYear();

                addIncomeRecordByTeamidYear(teamid, year);
            }

        }


        return result;

    }

}
