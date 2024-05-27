package id.co.map.spk.repositories;

import id.co.map.spk.entities.RoleEntity;
import id.co.map.spk.entities.SbuEntity;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface SbuRepository {

    List<SbuEntity> findAll();
    SbuEntity findBySbuId(String sbuId);
    SbuEntity findbySbuCodeDesc(String sbuCode, String sbuDesc);
    List<SbuEntity> findByCompany(String companyId);
    List<SbuEntity> findByListCompany(String company);
    List<SbuEntity> findByUserandCompany(String username, String companyId);
    void add(SbuEntity sbu) throws ClassNotFoundException, SQLException;
    Map<String, Object> findSbuForPagination(String sbuId, String sbuDesc, Integer pageNumber, Integer size);
    List<SbuEntity> findByUsername(String username);
    List<SbuEntity> findByUsernamebyComp(String username);
    void updateCodeDescbySbuId(SbuEntity sbu, String sbuId) throws ClassNotFoundException, SQLException;
    void updateRulebySbuId(String rule, String sbuId) throws ClassNotFoundException, SQLException;
}
