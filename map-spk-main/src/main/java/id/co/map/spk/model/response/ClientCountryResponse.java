package id.co.map.spk.model.response;


import id.co.map.spk.entities.CountryEntity;

public class ClientCountryResponse extends ClientResponse {
  private CountryEntity country;

  public CountryEntity getCountry() {
    return country;
  }

  public void setCountry(CountryEntity country) {
    this.country = country;
  }
}
