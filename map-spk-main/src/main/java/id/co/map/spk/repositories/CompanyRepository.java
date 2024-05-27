package id.co.map.spk.repositories;

import id.co.map.spk.entities.CompanyEntity;
import id.co.map.spk.entities.CountryEntity;
import id.co.map.spk.model.AppCompany;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface CompanyRepository {

    List<CompanyEntity> findByUserName(String userName);
    CompanyEntity findByCompanyId(String companyId);
    CountryEntity findCountryByCompanyId(String companyId);
    List<CompanyEntity> findAll();
    void add(AppCompany company) throws ClassNotFoundException, SQLException;
    void delete(String sbuDelete) throws ClassNotFoundException, SQLException;
    Map<String, Object> findUserForPagination(String companyId, String companyName, String sbuId, Integer pageNumber, Integer size);
    void updateNamebyCompId(String compname, String companyId) throws ClassNotFoundException, SQLException;
    void updateCountrybyCompId(Integer countryId, String companyId) throws ClassNotFoundException, SQLException;
    void updateNpwpbyCompId(String npwp, String companyId) throws ClassNotFoundException, SQLException;
    void updateAdd1byCompId(String add1, String companyId) throws ClassNotFoundException, SQLException;
    void updateAdd2byCompId(String add2, String companyId) throws ClassNotFoundException, SQLException;
    void updateCoabyCompId(String coa, String companyId) throws ClassNotFoundException, SQLException;
    void updateHeadImgbyCompId(String headimg, String companyId) throws ClassNotFoundException, SQLException;
}
