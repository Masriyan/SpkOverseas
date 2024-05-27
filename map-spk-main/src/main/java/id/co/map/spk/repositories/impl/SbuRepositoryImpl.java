package id.co.map.spk.repositories.impl;

import id.co.map.spk.entities.CompanyEntity;
import id.co.map.spk.entities.RoleEntity;
import id.co.map.spk.entities.SbuEntity;
import id.co.map.spk.entities.StoreEntity;
import id.co.map.spk.repositories.SbuRepository;
import id.co.map.spk.repositories.StoreRepository;
import id.co.map.spk.utils.DbConProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.*;

@Repository
public class SbuRepositoryImpl implements SbuRepository {

    private static final Logger logger = LoggerFactory.getLogger(StoreRepositoryImpl.class);
    private final DbConProperty dbConProperty;

    public SbuRepositoryImpl(DbConProperty dbConProperty) {
        this.dbConProperty = dbConProperty;
    }

    @Override
    public List<SbuEntity> findAll() {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String query = "SELECT * FROM Sbu";
        List<SbuEntity> sbus = new ArrayList<>();

        try{
            Class.forName(dbConProperty.getDriverClassName());
            con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();

            if(rs.next()){
                do{
                    SbuEntity sbu = new SbuEntity();
                    sbu.setSbuId(rs.getInt("SbuId"));
                    sbu.setCompanyId(rs.getString("CompanyId"));
                    sbu.setSbuCode(rs.getString("SbuCode"));
                    sbu.setSbuDesc(rs.getString("SbuDesc"));
                    sbu.setApprovalRulesId(rs.getString("ApprovalRulesId"));
                    sbus.add(sbu);
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

        return sbus;
    }

    @Override
    public SbuEntity findBySbuId(String sbuId) {

        SbuEntity sbu = new SbuEntity();
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String query = String.format("SELECT * FROM Sbu WHERE SbuId = '%s'", sbuId);

        logger.debug("=========================== Find Sbu By Id ===========================");
        logger.debug("Query       : " + query);
        logger.debug("=========================== Find Sbu By Id ===========================");

        try{
            Class.forName(dbConProperty.getDriverClassName());
            con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();

            if(rs.next()){
                    sbu.setSbuId(rs.getInt("SbuId"));
                    sbu.setSbuCode(rs.getString("SbuCode"));
                    sbu.setSbuDesc(rs.getString("SbuDesc"));
                    sbu.setApprovalRulesId(rs.getString("ApprovalRulesId"));
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

        return sbu;
    }

    @Override
    public SbuEntity findbySbuCodeDesc(String sbuCode, String sbuDesc){

        SbuEntity sbu = new SbuEntity();
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String query = String.format("SELECT * FROM Sbu WHERE SbuCode = '%s' AND SbuDesc = '%s'", sbuCode,sbuDesc);

        logger.debug("=========================== Find Sbu By Code & Desc ===========================");
        logger.debug("Query       : " + query);
        logger.debug("=========================== Find Sbu By Code & Desc ===========================");

        try{
            Class.forName(dbConProperty.getDriverClassName());
            con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();

            if(rs.next()){
                sbu.setSbuId(rs.getInt("SbuId"));
                sbu.setSbuCode(rs.getString("SbuCode"));
                sbu.setSbuDesc(rs.getString("SbuDesc"));
                sbu.setApprovalRulesId(rs.getString("ApprovalRulesId"));
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

        return sbu;
    }

    @Override
    public List<SbuEntity> findByCompany(String companyId) {

        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String query = String.format("EXECUTE Usp_FindSbuByCompany '%s'", companyId);
        List<SbuEntity> sbus = new ArrayList<>();

        logger.debug("=========================== Find Sbu By Company ===========================");
        logger.debug("Query       : " + query);
        logger.debug("=========================== Find Sbu By Company ===========================");

        try{
            Class.forName(dbConProperty.getDriverClassName());
            con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();

            if(rs.next()){
                do{
                    SbuEntity sbu = new SbuEntity();
                    sbu.setSbuId(rs.getInt("SbuId"));
                    sbu.setSbuCode(rs.getString("SbuCode"));
                    sbu.setSbuDesc(rs.getString("SbuDesc"));
                    sbu.setApprovalRulesId(rs.getString("ApprovalRulesId"));
                    sbus.add(sbu);
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

        return sbus;
    }

    @Override
    public List<SbuEntity> findByListCompany(String company) {

        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String query = String.format("EXECUTE Usp_FindSbuFromListCompany '%s'", company);
        List<SbuEntity> sbus = new ArrayList<>();

        logger.debug("=========================== Find Sbu By List Company ===========================");
        logger.debug("Query       : " + query);
        logger.debug("=========================== Find Sbu By List Company ===========================");

        try{
            Class.forName(dbConProperty.getDriverClassName());
            con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();

            if(rs.next()){
                do{
                    SbuEntity sbu = new SbuEntity();
                    sbu.setSbuId(rs.getInt("SbuId"));
                    sbu.setSbuCode(rs.getString("SbuCode"));
                    sbu.setSbuDesc(rs.getString("SbuDesc"));
                    sbu.setApprovalRulesId(rs.getString("ApprovalRulesId"));
                    sbus.add(sbu);
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

        return sbus;
    }

    @Override
    public List<SbuEntity> findByUserandCompany(String username, String companyId) {

        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
       //String query = String.format("EXECUTE Usp_FindSbuFromUserAndCompany '%s,%s'", username,companyId);
        List<SbuEntity> sbus = new ArrayList<>();

        String query = String.format("EXECUTE Usp_FindSbuFromUserAndCompany  ?, ?");

        try{
            Class.forName(dbConProperty.getDriverClassName());
            con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());

            pst = con.prepareStatement(query);
            pst.setString(1, username);
            pst.setString(2, companyId);

            rs = pst.executeQuery();

            if(rs.next()){
                do{
                    SbuEntity sbu = new SbuEntity();
                    sbu.setSbuId(rs.getInt("SbuId"));
                    sbu.setSbuCode(rs.getString("SbuCode"));
                    sbu.setSbuDesc(rs.getString("SbuDesc"));
                    sbu.setApprovalRulesId(rs.getString("ApprovalRulesId"));
                    sbus.add(sbu);
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

        return sbus;
    }

    @Override
    public void add(SbuEntity sbu) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;

        String query = String.format("EXECUTE Usp_AddSbu ?, ?, ?, ?");

        Class.forName(dbConProperty.getDriverClassName());
        con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());

        pstmt = con.prepareStatement(query);
        pstmt.setString(1, sbu.getSbuCode());
        pstmt.setString(2, sbu.getSbuDesc());
        pstmt.setString(3, sbu.getCompanyId());
        pstmt.setString(4, sbu.getApprovalRulesId());

        pstmt.execute();

        if(con != null) con.close();
        if(pstmt != null) pstmt.close();
    }

    @Override
    public Map<String, Object> findSbuForPagination(String SbuId, String sbuDesc, Integer pageNumber, Integer size) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        Map<String, Object> map = new HashMap<>();
        List<SbuEntity> sbus = new ArrayList<>();
        Integer totalCount = 0;

        try{
            Class.forName(dbConProperty.getDriverClassName());
            con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());
            pst = con.prepareStatement("EXECUTE Usp_FindSbuForPagination ?, ?, ?, ?");
            pst.setString(1, SbuId);
            pst.setString(2, sbuDesc);
            pst.setInt(3, pageNumber);
            pst.setInt(4, size);
            rs = pst.executeQuery();

            if(rs.next()){
                do{
                    SbuEntity sbu = new SbuEntity();
                    sbu.setSbuId(rs.getInt("SbuId"));
                    sbu.setSbuCode(rs.getString("SbuCode"));
                    sbu.setSbuDesc(rs.getString("SbuDesc"));
                    sbu.setCompanyId(rs.getString("CompanyId"));
                    sbu.setApprovalRulesId(rs.getString("ApprovalRulesId"));
                    sbus.add(sbu);
                }while (rs.next());
            }
            map.put("sbus", sbus);
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
    public List<SbuEntity> findByUsername(String username) {

        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String query = String.format("EXECUTE Usp_FindSbuFromUsername '%s'", username);
        List<SbuEntity> sbus = new ArrayList<>();

        logger.debug("=========================== Find Sbu By Username ===========================");
        logger.debug("Query       : " + query);
        logger.debug("=========================== Find Sbu By Username ===========================");

        try{
            Class.forName(dbConProperty.getDriverClassName());
            con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();

            if(rs.next()){
                do{
                    SbuEntity sbu = new SbuEntity();
                    sbu.setSbuId(rs.getInt("SbuId"));
                    sbu.setSbuCode(rs.getString("SbuCode"));
                    sbu.setSbuDesc(rs.getString("SbuDesc"));
                    sbus.add(sbu);
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

        return sbus;
    }

    @Override
    public List<SbuEntity> findByUsernamebyComp(String username) {

        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String query = String.format("EXECUTE Usp_FindSbuFromUsernamebyCompany '%s'", username);
        List<SbuEntity> sbus = new ArrayList<>();

        logger.debug("=========================== Find Sbu from Username by Company ===========================");
        logger.debug("Query       : " + query);
        logger.debug("=========================== Find Sbu from Username by Company ===========================");

        try{
            Class.forName(dbConProperty.getDriverClassName());
            con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();

            if(rs.next()){
                do{
                    SbuEntity sbu = new SbuEntity();
                    sbu.setSbuId(rs.getInt("SbuId"));
                    sbu.setSbuCode(rs.getString("SbuCode"));
                    sbu.setSbuDesc(rs.getString("SbuDesc"));
                    sbu.setApprovalRulesId(rs.getString("ApprovalRulesId"));
                    sbus.add(sbu);
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

        return sbus;
    }

    @Override
    public void updateCodeDescbySbuId(SbuEntity sbu, String sbuId) throws ClassNotFoundException, SQLException {

        Connection con = null;
        PreparedStatement pstUpdate = null;
        String qryUpdate = "UPDATE Sbu Set SbuCode = ? , SbuDesc = ? WHERE SbuId = ?";

        Class.forName(dbConProperty.getDriverClassName());
        con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());

        //update table UserRole registration
        pstUpdate = con.prepareStatement(qryUpdate);

        pstUpdate.setString(1, sbu.getSbuCode());
        pstUpdate.setString(2, sbu.getSbuDesc());
        pstUpdate.setInt(3, Integer.parseInt(sbuId));
        pstUpdate.execute();

        if(pstUpdate != null) pstUpdate.close();
        if(con != null) con.close();

    }

    @Override
    public void updateRulebySbuId(String rule, String sbuId) throws ClassNotFoundException, SQLException {
        String[] sbu = sbuId.split("-");
        String sbuCode = sbu[0].trim();
        String sbuDesc = sbu[1].trim();

        Connection con = null;
        PreparedStatement pstUpdate = null;
        String qryUpdate = "UPDATE Sbu Set ApprovalRulesId = ? WHERE SbuCode = ? AND SbuDesc = ?";

        Class.forName(dbConProperty.getDriverClassName());
        con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());

        //update table UserRole registration
        pstUpdate = con.prepareStatement(qryUpdate);

        pstUpdate.setString(1, rule);
        pstUpdate.setString(2, sbuCode);
        pstUpdate.setString(3, sbuDesc);
        pstUpdate.execute();

        if(pstUpdate != null) pstUpdate.close();
        if(con != null) con.close();
    }

}
