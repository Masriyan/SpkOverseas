package id.co.map.spk.repositories.impl;

import id.co.map.spk.entities.VendorEntity;
import id.co.map.spk.repositories.VendorRepository;
import id.co.map.spk.utils.DbConProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class VendorRepositoryImpl implements VendorRepository {

    private static final Logger logger = LoggerFactory.getLogger(VendorRepositoryImpl.class);
    private final DbConProperty dbConProperty;

    public VendorRepositoryImpl(DbConProperty dbConProperty) {
        this.dbConProperty = dbConProperty;
    }

    @Override
    public List<VendorEntity> findByUserAndCompany(String userName, String companyId) {

        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String query = String.format("EXECUTE Usp_FindVendorByUserAndCompany '%s', '%s'", userName, companyId);
        List<VendorEntity> vendors = new ArrayList<>();

        try{
            Class.forName(dbConProperty.getDriverClassName());
            con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();

            if(rs.next()){
                do{
                    VendorEntity vendor = new VendorEntity();
                    vendor.setVendorId(rs.getString("VendorId"));
                    vendor.setCompanyId(rs.getString("CompanyId"));
                    vendor.setVendorName(rs.getString("VendorName"));
                    vendor.setStreet(rs.getString("Street"));
                    vendor.setCity(rs.getString("City"));
                    vendor.setNpwp(rs.getString("Npwp"));
                    vendor.setTelephone(rs.getString("Telephone"));
                    vendor.setDataTimeStamp(rs.getString("DataTimeStamp"));
                    vendors.add(vendor);
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

        return vendors;
    }

    @Override
    public VendorEntity findByVendorIdAndCompanyId(String vendorId, String companyId) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        VendorEntity vendor = new VendorEntity();
        String query = String.format("SELECT VendorId, CompanyId, VendorName, Street, City, Npwp, Telephone, DataTimeStamp FROM Vendor WHERE VendorId = '%s' AND CompanyId = '%s'", vendorId, companyId);

        logger.debug("=========================== Find Vendor By VendorId ===========================");
        logger.debug("Query       : " + query);
        logger.debug("=========================== Find Vendor By CompanyId ===========================");

        try{
            Class.forName(dbConProperty.getDriverClassName());
            con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();

            if(rs.next()){
                do{
                    vendor.setVendorId(rs.getString("VendorId"));
                    vendor.setCompanyId(rs.getString("CompanyId"));
                    vendor.setVendorName(rs.getString("VendorName"));
                    vendor.setStreet(rs.getString("Street"));
                    vendor.setCity(rs.getString("City"));
                    vendor.setNpwp(rs.getString("Npwp"));
                    vendor.setTelephone(rs.getString("Telephone"));
                    vendor.setDataTimeStamp(rs.getString("DataTimeStamp"));

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

        return vendor;
    }

    @Override
    public List<VendorEntity> findByUsername(String userName) {

        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String query = String.format("EXECUTE Usp_FindVendorByUser '%s'", userName);
        List<VendorEntity> vendors = new ArrayList<>();

        try{
            Class.forName(dbConProperty.getDriverClassName());
            con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();

            if(rs.next()){
                do{
                    VendorEntity vendor = new VendorEntity();
                    vendor.setVendorId(rs.getString("VendorId"));
                    //vendor.setCompanyId(rs.getString("CompanyId"));
                    vendor.setVendorName(rs.getString("VendorName"));
                    vendor.setStreet(rs.getString("Street"));
                    vendor.setCity(rs.getString("City"));
                    vendor.setNpwp(rs.getString("Npwp"));
                    vendor.setTelephone(rs.getString("Telephone"));
                    //vendor.setDataTimeStamp(rs.getString("DataTimeStamp"));
                    vendors.add(vendor);
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

        return vendors;
    }
}
