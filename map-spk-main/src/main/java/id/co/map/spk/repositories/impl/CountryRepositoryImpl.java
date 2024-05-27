package id.co.map.spk.repositories.impl;

import id.co.map.spk.entities.ApprovalRulesEntity;
import id.co.map.spk.entities.CompanyEntity;
import id.co.map.spk.entities.CountryEntity;
import id.co.map.spk.model.AppCompany;
import id.co.map.spk.repositories.CountryRepository;
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
public class CountryRepositoryImpl implements CountryRepository {
  private static final Logger logger = LoggerFactory.getLogger(CompanyRepositoryImpl.class);
  private final DbConProperty dbConProperty;

  public CountryRepositoryImpl(DbConProperty dbConProperty) {
    this.dbConProperty = dbConProperty;
  }


  @Override
  public void updateCountryCodeById(String countryCode, Integer countryId) throws ClassNotFoundException, SQLException {
    Connection con = null;
    PreparedStatement pstUpdate = null;
    String qryUpdate = "UPDATE Country Set CountryCode = ? WHERE CountryId = ?";

    Class.forName(dbConProperty.getDriverClassName());
    con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());

    //delete table UserRole registration
    pstUpdate = con.prepareStatement(qryUpdate);
    pstUpdate.setString(1, countryCode);
    pstUpdate.setInt(2, countryId);
    pstUpdate.execute();

    if(pstUpdate != null) pstUpdate.close();
    if(con != null) con.close();
  }

  @Override
  public void updateCountryNameById(String countryName, Integer countryId) throws ClassNotFoundException, SQLException {
    Connection con = null;
    PreparedStatement pstUpdate = null;
    String qryUpdate = "UPDATE Country Set CountryName = ? WHERE CountryId = ?";

    Class.forName(dbConProperty.getDriverClassName());
    con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());

    //delete table UserRole registration
    pstUpdate = con.prepareStatement(qryUpdate);
    pstUpdate.setString(1, countryName);
    pstUpdate.setInt(2, countryId);
    pstUpdate.execute();

    if(pstUpdate != null) pstUpdate.close();
    if(con != null) con.close();
  }


  @Override
  public void updateLabelById(String labelData,String labelName, Integer countryId) throws ClassNotFoundException, SQLException {
    Connection con = null;
    PreparedStatement pstUpdate = null;
    String qryUpdate = "UPDATE Country Set "+ labelName+ " = ? WHERE CountryId = ?";

    Class.forName(dbConProperty.getDriverClassName());
    con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());

    //delete table UserRole registration
    pstUpdate = con.prepareStatement(qryUpdate);
    pstUpdate.setString(1, labelData);
    pstUpdate.setInt(2, countryId);
    pstUpdate.execute();

    if(pstUpdate != null) pstUpdate.close();
    if(con != null) con.close();
  }

  @Override
  public List<CountryEntity> findAllDistinctRules() {
    Connection con = null;
    Statement stmt = null;
    ResultSet rs = null;
    String query = String.format("SELECT DISTINCT CountryId , CountryName FROM Country");
    List<CountryEntity> country = new ArrayList<>();

    logger.debug("=========================== Find Coutnry DISTINCT ===========================");
    logger.debug("Query       : " + query);

    try{
      Class.forName(dbConProperty.getDriverClassName());
      con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());
      stmt = con.createStatement();
      rs = stmt.executeQuery(query);

      if(rs.next()){
        do{
          CountryEntity countryEntity = new CountryEntity();
          countryEntity.setCountryId(rs.getInt("CountryId"));
          countryEntity.setCountryName(rs.getString("CountryName"));
          country.add(countryEntity);
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
    logger.debug("=========================== Find Country DISTINCT ===========================");

    return country;
  }

  @Override
  public void add(CountryEntity country) throws ClassNotFoundException, SQLException {
    Connection con = null;
    PreparedStatement pstmt = null;

    String query = String.format("INSERT INTO Country (CountryCode,CountryName,label1,label2,label3,label4,label5,label6,label7,label8,label9,label10,label11,label12,label13,label14,label15,label16,label17,label18,label19,label20,label21,label22,label23,label24,label25,label26,label27,label28,label29,label30,label31) " +
        "VALUES (?, ?,?,?, ?,?,?, ?,?,?, ?,?,?, ?,?,?, ?,?,?, ?,?,?, ?,?,?, ?,?,?, ?,?,?, ?,?)");

    Class.forName(dbConProperty.getDriverClassName());
    con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());

    pstmt = con.prepareStatement(query);
    pstmt.setString(1, country.getCountryCode());
    pstmt.setString(2, country.getCountryName());
    pstmt.setString(3, country.getLabel1());
    pstmt.setString(4, country.getLabel2());
    pstmt.setString(5, country.getLabel3());
    pstmt.setString(6, country.getLabel4());
    pstmt.setString(7, country.getLabel5());
    pstmt.setString(8, country.getLabel6());
    pstmt.setString(9, country.getLabel7());
    pstmt.setString(10, country.getLabel8());
    pstmt.setString(11, country.getLabel9());
    pstmt.setString(12, country.getLabel10());
    pstmt.setString(13, country.getLabel11());
    pstmt.setString(14, country.getLabel12());
    pstmt.setString(15, country.getLabel13());
    pstmt.setString(16, country.getLabel14());
    pstmt.setString(17, country.getLabel15());
    pstmt.setString(18, country.getLabel16());
    pstmt.setString(19, country.getLabel17());
    pstmt.setString(20, country.getLabel18());
    pstmt.setString(21, country.getLabel19());
    pstmt.setString(22, country.getLabel20());
    pstmt.setString(23, country.getLabel21());
    pstmt.setString(24, country.getLabel22());
    pstmt.setString(25, country.getLabel23());
    pstmt.setString(26, country.getLabel24());
    pstmt.setString(27, country.getLabel25());
    pstmt.setString(28, country.getLabel26());
    pstmt.setString(29, country.getLabel27());
    pstmt.setString(30, country.getLabel28());
    pstmt.setString(31, country.getLabel29());
    pstmt.setString(32, country.getLabel30());
    pstmt.setString(33, country.getLabel31());


    pstmt.execute();

    if(con != null) con.close();
    if(pstmt != null) pstmt.close();
  }

  @Override
  public void delete(String countryId) throws ClassNotFoundException, SQLException {
    Connection con = null;
    PreparedStatement pstDelete = null;
    String qryDelete = "DELETE FROM Country WHERE CountryCode = ? AND CountryName = ?";

    String[] countryText = countryId.split(",");
    String countryCode  = countryText[0];
    String countryName = countryText[1];

    Class.forName(dbConProperty.getDriverClassName());
    con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());

    //delete table UserRole registration
    pstDelete = con.prepareStatement(qryDelete);
    pstDelete.setString(1, countryCode);
    pstDelete.setString(2, countryName);
    pstDelete.execute();

    if(pstDelete != null) pstDelete.close();
    if(con != null) con.close();
  }

  @Override
  public Map<String, Object> findCountryForPagination(String countryCode, String countryName,  Integer pageNumber, Integer size) {
    Connection con = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    Map<String, Object> map = new HashMap<>();
    List<CountryEntity> countries = new ArrayList<>();
    Integer totalCount = 0;

    try{
      Class.forName(dbConProperty.getDriverClassName());
      con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());
      pst = con.prepareStatement("EXECUTE Usp_FindCountryForPagination ?, ?, ?, ?");
      pst.setString(1, countryCode);
      pst.setString(2, countryName);
      pst.setInt(3, pageNumber);
      pst.setInt(4, size);
      rs = pst.executeQuery();

      if(rs.next()){

        totalCount = rs.getInt("TotalCount");

        do{
          CountryEntity country = new CountryEntity();
          country.setCountryId(rs.getInt("CountryId"));
          country.setCountryCode(rs.getString("CountryCode"));
          country.setCountryName(rs.getString("CountryName"));
          country.setApprovalRulesId(rs.getString("ApprovalRulesId"));
          country.setDataTimeStamp(rs.getString("Data_Time_Stamp"));
          countries.add(country);
        }while (rs.next());
      }
      map.put("countries", countries);
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
  public CountryEntity findbyId(Integer countryId) {
    Connection con = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    CountryEntity country = new CountryEntity();
    String query = String.format("SELECT CountryId,CountryCode,CountryName,label1,label2,label3,label4,label5,label6,label7,label8,label9,label10," +
        "label11,label12,label13,label14,label15,label16,label17,label18,label19,label20," +
        "label21,label22,label23,label24,label25,label26,label27,label28,label29,label30,label31,ApprovalRulesId,Data_Time_Stamp FROM Country WHERE CountryId = '%s'", countryId);


    try{
      Class.forName(dbConProperty.getDriverClassName());
      con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());
      pst = con.prepareStatement(query);
      rs = pst.executeQuery();

      if(rs.next()){
        do{
          country.setCountryId(rs.getInt("CountryId"));
          country.setCountryCode(rs.getString("CountryCode"));
          country.setCountryName(rs.getString("CountryName"));
          country.setApprovalRulesId(rs.getString("ApprovalRulesId"));
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
          country.setDataTimeStamp(rs.getString("Data_Time_Stamp"));

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
}
