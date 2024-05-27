package id.co.map.spk.repositories.impl;

import id.co.map.spk.entities.SpkHistoryEntity;
import id.co.map.spk.enums.SpkStatus;
import id.co.map.spk.repositories.SpkHistoryRepository;
import id.co.map.spk.utils.DbConProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class SpkHistoryRepositoryImpl implements SpkHistoryRepository {

    private static final Logger logger = LoggerFactory.getLogger(SpkHistoryRepositoryImpl.class);
    private final DbConProperty dbConProperty;

    public SpkHistoryRepositoryImpl(DbConProperty dbConProperty) {
        this.dbConProperty = dbConProperty;
    }

    @Override
    public List<SpkHistoryEntity> findBySpkId(String spkId) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String query = String.format("SELECT * FROM SpkHistory WHERE SpkId =  '%s' ORDER BY DataTimeStamp DESC", spkId);
        List<SpkHistoryEntity> histories = new ArrayList<>();

        logger.debug("=========================== Find SpkHistory By SpkId ===========================");
        logger.debug("Query       : " + query);
        logger.debug("=========================== Find SpkHistory By SpkId ===========================");

        try{
            Class.forName(dbConProperty.getDriverClassName());
            con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();

            if(rs.next()){
                do{
                    SpkHistoryEntity history = new SpkHistoryEntity();
                    history.setSpkId(rs.getString("SpkId"));
                    history.setUserName(rs.getString("UserName"));
                    history.setStatus(SpkStatus.valueOf(rs.getString("StatusName")));
                    history.setSpkNote(rs.getString("SpkNote"));
                    history.setDataTimeStamp(rs.getString("DataTimeStamp"));
                    histories.add(history);
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
        return histories;
    }

    @Override
    public List<SpkHistoryEntity> findByIdForPrint(String spkId) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String query = String.format("EXECUTE Usp_findSpkHistoryByIdForPrint '%s'", spkId);
        List<SpkHistoryEntity> histories = new ArrayList<>();

        logger.debug("=========================== Find SpkHistory By SpkId ===========================");
        logger.debug("Query       : " + query);
        logger.debug("=========================== Find SpkHistory By SpkId ===========================");

        try{
            Class.forName(dbConProperty.getDriverClassName());
            con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();

            if(rs.next()){
                do{
                    SpkHistoryEntity history = new SpkHistoryEntity();
                    history.setSpkId(rs.getString("SpkId"));
                    history.setUserName(rs.getString("UserName"));
                    history.setStatus(SpkStatus.valueOf(rs.getString("StatusName")));
                    history.setSpkNote(rs.getString("SpkNote"));
                    history.setDataTimeStamp(rs.getString("DataTimeStamp"));
                    histories.add(history);
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
        return histories;
    }

    @Override
    public void add(SpkHistoryEntity spkHistory) throws ClassNotFoundException, SQLException {

        Connection con = null;
        PreparedStatement pstmt = null;
        String query = "EXECUTE Usp_AddSpkHistory ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?";

        Class.forName(dbConProperty.getDriverClassName());
        con = DriverManager.getConnection(dbConProperty.getConnectionUrl(), dbConProperty.getUserName(), dbConProperty.getPassword());
        pstmt = con.prepareStatement(query);
        pstmt.setString(1, spkHistory.getSpkId());
        pstmt.setString(2, spkHistory.getUserName());
        pstmt.setString(3, spkHistory.getAssetNo());
        pstmt.setString(4, spkHistory.getInternalOrder());
        pstmt.setString(5, spkHistory.getPurchaseOrder());
        pstmt.setString(6, spkHistory.getEntrySheet1());
        pstmt.setString(7, spkHistory.getEntrySheet2());
        pstmt.setString(8, spkHistory.getEntrySheet3());
        pstmt.setString(9, spkHistory.getEntrySheet4());
        pstmt.setString(10, spkHistory.getEntrySheet5());
        pstmt.setDouble(11, spkHistory.getGrAmount1() == null ? 0 : spkHistory.getGrAmount1());
        pstmt.setDouble(12, spkHistory.getGrAmount2() == null ? 0 : spkHistory.getGrAmount2());
        pstmt.setDouble(13, spkHistory.getGrAmount3() == null ? 0 : spkHistory.getGrAmount3());
        pstmt.setDouble(14, spkHistory.getGrAmount4() == null ? 0 : spkHistory.getGrAmount4());
        pstmt.setDouble(15, spkHistory.getGrAmount5() == null ? 0 : spkHistory.getGrAmount5());
        pstmt.setString(16, spkHistory.getStatus().name());
        pstmt.setString(17, spkHistory.getSpkNote());

        pstmt.executeUpdate();

        if(con != null) con.close();
        if(pstmt != null) pstmt.close();

    }
}
