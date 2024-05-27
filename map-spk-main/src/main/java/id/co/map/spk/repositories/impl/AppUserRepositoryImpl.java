package id.co.map.spk.repositories.impl;

import id.co.map.spk.entities.AppUserEntity;
import id.co.map.spk.entities.CompanyEntity;
import id.co.map.spk.entities.RoleEntity;
import id.co.map.spk.entities.SbuEntity;
import id.co.map.spk.enums.RegistrationStatus;
import id.co.map.spk.model.AppUser;
import id.co.map.spk.model.UserPagination;
import id.co.map.spk.repositories.AppUserRepository;
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
public class AppUserRepositoryImpl implements AppUserRepository {

    private static final Logger logger = LoggerFactory.getLogger(AssetClassRepositoryImpl.class);
    private final DbConProperty dbConProperty;

    public AppUserRepositoryImpl(DbConProperty dbConProperty) {
        this.dbConProperty = dbConProperty;
    }

    @Override
    public AppUserEntity findByUsername(String userName) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String query = String.format("SELECT * FROM AppUser WHERE UserName = '%s'", userName);
        AppUserEntity user = new AppUserEntity();

        logger.debug("=========================== Find AppUser By UserName ===========================");
        logger.debug("Query       : " + query);
        logger.debug("=========================== Find AppUser By UserName ===========================");

        try{
            Class.forName(dbConProperty.getDriverClassName());
            con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();

            if(rs.next()){
                do{

                    user.setUserName(rs.getString("UserName"));
                    user.setFirstName(rs.getString("FirstName"));
                    user.setLastName(rs.getString("LastName"));
                    user.setEmail(rs.getString("Email"));
                    user.setEncryptedPassword(rs.getString("EncryptedPassword"));
                    user.setEnbaled(rs.getBoolean("Enabled"));
                    user.setDataTimeStamp(rs.getString("DataTimeStamp"));
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

        return user;
    }

    @Override
    public List<AppUserEntity> findByCompanyAndRole(String company, Integer roleId) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String query = String.format("EXECUTE Usp_FindAppUserByCompanyAndRole '%s', %s", company, roleId);
        List<AppUserEntity> users = new ArrayList<>();

        logger.debug("=========================== Find AppUser By Company and Role ===========================");
        logger.debug("Query       : " + query);
        logger.debug("=========================== Find AppUser By Company and Role ===========================");

        try{
            Class.forName(dbConProperty.getDriverClassName());
            con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();

            if(rs.next()){
                do{
                    AppUserEntity user = new AppUserEntity();
                    user.setUserName(rs.getString("UserName"));
                    user.setFirstName(rs.getString("FirstName"));
                    user.setLastName(rs.getString("LastName"));
                    user.setEmail(rs.getString("Email"));
                    user.setEncryptedPassword(rs.getString("EncryptedPassword"));
                    user.setEnbaled(rs.getBoolean("Enabled"));
                    user.setDataTimeStamp(rs.getString("DataTimeStamp"));
                    users.add(user);
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

        return users;
    }

    @Override
    public List<AppUserEntity> findBySbuAndRole(String sbu, Integer roleId) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String query = String.format("EXECUTE Usp_FindAppUserBySbuAndRole '%s', %s", sbu, roleId);
        List<AppUserEntity> users = new ArrayList<>();

        logger.debug("=========================== Find AppUser By Sbu and Role ===========================");
        logger.debug("Query       : " + query);
        logger.debug("=========================== Find AppUser By Sbu and Role ===========================");

        try{
            Class.forName(dbConProperty.getDriverClassName());
            con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();

            if(rs.next()){
                do{
                    AppUserEntity user = new AppUserEntity();
                    user.setUserName(rs.getString("UserName"));
                    user.setFirstName(rs.getString("FirstName"));
                    user.setLastName(rs.getString("LastName"));
                    user.setEmail(rs.getString("Email"));
                    user.setEncryptedPassword(rs.getString("EncryptedPassword"));
                    user.setEnbaled(rs.getBoolean("Enabled"));
                    user.setDataTimeStamp(rs.getString("DataTimeStamp"));
                    users.add(user);
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

        return users;
    }

    @Override
    public List<AppUserEntity> findRejectedUser(String spkId) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String query = String.format("SELECT AU.UserName , AU.FirstName , AU.LastName , AU.Email, AU.EncryptedPassword, AU.Enabled, AU.DataTimeStamp \n" +
                "FROM AppUser AU \n" +
                "INNER JOIN SpkHistory SH ON AU.UserName = SH.UserName\n" +
                "WHERE SH.SpkId = '%s' AND StatusName <> 'REJECTED'" +
                "group by AU.UserName , AU.FirstName , AU.LastName , AU.Email, AU.EncryptedPassword, AU.Enabled, AU.DataTimeStamp", spkId  );
        List<AppUserEntity> users = new ArrayList<>();

        logger.debug("=========================== Find Rejected AppUser ===========================");
        logger.debug("Query       : " + query);
        logger.debug("=========================== Find Rejected AppUser ===========================");

        try{
            Class.forName(dbConProperty.getDriverClassName());
            con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();

            if(rs.next()){
                do{
                    AppUserEntity user = new AppUserEntity();
                    user.setUserName(rs.getString("UserName"));
                    user.setFirstName(rs.getString("FirstName"));
                    user.setLastName(rs.getString("LastName"));
                    user.setEmail(rs.getString("Email"));
                    user.setEncryptedPassword(rs.getString("EncryptedPassword"));
                    user.setEnbaled(rs.getBoolean("Enabled"));
                    user.setDataTimeStamp(rs.getString("DataTimeStamp"));
                    users.add(user);
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

        return users;
    }

    @Override
    public void add(AppUser newUser, String registeredBy) throws ClassNotFoundException, SQLException {

        Connection con = null;
        PreparedStatement pstRegistration = null, pstUser = null, pstUserRole = null, pstUserCompany = null;
        String qryRegistration = "INSERT INTO UserRegistration (UserName, FirstName, LastName, RegistrationStatus, RegisteredBy) VALUES (?, ?, ?, ?, ?)";
        String qryUser = "INSERT INTO AppUser (Username, FirstName, LastName, Email, EncryptedPassword, Enabled, DataTimeStamp) VALUES(?, ?, ?, ?, ?, ?, ?)";
        String qryUserRole= "INSERT INTO UserRole (Username, RoleId, DataTimeStamp) VALUES(?, ?, ?)";
        //String qryUserCompanyQuery = "INSERT INTO UserCompany (Username, CompanyId, DataTimeStamp, SbuId) VALUES(?, ?, ?, ?)";
        String qryUserCompanyQuery = "EXECUTE Usp_AddUserCompany ?, ?";
        String username = newUser.getUserName();
        String dataTimeStamp = newUser.getDataTimeStamp();

        Class.forName(dbConProperty.getDriverClassName());

        con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());
        con.setAutoCommit(false);

        //insert into table UserRegistration
        pstRegistration = con.prepareStatement(qryRegistration);
        pstRegistration.setString(1, newUser.getUserName());
        pstRegistration.setString(2, newUser.getFirstName());
        pstRegistration.setString(3, newUser.getLastName());
        pstRegistration.setString(4, RegistrationStatus.ACTIVE.name());
        pstRegistration.setString(5, registeredBy);
        pstRegistration.execute();

        //insert into table AppUser
        pstUser = con.prepareStatement(qryUser);
        pstUser.setString(1, username);
        pstUser.setString(2, newUser.getFirstName());
        pstUser.setString(3, newUser.getLastName());
        pstUser.setString(4, newUser.getEmail());
        pstUser.setString(5, "");
        pstUser.setString(6, "1");
        pstUser.setString(7, dataTimeStamp);
        pstUser.execute();

        //insert into table UserRole
        pstUserRole = con.prepareStatement(qryUserRole);
        for (RoleEntity role: newUser.getRoles()) {
            pstUserRole.setString(1, username);
            pstUserRole.setLong(2, role.getRoleId());
            pstUserRole.setString(3,dataTimeStamp);
            pstUserRole.addBatch();
        }

        pstUserRole.executeBatch();

        //insert into table UserCompany
        pstUserCompany = con.prepareStatement(qryUserCompanyQuery);
        for (SbuEntity sbu: newUser.getSbus()) {
            pstUserCompany.setString(1, username);
            pstUserCompany.setInt(2,sbu.getSbuId());
            pstUserCompany.addBatch();
        }

        pstUserCompany.executeBatch();

        //commit data
        con.commit();

        if(pstRegistration != null) pstUser.close();
        if(pstUser != null) pstUser.close();
        if(pstUserRole != null) pstUserRole.close();
        if(pstUserCompany != null) pstUserCompany.close();
        if(con != null) con.close();
    }

    @Override
    public void changePassword(String username, String encryptedPassword) throws ClassNotFoundException, SQLException {

        String query = "UPDATE AppUser SET EncryptedPassword = ? WHERE UserName = ?";
        Connection con = null;
        PreparedStatement pstmt = null;

        Class.forName(dbConProperty.getDriverClassName());
        con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());
        pstmt = con.prepareStatement(query);
        pstmt.setString(1, encryptedPassword);
        pstmt.setString(2, username);
        pstmt.execute();

        if(pstmt != null) pstmt.close();
        if(con != null) con.close();
    }

    @Override
    public Map<String, Object> findUserForPagination(String username, String name, Integer roleId, Integer pageNumber, Integer size) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        Map<String, Object> map = new HashMap<>();
        List<UserPagination> users = new ArrayList<>();
        Integer totalCount = 0;

        try{
            Class.forName(dbConProperty.getDriverClassName());
            con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());
            pst = con.prepareStatement("EXECUTE Usp_FindUserForPagination ?, ?, ?, ?, ?");
            pst.setString(1, username);
            pst.setString(2, name);
            pst.setInt(3, roleId);
            pst.setInt(4, pageNumber);
            pst.setInt(5, size);
            rs = pst.executeQuery();

            if(rs.next()){

                totalCount = rs.getInt("TotalCount");

                do{
                    UserPagination user = new UserPagination();
                    user.setUserName(rs.getString("UserName"));
                    user.setFirstName(rs.getString("FirstName"));
                    user.setLastName(rs.getString("LastName"));
                    user.setEmail(rs.getString("Email"));
                    user.setRoleId(rs.getInt("RoleId"));
                    user.setRoleName(rs.getString("RoleName"));

                    if(rs.getString("Enabled").equals("1")) user.setEnabled(true);
                    else user.setEnabled(false);

                    users.add(user);
                }while (rs.next());
            }
            map.put("users", users);
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
    public void activateUser(String registrationKey, String userName, String encryptedPassword) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement pstRegistration = null, pstUser = null;
        String qryRegistration = "UPDATE UserRegistration SET ActivatedDateTime = GETDATE(), RegistrationStatus = ? WHERE RegistrationKey = ?";
        String qryUser = "UPDATE AppUser SET EncryptedPassword = ?, Enabled = ? WHERE UserName = ?";

        Class.forName(dbConProperty.getDriverClassName());

        con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());
        con.setAutoCommit(false);

        //insert into table UserRegistration
        pstRegistration = con.prepareStatement(qryRegistration);
        pstRegistration.setString(1, RegistrationStatus.ACTIVE.name());
        pstRegistration.setString(2, registrationKey);
        pstRegistration.execute();

        //insert into table AppUser
        pstUser = con.prepareStatement(qryUser);
        pstUser.setString(1, encryptedPassword);
        pstUser.setString(2, "1");
        pstUser.setString(3, userName);
        pstUser.execute();

        //commit data
        con.commit();

        if(pstRegistration != null) pstUser.close();
        if(pstUser != null) pstUser.close();
        if(con != null) con.close();
    }

    @Override
    public void delete(String username) throws ClassNotFoundException, SQLException{
        Connection con = null;
        PreparedStatement pstRegistration = null, pstUser = null, pstUserRole = null, pstUserCompany = null;
        String qryRegistration = "DELETE FROM UserRegistration WHERE UserName = ?";
        String qryUser = "DELETE FROM AppUser WHERE UserName = ?";
        String qryUserRole= "DELETE FROM UserRole WHERE UserName = ?";
        String qryUserCompanyQuery = "DELETE FROM UserCompany WHERE UserName = ?";

        Class.forName(dbConProperty.getDriverClassName());

        con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());
        con.setAutoCommit(false);

        //delete table user registration
        pstRegistration = con.prepareStatement(qryRegistration);
        pstRegistration.setString(1, username);
        pstRegistration.execute();

        //delete table appUser registration
        pstUser = con.prepareStatement(qryUser);
        pstUser.setString(1, username);
        pstUser.execute();

        //delete table UserRole registration
        pstUserRole = con.prepareStatement(qryUserRole);
        pstUserRole.setString(1, username);
        pstUserRole.execute();

        //delete table UserRole registration
        pstUserCompany = con.prepareStatement(qryUserCompanyQuery);
        pstUserCompany.setString(1, username);
        pstUserCompany.execute();

        con.commit();
        if(pstRegistration != null) pstUser.close();
        if(pstUser != null) pstUser.close();
        if(pstUserRole != null) pstUserRole.close();
        if(pstUserCompany != null) pstUserCompany.close();
        if(con != null) con.close();
    }

    @Override
    public void updateEmail(String email, String username) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement pstUpdate = null;
        String qryUpdate = "UPDATE AppUser Set Email = ? WHERE UserName = ?";

        Class.forName(dbConProperty.getDriverClassName());
        con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());

        //delete table UserRole registration
        pstUpdate = con.prepareStatement(qryUpdate);
        pstUpdate.setString(1, email);
        pstUpdate.setString(2, username);
        pstUpdate.execute();

        if(pstUpdate != null) pstUpdate.close();
        if(con != null) con.close();
    }

    @Override
    public void removeCompany(String companyId, String username) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement pstUpdate = null;
        String qryRemove = "DELETE FROM UserCompany WHERE CompanyId = ? AND UserName = ?";

        Class.forName(dbConProperty.getDriverClassName());
        con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());

        //delete table UserRole registration
        pstUpdate = con.prepareStatement(qryRemove);
        pstUpdate.setString(1, companyId);
        pstUpdate.setString(2, username);
        pstUpdate.execute();

        if(pstUpdate != null) pstUpdate.close();
        if(con != null) con.close();
    }

    @Override
    public void removeSbu(String sbuId, String userName) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement pstUpdate = null;
        String qryRemove = "DELETE FROM UserCompany WHERE SbuId = ? AND UserName = ?";

        Class.forName(dbConProperty.getDriverClassName());
        con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());

        //delete table UserRole registration
        pstUpdate = con.prepareStatement(qryRemove);
        pstUpdate.setString(1, sbuId);
        pstUpdate.setString(2, userName);
        pstUpdate.execute();

        if(pstUpdate != null) pstUpdate.close();
        if(con != null) con.close();
    }

    @Override
    public void addNewCompanies(String userName, List<CompanyEntity> newCompanies, String currentDateTime) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        String query =
                "INSERT UserCompany (UserName, CompanyId, DataTimeStamp , SbuId) VALUES (?, ?, ?, ?)"               ;
        Class.forName(dbConProperty.getDriverClassName());

        con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());
        con.setAutoCommit(false);

        pstmt = con.prepareStatement(query);

        for(CompanyEntity company: newCompanies){
            pstmt.setString(1, userName);
            pstmt.setString(2, company.getCompanyId());
            pstmt.setString(3, currentDateTime);
            pstmt.setInt(4, 0);
            pstmt.addBatch();
        }

        pstmt.executeBatch();

        con.commit();
        if(pstmt != null) pstmt.close();
        if(con != null) con.close();
    }

    @Override
    public void addNewSbus(String userName, List<SbuEntity> newsbus) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement pstUserCompany = null;
        //String query = "INSERT UserCompany (UserName, SbuId, CompanyId, DataTimeStamp) VALUES (?, ?, ?)";
        String qryUserCompanyQuery = "EXECUTE Usp_AddUserCompany ?, ?";
        Class.forName(dbConProperty.getDriverClassName());

        con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());
        con.setAutoCommit(false);

        pstUserCompany = con.prepareStatement(qryUserCompanyQuery);
        for (SbuEntity sbu: newsbus) {
            pstUserCompany.setString(1, userName);
            pstUserCompany.setInt(2,sbu.getSbuId());
            pstUserCompany.addBatch();
        }

        pstUserCompany.executeBatch();

        con.commit();
        if(pstUserCompany != null) pstUserCompany.close();
        if(con != null) con.close();
    }

    @Override
    public AppUserEntity findByEmail(String email) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String query = String.format("SELECT * FROM AppUser WHERE Email = '%s'", email);
        AppUserEntity user = new AppUserEntity();

        logger.debug("=========================== Find AppUser By Email ===========================");
        logger.debug("Query       : " + query);
        logger.debug("=========================== Find AppUser By Email ===========================");

        try{
            Class.forName(dbConProperty.getDriverClassName());
            con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();

            if(rs.next()){
                do{
                    user.setUserName(rs.getString("UserName"));
                    user.setFirstName(rs.getString("FirstName"));
                    user.setLastName(rs.getString("LastName"));
                    user.setEmail(rs.getString("Email"));
                    user.setEncryptedPassword(rs.getString("EncryptedPassword"));
                    user.setEnbaled(rs.getBoolean("Enabled"));
                    user.setDataTimeStamp(rs.getString("DataTimeStamp"));
                }while (rs.next());
            }
            else{
                return null;
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

        return user;
    }
}
