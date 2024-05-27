package id.co.map.spk.repositories.impl;

import id.co.map.spk.entities.CostCenterEntity;
import id.co.map.spk.repositories.CostCenterRepository;
import id.co.map.spk.utils.DbConProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CostCenterRepositoryImpl  implements CostCenterRepository {

    private static final Logger logger = LoggerFactory.getLogger(CostCenterRepositoryImpl.class);
    private final DbConProperty dbConProperty;

    public CostCenterRepositoryImpl(DbConProperty dbConProperty) {
        this.dbConProperty = dbConProperty;
    }

    @Override
    public List<CostCenterEntity> findByCompany(String companyId) {

        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String query = String.format("EXECUTE Usp_FindCostCenterByCompany '%s'", companyId);
        List<CostCenterEntity> costCenters = new ArrayList<>();

        try{
            Class.forName(dbConProperty.getDriverClassName());
            con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();

            if(rs.next()){
                do{
                    CostCenterEntity costCenter = new CostCenterEntity();
                    costCenter.setCostCenterId(rs.getString("CostCenterId"));
                    costCenter.setCompanyId(rs.getString("CompanyId"));
                    costCenter.setCostCenterName(rs.getString("CostCenterName"));
                    costCenter.setDataTimeStamp(rs.getString("DataTimeStamp"));
                    costCenters.add(costCenter);
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

        return costCenters;
    }

    @Override
    public List<CostCenterEntity> findByCompanyAndStore(String companyId, String storeId) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String query = String.format("EXECUTE Usp_FindCostCenterByCompanyAndStore '%s', '%s'", companyId, storeId);
        List<CostCenterEntity> costCenters = new ArrayList<>();

        try{
            Class.forName(dbConProperty.getDriverClassName());
            con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();

            if(rs.next()){
                do{
                    CostCenterEntity costCenter = new CostCenterEntity();
                    costCenter.setCostCenterId(rs.getString("CostCenterId"));
                    costCenter.setCompanyId(rs.getString("CompanyId"));
                    costCenter.setCostCenterName(rs.getString("CostCenterName"));
                    costCenter.setDataTimeStamp(rs.getString("DataTimeStamp"));
                    costCenters.add(costCenter);
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

        return costCenters;
    }

    @Override
    public CostCenterEntity findByIdAndCompany(String costCenterId, String companyId) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        CostCenterEntity costCenter = new CostCenterEntity();
        String query = String.format("SELECT CostCenterId, CompanyId, CostCenterName, DataTimeStamp FROM CostCenter WHERE CostCenterId = '%s' AND CompanyId = '%s'", costCenterId, companyId);

        logger.debug("=========================== Find CostCenter By CostCenterId and CompanyId ===========================");
        logger.debug("Query       : " + query);
        logger.debug("=========================== Find CostCenter By CostCenterId and CompanyId ===========================");

        try{
            Class.forName(dbConProperty.getDriverClassName());
            con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();

            if(rs.next()){
                do{
                    costCenter.setCostCenterId(rs.getString("CostCenterId"));
                    costCenter.setCompanyId(rs.getString("Companyid"));
                    costCenter.setCostCenterName(rs.getString("CostCenterName"));
                    costCenter.setDataTimeStamp(rs.getString("DataTimeStamp"));
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

        return costCenter;
    }
}
