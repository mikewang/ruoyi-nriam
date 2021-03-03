package com.ruoyi.contract.mapper;

import com.ruoyi.contract.domain.AudContractdoc;
import java.util.List;

public interface AudContractdocMapper {

    int insertAudContractdoc(AudContractdoc record);

    List<AudContractdoc> selectAll();
}