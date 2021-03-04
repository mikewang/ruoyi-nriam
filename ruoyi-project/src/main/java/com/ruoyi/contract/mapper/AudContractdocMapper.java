package com.ruoyi.contract.mapper;

import com.ruoyi.contract.domain.AudContractdoc;
import com.ruoyi.project.domain.AudProjectdoc;

import java.util.List;

public interface AudContractdocMapper {

    int insertAudContractdoc(AudContractdoc record);

    List<AudContractdoc> selectAudContractdocList(AudContractdoc record);

    int deleteAudContractdoc(AudContractdoc record);

    int mergeAudContractdoc(List<AudContractdoc> records);
    int sourceMergeAudContractdoc(List<AudContractdoc> records);

}