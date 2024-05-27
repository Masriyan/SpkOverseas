package id.co.map.spk.services.impl;

import id.co.map.spk.entities.CountryEntity;
import id.co.map.spk.enums.ClientResponseStatus;
import id.co.map.spk.model.AppCompany;
import id.co.map.spk.model.response.ClientCompanyResponse;
import id.co.map.spk.model.response.ClientCountryResponse;
import id.co.map.spk.repositories.CountryRepository;
import id.co.map.spk.services.CountryService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Service
public class CountryServiceImpl implements CountryService {

  private static final Logger logger = LogManager.getLogger(CountryServiceImpl.class);

  private final CountryRepository countryRepository;

  public CountryServiceImpl(CountryRepository countryRepository) {
    this.countryRepository = countryRepository;
  }

  @Override
  public ClientCountryResponse updateCountryCode(String countryCode,Integer countryId) {
    ClientCountryResponse response = new ClientCountryResponse();
    try {
      countryRepository.updateCountryCodeById(countryCode,countryId);
      response.setResponseCode(ClientResponseStatus.COUNTRY_CODE_CHANGE.getCode());
      response.setResponseMessage(ClientResponseStatus.COUNTRY_CODE_CHANGE.getMessage());
    } catch (ClassNotFoundException | SQLException e) {
      e.printStackTrace();

      response.setResponseCode(ClientResponseStatus.SQL_ERROR.getCode());
      response.setResponseMessage(ClientResponseStatus.SQL_ERROR.getMessage());
      response.setCountry(null);
    }
    return response;
  }

  @Override
  public ClientCountryResponse updateCountryName(String countryName,Integer countryId) {
    ClientCountryResponse response = new ClientCountryResponse();
    try {
      countryRepository.updateCountryNameById(countryName,countryId);
      response.setResponseCode(ClientResponseStatus.COUNTRY_NAME_CHANGE.getCode());
      response.setResponseMessage(ClientResponseStatus.COUNTRY_NAME_CHANGE.getMessage());
    } catch (ClassNotFoundException | SQLException e) {
      e.printStackTrace();

      response.setResponseCode(ClientResponseStatus.SQL_ERROR.getCode());
      response.setResponseMessage(ClientResponseStatus.SQL_ERROR.getMessage());
      response.setCountry(null);
    }
    return response;
  }
  @Override
  public ClientCountryResponse updateLabel(String labelData,String labelName,Integer countryId) {
    ClientCountryResponse response = new ClientCountryResponse();
    try {
      countryRepository.updateLabelById(labelData,labelName,countryId);
      response.setResponseCode(ClientResponseStatus.COUNTRY_LABEL_CHANGE.getCode());
      response.setResponseMessage(ClientResponseStatus.COUNTRY_LABEL_CHANGE.getMessage());
    } catch (ClassNotFoundException | SQLException e) {
      e.printStackTrace();

      response.setResponseCode(ClientResponseStatus.SQL_ERROR.getCode());
      response.setResponseMessage(ClientResponseStatus.SQL_ERROR.getMessage());
      response.setCountry(null);
    }
    return response;
  }



  @Override
  public ClientCountryResponse add(CountryEntity country) {
    ClientCountryResponse response = new ClientCountryResponse();
    try {
      countryRepository.add(country);
      response.setResponseCode(ClientResponseStatus.COUNTRY_ADD.getCode());
      response.setResponseMessage(ClientResponseStatus.COUNTRY_ADD.getMessage());
      response.setCountry(country);

    } catch (ClassNotFoundException | SQLException e) {
      logger.error("============================== Error Add New Company ==============================");
      logger.error(e.getMessage());
      e.printStackTrace();
      logger.error("============================== Error Add New Company ==============================");

      response.setResponseCode(ClientResponseStatus.SERVER_ERROR.getCode());
      response.setResponseMessage(ClientResponseStatus.SERVER_ERROR.getMessage() + e.getMessage());
      response.setCountry(country);
    }
    return response;
  }

  @Override
  public Map<String, Object> findCountryPagination(String countryCode, String countryName, Integer pageNumber, Integer size, Integer draw) {
    Map<String, Object> findResult = countryRepository.findCountryForPagination(countryCode, countryName, pageNumber, size);
    Map<String, Object> map = new HashMap<>();
    map.put("draw", draw);
    map.put("recordsTotal", findResult.get("totalCount"));
    map.put("recordsFiltered", findResult.get("totalCount"));
    map.put("data", findResult.get("countries"));

    return map;
  }

  @Override
  public ClientCountryResponse delete(String countryId){
    ClientCountryResponse response = new ClientCountryResponse();
    try {
      countryRepository.delete(countryId);
      response.setResponseCode(ClientResponseStatus.COUNTRY_DELETED.getCode());
      response.setResponseMessage(ClientResponseStatus.COUNTRY_DELETED.getMessage());
    } catch (ClassNotFoundException | SQLException e) {
      e.printStackTrace();

      response.setResponseCode(ClientResponseStatus.SQL_ERROR.getCode());
      response.setResponseMessage(ClientResponseStatus.SQL_ERROR.getMessage());
      response.setCountry(null);
    }
    return response;
  }
}
