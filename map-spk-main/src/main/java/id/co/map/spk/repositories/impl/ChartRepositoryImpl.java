package id.co.map.spk.repositories.impl;

import id.co.map.spk.model.chart.SpkCompany;
import id.co.map.spk.repositories.ChartRepository;
import id.co.map.spk.utils.DbConProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Awie on 11/19/2019
 */
@Repository
public class ChartRepositoryImpl implements ChartRepository {

    private static final Logger logger = LoggerFactory.getLogger(ChartRepositoryImpl.class);
    private final DbConProperty dbConProperty;

    public ChartRepositoryImpl(DbConProperty dbConProperty) {
        this.dbConProperty = dbConProperty;
    }

    @Override
    public List<SpkCompany> getPendingSpkGroupedByCompany(String userName) {

        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String query = String.format("EXECUTE Usp_GetTotalOnProcessSpkByUserName '%s'", userName);
        List<SpkCompany> spkCompanies = new ArrayList<>();

        logger.debug("=========================== Get Total On Process Spk By UserName ===========================");
        logger.debug("Query       : " + query);
        logger.debug("=========================== Get Total On Process Spk By UserName ===========================");

        try{
            Class.forName(dbConProperty.getDriverClassName());
            con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();

            if(rs.next()){
                do{
                    SpkCompany spkCompany = new SpkCompany(
                            rs.getString("CompanyId"),
                            rs.getInt("NumberOfSpk")
                    );
                    spkCompanies.add(spkCompany);
                }while (rs.next());
            }
        }catch (ClassNotFoundException ce){
            logger.error("ClassNotFoundException : " + ce.getMessage());
        } catch (SQLException se){
            logger.error("SQLException : " + se.getMessage());
        } finally {
            try { con.close(); }catch (SQLException e){}
            try { pst.close(); }catch (SQLException e){}
            try {rs.close(); }catch (SQLException e){}
        }

        return spkCompanies;
    }
}
