package id.co.map.spk.services;

import id.co.map.spk.entities.SbuEntity;
import id.co.map.spk.model.response.ClientCompanyResponse;
import id.co.map.spk.model.response.ClientSbuResponse;

import java.util.Map;

public interface SbuService  {

    ClientSbuResponse add(SbuEntity sbu);
    Map<String, Object> findSbuPagination(String SbuId, String SbuDesc, Integer pageNumber, Integer size, Integer draw);
    ClientSbuResponse updateCodeDesc(SbuEntity sbu, String sbuId);
    ClientSbuResponse updateRule(String rule, String sbuId);
}
