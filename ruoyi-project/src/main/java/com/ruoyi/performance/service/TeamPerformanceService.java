package com.ruoyi.performance.service;

import com.ruoyi.achieve.domain.*;
import com.ruoyi.achieve.mapper.*;
import com.ruoyi.common.constant.CHString;
import com.ruoyi.common.enums.AchieveStatus;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.performance.domain.*;
import com.ruoyi.performance.mapper.*;
import com.ruoyi.project.domain.AudProject;
import com.ruoyi.project.mapper.AudProjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class TeamPerformanceService {

    private static final Logger log = LoggerFactory.getLogger(TeamPerformanceService.class);

    @Resource
    PerTeamperformanceMapper teamperformanceMapper;

    @Resource
    PerConfirmqequestMapper confirmqequestMapper;

    @Resource
    AudProjectMapper projectMapper;
    @Resource
    AchSoftwareMapper softwareMapper;

    @Resource
    AchProductMapper productMapper;

    @Resource
    AchAppraisalMapper appraisalMapper;
    @Resource
    AchPrizeMapper prizeMapper;
    @Resource
    AchPatentMapper patentMapper;

    @Resource
    AchThesisMapper thesisMapper;

    @Resource
    AchArticleMapper articleMapper;

    @Resource
    AchStandardMapper standardMapper;
    @Resource
    AchTechMapper techMapper;

    @Resource
    PerScoreschangelogMapper scoreschangelogMapper;


    public List<PerTeamperformance> selectPerTeamperformance(PerTeamperformance record) {

        List<PerTeamperformance> list = teamperformanceMapper.selectPerTeamperformance(record);

        for (PerTeamperformance row : list) {

            if (row.getIndicatortype().equals(CHString.IndicatorProject)) {
               AudProject project = projectMapper.selectProjectById(row.getAchieveid());

               row.setIndicatortypeDetail(project.getProjectname());
            }
            if (row.getIndicatortype().equals(CHString.Software)) {

                AchSoftware software = softwareMapper.selectAchSoftwareById(row.getAchieveid());

                row.setIndicatortypeDetail(software.getSoftwarename());
            }

            if (row.getIndicatortype().equals(CHString.Product)) {

                AchProduct product = productMapper.selectAchProductById(row.getAchieveid());

                row.setIndicatortypeDetail(product.getProductname());
            }

            if (row.getIndicatortype().equals(CHString.Appraisal)) {

                AchAppraisal appraisal = appraisalMapper.selectAchAppraisalById(row.getAchieveid());

                row.setIndicatortypeDetail(appraisal.getAppraisalname());
            }

            if (row.getIndicatortype().equals(CHString.Prize)) {

                AchPrize prize = prizeMapper.selectAchPrizeById(row.getAchieveid());

                row.setIndicatortypeDetail(prize.getPrizename());
            }

            if (row.getIndicatortype().equals(CHString.Patent)) {

                Integer patentId = row.getAchieveid();

                AchPatent patent = patentMapper.selectAchPatentById(patentId);

                row.setIndicatortypeDetail(patent.getPatentname());
            }

            if (row.getIndicatortype().equals(CHString.Thesis)) {

                Integer patentId = row.getAchieveid();

                AchThesis patent = thesisMapper.selectAchThesisById(patentId);

                row.setIndicatortypeDetail(patent.getThesisname());
            }

            if (row.getIndicatortype().equals(CHString.Article)) {

                Integer patentId = row.getAchieveid();

                AchArticle patent = articleMapper.selectAchArticleById(patentId);

                row.setIndicatortypeDetail(patent.getArticlename());
            }

            if (row.getIndicatortype().equals(CHString.Standard)) {

                Integer patentId = row.getAchieveid();

                AchStandard patent = standardMapper.selectAchStandardById(patentId);

                row.setIndicatortypeDetail(patent.getStandardname());
            }

            if (row.getIndicatortype().equals(CHString.Tech)) {

                Integer patentId = row.getAchieveid();

                AchTech patent = techMapper.selectAchTechById(patentId);

                row.setIndicatortypeDetail(patent.getTechname());
            }

            List <String> sets = new ArrayList<>();
            sets.add(CHString.IndicatorFundNational);
            sets.add(CHString.IndicatorFundOther);
            sets.add(CHString.IncomeDevelop);
            sets.add(CHString.IncomeTransfer);

            if (sets.contains(row.getIndicatortype())) {
                row.setIndicatortypeDetail("详情");
            }

            if (row.getIndicatortype().equals("加分申请")) {
                row.setIndicatortypeDetail("申请详情");
            }
        }

        return list;
    }


    public Integer selectPerTeamperformancePointsSum(PerTeamperformance record) {

        Integer pointssum = teamperformanceMapper.selectPerTeamperformancePointsSum(record);

        return pointssum;
    }

    public PerConfirmqequest selectPerConfirmRequestByTeamYear(PerConfirmqequest record) {

        List<PerConfirmqequest> list = confirmqequestMapper.selectPerConfirmRequest(record);

        PerConfirmqequest quest = new PerConfirmqequest();

        if (list.size() > 0) {
            quest = list.get(0);
        }

        return quest;
    }

    @Transactional
    public Integer insertPerConfirmRequest(PerConfirmqequest record) {
        Integer result = 1;

        result = confirmqequestMapper.insertPerConfirmqequest(record);


        return result;
    }


    @Transactional
    public Integer updatePerTeamperformancePoint(PerTeamperformance record, PerScoreschangelog scoreschangelog){
        Integer result = 1;
        //修改分数

        result = teamperformanceMapper.updatePerTeamperformancePoint(record);
        ////记录日志

        scoreschangelogMapper.insertPerScoreschangelog(scoreschangelog);

        return result;
    }

    @Transactional
    public Integer updatePerTeamperformance(PerTeamperformance record){
        Integer result = 1;

        result = teamperformanceMapper.updatePerTeamperformance(record);

        return result;
    }

    public Integer updatePerTeamperformanceStatus(PerTeamperformance record){
        Integer result = 1;

        result = teamperformanceMapper.updatePerTeamperformanceStatus(record);

        return result;
    }


    public List<PerScoreschangelog>  selectPerScoreschangelog(PerScoreschangelog scoreschangelog) {

        List<PerScoreschangelog> list = scoreschangelogMapper.selectPerScoreschangelog(scoreschangelog);

        return list;
    }


    @Transactional
    public Integer updatePerTeamperformanceDeletedById(PerTeamperformance record){
        Integer result = 1;
        //修改分数

        result = teamperformanceMapper.updatePerTeamperformanceDeletedById(record);


        return result;
    }

}
