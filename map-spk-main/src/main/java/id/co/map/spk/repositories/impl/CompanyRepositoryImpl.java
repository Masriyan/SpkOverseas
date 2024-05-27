package id.co.map.spk.repositories.impl;

import id.co.map.spk.entities.CompanyEntity;
import id.co.map.spk.entities.CountryEntity;
import id.co.map.spk.model.AppCompany;
import id.co.map.spk.repositories.CompanyRepository;
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
public class CompanyRepositoryImpl implements CompanyRepository {

    private static final Logger logger = LoggerFactory.getLogger(CompanyRepositoryImpl.class);
    private final DbConProperty dbConProperty;

    public CompanyRepositoryImpl(DbConProperty dbConProperty) {
        this.dbConProperty = dbConProperty;
    }

    @Override
    public List<CompanyEntity> findByUserName(String userName) {

        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String query = String.format("EXECUTE Usp_FindCompanyByUsername '%s'", userName);
        List<CompanyEntity> companies = new ArrayList<>();

        logger.debug("=========================== Find Companies By UserName ===========================");
        logger.debug("Query       : " + query);
        logger.debug("=========================== Find Companies By UserName ===========================");

        try{
            Class.forName(dbConProperty.getDriverClassName());
            con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();

            if(rs.next()){
                do{
                    CompanyEntity company = new CompanyEntity();
                    company.setCompanyId(rs.getString("CompanyId"));
                    company.setProcessingGroup(rs.getString("ProcessingGroup"));
                    company.setCompanyName(rs.getString("CompanyName"));
                    company.setCoa(rs.getString("Coa"));
                    company.setNpwp(rs.getString("Npwp"));
                    company.setAddress1(rs.getString("Address1"));
                    company.setAddress2(rs.getString("Address2"));
                    company.setCity(rs.getString("City"));
                    company.setHeaderImage(rs.getString("HeaderImage"));
                    company.setApprovalRulesId(rs.getString("ApprovalRulesId"));
                    company.setDataTimeStamp(rs.getString("DataTimeStamp"));
                    companies.add(company);
                }while (rs.next());
            }
        }catch (ClassNotFoundException ce){
            logger.error("ClassNotFoundException : " + ce.getMessage());
        }catch (SQLException se){
            logger.error("SQLException : " + se.getMessage());
        }finally {
            try { con.close(); }catch (SQLException e){}
            try { pst.close(); }catch (SQLException e){}
            try { rs.close(); }catch (SQLException e){}
        }

        return companies;
    }

    @Override
    public CompanyEntity findByCompanyId(String companyId) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        CompanyEntity company = new CompanyEntity();
        String query = String.format("SELECT CompanyId, ProcessingGroup, " +
            "CompanyName, Coa, Npwp, Address1, Address2, City, HeaderImage, C.ApprovalRulesId, DataTimeStamp , ISNULL(D.CountryName, '-') as CountryName, C.CountryId FROM Company " +
            "C LEFT JOIN Country D on C.CountryId = D.CountryId WHERE CompanyId = '%s'", companyId);

        logger.debug("=========================== Find Companies By CompanyId ===========================");
        logger.debug("Query       : " + query);
        logger.debug("=========================== Find Companies By CompanyId ===========================");

        try{
            Class.forName(dbConProperty.getDriverClassName());
            con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();

            if(rs.next()){
                do{
                    company.setCompanyId(rs.getString("CompanyId"));
                    company.setProcessingGroup(rs.getString("ProcessingGroup"));
                    company.setCompanyName(rs.getString("CompanyName"));
                    company.setCoa(rs.getString("Coa"));
                    company.setNpwp(rs.getString("Npwp"));
                    company.setAddress1(rs.getString("Address1"));
                    company.setAddress2(rs.getString("Address2"));
                    company.setCity(rs.getString("City"));
                    company.setHeaderImage(rs.getString("HeaderImage"));
                    company.setApprovalRulesId(rs.getString("ApprovalRulesId"));
                    company.setDataTimeStamp(rs.getString("DataTimeStamp"));
                    company.setCountryName(rs.getString("CountryName"));
                    company.setCountryId(rs.getString("CountryId"));
                }while (rs.next());
            }
        }catch (ClassNotFoundException ce){
            logger.error("ClassNotFoundException : " + ce.getMessage());
        }catch (SQLException se){
            logger.error("SQLException : " + se.getMessage());
        }finally {
            try { con.close(); }catch (SQLException e){}
            try { pst.close(); }catch (SQLException e){}
            try { rs.close(); }catch (SQLException e){}
        }

        return company;
    }

    @Override
    public CountryEntity findCountryByCompanyId(String companyId) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        CountryEntity country = new CountryEntity();
        String query = String.format("SELECT D.* FROM Company " +
            "C LEFT JOIN Country D on C.CountryId = D.CountryId WHERE CompanyId = '%s'", companyId);

        logger.debug("=========================== Find Companies By CompanyId ===========================");
        logger.debug("Query       : " + query);
        logger.debug("=========================== Find Companies By CompanyId ===========================");

        try{
            Class.forName(dbConProperty.getDriverClassName());
            con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();

            if(rs.next()){
                do{
                    country.setLabel1(rs.getString("label1"));
                    country.setLabel2(rs.getString("label2"));
                    country.setLabel3(rs.getString("label3"));
                    country.setLabel4(rs.getString("label4"));
                    country.setLabel5(rs.getString("label5"));
                    country.setLabel6(rs.getString("label6"));
                    country.setLabel7(rs.getString("label7"));
                    country.setLabel8(rs.getString("label8"));
                    country.setLabel9(rs.getString("label9"));
                    country.setLabel10(rs.getString("label10"));

                    country.setLabel11(rs.getString("label11"));
                    country.setLabel12(rs.getString("label12"));
                    country.setLabel13(rs.getString("label13"));
                    country.setLabel14(rs.getString("label14"));
                    country.setLabel15(rs.getString("label15"));
                    country.setLabel16(rs.getString("label16"));
                    country.setLabel17(rs.getString("label17"));
                    country.setLabel18(rs.getString("label18"));
                    country.setLabel19(rs.getString("label19"));
                    country.setLabel20(rs.getString("label20"));

                    country.setLabel21(rs.getString("label21"));
                    country.setLabel22(rs.getString("label22"));
                    country.setLabel23(rs.getString("label23"));
                    country.setLabel24(rs.getString("label24"));
                    country.setLabel25(rs.getString("label25"));
                    country.setLabel26(rs.getString("label26"));
                    country.setLabel27(rs.getString("label27"));
                    country.setLabel28(rs.getString("label28"));
                    country.setLabel29(rs.getString("label29"));
                    country.setLabel30(rs.getString("label30"));
                    country.setLabel31(rs.getString("label31"));
                }while (rs.next());
            }
        }catch (ClassNotFoundException ce){
            logger.error("ClassNotFoundException : " + ce.getMessage());
        }catch (SQLException se){
            logger.error("SQLException : " + se.getMessage());
        }finally {
            try { con.close(); }catch (SQLException e){}
            try { pst.close(); }catch (SQLException e){}
            try { rs.close(); }catch (SQLException e){}
        }

        return country;
    }

    @Override
    public List<CompanyEntity> findAll() {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String query = String.format("SELECT * FROM Company");
        List<CompanyEntity> companies = new ArrayList<>();

        try{
            Class.forName(dbConProperty.getDriverClassName());
            con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();

            if(rs.next()){
                do{
                    CompanyEntity company = new CompanyEntity();
                    company.setCompanyId(rs.getString("CompanyId"));
                    company.setProcessingGroup(rs.getString("ProcessingGroup"));
                    company.setCompanyName(rs.getString("CompanyName"));
                    company.setCoa(rs.getString("Coa"));
                    company.setNpwp(rs.getString("Npwp"));
                    company.setAddress1(rs.getString("Address1"));
                    company.setAddress2(rs.getString("Address2"));
                    company.setCity(rs.getString("City"));
                    company.setHeaderImage(rs.getString("HeaderImage"));
                    company.setApprovalRulesId(rs.getString("ApprovalRulesId"));
                    company.setDataTimeStamp(rs.getString("DataTimeStamp"));
//                    company.setCountryName(rs.getString("CompanyName"));
                    companies.add(company);
                }while (rs.next());
            }
        }catch (ClassNotFoundException ce){
            logger.error("ClassNotFoundException : " + ce.getMessage());
        }catch (SQLException se){
            logger.error("SQLException : " + se.getMessage());
        }finally {
            try { con.close(); }catch (SQLException e){}
            try { pst.close(); }catch (SQLException e){}
            try { rs.close(); }catch (SQLException e){}
        }

        return companies;
    }

    @Override
    public void add(AppCompany company) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;

        //String query = String.format("INSERT INTO Company (CompanyId, ProcessingGroup, CompanyName, Coa, Npwp, Address1, Address2, City, HeaderImage, ApprovalRulesId) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        //String query = String.format("EXECUTE Usp_AddCompany ?, ?, ?, ?, ?, ?, ?, ?, ?, ?");
        String query = String.format("EXECUTE Usp_AddCompanySbu ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?");

        Class.forName(dbConProperty.getDriverClassName());
        con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());

        pstmt = con.prepareStatement(query);
        pstmt.setString(1, company.getCompanyId());
        pstmt.setString(2, company.getProcessingGroup());
        pstmt.setString(3, company.getCompanyName());
        pstmt.setString(4, company.getSbuCode());
        pstmt.setString(5, company.getSbuDesc());
        pstmt.setString(6, company.getCoa());
        pstmt.setString(7, company.getNpwp());
        pstmt.setString(8, company.getAddress1());
        pstmt.setString(9, company.getAddress2());
        pstmt.setString(10, company.getCity());
        pstmt.setString(11, company.getHeaderImage());
        pstmt.setString(12, company.getApprovalRulesId());
        pstmt.setInt(13, company.getCountryData());

        pstmt.execute();

        if(con != null) con.close();
        if(pstmt != null) pstmt.close();
    }

    @Override
    public void delete(String sbuDelete) throws ClassNotFoundException, SQLException{
        Connection con = null;
        PreparedStatement pstDeleteSbu = null;

        String[] sbutext = sbuDelete.split(",");
        String compid  = sbutext[0];
        String sbuCode = sbutext[1];
        String sbuDesc = sbutext[2];

        String query = String.format("EXECUTE Usp_DeleteCompanySbu ?, ?, ?");

        Class.forName(dbConProperty.getDriverClassName());
        con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());
        //con.setAutoCommit(false);

        pstDeleteSbu = con.prepareStatement(query);
        pstDeleteSbu.setString(1, compid);
        pstDeleteSbu.setString(2, sbuCode);
        pstDeleteSbu.setString(3, sbuDesc);

        //con.commit();
        pstDeleteSbu.execute();

        if(pstDeleteSbu != null) pstDeleteSbu.close();
        if(con != null) con.close();
    }

    @Override
    public Map<String, Object> findUserForPagination(String companyId, String companyName, String sbuId,Integer pageNumber, Integer size) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        Map<String, Object> map = new HashMap<>();
        List<AppCompany> companies = new ArrayList<>();
        Integer totalCount = 0;

        try{
            Class.forName(dbConProperty.getDriverClassName());
            con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());
            pst = con.prepareStatement("EXECUTE Usp_FindCompanyForPagination ?, ?, ?, ?, ?");
            pst.setString(1, companyId);
            pst.setString(2, companyName);
            pst.setString(3, sbuId);
            pst.setInt(4, pageNumber);
            pst.setInt(5, size);
            rs = pst.executeQuery();

            if(rs.next()){

                totalCount = rs.getInt("TotalCount");

                do{
                    //CompanyEntity company = new CompanyEntity();
                    AppCompany company = new AppCompany();
                    company.setCompanyId(rs.getString("CompanyId"));
                    company.setProcessingGroup(rs.getString("ProcessingGroup"));
                    company.setCompanyName(rs.getString("CompanyName"));
                    company.setSbuCode(rs.getString("SbuCode"));
                    company.setSbuDesc(rs.getString("SbuDesc"));
                    company.setCoa(rs.getString("Coa"));
                    company.setNpwp(rs.getString("Npwp"));
                    company.setAddress1(rs.getString("Address1"));
                    company.setAddress2(rs.getString("Address2"));
                    company.setCity(rs.getString("City"));
                    company.setHeaderImage(rs.getString("HeaderImage"));
                    company.setApprovalRulesId(rs.getString("ApprovalRulesId"));
                    company.setDataTimeStamp(rs.getString("DataTimeStamp"));
                    company.setCountryName(rs.getString("CountryName"));
                    companies.add(company);
                }while (rs.next());
            }
            map.put("companies", companies);
            map.put("totalCount", totalCount);

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
    public void updateNamebyCompId(String compname, String companyId) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement pstUpdate = null;
        String qryUpdate = "UPDATE Company Set CompanyName = ? WHERE CompanyId = ?";

        Class.forName(dbConProperty.getDriverClassName());
        con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());

        //delete table UserRole registration
        pstUpdate = con.prepareStatement(qryUpdate);
        pstUpdate.setString(1, compname);
        pstUpdate.setString(2, companyId);
        pstUpdate.execute();

        if(pstUpdate != null) pstUpdate.close();
        if(con != null) con.close();
    }

    @Override
    public void updateCountrybyCompId(Integer countryId, String companyId) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement pstUpdate = null;
        String qryUpdate = "UPDATE Company Set CountryId = ? WHERE CompanyId = ?";

        Class.forName(dbConProperty.getDriverClassName());
        con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());

        //delete table UserRole registration
        pstUpdate = con.prepareStatement(qryUpdate);
        pstUpdate.setInt(1, countryId);
        pstUpdate.setString(2, companyId);
        pstUpdate.execute();

        if(pstUpdate != null) pstUpdate.close();
        if(con != null) con.close();
    }

    @Override
    public void updateNpwpbyCompId(String npwp, String companyId) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement pstUpdate = null;
        String qryUpdate = "UPDATE Company Set Npwp = ? WHERE CompanyId = ?";

        Class.forName(dbConProperty.getDriverClassName());
        con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());

        //delete table UserRole registration
        pstUpdate = con.prepareStatement(qryUpdate);
        pstUpdate.setString(1, npwp);
        pstUpdate.setString(2, companyId);
        pstUpdate.execute();

        if(pstUpdate != null) pstUpdate.close();
        if(con != null) con.close();
    }

    @Override
    public void updateAdd1byCompId(String add1, String companyId) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement pstUpdate = null;
        String qryUpdate = "UPDATE Company Set Address1 = ? WHERE CompanyId = ?";

        Class.forName(dbConProperty.getDriverClassName());
        con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());

        //delete table UserRole registration
        pstUpdate = con.prepareStatement(qryUpdate);
        pstUpdate.setString(1, add1);
        pstUpdate.setString(2, companyId);
        pstUpdate.execute();

        if(pstUpdate != null) pstUpdate.close();
        if(con != null) con.close();
    }

    @Override
    public void updateAdd2byCompId(String add2, String companyId) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement pstUpdate = null;
        String qryUpdate = "UPDATE Company Set Address2 = ? WHERE CompanyId = ?";

        Class.forName(dbConProperty.getDriverClassName());
        con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());

        //delete table UserRole registration
        pstUpdate = con.prepareStatement(qryUpdate);
        pstUpdate.setString(1, add2);
        pstUpdate.setString(2, companyId);
        pstUpdate.execute();

        if(pstUpdate != null) pstUpdate.close();
        if(con != null) con.close();
    }

    @Override
    public void updateCoabyCompId(String coa, String companyId) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement pstUpdate = null;
        String qryUpdate = "UPDATE Company Set Coa = ? WHERE CompanyId = ?";

        Class.forName(dbConProperty.getDriverClassName());
        con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());

        //delete table UserRole registration
        pstUpdate = con.prepareStatement(qryUpdate);
        pstUpdate.setString(1, coa);
        pstUpdate.setString(2, companyId);
        pstUpdate.execute();

        if(pstUpdate != null) pstUpdate.close();
        if(con != null) con.close();
    }

    @Override
    public void updateHeadImgbyCompId(String headimg, String companyId) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement pstUpdate = null;
        String qryUpdate = "UPDATE Company Set HeaderImage = ? WHERE CompanyId = ?";

        Class.forName(dbConProperty.getDriverClassName());
        con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());

        //delete table UserRole registration
        pstUpdate = con.prepareStatement(qryUpdate);
        pstUpdate.setString(1, headimg);
        pstUpdate.setString(2, companyId);
        pstUpdate.execute();

        if(pstUpdate != null) pstUpdate.close();
        if(con != null) con.close();
    }
}
