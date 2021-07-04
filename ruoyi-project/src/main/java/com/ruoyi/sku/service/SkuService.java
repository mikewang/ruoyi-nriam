package com.ruoyi.sku.service;

import com.ruoyi.audit.domain.AudApply;
import com.ruoyi.audit.domain.AudMessage;
import com.ruoyi.audit.mapper.AudApplyMapper;
import com.ruoyi.audit.mapper.AudMessageMapper;
import com.ruoyi.common.constant.MID;
import com.ruoyi.common.enums.ProjectStatus;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.sku.domain.*;
import com.ruoyi.sku.mapper.*;
import com.ruoyi.system.domain.SysUserMenu;
import com.ruoyi.system.mapper.SysUserMenuMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class SkuService {
    private static final Logger log = LoggerFactory.getLogger(SkuService.class);

    @Resource
    private SkuInfoMapper skuInfoMapper;

    @Resource
    private SkuPhotoMapper skuPhotoMapper;


    public List<SkuInfo> selectSkuList(SkuInfo skuInfo) {

        List<SkuInfo> list = skuInfoMapper.selectSkuInfo(skuInfo);

        for (SkuInfo skuInfo1 : list) {
            SkuPhoto photo = new SkuPhoto();
            photo.setSkuId(skuInfo1.getSkuId());
            List<SkuPhoto> photos = skuPhotoMapper.selectSkuPhoto(photo);
            skuInfo1.setPhotoList(photos);
        }

        return list;
    }

    public SkuInfo selectSkuInfoById(Long skuId) {

        SkuInfo skuInfo = skuInfoMapper.selectSkuInfoById(skuId);

        if (skuInfo != null) {
            SkuPhoto photo = new SkuPhoto();
            photo.setSkuId(skuInfo.getSkuId());
            List<SkuPhoto> photos = skuPhotoMapper.selectSkuPhoto(photo);
            skuInfo.setPhotoList(photos);
        }

        return skuInfo;
    }



    public Integer queryIfDuplicate(SkuInfo skuInfo) {
        return 1;
    }



    @Transactional
    public Long insertSkuInfo(SkuInfo skuInfo) {
        log.debug("insertSkuInfo is below.");

        Integer rows = skuInfoMapper.insertSkuInfo(skuInfo);

        if (rows > 0) {
            Long skuId = skuInfo.getSkuId();

            for ( SkuPhoto photo :skuInfo.getPhotoList()) {
                photo.setSkuId(skuId);
                skuPhotoMapper.insertSkuPhoto(photo);
            }
        }

        Long skuId = rows > 0 ? skuInfo.getSkuId() : rows;

        return skuId;
    }


}