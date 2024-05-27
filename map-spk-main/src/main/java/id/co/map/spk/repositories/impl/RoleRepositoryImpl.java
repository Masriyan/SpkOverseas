package id.co.map.spk.repositories.impl;

import id.co.map.spk.entities.RoleEntity;
import id.co.map.spk.repositories.RoleRepository;
import id.co.map.spk.utils.DbConProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Awie on 10/31/2019
 */
@Repository
public class RoleRepositoryImpl implements RoleRepository {

    private static final Logger logger = LoggerFactory.getLogger(RoleRepositoryImpl.class);
    private final DbConProperty dbConProperty;

    public RoleRepositoryImpl(DbConProperty dbConProperty) {
        this.dbConProperty = dbConProperty;
    }

    @Override
    public List<RoleEntity> findAll() {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String query = "SELECT * FROM AppRole";
        List<RoleEntity> roles = new ArrayList<>();

        try{
            Class.forName(dbConProperty.getDriverClassName());
            con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();

            if(rs.next()){
                do{
                    RoleEntity role = new RoleEntity();
                    role.setRoleId(rs.getLong("RoleId"));
                    role.setRoleName(rs.getString("RoleName").substring(5));
                    role.setDataTimeStamp(rs.getString("DataTimeStamp"));
                    roles.add(role);
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

        return roles;
    }

    @Override
    public List<RoleEntity> findByUserName(String username) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String query ="SELECT AR.* \n" +
                "FROM AppRole AR\n" +
                "INNER JOIN UserRole UR ON AR.RoleId = UR.RoleId\n" +
                "WHERE UR.UserName = ?";
        List<RoleEntity> roles = new ArrayList<>();

        try{
            Class.forName(dbConProperty.getDriverClassName());
            con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());
            pst = con.prepareStatement(query);
            pst.setString(1, username);
            rs = pst.executeQuery();

            if(rs.next()){
                do{
                    RoleEntity role = new RoleEntity();
                    role.setRoleId(rs.getLong("RoleId"));
                    role.setRoleName(rs.getString("RoleName").substring(5));
                    role.setDataTimeStamp(rs.getString("DataTimeStamp"));
                    roles.add(role);
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

        return roles;
    }
}
