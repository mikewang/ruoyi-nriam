package com.ruoyi.achieve.service;

import com.ruoyi.achieve.domain.AchAuthor;
import com.ruoyi.achieve.domain.AchPatent;
import com.ruoyi.achieve.domain.AchPrize;
import com.ruoyi.achieve.domain.AuthorOrderRate;
import com.ruoyi.achieve.mapper.AchAuthorMapper;
import com.ruoyi.achieve.mapper.AchPatentMapper;
import com.ruoyi.achieve.mapper.AchPrizeMapper;
import com.ruoyi.audit.mapper.AudApplyMapper;
import com.ruoyi.audit.mapper.AudMessageMapper;
import com.ruoyi.common.constant.CHString;
import com.ruoyi.performance.domain.PerIndicatororderrate;
import com.ruoyi.performance.mapper.PerIndicatororderrateMapper;
import com.ruoyi.performance.service.TeamPerformanceService;
import com.ruoyi.system.mapper.SysUserMenuMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


@Service
public class AchAuthorService {
    private static final Logger log = LoggerFactory.getLogger(AchAuthorService.class);

    @Resource
    private AchAuthorMapper authorMapper;


    @Resource
    private AchPatentMapper patentMapper;
    @Resource
    private AchPrizeMapper prizeMapper;

    @Resource
    private PerIndicatororderrateMapper indicatororderrateMapper;

    @Resource
    private AudApplyMapper applyMapper;

    @Resource
    private TeamPerformanceService teamperformanceService;

    @Resource
    private SysUserMenuMapper userMenuMapper;

    @Resource
    private AudMessageMapper messageMapper;


    public List<AchAuthor> selectAchAuthorList(AchAuthor author) {

        List<AchAuthor> authorList = authorMapper.selectAchAuthorList(author);

        log.debug("AchAuthor list is request.");
        return authorList;
    }


    public AuthorOrderRate getOrderRateOfTeamAchieve(AchAuthor author ) {

        AuthorOrderRate orderRate = new AuthorOrderRate();
        orderRate.setRate(BigDecimal.valueOf(0));
        orderRate.setOrder(-1);
        orderRate.setUserId(-1);

        List<AchAuthor> authorList = authorMapper.selectAchAuthorList(author);

        Collections.sort(authorList, new Comparator<AchAuthor>() {
            @Override
            public int compare(AchAuthor o1, AchAuthor o2) {
                return o1.getOrdernumber() - o2.getOrdernumber();
            }
        });

        if (author.getAchievetype().equals(CHString.Thesis))   //论文只要本所成员是通讯作者，就计算1分
        {
            //循环
            for (AchAuthor achAuthor : authorList) {

                if (achAuthor.getIfreporter())  //本所成员是通讯作者，系数为1
                {
                    orderRate.setRate(BigDecimal.valueOf(1));
                    orderRate.setOrder(999); //通讯作者的排序
                    orderRate.setUserId(achAuthor.getUserid());
                    return orderRate;
                }
            }
        }


        //判断排位，对于专利和获奖，直接根据成果的本所排名属性来获取
        //对于论文、著作、标准、软著、鉴定成果，根据第一个本所作者的排位来获取
        // string userid = ds.Tables[0].Rows[0]["UserID"].ToString();   //第一个本所作者，分数归属于他

        orderRate.setUserId(authorList.get(0).getUserid()); //第一个本所作者，分数归属于他
        orderRate.setOrder(1);

        //专利
        if (author.getAchievetype().equals(CHString.Patent)) {
            AchPatent patent = patentMapper.selectAchPatentById(author.getRelatedid());
            Integer order = patent.getOurunitorder();
            orderRate.setOrder(order);
        }
        //获奖
        if (author.getAchievetype().equals(CHString.Prize)) {
            AchPrize prize = prizeMapper.selectAchPrizeById(author.getRelatedid());
            Integer order = prize.getOurunitorder();
            orderRate.setOrder(order);
        }

        List<String> others = new ArrayList<>();
        others.add(CHString.Thesis);
        others.add(CHString.Article);
        others.add(CHString.Standard);
        others.add(CHString.Software);
        others.add(CHString.Appraisal);

        //本所作者不是通讯作者的情况
        if (others.contains(author.getAchievetype())) {
            //看作者排位
            Integer order = authorList.get(0).getOrdernumber();
            orderRate.setOrder(order);
        }

        Integer order = orderRate.getOrder();
        if (order > 5) {
            orderRate.setOrder(5);
        }

        PerIndicatororderrate indicatororderrate = indicatororderrateMapper.selectPerIndicatororderrateById(orderRate.getOrder());

        BigDecimal rate = indicatororderrate.getRate();
        orderRate.setRate(rate);

        return orderRate;
    }


    @Transactional
    public Integer insertAchAuthor(AchAuthor author) {

        return  1;
    }


}