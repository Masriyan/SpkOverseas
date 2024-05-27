package id.co.map.spk.services.impl;

import id.co.map.spk.entities.CompanyEntity;
import id.co.map.spk.enums.ClientResponseStatus;
import id.co.map.spk.model.AppCompany;
import id.co.map.spk.model.response.ClientCompanyResponse;
import id.co.map.spk.model.response.ClientUserResponse;
import id.co.map.spk.repositories.CompanyRepository;
import id.co.map.spk.services.CompanyService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Awie on 11/3/2019
 */
@Service
public class CompanyServiceImpl implements CompanyService {

    private static final Logger logger = LogManager.getLogger(CompanyServiceImpl.class);

    private final CompanyRepository companyRepository;

    public CompanyServiceImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public ClientCompanyResponse add(AppCompany company) {

        ClientCompanyResponse response = new ClientCompanyResponse();

        try {
            companyRepository.add(company);
            response.setResponseCode(ClientResponseStatus.COMPANY_ADDED.getCode());
            response.setResponseMessage(ClientResponseStatus.COMPANY_ADDED.getMessage());
            response.setCompany(company);

        } catch (ClassNotFoundException | SQLException e) {
            logger.error("============================== Error Add New Company ==============================");
            logger.error(e.getMessage());
            e.printStackTrace();
            logger.error("============================== Error Add New Company ==============================");

            response.setResponseCode(ClientResponseStatus.SERVER_ERROR.getCode());
            response.setResponseMessage(ClientResponseStatus.SERVER_ERROR.getMessage() + e.getMessage());
            response.setCompany(company);
        }

        return response;
    }

    @Override
    public Map<String, Object> findUserPagination(String companyId, String companyName, String sbuId, Integer pageNumber, Integer size, Integer draw) {
        Map<String, Object> findResult = companyRepository.findUserForPagination(companyId, companyName, sbuId, pageNumber, size);
        Map<String, Object> map = new HashMap<>();
        map.put("draw", draw);
        map.put("recordsTotal", findResult.get("totalCount"));
        map.put("recordsFiltered", findResult.get("totalCount"));
        map.put("data", findResult.get("companies"));

        return map;
    }

    @Override
    public ClientCompanyResponse delete(String sbuDelete){
        ClientCompanyResponse response = new ClientCompanyResponse();
        try {
            companyRepository.delete(sbuDelete);
            response.setResponseCode(ClientResponseStatus.SBU_DELETED.getCode());
            response.setResponseMessage(ClientResponseStatus.SBU_DELETED.getMessage());
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();

            response.setResponseCode(ClientResponseStatus.SQL_ERROR.getCode());
            response.setResponseMessage(ClientResponseStatus.SQL_ERROR.getMessage());
            response.setCompany(null);
        }
        return response;
    }

    @Override
    public ClientCompanyResponse updateCompName(String compname, String companyId) {
        ClientCompanyResponse response = new ClientCompanyResponse();
        try {
            companyRepository.updateNamebyCompId(compname,companyId);
            response.setResponseCode(ClientResponseStatus.COMPANY_NAME_CHANGED.getCode());
            response.setResponseMessage(ClientResponseStatus.COMPANY_NAME_CHANGED.getMessage());
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();

            response.setResponseCode(ClientResponseStatus.SQL_ERROR.getCode());
            response.setResponseMessage(ClientResponseStatus.SQL_ERROR.getMessage());
            response.setCompany(null);
        }
        return response;
    }

    @Override
    public ClientCompanyResponse updateCountry(Integer countryId, String companyId) {
        ClientCompanyResponse response = new ClientCompanyResponse();
        try {
            companyRepository.updateCountrybyCompId(countryId,companyId);
            response.setResponseCode(ClientResponseStatus.COMPANY_COUNTRY_CHANGED.getCode());
            response.setResponseMessage(ClientResponseStatus.COMPANY_COUNTRY_CHANGED.getMessage());
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();

            response.setResponseCode(ClientResponseStatus.SQL_ERROR.getCode());
            response.setResponseMessage(ClientResponseStatus.SQL_ERROR.getMessage());
            response.setCompany(null);
        }
        return response;
    }

    @Override
    public ClientCompanyResponse updateNpwp(String npwp, String companyId) {
        ClientCompanyResponse response = new ClientCompanyResponse();
        try {
            companyRepository.updateNpwpbyCompId(npwp,companyId);
            response.setResponseCode(ClientResponseStatus.COMPANY_NPWP_CHANGED.getCode());
            response.setResponseMessage(ClientResponseStatus.COMPANY_NPWP_CHANGED.getMessage());
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();

            response.setResponseCode(ClientResponseStatus.SQL_ERROR.getCode());
            response.setResponseMessage(ClientResponseStatus.SQL_ERROR.getMessage());
            response.setCompany(null);
        }
        return response;
    }

    @Override
    public ClientCompanyResponse updateAdd1(String add1, String companyId) {
        ClientCompanyResponse response = new ClientCompanyResponse();
        try {
            companyRepository.updateAdd1byCompId(add1,companyId);
            response.setResponseCode(ClientResponseStatus.COMPANY_ADD1_CHANGED.getCode());
            response.setResponseMessage(ClientResponseStatus.COMPANY_ADD1_CHANGED.getMessage());
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();

            response.setResponseCode(ClientResponseStatus.SQL_ERROR.getCode());
            response.setResponseMessage(ClientResponseStatus.SQL_ERROR.getMessage());
            response.setCompany(null);
        }
        return response;
    }

    @Override
    public ClientCompanyResponse updateAdd2(String add2, String companyId) {
        ClientCompanyResponse response = new ClientCompanyResponse();
        try {
            companyRepository.updateAdd2byCompId(add2,companyId);
            response.setResponseCode(ClientResponseStatus.COMPANY_ADD2_CHANGED.getCode());
            response.setResponseMessage(ClientResponseStatus.COMPANY_ADD2_CHANGED.getMessage());
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();

            response.setResponseCode(ClientResponseStatus.SQL_ERROR.getCode());
            response.setResponseMessage(ClientResponseStatus.SQL_ERROR.getMessage());
            response.setCompany(null);
        }
        return response;
    }

    @Override
    public ClientCompanyResponse updateCoa(String coa, String companyId) {
        ClientCompanyResponse response = new ClientCompanyResponse();
        try {
            companyRepository.updateCoabyCompId(coa,companyId);
            response.setResponseCode(ClientResponseStatus.COMPANY_COA_CHANGED.getCode());
            response.setResponseMessage(ClientResponseStatus.COMPANY_COA_CHANGED.getMessage());
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();

            response.setResponseCode(ClientResponseStatus.SQL_ERROR.getCode());
            response.setResponseMessage(ClientResponseStatus.SQL_ERROR.getMessage());
            response.setCompany(null);
        }
        return response;
    }

    @Override
    public ClientCompanyResponse updateHeadimg(String headimg, String companyId) {
        ClientCompanyResponse response = new ClientCompanyResponse();
        try {
            companyRepository.updateHeadImgbyCompId(headimg,companyId);
            response.setResponseCode(ClientResponseStatus.COMPANY_HEADIMG_CHANGED.getCode());
            response.setResponseMessage(ClientResponseStatus.COMPANY_HEADIMG_CHANGED.getMessage());
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();

            response.setResponseCode(ClientResponseStatus.SQL_ERROR.getCode());
            response.setResponseMessage(ClientResponseStatus.SQL_ERROR.getMessage());
            response.setCompany(null);
        }
        return response;
    }
}
