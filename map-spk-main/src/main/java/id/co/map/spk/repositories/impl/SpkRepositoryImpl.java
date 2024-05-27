package id.co.map.spk.repositories.impl;

import id.co.map.spk.entities.SuratPerintahKerjaEntity;
import id.co.map.spk.enums.SpkStatus;
import id.co.map.spk.repositories.SpkRepository;
import id.co.map.spk.utils.DbConProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class SpkRepositoryImpl implements SpkRepository {

    private static final Logger logger = LoggerFactory.getLogger(SpkRepositoryImpl.class);
    private final DbConProperty dbConProperty;

    public SpkRepositoryImpl(DbConProperty dbConProperty) {
        this.dbConProperty = dbConProperty;
    }

    @Override
    public void add(SuratPerintahKerjaEntity spk, String userName) throws ClassNotFoundException, SQLException {

        Connection con = null;
        Statement stm = null;

        String query = String.format("EXECUTE Usp_AddSpk '%s', '%s', '%s', '%s', '%s', '%s',  '%s', '%s', '%s', '%s', '%s', '%s', '%s', %s, '%s', %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, '%s', '%s', '%s' , '%s', '%s', '%s', '%s', '%s', '%s'",
                spk.getSpkId(), spk.getSpkDescription(), spk.getCompanyId(), spk.getSbuId(), spk.getSpkType(), spk.getProcessingGroup(), spk.getCoa(),
                spk.getApprovedQuotationDate(), spk.getEstimateStoreOpeningDate(), spk.getStoreId(), spk.getVendorId(),
                spk.getAssetClassId(), spk.getCostCenterId(), spk.getAmount(), spk.getContactPerson(),
                spk.getTop1Amount(), spk.getTop2Amount(),spk.getTop3Amount(), spk.getTop4Amount(), spk.getTop5Amount(),
                spk.getTop1Percentage(), spk.getTop2Percentage(), spk.getTop3Percentage(), spk.getTop4Percentage(), spk.getTop5Percentage(),
                spk.getTop1Message(), spk.getTop2Message(), spk.getTop3Message(), spk.getTop4Message(), spk.getTop5Message(),
                spk.getCreatedTimeStamp(), spk.getStatus().name(), spk.getQuotationFile(), userName);

        logger.debug("=========================== Add SPK ===========================");
        logger.debug("Query       : " + query);
        logger.debug("=========================== Add SPK ===========================");

        Class.forName(dbConProperty.getDriverClassName());
        con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());

        stm = con.createStatement();
        stm.execute(query);

        if(con != null) con.close();
        if(stm != null) stm.close();
    }

    @Override
    public void updte(SuratPerintahKerjaEntity spk, String userName) throws ClassNotFoundException, SQLException {

        Connection con = null;
        Statement stm = null;

        String query = String.format("EXECUTE Usp_EditSpk '%s', '%s', '%s', '%s', '%s',  '%s', '%s', '%s', '%s', '%s', '%s', '%s', %s, '%s', %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, '%s', '%s', '%s' , '%s', '%s', '%s', '%s', '%s', '%s'",
                spk.getSpkId(), spk.getSpkDescription(), spk.getCompanyId(), spk.getSbuId(), spk.getProcessingGroup(), spk.getCoa(),
                spk.getApprovedQuotationDate(), spk.getEstimateStoreOpeningDate(), spk.getStoreId(), spk.getVendorId(),
                spk.getAssetClassId(), spk.getCostCenterId(), spk.getAmount(), spk.getContactPerson(),
                spk.getTop1Amount(), spk.getTop2Amount(),spk.getTop3Amount(), spk.getTop4Amount(), spk.getTop5Amount(),
                spk.getTop1Percentage(), spk.getTop2Percentage(), spk.getTop3Percentage(), spk.getTop4Percentage(), spk.getTop5Percentage(),
                spk.getTop1Message(), spk.getTop2Message(), spk.getTop3Message(), spk.getTop4Message(), spk.getTop5Message(),
                spk.getCreatedTimeStamp(), spk.getStatus().name(), spk.getQuotationFile(), userName);

        logger.debug("=========================== Edit SPK ===========================");
        logger.debug("Query       : " + query);
        logger.debug("=========================== Edit SPK ===========================");

        Class.forName(dbConProperty.getDriverClassName());
        con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());

        stm = con.createStatement();
        stm.execute(query);

        if(con != null) con.close();
        if(stm != null) stm.close();
    }

    @Override
    public List<SuratPerintahKerjaEntity> findByCreatedTimeStamp(String createDate) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String query = String.format("EXECUTE Usp_FindSpkByCreatedDate '%s'", createDate);
        List<SuratPerintahKerjaEntity> spkList = new ArrayList<>();

        logger.debug("=========================== Find SPK By CreatedDate ===========================");
        logger.debug("Query       : " + query);
        logger.debug("=========================== Find SPK By CreatedDate ===========================");


        try{
            Class.forName(dbConProperty.getDriverClassName());
            con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();

            if(rs.next()){
                do{
                    SuratPerintahKerjaEntity spk = new SuratPerintahKerjaEntity();
                    spk.setSpkId(rs.getString("SpkId"));
                    spk.setSpkDescription(rs.getString("SpkDescription"));
                    spk.setCompanyId(rs.getString("CompanyId"));
                    spk.setSbuId(rs.getString("SbuId"));
                    spk.setSpkType(rs.getString("SpkType"));
                    spk.setProcessingGroup(rs.getString("ProcessingGroup"));
                    spk.setCoa(rs.getString("Coa"));
                    spk.setApprovedQuotationDate(rs.getString("ApprovedQuotationDate"));
                    spk.setStoreId(rs.getString("StoreId"));
                    spk.setVendorId(rs.getString("VendorId"));
                    spk.setAssetNo(rs.getString("AssetNo"));
                    spk.setAssetClassId(rs.getString("AssetClassId"));
                    spk.setCostCenterId(rs.getString("CostCenterId"));
                    spk.setAmount(rs.getDouble("Amount"));
                    spk.setContactPerson(rs.getString("ContactPerson"));
                    spk.setInternalOrder(rs.getString("InternalOrder"));
                    spk.setPurchaseOrder(rs.getString("PurchaseOrder"));
                    spk.setTop1Amount(rs.getDouble("Top1Amount"));
                    spk.setTop2Amount(rs.getDouble("Top2Amount"));
                    spk.setTop3Amount(rs.getDouble("Top3Amount"));
                    spk.setTop4Amount(rs.getDouble("Top4Amount"));
                    spk.setTop5Amount(rs.getDouble("Top5Amount"));
                    spk.setTop1Percentage(rs.getDouble("Top1Percentage"));
                    spk.setTop2Percentage(rs.getDouble("Top2Percentage"));
                    spk.setTop3Percentage(rs.getDouble("Top3Percentage"));
                    spk.setTop4Percentage(rs.getDouble("Top4Percentage"));
                    spk.setTop5Percentage(rs.getDouble("Top5Percentage"));
                    spk.setEntrySheet1(rs.getString("EntrySheet1"));
                    spk.setEntrySheet2(rs.getString("EntrySheet2"));
                    spk.setEntrySheet3(rs.getString("EntrySheet3"));
                    spk.setEntrySheet4(rs.getString("EntrySheet4"));
                    spk.setEntrySheet5(rs.getString("EntrySheet5"));
                    spk.setGrAmount1(rs.getDouble("GrAmount1"));
                    spk.setGrAmount2(rs.getDouble("GrAmount2"));
                    spk.setGrAmount3(rs.getDouble("GrAmount3"));
                    spk.setGrAmount4(rs.getDouble("GrAmount4"));
                    spk.setGrAmount5(rs.getDouble("GrAmount5"));
                    spk.setTop1Message(rs.getString("Top1Message"));
                    spk.setTop2Message(rs.getString("Top2Message"));
                    spk.setTop3Message(rs.getString("Top3Message"));
                    spk.setTop4Message(rs.getString("Top4Message"));
                    spk.setTop5Message(rs.getString("Top5Message"));
                    spk.setCreatedTimeStamp(rs.getString("CreatedTimeStamp"));
                    spk.setStatus(SpkStatus.valueOf(rs.getString("StatusName")));
                    spk.setQuotationFile(rs.getString("QuotationFile"));
                    spk.setDataTimeStamp(rs.getString("DataTimeStamp"));
                    spkList.add(spk);
                }while (rs.next());
            }
        }catch (ClassNotFoundException ce){
            logger.error("ClassNotFoundException : " + ce.getMessage());
        } catch (SQLException se){
            logger.error("SQLException : " + se.getMessage());
        } finally {
            try { con.close(); }catch (SQLException e){}
            try { pst.close(); }catch (SQLException e){}
            try { rs.close(); }catch (SQLException e){}
        }

        return spkList;
    }

    @Override
    public List<SuratPerintahKerjaEntity> findByCreatedPeriod(Integer month, Integer period) {

        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String query = String.format("EXECUTE Usp_FindSpkByPeriod '%s', '%s'", month, period);
        List<SuratPerintahKerjaEntity> spkList = new ArrayList<>();

        logger.debug("=========================== Find SPK By Period ===========================");
        logger.debug("Query       : " + query);
        logger.debug("=========================== Find SPK By Period ===========================");

        try{
            Class.forName(dbConProperty.getDriverClassName());
            con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();

            if(rs.next()){
                do{
                    SuratPerintahKerjaEntity spk = new SuratPerintahKerjaEntity();
                    spk.setSpkId(rs.getString("SpkId"));
                    spk.setSpkDescription(rs.getString("SpkDescription"));
                    spk.setCompanyId(rs.getString("CompanyId"));
                    spk.setSbuId(rs.getString("SbuId"));
                    spk.setSpkType(rs.getString("SpkType"));
                    spk.setProcessingGroup(rs.getString("ProcessingGroup"));
                    spk.setCoa(rs.getString("Coa"));
                    spk.setApprovedQuotationDate(rs.getString("ApprovedQuotationDate"));
                    spk.setStoreId(rs.getString("StoreId"));
                    spk.setVendorId(rs.getString("VendorId"));
                    spk.setAssetNo(rs.getString("AssetNo"));
                    spk.setAssetClassId(rs.getString("AssetClassId"));
                    spk.setCostCenterId(rs.getString("CostCenterId"));
                    spk.setAmount(rs.getDouble("Amount"));
                    spk.setContactPerson(rs.getString("ContactPerson"));
                    spk.setInternalOrder(rs.getString("InternalOrder"));
                    spk.setPurchaseOrder(rs.getString("PurchaseOrder"));
                    spk.setTop1Amount(rs.getDouble("Top1Amount"));
                    spk.setTop2Amount(rs.getDouble("Top2Amount"));
                    spk.setTop3Amount(rs.getDouble("Top3Amount"));
                    spk.setTop4Amount(rs.getDouble("Top4Amount"));
                    spk.setTop5Amount(rs.getDouble("Top5Amount"));
                    spk.setTop1Percentage(rs.getDouble("Top1Percentage"));
                    spk.setTop2Percentage(rs.getDouble("Top2Percentage"));
                    spk.setTop3Percentage(rs.getDouble("Top3Percentage"));
                    spk.setTop4Percentage(rs.getDouble("Top4Percentage"));
                    spk.setTop5Percentage(rs.getDouble("Top5Percentage"));
                    spk.setCreatedTimeStamp(rs.getString("CreatedTimeStamp"));
                    spk.setStatus(SpkStatus.valueOf(rs.getString("StatusName")));
                    spk.setDataTimeStamp(rs.getString("DataTimeStamp"));
                    spkList.add(spk);
                }while (rs.next());
            }
        }catch (ClassNotFoundException ce){
            logger.error("ClassNotFoundException : " + ce.getMessage());
        } catch (SQLException se){
            logger.error("SQLException : " + se.getMessage());
        } finally {
            try { con.close(); }catch (SQLException e){}
            try { pst.close(); }catch (SQLException e){}
            try { rs.close(); }catch (SQLException e){}
        }

        return spkList;
    }

    @Override
    public SuratPerintahKerjaEntity findById(String spkId) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String query = String.format("SELECT * FROM Spk WHERE SpkId =  '%s'", spkId);
        SuratPerintahKerjaEntity spk = new SuratPerintahKerjaEntity();

        logger.debug("=========================== Find SPK By SpkId ===========================");
        logger.debug("Query       : " + query);
        logger.debug("=========================== Find SPK By SpkId ===========================");

        try{
            Class.forName(dbConProperty.getDriverClassName());
            con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();

            if(rs.next()){
                do{
                    spk.setSpkId(rs.getString("SpkId"));
                    spk.setSpkDescription(rs.getString("SpkDescription"));
                    spk.setCompanyId(rs.getString("CompanyId"));
                    spk.setSbuId(rs.getString("SbuId"));
                    spk.setSpkType(rs.getString("SpkType"));
                    spk.setProcessingGroup(rs.getString("ProcessingGroup"));
                    spk.setCoa(rs.getString("Coa"));
                    spk.setApprovedQuotationDate(rs.getString("ApprovedQuotationDate"));
                    spk.setEstimateStoreOpeningDate(rs.getString("EstimateStoreOpeningDate"));
                    spk.setStoreId(rs.getString("StoreId"));
                    spk.setVendorId(rs.getString("VendorId"));
                    spk.setAssetNo(rs.getString("AssetNo"));
                    spk.setAssetClassId(rs.getString("AssetClassId"));
                    spk.setCostCenterId(rs.getString("CostCenterId"));
                    spk.setAmount(rs.getDouble("Amount"));
                    spk.setContactPerson(rs.getString("ContactPerson"));
                    spk.setInternalOrder(rs.getString("InternalOrder"));
                    spk.setPurchaseOrder(rs.getString("PurchaseOrder"));
                    spk.setTop1Amount(rs.getDouble("Top1Amount"));
                    spk.setTop2Amount(rs.getDouble("Top2Amount"));
                    spk.setTop3Amount(rs.getDouble("Top3Amount"));
                    spk.setTop4Amount(rs.getDouble("Top4Amount"));
                    spk.setTop5Amount(rs.getDouble("Top5Amount"));
                    spk.setTop1Percentage(rs.getDouble("Top1Percentage"));
                    spk.setTop2Percentage(rs.getDouble("Top2Percentage"));
                    spk.setTop3Percentage(rs.getDouble("Top3Percentage"));
                    spk.setTop4Percentage(rs.getDouble("Top4Percentage"));
                    spk.setTop5Percentage(rs.getDouble("Top5Percentage"));
                    spk.setEntrySheet1(rs.getString("EntrySheet1"));
                    spk.setEntrySheet2(rs.getString("EntrySheet2"));
                    spk.setEntrySheet3(rs.getString("EntrySheet3"));
                    spk.setEntrySheet4(rs.getString("EntrySheet4"));
                    spk.setEntrySheet5(rs.getString("EntrySheet5"));
                    spk.setGrAmount1(rs.getDouble("GrAmount1"));
                    spk.setGrAmount2(rs.getDouble("GrAmount2"));
                    spk.setGrAmount3(rs.getDouble("GrAmount3"));
                    spk.setGrAmount4(rs.getDouble("GrAmount4"));
                    spk.setGrAmount5(rs.getDouble("GrAmount5"));
                    spk.setActualAmount(rs.getDouble("ActualAmount"));
                    spk.setTop1Message(rs.getString("Top1Message"));
                    spk.setTop2Message(rs.getString("Top2Message"));
                    spk.setTop3Message(rs.getString("Top3Message"));
                    spk.setTop4Message(rs.getString("Top4Message"));
                    spk.setTop5Message(rs.getString("Top5Message"));
                    spk.setCreatedTimeStamp(rs.getString("CreatedTimeStamp"));
                    spk.setStatus(SpkStatus.valueOf(rs.getString("StatusName")));
                    spk.setQuotationFile(rs.getString("QuotationFile"));
                    spk.setDataTimeStamp(rs.getString("DataTimeStamp"));
                }while (rs.next());
            }
        }catch (ClassNotFoundException ce){
            logger.error("ClassNotFoundException : " + ce.getMessage());
        } catch (SQLException se){
            logger.error("SQLException : " + se.getMessage());
        } finally {
            try { con.close(); }catch (SQLException e){}
            try { pst.close(); }catch (SQLException e){}
            try { rs.close(); }catch (SQLException e){}
        }

        return spk;
    }

    @Override
    public void updateStatus(String spkId, String statusName, String note, String userName) throws ClassNotFoundException, SQLException{
        Connection con = null;
        Statement stm = null;

        String query = String.format("EXECUTE Usp_UpdateSpkStatus '%s', '%s', '%s', '%s'", spkId, statusName, note, userName);

        logger.debug("=========================== Update SPK Status ===========================");
        logger.debug("Query       : " + query);
        logger.debug("=========================== Update SPK Status ===========================");

        Class.forName(dbConProperty.getDriverClassName());
        con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());

        stm = con.createStatement();
        stm.execute(query);

        if(con != null) con.close();
        if(stm != null) stm.close();
    }

    @Override
    public void updateStatusAndAssetNo(String spkId, String statusName, String assetNo, String note, String userName) throws ClassNotFoundException, SQLException {
        Connection con = null;
        Statement stm = null;

        String query = String.format("EXECUTE Usp_UpdateSpkStatusAndAssetNo '%s', '%s', '%s', '%s', '%s'", spkId, statusName, assetNo, note, userName);

        logger.debug("=========================== Update SPK Status and Asset No ===========================");
        logger.debug("Query       : " + query);
        logger.debug("=========================== Update SPK Status and Asset No ===========================");

        Class.forName(dbConProperty.getDriverClassName());
        con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());

        stm = con.createStatement();
        stm.execute(query);

        if(con != null) con.close();
        if(stm != null) stm.close();
    }

    @Override
    public void updateStatusAndInternalOrder(String spkId, String statusName, String internalOrder, String note, String userName) throws ClassNotFoundException, SQLException {
        Connection con = null;
        Statement stm = null;

        String query = String.format("EXECUTE Usp_UpdateSpkStatusAndInternalOrder '%s', '%s', '%s', '%s', '%s'", spkId, statusName, internalOrder, note, userName);

        logger.debug("=========================== Update SPK Status and Internal Order ===========================");
        logger.debug("Query       : " + query);
        logger.debug("=========================== Update SPK Status and Internal Order ===========================");

        Class.forName(dbConProperty.getDriverClassName());
        con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());

        stm = con.createStatement();
        stm.execute(query);

        if(con != null) con.close();
        if(stm != null) stm.close();
    }

    @Override
    public Map<String, Object> findSpkForPagination(String username, String spkId, String spkDescription, String companyId, String sbuId, String approvedQuotationDate, String storeId, String vendorId, String status, Integer pageNumber, Integer size) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        Map<String, Object> map = new HashMap<>();
        List<SuratPerintahKerjaEntity> spkList = new ArrayList<>();
        Integer totalCount = 0;

        try{
            Class.forName(dbConProperty.getDriverClassName());
            con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());
            pst = con.prepareStatement("EXECUTE Usp_FindSpkForPagination ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?");
            pst.setString(1, username);
            pst.setString(2, spkId);
            pst.setString(3, spkDescription);
            pst.setString(4, companyId);
            pst.setString(5, sbuId);
            pst.setString(6, approvedQuotationDate);
            pst.setString(7, storeId);
            pst.setString(8, vendorId);
            pst.setString(9, status);
            pst.setInt(10, pageNumber);
            pst.setInt(11, size);
            rs = pst.executeQuery();

            if(rs.next()){

                totalCount = rs.getInt("TotalCount");

                do{
                    SuratPerintahKerjaEntity spk = new SuratPerintahKerjaEntity();
                    spk.setSpkId(rs.getString("SpkId"));
                    spk.setSpkDescription(rs.getString("SpkDescription"));
                    spk.setCompanyId(rs.getString("CompanyId"));
                    spk.setSbuId(rs.getString("SbuId"));
                    spk.setSbuCode(rs.getString("SbuCode"));
                    spk.setSpkType(rs.getString("SpkType"));
                    spk.setStoreId(rs.getString("StoreId"));
                    spk.setVendorId(rs.getString("Vendor"));
                    spk.setAssetNo(rs.getString("AssetNo"));
                    spk.setInternalOrder(rs.getString("InternalOrder"));
                    spk.setPurchaseOrder(rs.getString("PurchaseOrder"));
                    spk.setAmount(rs.getDouble("Amount"));
                    spk.setGrAmount1(rs.getDouble("GrAmount1"));
                    spk.setGrAmount2(rs.getDouble("GrAmount2"));
                    spk.setGrAmount3(rs.getDouble("GrAmount3"));
                    spk.setGrAmount4(rs.getDouble("GrAmount4"));
                    spk.setGrAmount5(rs.getDouble("GrAmount5"));
                    spk.setActualAmount(rs.getDouble("ActualAmount"));
                    spk.setStatus(SpkStatus.valueOf(rs.getString("StatusName")));

                    spkList.add(spk);
                }while (rs.next());
            }
            map.put("spkList", spkList);
            map.put("totalCount", totalCount);

            logger.debug("Page Count SPK = "+spkList.size());

        }catch (ClassNotFoundException ce){
            logger.error("ClassNotFoundException : " + ce.getMessage());
        } catch (SQLException se){
            logger.error("SQLException : " + se.getMessage());
        } finally {
            try { if(con != null) con.close(); }catch (SQLException e){}
            try { if(pst != null ) pst.close(); }catch (SQLException e){}
            try { if(rs != null) rs.close(); }catch (SQLException e){}
        }
        return map;
    }

    @Override
    public List<SuratPerintahKerjaEntity> findListSpkforExcel(String username, String spkId, String spkDescription, String companyId, String sbuId, String approvedQuotationDate, String storeId, String vendorId, String status) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        List<SuratPerintahKerjaEntity> spkList = new ArrayList<>();

        try{
            Class.forName(dbConProperty.getDriverClassName());
            con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());
            pst = con.prepareStatement("EXECUTE Usp_FindListSpkForExcel ?, ?, ?, ?, ?, ?, ?, ?, ?");
            pst.setString(1, username);
            pst.setString(2, spkId);
            pst.setString(3, spkDescription);
            pst.setString(4, companyId);
            pst.setString(5, sbuId);
            pst.setString(6, approvedQuotationDate);
            pst.setString(7, storeId);
            pst.setString(8, vendorId);
            pst.setString(9, status);
            rs = pst.executeQuery();

            if(rs.next()){

                do{
                    SuratPerintahKerjaEntity spk = new SuratPerintahKerjaEntity();
                    spk.setSpkId(rs.getString("SpkId"));
                    spk.setSpkDescription(rs.getString("SpkDescription"));
                    spk.setCompanyId(rs.getString("CompanyId"));
                    spk.setSbuId(rs.getString("SbuId"));
                    spk.setSbuCode(rs.getString("SbuCode"));
                    spk.setSpkType(rs.getString("SpkType"));
                    spk.setStoreId(rs.getString("StoreId"));
                    spk.setVendorId(rs.getString("Vendor"));
                    spk.setAssetNo(rs.getString("AssetNo"));
                    spk.setInternalOrder(rs.getString("InternalOrder"));
                    spk.setPurchaseOrder(rs.getString("PurchaseOrder"));
                    spk.setAmount(rs.getDouble("Amount"));
                    spk.setGrAmount1(rs.getDouble("GrAmount1"));
                    spk.setGrAmount2(rs.getDouble("GrAmount2"));
                    spk.setGrAmount3(rs.getDouble("GrAmount3"));
                    spk.setGrAmount4(rs.getDouble("GrAmount4"));
                    spk.setGrAmount5(rs.getDouble("GrAmount5"));
                    spk.setActualAmount(rs.getDouble("ActualAmount"));
                    spk.setStatus(SpkStatus.valueOf(rs.getString("StatusName")));

                    spkList.add(spk);
                }while (rs.next());
            }

            logger.debug("Excel Count SPK = "+spkList.size());

        }catch (ClassNotFoundException ce){
            logger.error("ClassNotFoundException : " + ce.getMessage());
        } catch (SQLException se){
            logger.error("SQLException : " + se.getMessage());
        } finally {
            try { if(con != null) con.close(); }catch (SQLException e){}
            try { if(pst != null ) pst.close(); }catch (SQLException e){}
            try { if(rs != null) rs.close(); }catch (SQLException e){}
        }
        return spkList;
    }

    @Override
    public void updateStatusAndPurchaseOrder(String spkId, String statusName, String purchaseOrder, String note, String userName) throws ClassNotFoundException, SQLException {
        Connection con = null;
        Statement stm = null;

        String query = String.format("EXECUTE Usp_UpdateSpkStatusAndPurchaseOrder '%s', '%s', '%s', '%s', '%s'", spkId, statusName, purchaseOrder, note, userName);

        logger.debug("=========================== Update SPK Status and Purchase Order ===========================");
        logger.debug("Query       : " + query);
        logger.debug("=========================== Update SPK Status and Purchase Order ===========================");

        Class.forName(dbConProperty.getDriverClassName());
        con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());

        stm = con.createStatement();
        stm.execute(query);

        if(con != null) con.close();
        if(stm != null) stm.close();
    }

    @Override
    public SuratPerintahKerjaEntity updateEntrySheetAndGrAmount(String spkId, String username, String entrySheet, Double grAmount, String statusName, String spkNote, int termOfPayment) throws ClassNotFoundException, SQLException {
        SuratPerintahKerjaEntity spk = new SuratPerintahKerjaEntity();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String query = String.format("EXECUTE Usp_UpdateSpkEntrySheetAndGrAmount '%s', '%s', '%s', %s, '%s', '%s', %s", spkId, username, entrySheet, grAmount, statusName, spkNote, termOfPayment);

        logger.debug("=========================== Update SPK Entry Sheet and GrAmount ===========================");
        logger.debug("Query       : " + query);
        logger.debug("=========================== Update SPK Entry Sheet and GrAmount ===========================");

        Class.forName(dbConProperty.getDriverClassName());
        con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());

        pstmt = con.prepareStatement(query);
        rs = pstmt.executeQuery();

        if(rs.next()){
            do{
                spk.setSpkId(rs.getString("SpkId"));
                spk.setSpkDescription(rs.getString("SpkDescription"));
                spk.setCompanyId(rs.getString("CompanyId"));
                spk.setSbuId(rs.getString("SbuId"));
                spk.setSpkType(rs.getString("SpkType"));
                spk.setProcessingGroup(rs.getString("ProcessingGroup"));
                spk.setCoa(rs.getString("Coa"));
                spk.setApprovedQuotationDate(rs.getString("ApprovedQuotationDate"));
                spk.setStoreId(rs.getString("StoreId"));
                spk.setVendorId(rs.getString("VendorId"));
                spk.setAssetNo(rs.getString("AssetNo"));
                spk.setAssetClassId(rs.getString("AssetClassId"));
                spk.setCostCenterId(rs.getString("CostCenterId"));
                spk.setAmount(rs.getDouble("Amount"));
                spk.setContactPerson(rs.getString("ContactPerson"));
                spk.setInternalOrder(rs.getString("InternalOrder"));
                spk.setPurchaseOrder(rs.getString("PurchaseOrder"));
                spk.setTop1Amount(rs.getDouble("Top1Amount"));
                spk.setTop2Amount(rs.getDouble("Top2Amount"));
                spk.setTop3Amount(rs.getDouble("Top3Amount"));
                spk.setTop4Amount(rs.getDouble("Top4Amount"));
                spk.setTop5Amount(rs.getDouble("Top5Amount"));
                spk.setTop1Percentage(rs.getDouble("Top1Percentage"));
                spk.setTop2Percentage(rs.getDouble("Top2Percentage"));
                spk.setTop3Percentage(rs.getDouble("Top3Percentage"));
                spk.setTop4Percentage(rs.getDouble("Top4Percentage"));
                spk.setTop5Percentage(rs.getDouble("Top5Percentage"));
                spk.setEntrySheet1(rs.getString("EntrySheet1"));
                spk.setEntrySheet2(rs.getString("EntrySheet2"));
                spk.setEntrySheet3(rs.getString("EntrySheet3"));
                spk.setEntrySheet4(rs.getString("EntrySheet4"));
                spk.setEntrySheet5(rs.getString("EntrySheet5"));
                spk.setGrAmount1(rs.getDouble("GrAmount1"));
                spk.setGrAmount2(rs.getDouble("GrAmount2"));
                spk.setGrAmount3(rs.getDouble("GrAmount3"));
                spk.setGrAmount4(rs.getDouble("GrAmount4"));
                spk.setGrAmount5(rs.getDouble("GrAmount5"));
                spk.setTop1Message(rs.getString("Top1Message"));
                spk.setTop2Message(rs.getString("Top2Message"));
                spk.setTop3Message(rs.getString("Top3Message"));
                spk.setTop4Message(rs.getString("Top4Message"));
                spk.setTop5Message(rs.getString("Top5Message"));
                spk.setCreatedTimeStamp(rs.getString("CreatedTimeStamp"));
                spk.setStatus(SpkStatus.valueOf(rs.getString("StatusName")));
                spk.setQuotationFile(rs.getString("QuotationFile"));
                spk.setDataTimeStamp(rs.getString("DataTimeStamp"));

            }while (rs.next());
        }

        if(con != null) con.close();
        if(pstmt != null) pstmt.close();

        return spk;
    }

    @Override
    public void updateStatusAndActualAmount(String spkId, String statusName, String note, String userName, Double actualAmount) throws ClassNotFoundException, SQLException {
        Connection con = null;
        Statement stm = null;

        String query = String.format("EXECUTE Usp_UpdateSpkStatusAndActualAmount '%s', '%s', '%s', '%s', %s", spkId, statusName, note, userName, actualAmount);

        logger.debug("=========================== Update SPK Status and Actual Amount ===========================");
        logger.debug("Query       : " + query);
        logger.debug("=========================== Update SPK Status and Actual Amount ===========================");

        Class.forName(dbConProperty.getDriverClassName());
        con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());

        stm = con.createStatement();
        stm.execute(query);

        if(con != null) con.close();
        if(stm != null) stm.close();
    }

    @Override
    public void updateAssetClassId(String spkId, String assetClassId, String note, String userName, String statusName) throws ClassNotFoundException, SQLException {
        Connection con = null;
        Statement stm = null;

        String query = String.format("EXECUTE Usp_UpdateSpkAssetClassId '%s', '%s', '%s', '%s', '%s'", spkId, assetClassId, note, userName, statusName);

        logger.debug("=========================== Update SPK AssetClassId ===========================");
        logger.debug("Query       : " + query);
        logger.debug("=========================== Update SPK AssetClassId ===========================");

        Class.forName(dbConProperty.getDriverClassName());
        con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());

        stm = con.createStatement();
        stm.execute(query);

        if(con != null) con.close();
        if(stm != null) stm.close();
    }

    @Override
    public List<SuratPerintahKerjaEntity> findUserShouldTakeAction(String userName) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String query = String.format("EXECUTE Usp_FindSpkUserShouldTakeAction '%s'", userName);
        List<SuratPerintahKerjaEntity> spkList = new ArrayList<>();

        logger.debug("=========================== Find SPK User Should Take Action ===========================");
        logger.debug("Query       : " + query);
        logger.debug("=========================== Find SPK User Should Take Action ===========================");

        try{
            Class.forName(dbConProperty.getDriverClassName());
            con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();

            if(rs.next()){
                do{
                    SuratPerintahKerjaEntity spk = new SuratPerintahKerjaEntity();
                    spk.setSpkId(rs.getString("SpkId"));
                    spk.setSpkDescription(rs.getString("SpkDescription"));
                    spk.setCompanyId(rs.getString("CompanyId"));
                    spk.setSbuId(rs.getString("SbuId"));
                    spk.setSpkType(rs.getString("SpkType"));
                    spk.setStoreId(rs.getString("StoreId"));
                    spk.setVendorId(rs.getString("VendorId"));
                    spk.setAssetNo(rs.getString("AssetNo"));
                    spk.setAmount(rs.getDouble("Amount"));
                    spk.setInternalOrder(rs.getString("InternalOrder"));
                    spk.setPurchaseOrder(rs.getString("PurchaseOrder"));
                    spkList.add(spk);
                }while (rs.next());
            }
        }catch (ClassNotFoundException ce){
            logger.error("ClassNotFoundException : " + ce.getMessage());
        } catch (SQLException se){
            logger.error("SQLException : " + se.getMessage());
        } finally {
            try { con.close(); }catch (SQLException e){}
            try { pst.close(); }catch (SQLException e){}
            try { rs.close(); }catch (SQLException e){}
        }

        return spkList;
    }
}
