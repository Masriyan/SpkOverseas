package id.co.map.spk.model;

import id.co.map.spk.entities.CompanyEntity;

public class AppCompany extends CompanyEntity {
    private String sbuCode;
    private String sbuDesc;

    private Integer countryData;

    public String getSbuCode() {
        return sbuCode;
    }

    public void setSbuCode(String sbuCode) {
        this.sbuCode = sbuCode;
    }

    public String getSbuDesc() {
        return sbuDesc;
    }

    public void setSbuDesc(String sbuDesc) {
        this.sbuDesc = sbuDesc;
    }

    public Integer getCountryData() {
        return countryData;
    }

    public void setCountryData(Integer countryData) {
        this.countryData = countryData;
    }
}
