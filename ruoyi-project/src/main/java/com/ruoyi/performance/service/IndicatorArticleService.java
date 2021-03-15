package com.ruoyi.performance.service;

import com.ruoyi.achieve.domain.AchArticle;
import com.ruoyi.achieve.domain.AchStandard;
import com.ruoyi.achieve.mapper.AchArticleMapper;
import com.ruoyi.achieve.mapper.AchStandardMapper;
import com.ruoyi.common.enums.AchieveStatus;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.performance.domain.PerIndicatorarticle;
import com.ruoyi.performance.domain.PerIndicatorarticle;
import com.ruoyi.performance.domain.PerRelation;
import com.ruoyi.performance.domain.PerTeamperformance;
import com.ruoyi.performance.mapper.PerIndicatorarticleMapper;
import com.ruoyi.performance.mapper.PerRelationMapper;
import com.ruoyi.performance.mapper.PerTeamperformanceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


@Service
public class IndicatorArticleService {

    private static final Logger log = LoggerFactory.getLogger(IndicatorArticleService.class);

    @Resource
    PerIndicatorarticleMapper indicatorarticleMapper;



    @Resource
    AchArticleMapper articleMapper;

    @Resource
    PerRelationMapper relationMapper;

    @Resource
    PerTeamperformanceMapper teamperformanceMapper;


    public List<PerIndicatorarticle> selectPerIndicatorarticle(PerIndicatorarticle standard) {

        List<PerIndicatorarticle> list = indicatorarticleMapper.selectPerIndicatorarticle(standard);

        return list;
    }


    private String getDescription(PerIndicatorarticle indicatorarticle) {
        String description = "著作-" + indicatorarticle.getArticletype() + "-" + indicatorarticle.getPoints() + "分";

        return description;
    }


    private Integer addPerTeamperformances(PerIndicatorarticle indicatorstandard){
        Integer result = 1;

        String currYear = DateUtils.dateTimeNow("yyyy");
        AchArticle record = new AchArticle();
        record.setStatus(AchieveStatus.ZhengChang.getCode());
        record.setPublishyear(Integer.valueOf(currYear));
        record.setArticletype(indicatorstandard.getIndicatorarticleid());

        List<AchArticle>   list =   articleMapper.selectAchArticle(record);

        if (list.size() > 0) {
            //标准
            PerRelation relation = new PerRelation();
            relation.setIndicatortype("著作");
            List<PerRelation>  relationList = relationMapper.selectPerRelation(relation);

            PerRelation rel = relationList.get(0);

            for(AchArticle pz : list) {

                PerTeamperformance teamperformance = new PerTeamperformance();

                teamperformance.setTeamid(pz.getTeamid());
                teamperformance.setPerformanceyear(Integer.valueOf(currYear));
                teamperformance.setIndicatortype("著作");
                teamperformance.setIndicatorid(indicatorstandard.getIndicatorarticleid());
                teamperformance.setAchieveid(pz.getArticleid());

                String description = getDescription(indicatorstandard);
                teamperformance.setDescription(description);
                teamperformance.setPoints(indicatorstandard.getPoints());
                teamperformance.setLevel1id(rel.getLevel1id());
                teamperformance.setLevel1name(rel.getLevel1name());
                teamperformance.setLevel2id(rel.getLevel2id());
                teamperformance.setLevel2name(rel.getLevel2name());
                result =  teamperformanceMapper.insertPerTeamperformance(teamperformance);

            }

        }
        return result;
    }

    private Integer deletePerTeamperformances(PerIndicatorarticle indicatorstandard) {
        Integer result = 1;

        Integer curYear = Integer.valueOf(DateUtils.dateTimeNow("yyyy"));
        PerTeamperformance teamperformance = new PerTeamperformance();
        teamperformance.setPerformanceyear(curYear);
        teamperformance.setIndicatortype("著作");
        teamperformance.setIndicatorid(indicatorstandard.getIndicatorarticleid());
        teamperformance.setStatus("待确认");
        result = teamperformanceMapper.updatePerTeamperformanceDeleted(teamperformance);

        return result;
    }


    @Transactional
    public Integer updatePerIndicatorarticle(PerIndicatorarticle standard) {
        Integer result = 1;

        result = indicatorarticleMapper.updatePerIndicatorarticle(standard);

        // 评分记录需要修改，因为标准变了。
        //更新评分记录
        // 先删除
        deletePerTeamperformances(standard);

        //再增加新的
        addPerTeamperformances(standard);

        return result;
    }


}
