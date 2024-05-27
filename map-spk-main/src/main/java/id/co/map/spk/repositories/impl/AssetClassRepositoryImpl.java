package id.co.map.spk.repositories.impl;

import id.co.map.spk.entities.AssetClassEntity;
import id.co.map.spk.repositories.AssetClassRepository;
import id.co.map.spk.utils.DbConProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AssetClassRepositoryImpl implements AssetClassRepository {

    private static final Logger logger = LoggerFactory.getLogger(AssetClassRepositoryImpl.class);
    private final DbConProperty dbConProperty;

    public AssetClassRepositoryImpl(DbConProperty dbConProperty) {
        this.dbConProperty = dbConProperty;
    }

    @Override
    public List<AssetClassEntity> findAllAssetClass() {

        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String query = "EXECUTE Usp_FindAllAssetClass";
        List<AssetClassEntity> assets = new ArrayList<>();

        logger.debug("=========================== Find All Asset Class ===========================");
        logger.debug("Query       : " + query);
        logger.debug("=========================== Find All Asset Class ===========================");

        try{
            Class.forName(dbConProperty.getDriverClassName());
            con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();

            if(rs.next()){
                do{
                    AssetClassEntity asset = new AssetClassEntity(
                            rs.getString("AssetClassId"),
                            rs.getString("AssetTypeId"),
                            rs.getString("AssetClassName"),
                            rs.getString("DataTimeStamp"));
                    assets.add(asset);
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

        return assets;
    }

    @Override
    public AssetClassEntity findById(String assetClassId) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        AssetClassEntity assetClass = new AssetClassEntity();
        String query = String.format("SELECT AssetClassId, AssetTypeId, AssetClassName, DataTimeStamp FROM AssetClass WHERE  AssetClassId = '%s'", assetClassId);

        logger.debug("=========================== Find AssetClass By AssetClassId ===========================");
        logger.debug("Query       : " + query);
        logger.debug("=========================== Find AssetClass By AssetClassId ===========================");

        try{
            Class.forName(dbConProperty.getDriverClassName());
            con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();

            if(rs.next()){
                do{
                    assetClass.setAssetClassId(rs.getString("AssetClassId"));
                    assetClass.setAssetTypeId(rs.getString("AssetTypeId"));
                    assetClass.setAssetClassName(rs.getString("AssetClassName"));
                    assetClass.setDataTimeStamp(rs.getString("DataTimeStamp"));
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

        return assetClass;
    }

    @Override
    public List<AssetClassEntity> findSameType(String assetClassId) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        List<AssetClassEntity> assets = new ArrayList<>();
        String query = String.format("EXECUTE Usp_FindSameTypeAsset '%s'", assetClassId);

        logger.debug("=========================== Find Same Type AssetClass ===========================");
        logger.debug("Query       : " + query);
        logger.debug("=========================== Find Same Type AssetClass ===========================");

        try{
            Class.forName(dbConProperty.getDriverClassName());
            con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();

            if(rs.next()){
                do{
                    AssetClassEntity asset = new AssetClassEntity();
                    asset.setAssetClassId(rs.getString("AssetClassId"));
                    asset.setAssetTypeId(rs.getString("AssetTypeId"));
                    asset.setAssetClassName(rs.getString("AssetClassName"));
                    asset.setDataTimeStamp(rs.getString("DataTimeStamp"));
                    assets.add(asset);
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

        return assets;
    }
}
