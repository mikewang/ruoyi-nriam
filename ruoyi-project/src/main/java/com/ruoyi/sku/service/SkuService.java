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
import java.util.*;

@Service
public class SkuService {
    private static final Logger log = LoggerFactory.getLogger(SkuService.class);

    @Resource
    private SkuInfoMapper skuInfoMapper;

    @Resource
    private SkuPhotoMapper skuPhotoMapper;

    @Resource
    private SkuFileMapper skuFileMapper;

    @Resource
    private SkuExportMapper skuExportMapper;

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

    public List<SkuInfo> batchSelectSkuList(SkuInfo skuInfo) {

        List<SkuInfo> list = skuInfoMapper.batchSelectSkuList(skuInfo);

        for (SkuInfo skuInfo1 : list) {
            if (skuInfo1.getSkuId() != null ) {
                SkuPhoto photo = new SkuPhoto();
                photo.setSkuId(skuInfo1.getSkuId());
                List<SkuPhoto> photos = skuPhotoMapper.selectSkuPhoto(photo);
                skuInfo1.setPhotoList(photos);
            }
            else {
//                skuInfo1.setPhotoList(new ArrayList<>());
            }
        }

        return list;
    }

    public List<SkuInfo> selectSkuListByPhotoSizeValue(SkuInfo skuInfo) {

        List<SkuInfo> list = skuInfoMapper.selectSkuListByPhotoSizeValue(skuInfo);

        for (SkuInfo skuInfo1 : list) {
            if (skuInfo1.getSkuId() != null ) {
                SkuPhoto photo = new SkuPhoto();
                photo.setSkuId(skuInfo1.getSkuId());
                List<SkuPhoto> photos = skuPhotoMapper.selectSkuPhoto(photo);
                skuInfo1.setPhotoList(photos);
            }
        }

        return list;
    }


    public List<SkuInfo> exportSkuList(SkuInfo skuInfo) {

        if (skuInfo.getSkuNames() == null) {
            skuInfo.setSkuNames(new ArrayList<>());
            log.debug("skuInfo skunames is null, why?");
        }
        List<SkuInfo> list = skuInfoMapper.exportSkuList(skuInfo);

        for (SkuInfo skuInfo1 : list) {
            SkuPhoto photo = new SkuPhoto();
            photo.setSkuId(skuInfo1.getSkuId());
            List<SkuPhoto> photos = skuPhotoMapper.selectSkuPhoto(photo);
            skuInfo1.setPhotoList(photos);
        }

        return list;
    }

    public List<SkuInfo> exportSkuListByPhotoSizeValue(SkuInfo skuInfo) {

        List<SkuInfo> list = skuInfoMapper.exportSkuListByPhotoSizeValue(skuInfo);

        for (SkuInfo skuInfo1 : list) {
            if (skuInfo1.getSkuId() != null ) {
                SkuPhoto photo = new SkuPhoto();
                photo.setSkuId(skuInfo1.getSkuId());
                List<SkuPhoto> photos = skuPhotoMapper.selectSkuPhoto(photo);
                skuInfo1.setPhotoList(photos);
            }
        }

        return list;
    }


    public List<SkuInfo> batchSelectSkuListByPhotoSizeValue(SkuInfo skuInfo) {

        List<SkuInfo> list = skuInfoMapper.batchSelectSkuListByPhotoSizeValue(skuInfo);

        for (SkuInfo skuInfo1 : list) {
            if (skuInfo1.getSkuId() != null ) {
                SkuPhoto photo = new SkuPhoto();
                photo.setSkuId(skuInfo1.getSkuId());
                List<SkuPhoto> photos = skuPhotoMapper.selectSkuPhoto(photo);
                skuInfo1.setPhotoList(photos);
            }
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
        return skuInfoMapper.queryIfDuplicate(skuInfo);
    }

    @Transactional
    public Long insertSkuInfo(SkuInfo skuInfo) {
        log.debug("insertSkuInfo is below.");

        Integer rows = skuInfoMapper.insertSkuInfo(skuInfo);

        if (rows > 0) {
            Long skuId = skuInfo.getSkuId();
            for ( SkuPhoto photo :skuInfo.getPhotoList()) {
                photo.setSkuId(skuId);
                photo.setStatus(1);
                photo.setCreated(new Date());
                skuPhotoMapper.insertSkuPhoto(photo);
            }
        }

        Long skuId = rows > 0 ? skuInfo.getSkuId() : rows;

        return skuId;
    }

    @Transactional
    public Long updateSkuInfo(SkuInfo skuInfo) {
        log.debug("updateSkuInfo is below.");

        Integer rows = skuInfoMapper.updateSkuInfo(skuInfo);

        if (rows > 0) {
            Long skuId = skuInfo.getSkuId();
            for ( SkuPhoto photo :skuInfo.getPhotoList()) {
                photo.setSkuId(skuId);
                photo.setStatus(1);
                photo.setCreated(new Date());
                if (photo.getPhotoId() == null) {
                    skuPhotoMapper.insertSkuPhoto(photo);
                }
                else {
                    photo.setModified(new Date());
                    skuPhotoMapper.updateSkuPhoto(photo);
                }

            }
        }

        Long skuId = rows > 0 ? skuInfo.getSkuId() : rows;

        return skuId;
    }


    @Transactional
    public int deleteSkuInfoBySkuIds(Long[] skuIds)
    {
        // 必须将 Array 转换成 List，否则报错。
        List<Long> ids = Arrays.asList(skuIds);

        int rows =  skuInfoMapper.deleteSkuInfoBySkuIds(ids);

        return rows;
    }


    public List<SkuExport> selectSkuExport(SkuExport skuExport) {

        List<SkuExport> list = skuExportMapper.selectSkuExport(skuExport);

        return list;
    }


    @Transactional
    public Long insertSkuExport(SkuExport skuExport) {
        log.debug("insertSkuExport is below.");

        Integer rows = skuExportMapper.insertSkuExport(skuExport);

        Long exportId = rows > 0 ? skuExport.getExportId() : rows;

        return exportId;
    }

    @Transactional
    public int deleteSkuExportByExportIds(Long[] exportIds)
    {
        // 必须将 Array 转换成 List，否则报错。
        List<Long> ids = Arrays.asList(exportIds);

        int rows =  skuExportMapper.deleteSkuExportByExportIds(ids);

        return rows;
    }

    public SkuExport selectSkuExportById(Long exportId) {

        SkuExport skuExport = skuExportMapper.selectSkuExportById(exportId);

        return skuExport;
    }


    @Transactional
    public Long insertSkuFile(SkuFile skuFile) {
        log.debug("insertSkuFile is below.");

        skuFile.setStatus(1);
        skuFile.setCreated(new Date());
        Integer rows = skuFileMapper.insertSkuFile(skuFile);

        Long skuId = rows > 0 ? skuFile.getFileId() : rows;

        return skuId;
    }


    @Transactional
    public Long insertSkuInfoWithFiles(SkuInfo skuInfo) {
        log.debug("insertSkuInfoWithFiles is below.");

        Integer rows = skuInfoMapper.insertSkuInfo(skuInfo);

        if (rows > 0) {
            Long skuId = skuInfo.getSkuId();
            for ( SkuFile file :skuInfo.getPhotoFileList()) {
                file.setSkuId(skuId);
                file.setStatus(1);
                file.setModified(new Date());
                skuFileMapper.updateSkuFileSkuId(file);
            }
        }

        Long skuId = rows > 0 ? skuInfo.getSkuId() : rows;

        return skuId;
    }

    @Transactional
    public Long updateSkuInfoWithFiles(SkuInfo skuInfo) {
        log.debug("updateSkuInfoWithFiles is below.");

        Integer rows = skuInfoMapper.updateSkuInfo(skuInfo);

        if (rows > 0) {
            Long skuId = skuInfo.getSkuId();
            Set<Long> updatedFileids = new HashSet<>();

            for (SkuFile file : skuInfo.getPhotoFileList()) {
                updatedFileids.add(file.getFileId());
            }

            SkuFile record = new SkuFile();
            record.setSkuId(skuId);
            List<SkuFile> fileList = skuFileMapper.selectSkuFile(record);

            Set<Long> deletedFileids = new HashSet<>();

            for (SkuFile file : fileList) {
                if (updatedFileids.contains(file.getFileId())) {

                }
                else {
                    deletedFileids.add(file.getFileId());
                }
            }

            for (Long fileId : deletedFileids) {
                skuFileMapper.deleteSkuFileById(fileId);
            }

            for (SkuFile file : skuInfo.getPhotoFileList()) {
                file.setSkuId(skuId);
                file.setStatus(1);
                file.setModified(new Date());
                skuFileMapper.updateSkuFileSkuId(file);
            }

        }

        Long skuId = rows > 0 ? skuInfo.getSkuId() : rows;

        return skuId;
    }


    public List<SkuInfo> selectSkuListWithFile(SkuInfo skuInfo) {

        List<SkuInfo> list = skuInfoMapper.selectSkuInfo(skuInfo);

        for (SkuInfo skuInfo1 : list) {
            SkuFile file = new SkuFile();
            file.setSkuId(skuInfo1.getSkuId());

            List<SkuFile> files = skuFileMapper.selectSkuFile(file);

            log.debug("photoFileList get " + files.toString());

            skuInfo1.setPhotoFileList(files);
        }

        return list;
    }

    public List<SkuInfo> selectSkuListWithFileByPhotoSizeValue(SkuInfo skuInfo) {

        List<SkuInfo> list = skuInfoMapper.selectSkuListByPhotoSizeValue(skuInfo);

        for (SkuInfo skuInfo1 : list) {
            if (skuInfo1.getSkuId() != null ) {
                SkuFile file = new SkuFile();
                file.setSkuId(skuInfo1.getSkuId());

                List<SkuFile> files = skuFileMapper.selectSkuFile(file);
                skuInfo1.setPhotoFileList(files);
            }
        }

        return list;
    }



    public SkuInfo selectSkuInfoWithFilesById(Long skuId) {

        SkuInfo skuInfo = skuInfoMapper.selectSkuInfoById(skuId);

        if (skuInfo != null) {
            SkuFile file = new SkuFile();
            file.setSkuId(skuInfo.getSkuId());

            List<SkuFile> files = skuFileMapper.selectSkuFile(file);

            log.debug("photoFileList get " + files.toString());

            skuInfo.setPhotoFileList(files);
        }

        return skuInfo;
    }

    public List<SkuInfo> batchSelectSkuListWithFiles(SkuInfo skuInfo) {

        List<SkuInfo> list = skuInfoMapper.batchSelectSkuList(skuInfo);

        for (SkuInfo skuInfo1 : list) {
            SkuFile file = new SkuFile();
            file.setSkuId(skuInfo1.getSkuId());

            List<SkuFile> files = skuFileMapper.selectSkuFile(file);

            log.debug("photoFileList get " + files.toString());

            skuInfo1.setPhotoFileList(files);
        }


        return list;
    }


    public List<SkuInfo> batchSelectSkuListWithFilesByPhotoSizeValue(SkuInfo skuInfo) {

        List<SkuInfo> list = skuInfoMapper.batchSelectSkuListByPhotoSizeValue(skuInfo);

        for (SkuInfo skuInfo1 : list) {
            if (skuInfo1.getSkuId() != null ) {
                SkuFile file = new SkuFile();
                file.setSkuId(skuInfo1.getSkuId());

                List<SkuFile> files = skuFileMapper.selectSkuFile(file);
                skuInfo1.setPhotoFileList(files);
            }
        }

        return list;
    }



    public List<SkuInfo> exportSkuListWithFiles(SkuInfo skuInfo) {

        if (skuInfo.getSkuNames() == null) {
            skuInfo.setSkuNames(new ArrayList<>());
            log.debug("skuInfo skunames is null, why?");
        }
        List<SkuInfo> list = skuInfoMapper.exportSkuList(skuInfo);

        for (SkuInfo skuInfo1 : list) {
            SkuFile file = new SkuFile();
            file.setSkuId(skuInfo1.getSkuId());

            List<SkuFile> files = skuFileMapper.selectSkuFile(file);

            log.debug("photoFileList get " + files.toString());

            skuInfo1.setPhotoFileList(files);
        }


        return list;
    }

    public List<SkuInfo> exportSkuListWithFilesByPhotoSizeValue(SkuInfo skuInfo) {

        List<SkuInfo> list = skuInfoMapper.exportSkuListWithFilesByPhotoSizeValue(skuInfo);

        for (SkuInfo skuInfo1 : list) {
            if (skuInfo1.getSkuId() != null ) {
                SkuFile file = new SkuFile();
                file.setSkuId(skuInfo1.getSkuId());

                List<SkuFile> files = skuFileMapper.selectSkuFile(file);
                skuInfo1.setPhotoFileList(files);
            }
        }


        return list;
    }

}