package com.ruoyi.achieve.service;

import com.ruoyi.achieve.domain.AchAuthor;
import com.ruoyi.achieve.domain.AchPatent;
import com.ruoyi.achieve.mapper.AchAuthorMapper;
import com.ruoyi.achieve.mapper.AchPatentMapper;
import com.ruoyi.common.enums.AchieveStatus;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.project.domain.AudProject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class AchPatentService {
    private static final Logger log = LoggerFactory.getLogger(AchPatentService.class);

    @Resource
    private AchPatentMapper patentMapper;

    @Resource
    private AchAuthorMapper authorMapper;

    public List<AchPatent> selectAchPatentList(AchPatent patent) {
        List<AchPatent> patentList = patentMapper.selectAchPatentList(patent);

        for (AchPatent p : patentList) {
            Integer patentid = p.getPatentid();
            String achieveType = "专利";
            AchAuthor query = new AchAuthor();
            query.setAchievetype(achieveType);
            query.setRelatedid(patentid);

            List<AchAuthor> achAuthorList = authorMapper.selectAchAuthorList(query);
            List<String> personnames = new ArrayList<>();
            for (AchAuthor a : achAuthorList) {
                personnames.add(a.getPersonname());
            }
            String authors = StringUtils.joinWith(",", personnames);

            p.setAuthorList(achAuthorList);
            p.setAuthors(authors);

            //状态文字设置颜色
            if (p.getStatus() == AchieveStatus.DaiQueRen.getCode()) {
                p.setStatusColor(1);
            }
            else if (p.getStatus() == AchieveStatus.BuTongGuo.getCode()) {
                p.setStatusColor(-1);
            }
            else {
                p.setStatusColor(0);
            }

//            if (gdv_List.Rows[rowIndex].Cells[cellIndex].Text == "待审核")
//            {
//                gdv_List.Rows[rowIndex].Cells[cellIndex].ForeColor = System.Drawing.Color.Green;
//            }
//            if (gdv_List.Rows[rowIndex].Cells[cellIndex].Text == "审核不通过")
//            {
//                gdv_List.Rows[rowIndex].Cells[cellIndex].ForeColor = System.Drawing.Color.Red;
//            }

        }
        log.debug("AchPatent list is request.");
        return patentList;
    }

    public Integer queryIfDuplicate(AchPatent patent) {
        return patentMapper.queryIfDuplicate(patent);
    }

}