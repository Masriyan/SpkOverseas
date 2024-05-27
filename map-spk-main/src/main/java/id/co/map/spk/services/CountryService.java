package id.co.map.spk.services;

import id.co.map.spk.entities.CountryEntity;
import id.co.map.spk.model.AppCompany;
import id.co.map.spk.model.response.ClientCountryResponse;

import java.util.Map;

public interface CountryService {
  ClientCountryResponse add(CountryEntity country);
  Map<String, Object> findCountryPagination(String countryCode, String countryName, Integer pageNumber, Integer size, Integer draw);
  ClientCountryResponse delete(String countryId);

  ClientCountryResponse updateCountryCode(String countryCode,Integer countryId);
  ClientCountryResponse updateCountryName(String countryName,Integer countryId);
  ClientCountryResponse updateLabel(String labelData,String labelName,Integer countryId);
}
