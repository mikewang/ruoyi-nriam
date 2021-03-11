package com.ruoyi.contract.mapper;

import com.ruoyi.contract.domain.AudContract;
import java.util.List;

public interface AudContractMapper {

    List<AudContract> selectContractTijiaoren(AudContract record);

    int deleteByPrimaryKey(Integer contractid);

    int insertAudContract(AudContract record);

    int updateAudContract(AudContract record);

    int updateAudContractStatus(AudContract record);

    int updateAudContractFirstPayTime(AudContract record);

    AudContract selectContractById(Integer contractid);

    List<AudContract> selectContractXiangmu(AudContract record);
    List<AudContract> selectContractBumen(AudContract record);
    List<AudContract> selectContractChu(AudContract record);
    List<AudContract> selectContractFenguansuo(AudContract record);
    List<AudContract> selectContractSuo(AudContract record);

    List<AudContract> selectAll();

    int updateByPrimaryKey(AudContract record);
}