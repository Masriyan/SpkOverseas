package id.co.map.spk.repositories.impl;

import id.co.map.spk.entities.ForgotPasswordEntity;
import id.co.map.spk.repositories.ForgotPasswordRepository;
import id.co.map.spk.utils.DbConProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.*;

/**
 * @author Awie on 3/2/2020
 */
@Repository
public class ForgotPasswordRepositoryImpl implements ForgotPasswordRepository {

    private static final Logger logger = LoggerFactory.getLogger(ForgotPasswordRepositoryImpl.class);
    private final DbConProperty dbConProperty;

    public ForgotPasswordRepositoryImpl(DbConProperty dbConProperty) {
        this.dbConProperty = dbConProperty;
    }

    @Override
    public ForgotPasswordEntity save(String userName, String email, String status) throws ClassNotFoundException, SQLException {

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String query = "EXECUTE Usp_AddForgotPassword ?, ?, ?";
        ForgotPasswordEntity entity = new ForgotPasswordEntity();

        Class.forName(dbConProperty.getDriverClassName());
        con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());
        pstmt = con.prepareStatement(query);
        pstmt.setString(1, userName);
        pstmt.setString(2, email);
        pstmt.setString(3, status);

        rs = pstmt.executeQuery();

        if(rs.next()){
            do {
                entity.setForgotPasswordKey(rs.getString("ForgotPasswordKey"));
                entity.setUserName(rs.getString("UserName"));
                entity.setEmail(rs.getString("Email"));
                entity.setForgotPasswordStatus(rs.getString("ForgotPasswordStatus"));
                entity.setUpdatedDateTime(rs.getString("UpdatedDateTime"));
                entity.setCreatedDateTime(rs.getString("CreatedDateTime"));
            }while (rs.next());
        }

        return entity;
    }

    @Override
    public ForgotPasswordEntity findByUserNameAndStatus(String userName, String status) throws ClassNotFoundException, SQLException {

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String query = "SELECT * FROM ForgotPassword WHERE UserName = ? AND ForgotPasswordStatus = ?";
        ForgotPasswordEntity entity = new ForgotPasswordEntity();

        Class.forName(dbConProperty.getDriverClassName());
        con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());
        pstmt = con.prepareStatement(query);
        pstmt.setString(1, userName);
        pstmt.setString(2, status);

        rs = pstmt.executeQuery();

        if(rs.next()){
            do {
                entity.setForgotPasswordKey(rs.getString("ForgotPasswordKey"));
                entity.setUserName(rs.getString("UserName"));
                entity.setEmail(rs.getString("Email"));
                entity.setForgotPasswordStatus(rs.getString("ForgotPasswordStatus"));
                entity.setUpdatedDateTime(rs.getString("UpdatedDateTime"));
                entity.setCreatedDateTime(rs.getString("CreatedDateTime"));
            }while (rs.next());
        }else {
            return null;
        }

        return entity;
    }

    @Override
    public void updateStatusByKey(String key, String status) throws ClassNotFoundException, SQLException {

        Connection con = null;
        PreparedStatement pstmt = null;
        String query = "UPDATE ForgotPassword SET ForgotPasswordStatus = ?, UpdatedDateTime = GETDATE() WHERE ForgotPasswordKey = ?";
        ForgotPasswordEntity entity = new ForgotPasswordEntity();

        Class.forName(dbConProperty.getDriverClassName());
        con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());
        pstmt = con.prepareStatement(query);
        pstmt.setString(1, status);
        pstmt.setString(2, key);
        pstmt.execute();
    }

    @Override
    public ForgotPasswordEntity findByKey(String key) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String query = "SELECT * FROM ForgotPassword WHERE forgotPasswordKey = ?";
        ForgotPasswordEntity entity = new ForgotPasswordEntity();

        Class.forName(dbConProperty.getDriverClassName());
        con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());
        pstmt = con.prepareStatement(query);
        pstmt.setString(1, key);

        rs = pstmt.executeQuery();

        if(rs.next()){
            do {
                entity.setForgotPasswordKey(rs.getString("ForgotPasswordKey"));
                entity.setUserName(rs.getString("UserName"));
                entity.setEmail(rs.getString("Email"));
                entity.setForgotPasswordStatus(rs.getString("ForgotPasswordStatus"));
                entity.setUpdatedDateTime(rs.getString("UpdatedDateTime"));
                entity.setCreatedDateTime(rs.getString("CreatedDateTime"));
            }while (rs.next());
        }else {
            return null;
        }

        return entity;
    }
}
