package id.co.map.spk.services;

import id.co.map.spk.entities.CompanyEntity;
import id.co.map.spk.model.AppCompany;
import id.co.map.spk.model.response.ClientCompanyResponse;

import java.util.Map;

/**
 * @author Awie on 11/3/2019
 */
public interface CompanyService {

    ClientCompanyResponse add(AppCompany company);
    Map<String, Object> findUserPagination(String companyId, String companyName, String sbuId, Integer pageNumber, Integer size, Integer draw);
    ClientCompanyResponse delete(String sbuDelete);
    ClientCompanyResponse updateCompName(String compname, String companyId);
    ClientCompanyResponse updateCountry(Integer countryId, String companyId);
    ClientCompanyResponse updateNpwp(String npwp, String companyId);
    ClientCompanyResponse updateAdd1(String add1, String companyId);
    ClientCompanyResponse updateAdd2(String add2, String companyId);
    ClientCompanyResponse updateCoa(String coa, String companyId);
    ClientCompanyResponse updateHeadimg(String headimg, String companyId);
}
