package id.co.map.spk.repositories.impl;

import id.co.map.spk.entities.SpkStatusEntity;
import id.co.map.spk.repositories.SpkStatusRepository;
import id.co.map.spk.utils.DbConProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class SpkStatusRepositoryImpl implements SpkStatusRepository {

    private static final Logger logger = LoggerFactory.getLogger(SpkStatusRepositoryImpl.class);
    private final DbConProperty dbConProperty;

    public SpkStatusRepositoryImpl(DbConProperty dbConProperty) {
        this.dbConProperty = dbConProperty;
    }

    @Override
    public List<SpkStatusEntity> findAll() {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String query = String.format("SELECT * FROM SpkStatus ");
        List<SpkStatusEntity> data = new ArrayList<>();

        logger.debug("=========================== Find All Status ===========================");
        logger.debug("Query       : " + query);
        logger.debug("=========================== Find All Status ===========================");


        try{
            Class.forName(dbConProperty.getDriverClassName());
            con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();

            if(rs.next()){
                do{
                    SpkStatusEntity stat = new SpkStatusEntity();
                    stat.setStatusName(rs.getString("StatusName"));
                    data.add(stat);
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
