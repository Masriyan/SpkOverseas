package id.co.map.spk.repositories;

import id.co.map.spk.entities.SuratPerintahKerjaEntity;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface SpkRepository {

    void add(SuratPerintahKerjaEntity spk, String userName) throws ClassNotFoundException, SQLException;
    void updte(SuratPerintahKerjaEntity spk, String userName) throws ClassNotFoundException, SQLException;
    List<SuratPerintahKerjaEntity> findByCreatedTimeStamp(String createDate);
    List<SuratPerintahKerjaEntity> findByCreatedPeriod(Integer month, Integer year);
    SuratPerintahKerjaEntity findById(String spkId);
    void updateStatus(String spkId, String statusName, String note, String userName)throws ClassNotFoundException, SQLException;
    void updateStatusAndAssetNo(String spkId, String statusName, String assetNo, String note, String userName)throws ClassNotFoundException, SQLException;
    void updateStatusAndInternalOrder(String spkId, String statusName, String internalOrder, String note, String userName)throws ClassNotFoundException, SQLException;
    Map<String, Object> findSpkForPagination(String username, String spkId, String spkDescription, String companyId, String sbuId, String approvedQuotationDate, String storeId, String vendorId, String status, Integer pageNumber, Integer size);
    List<SuratPerintahKerjaEntity> findListSpkforExcel(String username, String spkId, String spkDescription, String companyId, String sbuId, String approvedQuotationDate, String storeId, String vendorId, String status);
    void updateStatusAndPurchaseOrder(String spkId, String statusName, String purchaseOrder, String note, String userName)throws ClassNotFoundException, SQLException;
    SuratPerintahKerjaEntity updateEntrySheetAndGrAmount(String spkId, String username, String entrySheet, Double grAmount, String statusName, String spkNote, int termOfPayment)throws ClassNotFoundException, SQLException;
    void updateStatusAndActualAmount(String spkId, String statusName, String note, String userName, Double actualAmount)throws ClassNotFoundException, SQLException;
    void updateAssetClassId(String spkId, String assetClassId, String note,String userName, String statusName) throws ClassNotFoundException, SQLException;
    List<SuratPerintahKerjaEntity> findUserShouldTakeAction(String userName);
}