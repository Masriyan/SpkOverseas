package id.co.map.spk.repositories;

import id.co.map.spk.entities.ApprovalRulesEntity;
import id.co.map.spk.entities.CompanyEntity;
import id.co.map.spk.entities.CountryEntity;
import id.co.map.spk.model.AppCompany;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface CountryRepository {
  void delete(String countryId) throws ClassNotFoundException, SQLException;
  Map<String, Object> findCountryForPagination(String countryCode, String countryName, Integer pageNumber, Integer size);
  void add(CountryEntity country) throws ClassNotFoundException, SQLException;
  CountryEntity findbyId(Integer countryId);
  List<CountryEntity> findAllDistinctRules();
  void updateCountryCodeById(String countryCode, Integer countryId) throws ClassNotFoundException, SQLException;
  void updateCountryNameById(String countryName, Integer countryId) throws ClassNotFoundException, SQLException;
  void updateLabelById(String labelData,String labelName, Integer countryId) throws ClassNotFoundException, SQLException;

}
