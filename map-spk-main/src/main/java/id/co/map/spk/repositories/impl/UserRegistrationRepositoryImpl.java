package id.co.map.spk.repositories.impl;

import id.co.map.spk.entities.UserRegistrationEntity;
import id.co.map.spk.repositories.UserRegistrationRepository;
import id.co.map.spk.utils.DbConProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.*;

/**
 * @author Awie on 11/4/2019
 */
@Repository
public class UserRegistrationRepositoryImpl implements UserRegistrationRepository {

    private static final Logger logger = LoggerFactory.getLogger(UserRegistrationRepositoryImpl.class);
    private final DbConProperty dbConProperty;

    public UserRegistrationRepositoryImpl(DbConProperty dbConProperty) {
        this.dbConProperty = dbConProperty;
    }

    @Override
    public UserRegistrationEntity findByKey(String registrationKey) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String query = String.format("SELECT * FROM UserRegistration WHERE RegistrationKey = '%s'", registrationKey);
        UserRegistrationEntity registration = new UserRegistrationEntity();

        logger.debug("=========================== Find UserRegistration By Key ===========================");
        logger.debug("Query       : " + query);
        logger.debug("=========================== Find UserRegistration By Key ===========================");

        try{
            Class.forName(dbConProperty.getDriverClassName());
            con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();

            if(rs.next()){
                do{
                    registration.setRegistrationKey(rs.getString("RegistrationKey"));
                    registration.setUserName(rs.getString("UserName"));
                    registration.setFirstName(rs.getString("FirstName"));
                    registration.setLastName(rs.getString("LastName"));
                    registration.setRegistrationStatus(rs.getString("RegistrationStatus"));
                    registration.setRegisteredBy(rs.getString("RegisteredBy"));
                    registration.setActivatedDateTime(rs.getString("ActivatedDateTime"));
                    registration.setActivatedDateTime(rs.getString("RegistrationDateTime"));
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
        return registration;
    }

    @Override
    public UserRegistrationEntity findByUserName(String userName) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String query = String.format("SELECT TOP 1 * FROM UserRegistration WHERE UserName = '%s' ORDER BY RegistrationDateTime DESC", userName);
        UserRegistrationEntity registration = new UserRegistrationEntity();

        logger.debug("=========================== Find UserRegistration By Key ===========================");
        logger.debug("Query       : " + query);
        logger.debug("=========================== Find UserRegistration By Key ===========================");

        try{
            Class.forName(dbConProperty.getDriverClassName());
            con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();

            if(rs.next()){
                do{
                    registration.setRegistrationKey(rs.getString("RegistrationKey"));
                    registration.setUserName(rs.getString("UserName"));
                    registration.setFirstName(rs.getString("FirstName"));
                    registration.setLastName(rs.getString("LastName"));
                    registration.setRegistrationStatus(rs.getString("RegistrationStatus"));
                    registration.setRegisteredBy(rs.getString("RegisteredBy"));
                    registration.setActivatedDateTime(rs.getString("ActivatedDateTime"));
                    registration.setActivatedDateTime(rs.getString("RegistrationDateTime"));
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
        return registration;
    }
}
