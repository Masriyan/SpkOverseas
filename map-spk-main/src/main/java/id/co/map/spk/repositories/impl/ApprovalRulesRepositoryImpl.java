package id.co.map.spk.repositories.impl;

import id.co.map.spk.entities.ApprovalRulesEntity;
import id.co.map.spk.entities.CompanyEntity;
import id.co.map.spk.entities.LvlRoleEntity;
import id.co.map.spk.entities.RoleEntity;
import id.co.map.spk.enums.RegistrationStatus;
import id.co.map.spk.model.AppRules;
import id.co.map.spk.repositories.ApprovalRulesRepository;
import id.co.map.spk.utils.DbConProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Awie on 8/26/2019
 */
@Repository
public class ApprovalRulesRepositoryImpl implements ApprovalRulesRepository {

    private static final Logger logger = LoggerFactory.getLogger(AssetClassRepositoryImpl.class);

    private final DbConProperty dbConProperty;

    public ApprovalRulesRepositoryImpl(DbConProperty dbConProperty) {
        this.dbConProperty = dbConProperty;
    }

    @Override
    public List<ApprovalRulesEntity> findById(String approvalRulesId) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String query = String.format("SELECT * FROM ApprovalRules WHERE ApprovalRulesId = '%s'", approvalRulesId);
        List<ApprovalRulesEntity> rules = new ArrayList<>();

        logger.debug("=========================== Find ApprovalRules By ApprovalRulesId ===========================");
        logger.debug("Query       : " + query);
        logger.debug("=========================== Find ApprovalRules By ApprovalRulesId ===========================");

        try{
            Class.forName(dbConProperty.getDriverClassName());
            con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();

            if(rs.next()){
                do{
                    ApprovalRulesEntity approvalRules = new ApprovalRulesEntity();
                    approvalRules.setApprovalRulesId(rs.getString("ApprovalRulesId"));
                    approvalRules.setAssetTypeId(rs.getString("AssetTypeId"));
                    approvalRules.setPoType(rs.getString("PoType"));
                    approvalRules.setApprovalLevel(rs.getInt("ApprovalLevel"));
                    approvalRules.setRoleId(rs.getInt("RoleId"));
                    approvalRules.setMinAmount(rs.getDouble("MinAmount"));
                    approvalRules.setMaxAmount(rs.getDouble("MaxAmount"));
                    approvalRules.setDataTimeStamp(rs.getString("DataTimeStamp"));
                    rules.add(approvalRules);
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

        return rules;
    }

    @Override
    public List<ApprovalRulesEntity> findBySbuId(String sbuId) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String query = String.format("EXECUTE Usp_FindApprovalRulesBySbuId '%s'", sbuId);
        List<ApprovalRulesEntity> rules = new ArrayList<>();

        logger.debug("=========================== Find ApprovalRules By SbuId ===========================");
        logger.debug("Query       : " + query);
        logger.debug("=========================== Find ApprovalRules By SbuId ===========================");

        try{
            Class.forName(dbConProperty.getDriverClassName());
            con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();

            if(rs.next()){
                do{
                    ApprovalRulesEntity approvalRules = new ApprovalRulesEntity();
                    approvalRules.setApprovalRulesId(rs.getString("ApprovalRulesId"));
                    approvalRules.setAssetTypeId(rs.getString("AssetTypeId"));
                    approvalRules.setPoType(rs.getString("PoType"));
                    approvalRules.setApprovalLevel(rs.getInt("ApprovalLevel"));
                    approvalRules.setRoleId(rs.getInt("RoleId"));
                    approvalRules.setMinAmount(rs.getDouble("MinAmount"));
                    approvalRules.setMaxAmount(rs.getDouble("MaxAmount"));
                    approvalRules.setDataTimeStamp(rs.getString("DataTimeStamp"));
                    approvalRules.setCountryId(rs.getInt("CountryId"));
                    rules.add(approvalRules);
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

        return rules;
    }



    @Override
    public List<ApprovalRulesEntity> findBySbuIdCountryId(String sbuId, String countryId) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String query = String.format("EXECUTE Usp_FindApprovalRulesBySbuIdCountryId '%s' , '%s'", sbuId, countryId);
        List<ApprovalRulesEntity> rules = new ArrayList<>();

        logger.debug("=========================== Find ApprovalRules By SbuId ===========================");
        logger.debug("Query       : " + query);
        logger.debug("=========================== Find ApprovalRules By SbuId ===========================");

        try{
            Class.forName(dbConProperty.getDriverClassName());
            con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();

            if(rs.next()){
                do{
                    ApprovalRulesEntity approvalRules = new ApprovalRulesEntity();
                    approvalRules.setApprovalRulesId(rs.getString("ApprovalRulesId"));
                    approvalRules.setAssetTypeId(rs.getString("AssetTypeId"));
                    approvalRules.setPoType(rs.getString("PoType"));
                    approvalRules.setApprovalLevel(rs.getInt("ApprovalLevel"));
                    approvalRules.setRoleId(rs.getInt("RoleId"));
                    approvalRules.setMinAmount(rs.getDouble("MinAmount"));
                    approvalRules.setMaxAmount(rs.getDouble("MaxAmount"));
                    approvalRules.setDataTimeStamp(rs.getString("DataTimeStamp"));
                    approvalRules.setCountryId(rs.getInt("CountryId"));
                    rules.add(approvalRules);
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

        return rules;
    }



    @Override
    public List<ApprovalRulesEntity> findByCompanyId(String companyId) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String query = String.format("EXECUTE Usp_FindApprovalRulesByCompanyId '%s'", companyId);
        List<ApprovalRulesEntity> rules = new ArrayList<>();

        logger.debug("=========================== Find ApprovalRules By ApprovalRulesId ===========================");
        logger.debug("Query       : " + query);
        logger.debug("=========================== Find ApprovalRules By ApprovalRulesId ===========================");

        try{
            Class.forName(dbConProperty.getDriverClassName());
            con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();

            if(rs.next()){
                do{
                    ApprovalRulesEntity approvalRules = new ApprovalRulesEntity();
                    approvalRules.setApprovalRulesId(rs.getString("ApprovalRulesId"));
                    approvalRules.setAssetTypeId(rs.getString("AssetTypeId"));
                    approvalRules.setPoType(rs.getString("PoType"));
                    approvalRules.setApprovalLevel(rs.getInt("ApprovalLevel"));
                    approvalRules.setRoleId(rs.getInt("RoleId"));
                    approvalRules.setMinAmount(rs.getDouble("MinAmount"));
                    approvalRules.setMaxAmount(rs.getDouble("MaxAmount"));
                    approvalRules.setDataTimeStamp(rs.getString("DataTimeStamp"));
                    rules.add(approvalRules);
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

        return rules;
    }

    @Override
    public List<ApprovalRulesEntity> findAllDistinctRules() {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        String query = String.format("SELECT DISTINCT ApprovalRulesId FROM ApprovalRules");
        List<ApprovalRulesEntity> rules = new ArrayList<>();

        logger.debug("=========================== Find ApprovalRules DISTINCT ===========================");
        logger.debug("Query       : " + query);

        try{
            Class.forName(dbConProperty.getDriverClassName());
            con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());
            stmt = con.createStatement();
            rs = stmt.executeQuery(query);

            if(rs.next()){
                do{
                    ApprovalRulesEntity approvalRules = new ApprovalRulesEntity();
                    approvalRules.setApprovalRulesId(rs.getString("ApprovalRulesId"));
                    rules.add(approvalRules);
                }while (rs.next());
            }
        }catch (ClassNotFoundException ce){
            logger.error("ClassNotFoundException : " + ce.getMessage());
        }catch (SQLException se){
            logger.error("SQLException : " + se.getMessage());
        }finally {
            try { con.close(); }catch (SQLException e){}
            try { stmt.close(); }catch (SQLException e){}
            try { rs.close(); }catch (SQLException e){}
        }
        logger.debug("=========================== Find ApprovalRules DISTINCT ===========================");

        return rules;
    }

    @Override
    public ApprovalRulesEntity findLasRuleId() {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        String query = String.format("SELECT DISTINCT top 1 ApprovalRulesId FROM ApprovalRules order by ApprovalRulesId desc");
        ApprovalRulesEntity approvalRule = new ApprovalRulesEntity();

        try{
            Class.forName(dbConProperty.getDriverClassName());
            con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());
            stmt = con.createStatement();
            rs = stmt.executeQuery(query);

            if(rs.next()){
                do{
                    approvalRule.setApprovalRulesId(rs.getString("ApprovalRulesId"));
                }while (rs.next());
            }
        }catch (ClassNotFoundException ce){
            logger.error("ClassNotFoundException : " + ce.getMessage());
        }catch (SQLException se){
            logger.error("SQLException : " + se.getMessage());
        }finally {
            try { con.close(); }catch (SQLException e){}
            try { stmt.close(); }catch (SQLException e){}
            try { rs.close(); }catch (SQLException e){}
        }

        return approvalRule;
    }

    @Override
    public void add(AppRules newrule) throws ClassNotFoundException, SQLException {

        String dataTimeStamp = newrule.getDataTimeStamp();
        Connection con = null;
        PreparedStatement pstAppRule = null;
        String qryAppRuleQuery = "EXECUTE Usp_AddApprovalRule ?,?,?,?,?,?,?,?";

        Class.forName(dbConProperty.getDriverClassName());

        logger.debug("Query       : " + qryAppRuleQuery);

        con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());
        con.setAutoCommit(false);

        //insert into table UserCompany
        pstAppRule = con.prepareStatement(qryAppRuleQuery);
        for (LvlRoleEntity levelRole: newrule.getAppruleList()) {
            pstAppRule.setString(1, newrule.getApprovalRulesId());
            pstAppRule.setString(2, newrule.getAssetTypeId());
            pstAppRule.setString(3, newrule.getPoType());
            pstAppRule.setInt(4, levelRole.getApprovalLevel());
            pstAppRule.setInt(5, levelRole.getRoleId());
            pstAppRule.setDouble(6, newrule.getMinAmount());
            pstAppRule.setDouble(7, newrule.getMaxAmount());
            pstAppRule.setInt(8, newrule.getCountryId());
            pstAppRule.addBatch();
        }

        pstAppRule.executeBatch();

        //commit data
        con.commit();

        if(pstAppRule != null) pstAppRule.close();
        if(con != null) con.close();
    }

    @Override
    public void delete(String rule) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement pstUserCompany = null;
        String[] rules = rule.split(",");
        String qryUserCompanyQuery = "DELETE FROM ApprovalRules WHERE ApprovalRulesId = ? " +
                "AND AssetTypeId = ? AND PoType = ? AND ApprovalLevel = ? "+
                "AND MinAmount = ? AND MaxAmount = ? ";

        Class.forName(dbConProperty.getDriverClassName());

        con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());
        con.setAutoCommit(false);

        //delete table UserRole registration
        pstUserCompany = con.prepareStatement(qryUserCompanyQuery);
        pstUserCompany.setString(1, rules[0]);
        pstUserCompany.setString(2, rules[1]);
        pstUserCompany.setString(3, rules[2]);
        pstUserCompany.setString(4, rules[3]);
        pstUserCompany.setString(5, rules[4]);
        pstUserCompany.setString(6, rules[5]);

        pstUserCompany.execute();

        con.commit();
        if(pstUserCompany != null) pstUserCompany.close();
        if(con != null) con.close();
    }

    @Override
    public void update(AppRules apprule) throws ClassNotFoundException, SQLException {

    }

    @Override
    public ApprovalRulesEntity getObject(String rule) {

        ApprovalRulesEntity ruleobj = new ApprovalRulesEntity();

        String[] ruleparse = rule.split(",");
        ruleobj.setApprovalRulesId(ruleparse[0]);
        ruleobj.setAssetTypeId(ruleparse[1]);
        ruleobj.setPoType(ruleparse[2]);
        ruleobj.setApprovalLevel(Integer.parseInt(ruleparse[3]));
        ruleobj.setMinAmount(Double.parseDouble(ruleparse[4]));
        ruleobj.setMaxAmount(Double.parseDouble(ruleparse[5]));

        return ruleobj;

    }

    @Override
    public Map<String, Object> findAppRuleForPagination(String approvalRulesId, String assetTypeId, String poType, String approvalLevel, String roleId, String minAmount, String maxAmount, Integer pageNumber, Integer size, Integer draw) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        Map<String, Object> map = new HashMap<>();
        List<ApprovalRulesEntity> apprules = new ArrayList<>();
        Integer totalCount = 0;

        try{
            Class.forName(dbConProperty.getDriverClassName());
            con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());
            pst = con.prepareStatement("EXECUTE Usp_FindAppRuleForPagination ?,?,?,?,?,?,?,?,?");
            pst.setString(1, approvalRulesId);
            pst.setString(2, assetTypeId);
            pst.setString(3, poType);
            pst.setString(4, approvalLevel);
            pst.setString(5, roleId);
            pst.setString(6, minAmount);
            pst.setString(7, maxAmount);
            pst.setInt(8, pageNumber);
            pst.setInt(9, size);
            rs = pst.executeQuery();

            if(rs.next()){

                totalCount = rs.getInt("TotalCount");

                do{
                    AppRules apprule = new AppRules();
                    apprule.setApprovalRulesId(rs.getString("approvalRulesId"));
                    apprule.setAssetTypeId(rs.getString("assetTypeId"));
                    apprule.setPoType(rs.getString("poType"));
                    apprule.setApprovalLevel(rs.getInt("approvalLevel"));
                    //apprule.setRoleId(rs.getInt("roleId"));
                    apprule.setRoleName(rs.getString("RoleName").substring(5));
                    apprule.setMinAmount(rs.getDouble("minAmount"));
                    apprule.setMaxAmount(rs.getDouble("maxAmount"));
                    apprule.setCountryName(rs.getString("CountryName"));
                    apprules.add(apprule);
                }while (rs.next());
            }
            map.put("apprules", apprules);
            map.put("totalCount", totalCount);

        }catch (ClassNotFoundException ce){
            logger.error("ClassNotFoundException : " + ce.getMessage());
        } catch (SQLException se){
            logger.error("SQLException : " + se.getMessage(),se);
        } finally {
            try { if(con != null) con.close(); }catch (SQLException e){}
            try { if(pst != null ) pst.close(); }catch (SQLException e){}
            try { if(rs != null) rs.close(); }catch (SQLException e){}
        }
        return map;
    }
}
