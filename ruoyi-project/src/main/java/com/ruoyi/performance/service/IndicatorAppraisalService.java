package com.ruoyi.performance.service;

import com.ruoyi.achieve.domain.AchAppraisal;
import com.ruoyi.achieve.mapper.AchAppraisalMapper;
import com.ruoyi.common.enums.AchieveStatus;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.performance.domain.PerIndicatorappraisal;
import com.ruoyi.performance.domain.PerIndicatorappraisal;
import com.ruoyi.performance.domain.PerRelation;
import com.ruoyi.performance.domain.PerTeamperformance;
import com.ruoyi.performance.mapper.PerIndicatorappraisalMapper;
import com.ruoyi.performance.mapper.PerRelationMapper;
import com.ruoyi.performance.mapper.PerTeamperformanceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class IndicatorAppraisalService {

    private static final Logger log = LoggerFactory.getLogger(IndicatorAppraisalService.class);

    @Resource
    PerIndicatorappraisalMapper indicatorappraisalMapper;


    @Resource
    AchAppraisalMapper appraisalMapper;

    @Resource
    PerRelationMapper relationMapper;

    @Resource
    PerTeamperformanceMapper teamperformanceMapper;

    public PerIndicatorappraisal selectPerIndicatorappraisalById(Integer fundid) {

        PerIndicatorappraisal fund = indicatorappraisalMapper.selectPerIndicatorappraisalById(fundid);

        return fund;
    }



    private String getDescription(PerIndicatorappraisal indicatorsoftware) {
        String description = "鉴定（评价）成果-" + "每项" + "-" + indicatorsoftware.getPoints() + "分";

        return description;
    }


    private Integer addPerTeamperformances(PerIndicatorappraisal indicatorsoftware){
        Integer result = 1;

        String currYear = DateUtils.dateTimeNow("yyyy");
        AchAppraisal record = new AchAppraisal();
        record.setStatus(AchieveStatus.ZhengChang.getCode());
        record.setPublishyear(Integer.valueOf(currYear));

        List<AchAppraisal> list =   appraisalMapper.selectAchAppraisal(record);

        if (list.size() > 0) {
            //鉴定（评价）成果
            PerRelation relation = new PerRelation();
            relation.setIndicatortype("鉴定（评价）成果");
            List<PerRelation>  relationList = relationMapper.selectPerRelation(relation);

            PerRelation rel = relationList.get(0);

            for(AchAppraisal pz : list) {

                PerTeamperformance teamperformance = new PerTeamperformance();

                teamperformance.setTeamid(pz.getTeamid());
                teamperformance.setPerformanceyear(Integer.valueOf(currYear));
                teamperformance.setIndicatortype("鉴定（评价）成果");
                teamperformance.setIndicatorid(indicatorsoftware.getIndicatorappraisalid());
                teamperformance.setAchieveid(pz.getAppraisalid());

                String description = getDescription(indicatorsoftware);
                teamperformance.setDescription(description);
                teamperformance.setPoints(indicatorsoftware.getPoints());
                teamperformance.setLevel1id(rel.getLevel1id());
                teamperformance.setLevel1name(rel.getLevel1name());
                teamperformance.setLevel2id(rel.getLevel2id());
                teamperformance.setLevel2name(rel.getLevel2name());
                result =  teamperformanceMapper.insertPerTeamperformance(teamperformance);

            }

        }
        return result;
    }

    private Integer deletePerTeamperformances(PerIndicatorappraisal indicatorsoftware) {
        Integer result = 1;

        Integer curYear = Integer.valueOf(DateUtils.dateTimeNow("yyyy"));
        PerTeamperformance teamperformance = new PerTeamperformance();
        teamperformance.setPerformanceyear(curYear);
        teamperformance.setIndicatortype("鉴定（评价）成果");
        teamperformance.setIndicatorid(indicatorsoftware.getIndicatorappraisalid());
        teamperformance.setStatus("待确认");
        result = teamperformanceMapper.updatePerTeamperformanceDeleted(teamperformance);

        return result;
    }


    @Transactional
    public Integer updatePerIndicatorappraisal(PerIndicatorappraisal appraisal) {
        Integer result = 1;

        result = indicatorappraisalMapper.updatePerIndicatorappraisal(appraisal);

        // 评分记录需要修改，因为鉴定（评价）成果变了。

       //更新评分记录
        // 先删除
        deletePerTeamperformances(appraisal);

        //再增加新的
        addPerTeamperformances(appraisal);

        return result;
    }

}
