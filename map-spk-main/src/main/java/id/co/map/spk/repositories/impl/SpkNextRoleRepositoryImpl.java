package id.co.map.spk.repositories.impl;

import id.co.map.spk.repositories.SpkNextRoleRepository;
import id.co.map.spk.utils.DbConProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Awie on 11/18/2019
 */
@Service
public class SpkNextRoleRepositoryImpl implements SpkNextRoleRepository {

    private static final Logger logger = LoggerFactory.getLogger(SpkRepositoryImpl.class);
    private final DbConProperty dbConProperty;

    public SpkNextRoleRepositoryImpl(DbConProperty dbConProperty) {
        this.dbConProperty = dbConProperty;
    }

    @Override
    public void insert(String spkId, Integer nextRoleId) throws ClassNotFoundException, SQLException {
        Connection con = null;
        Statement stm = null;

        String query = String.format("INSERT INTO SpkNextRole (SpkId, NextRoleId) VALUES ('%s', %s)", spkId, nextRoleId);

        logger.debug("=========================== Insert SpkNextRole ===========================");
        logger.debug("Query       : " + query);
        logger.debug("=========================== Insert SpkNextRole ===========================");

        Class.forName(dbConProperty.getDriverClassName());
        con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());

        stm = con.createStatement();
        stm.execute(query);

        if(con != null) con.close();
        if(stm != null) stm.close();
    }

    @Override
    public void update(String spkId, Integer nextRoleId){

        Connection con = null;
        Statement stm = null;

        try{
            String query = String.format("UPDATE SpkNextRole SET NextRoleId = '%s' , DataTimeStamp = GETDATE() WHERE SpkId =  '%s'", nextRoleId, spkId);

            logger.debug("=========================== Update SpkNextRole ===========================");
            logger.debug("Query       : " + query);
            logger.debug("=========================== Update SpkNextRole ===========================");

            Class.forName(dbConProperty.getDriverClassName());
            con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());

            stm = con.createStatement();
            stm.execute(query);
            if(con != null) con.close();
            if(stm != null) stm.close();

        }catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
    }
}
