package id.co.map.spk.repositories.impl;

import id.co.map.spk.entities.StoreEntity;
import id.co.map.spk.repositories.StoreRepository;
import id.co.map.spk.utils.DbConProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class StoreRepositoryImpl implements StoreRepository {

    private static final Logger logger = LoggerFactory.getLogger(StoreRepositoryImpl.class);
    private final DbConProperty dbConProperty;

    public StoreRepositoryImpl(DbConProperty dbConProperty) {
        this.dbConProperty = dbConProperty;
    }

    @Override
    public List<StoreEntity> findByCompany(String companyId) {

        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String query = String.format("EXECUTE Usp_FindStoreByCompany '%s'", companyId);
        List<StoreEntity> stores = new ArrayList<>();

        try{
            Class.forName(dbConProperty.getDriverClassName());
            con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();

            if(rs.next()){
                do{
                    StoreEntity store = new StoreEntity();
                    store.setStoreId(rs.getString("StoreId"));
                    store.setCompanyId(rs.getString("CompanyId"));
                    store.setStoreName(rs.getString("StoreName"));
                    store.setDataTimeStamp(rs.getString("DataTimeStamp"));
                    stores.add(store);
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

        return stores;
    }

    @Override
    public StoreEntity findByStoreIdAndCompanyId(String storeId, String companyId) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        StoreEntity store = new StoreEntity();
        String query = String.format("SELECT StoreId, CompanyId, StoreName, DataTimeStamp FROM Store WHERE StoreId = '%s' AND CompanyId = '%s'", storeId, companyId);

        logger.debug("=========================== Find Store By StoreId and CompanyId ===========================");
        logger.debug("Query       : " + query);
        logger.debug("=========================== Find Store By StoreId and CompanyId ===========================");

        try{
            Class.forName(dbConProperty.getDriverClassName());
            con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();

            if(rs.next()){
                do{
                    store.setStoreId(rs.getString("StoreID"));
                    store.setCompanyId(rs.getString("CompanyId"));
                    store.setStoreName(rs.getString("StoreName"));
                    store.setDataTimeStamp(rs.getString("DataTimeStamp"));
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

        return store;
    }

    @Override
    public List<StoreEntity> findByUsername(String username) {

        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String query = String.format("EXECUTE Usp_FindStoreByUsername '%s'", username);
        List<StoreEntity> stores = new ArrayList<>();

        try{
            Class.forName(dbConProperty.getDriverClassName());
            con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();

            if(rs.next()){
                do{
                    StoreEntity store = new StoreEntity();
                    store.setStoreId(rs.getString("StoreId"));
                    store.setCompanyId(rs.getString("CompanyId"));
                    store.setStoreName(rs.getString("StoreName"));
                    store.setDataTimeStamp(rs.getString("DataTimeStamp"));
                    stores.add(store);
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

        return stores;
    }
}
