package com.ruoyi.performance.service;

import com.ruoyi.achieve.domain.AchAppraisal;
import com.ruoyi.achieve.mapper.AchAppraisalMapper;
import com.ruoyi.common.core.domain.model.BasDoc;
import com.ruoyi.common.enums.AchieveStatus;
import com.ruoyi.common.enums.ProjectStatus;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.performance.domain.*;
import com.ruoyi.performance.mapper.PerFundMapper;
import com.ruoyi.performance.mapper.PerIndicatorappraisalMapper;
import com.ruoyi.performance.mapper.PerRelationMapper;
import com.ruoyi.performance.mapper.PerTeamperformanceMapper;
import com.ruoyi.project.domain.AudProject;
import com.ruoyi.project.mapper.AudProjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class FundreportService {

    private static final Logger log = LoggerFactory.getLogger(FundreportService.class);

    @Resource
    AudProjectMapper projectMapper;


    @Resource
    PerFundMapper fundMapper;


    public List<AudProject> selectPorjectFundreportList(AudProject record) {

//        + " and ( a.Status = " + (int)Entity.Enums.ProjectStatus.ZaiYan    //在研
//                + " or a.Status = " + (int)Entity.Enums.ProjectStatus.YiJieTi       //已完成的
//                + " or a.Status = " + (int)Entity.Enums.ProjectStatus.JieTiDaiQueRen   //验收待审核
//                + " or a.Status = " + (int)Entity.Enums.ProjectStatus.JietiBuTongGuo  //验收不通过


        List<Integer> statusList = new ArrayList<>();

        statusList.add(ProjectStatus.ZaiYan.getCode());
        statusList.add(ProjectStatus.YiJieTi.getCode());
        statusList.add(ProjectStatus.JieTiDaiQueRen.getCode());
        statusList.add(ProjectStatus.JietiBuTongGuo.getCode());
        record.setStatusList(statusList);

        List<AudProject> list = projectMapper.selectAudProjectList(record);

        for(AudProject p : list) {

            p.setProjectyear(record.getProjectyear()); // 没有自动生成，也无法自动生成。

            Integer projectid = p.getProjectid();
            PerFund fund = new PerFund();
            fund.setProjectid(projectid);
            fund.setYear(Integer.valueOf(record.getProjectyear()));
            List<PerFund> ll = fundMapper.selectPerFund(fund);

            if (ll.size() > 0) {
                log.debug("ll.size is " + ll.toString());
                PerFund fund1 = ll.get(0);
                p.setFundreport(fund1);
            }
            else {
                p.setFundreport(fund);
            }
        }

        return list;
    }

    @Transactional
    public Integer insertPerFund(PerFund record) {

        Integer  result = fundMapper.insertPerFund(record);

        return result;
    }

    @Transactional
    public Integer updatePerFundFundById(PerFund record) {
        Integer  result = fundMapper.updatePerFundFundById(record);

        return result;

    }

    @Transactional
    public Integer confirmPerFundByIds(List<Integer> ids) {
        Integer  result = 0;
        for (Integer fundid :ids) {

            PerFund record = fundMapper.selectPerFundById(fundid);
            if (record.getStatus().equals("已确认")) {
                continue;
            }
            record.setStatus("已确认");
            result = fundMapper.updatePerFundStatusById(record);


            //增加评分记录（从到账经费）(某团队)


        }
        return result;
    }

}
