package id.co.map.spk.model.response;

import id.co.map.spk.entities.CompanyEntity;

/**
 * @author Awie on 11/3/2019
 */
public class ClientCompanyResponse extends ClientResponse {

    private CompanyEntity company;

    public CompanyEntity getCompany() {
        return company;
    }

    public void setCompany(CompanyEntity company) {
        this.company = company;
    }
}
