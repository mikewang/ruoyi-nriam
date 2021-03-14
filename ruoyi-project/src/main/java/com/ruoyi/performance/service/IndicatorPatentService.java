package com.ruoyi.performance.service;

import com.ruoyi.achieve.domain.AchPatent;
import com.ruoyi.achieve.domain.AchPrize;
import com.ruoyi.achieve.mapper.AchPatentMapper;
import com.ruoyi.common.enums.AchieveStatus;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.performance.domain.*;
import com.ruoyi.performance.mapper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class IndicatorPatentService {

    private static final Logger log = LoggerFactory.getLogger(IndicatorPatentService.class);

    @Resource
    PerIndicatorpatentMapper indicatorpatentMapper;

    @Resource
    AchPatentMapper achPatentMapper;

    @Resource
    PerRelationMapper relationMapper;

    @Resource
    PerIndicatorMapper indicatorMapper;

    @Resource
    PerTeamperformanceMapper teamperformanceMapper;

    public List<PerIndicatorpatent> selectPerIndicatorpatent(PerIndicatorpatent patent) {

        List<PerIndicatorpatent> list = indicatorpatentMapper.selectPerIndicatorpatent(patent);

        return list;
    }


    private String getDescription(PerIndicatorpatent patent) {
        String description = "专利-" + patent.getPatenttype() + "-" + patent.getPoints() + "分";

        return description;
    }


    private Integer addPerTeamperformances(PerIndicatorpatent patent){
        Integer result = 1;

        String currYear = DateUtils.dateTimeNow("yyyy");
        AchPatent record = new AchPatent();
        record.setStatus(AchieveStatus.ZhengChang.getCode());
        record.setPasstime(currYear);
        record.setPatenttype(patent.getPatenttype());

        List<AchPatent>   patentList =      achPatentMapper.selectAchPatentList(record);

        if (patentList.size() > 0) {
            //获奖成果
            PerRelation relation = new PerRelation();
            relation.setIndicatortype("专利");
            List<PerRelation>  relationList = relationMapper.selectPerRelation(relation);

            PerRelation rel = relationList.get(0);

            for(AchPatent pz : patentList) {

                PerTeamperformance teamperformance = new PerTeamperformance();

                teamperformance.setTeamid(pz.getTeamid());
                teamperformance.setPerformanceyear(Integer.valueOf(currYear));
                teamperformance.setIndicatortype("专利");

                teamperformance.setIndicatorid(patent.getIndicatorpatentid());
                teamperformance.setAchieveid(pz.getPatentid());

                String description = getDescription(patent);
                teamperformance.setDescription(description);
                teamperformance.setPoints(patent.getPoints());
                teamperformance.setLevel1id(rel.getLevel1id());
                teamperformance.setLevel1name(rel.getLevel1name());
                teamperformance.setLevel2id(rel.getLevel2id());
                teamperformance.setLevel2name(rel.getLevel2name());
                result =  teamperformanceMapper.insertPerTeamperformance(teamperformance);

//                iteamper.AddNew(teamID, curYear, ConstantParameter.CHString_Prize, entity.IndicatorPrizeID.ToString(),
//                        achieveID, description, entity.Points, rela);
            }

        }
        return result;
    }

    private Integer deletePerTeamperformances(PerIndicatorpatent patent) {
        Integer result = 1;

        Integer curYear = Integer.valueOf(DateUtils.dateTimeNow("yyyy"));
        PerTeamperformance teamperformance = new PerTeamperformance();
        teamperformance.setPerformanceyear(curYear);
        teamperformance.setIndicatortype("专利");
        teamperformance.setIndicatorid(patent.getIndicatorpatentid());
        teamperformance.setStatus("待确认");
        result = teamperformanceMapper.updatePerTeamperformanceDeleted(teamperformance);

        return result;
    }



    @Transactional
    public Integer updatePerIndicatorpatent(PerIndicatorpatent patent) {
        Integer result = 1;

        result = indicatorpatentMapper.updatePerIndicatorpatent(patent);
        //
        //先删除旧的
        deletePerTeamperformances(patent);

        addPerTeamperformances(patent);

        return result;
    }

}
