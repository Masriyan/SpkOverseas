package id.co.map.spk.repositories.impl;

import id.co.map.spk.entities.UserCompanyEntity;
import id.co.map.spk.repositories.UserCompanyRepository;
import id.co.map.spk.utils.DbConProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Awie on 11/26/2019
 */
@Repository
public class UserCompanyRepositoryImpl implements UserCompanyRepository {

    private static final Logger logger = LoggerFactory.getLogger(SpkRepositoryImpl.class);
    private final DbConProperty dbConProperty;

    public UserCompanyRepositoryImpl(DbConProperty dbConProperty) {
        this.dbConProperty = dbConProperty;
    }

    @Override
    public List<UserCompanyEntity> findByUserName(String userName) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String query = String.format("SELECT * FROM UserCompany WHERE UserName = '%s'", userName);
        List<UserCompanyEntity> data = new ArrayList<>();

        logger.debug("=========================== Find UserCompany By UserName ===========================");
        logger.debug("Query       : " + query);
        logger.debug("=========================== Find UserCompany By UserName ===========================");


        try{
            Class.forName(dbConProperty.getDriverClassName());
            con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();

            if(rs.next()){
                do{
                    UserCompanyEntity userCompanyEntity = new UserCompanyEntity();
                    userCompanyEntity.setUserCompanyId(rs.getInt("UserCompanyId"));
                    userCompanyEntity.setUserName(rs.getString("UserName"));
                    userCompanyEntity.setCompanyId(rs.getString("CompanyId"));
                    userCompanyEntity.setSbuId(rs.getString("SbuId"));
                    userCompanyEntity.setDataTimeStamp(rs.getString("DataTimeStamp"));
                    data.add(userCompanyEntity);
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

        return data;
    }
}
