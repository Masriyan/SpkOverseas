package id.co.map.spk.repositories.impl;

import id.co.map.spk.entities.UserRoleEntity;
import id.co.map.spk.repositories.UserRoleRepository;
import id.co.map.spk.utils.DbConProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Awie on 8/27/2019
 */
@Repository
public class UserRoleRepositoryImpl implements UserRoleRepository {

    private static final Logger logger = LoggerFactory.getLogger(UserRoleRepositoryImpl.class);
    private final DbConProperty dbConProperty;

    public UserRoleRepositoryImpl(DbConProperty dbConProperty) {
        this.dbConProperty = dbConProperty;
    }

    @Override
    public List<UserRoleEntity> findByUser(String userName) {

        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        List<UserRoleEntity> userRoles = new ArrayList<>();
        String query = String.format("SELECT UserRoleId, UserName, RoleId, DataTimeStamp FROM UserRole WHERE UserName = '%s'", userName);

        logger.debug("=========================== Find UserRole By User ===========================");
        logger.debug("Query       : " + query);
        logger.debug("=========================== Find UserRole By User ===========================");

        try{
            Class.forName(dbConProperty.getDriverClassName());
            con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();

            if(rs.next()){
                do{
                    UserRoleEntity userRole = new UserRoleEntity();
                    userRole.setUserRoleId(rs.getInt("UserRoleId"));
                    userRole.setUserName(rs.getString("UserName"));
                    userRole.setRoleId(rs.getInt("RoleId"));
                    userRole.setDataTimeStamp(rs.getString("DataTimeStamp"));
                    userRoles.add(userRole);
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

        return userRoles;
    }

    @Override
    public List<UserRoleEntity> findByRoleId(Integer roleId) {

        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        List<UserRoleEntity> userRoles = new ArrayList<>();
        String query = String.format("SELECT UserRoleId, UserName, RoleId, DataTimeStamp FROM UserRole WHERE RoleId = '%s'", roleId);

        logger.debug("=========================== Find UserRole By RoleId ===========================");
        logger.debug("Query       : " + query);
        logger.debug("=========================== Find UserRole By RoleId ===========================");

        try{
            Class.forName(dbConProperty.getDriverClassName());
            con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();

            if(rs.next()){
                do{
                    UserRoleEntity userRole = new UserRoleEntity();
                    userRole.setUserRoleId(rs.getInt("UserRoleId"));
                    userRole.setUserName(rs.getString("UserName"));
                    userRole.setRoleId(rs.getInt("RoleId"));
                    userRole.setDataTimeStamp(rs.getString("DataTimeStamp"));
                    userRoles.add(userRole);
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

        return userRoles;
    }

    @Override
    public void updateUserRole(Integer roleId, String username) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement pstUpdate = null;
        String qryUpdate = "UPDATE UserRole Set RoleId = ?, DataTimeStamp = GETDATE() WHERE UserName = ?";

        Class.forName(dbConProperty.getDriverClassName());
        con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());

        //delete table UserRole registration
        pstUpdate = con.prepareStatement(qryUpdate);
        pstUpdate.setInt(1, roleId);
        pstUpdate.setString(2, username);
        pstUpdate.execute();

        if(pstUpdate != null) pstUpdate.close();
        if(con != null) con.close();
    }
}
