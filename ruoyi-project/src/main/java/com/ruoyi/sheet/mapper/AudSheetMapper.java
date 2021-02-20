package com.ruoyi.sheet.mapper;

import com.ruoyi.sheet.domain.AudSheet;
import java.util.List;

public interface AudSheetMapper {

    List<AudSheet> selectSheetTijiaoren(AudSheet record);

    List<AudSheet> selectSheetTijiaorenByUserid(Integer userid);

    AudSheet selectSheetById(Integer sheetid);

    int insertAudSheet(AudSheet record);

    List<AudSheet> selectSheetXiangmuByUserid(Integer userid);

    AudSheet selectByPrimaryKey(Integer sheetid);

    int updateAudSheetStatus(AudSheet record);

    List<AudSheet> selectSheetBuMenByUserid(Integer userid);
    List<AudSheet> selectSheetChuByUserid(Integer userid);
    List<AudSheet> selectSheetFenguansuoByUserid(Integer userid);
    List<AudSheet> selectSheetSuozhangByUserid(Integer userid);

}