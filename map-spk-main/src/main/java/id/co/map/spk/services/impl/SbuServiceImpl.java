package id.co.map.spk.services.impl;

import id.co.map.spk.entities.SbuEntity;
import id.co.map.spk.enums.ClientResponseStatus;
import id.co.map.spk.model.response.ClientCompanyResponse;
import id.co.map.spk.model.response.ClientSbuResponse;
import id.co.map.spk.repositories.SbuRepository;
import id.co.map.spk.services.SbuService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Service
public class SbuServiceImpl implements SbuService {

    private static final Logger logger = LogManager.getLogger(SbuServiceImpl.class);

    private final SbuRepository sbuRepository;

    public SbuServiceImpl(SbuRepository sbuRepository) {
        this.sbuRepository = sbuRepository;
    }

    @Override
    public ClientSbuResponse add(SbuEntity sbu) {
        ClientSbuResponse response = new ClientSbuResponse();

        try {
            sbuRepository.add(sbu);
            response.setResponseCode(ClientResponseStatus.SBU_ADDED.getCode());
            response.setResponseMessage(ClientResponseStatus.SBU_ADDED.getMessage());
            response.setSbu(sbu);
        } catch (ClassNotFoundException | SQLException e) {
            logger.error("============================== Error Add New Sbu ==============================");
            logger.error(e.getMessage());
            e.printStackTrace();
            logger.error("============================== Error Add New Sbu ==============================");

            response.setResponseCode(ClientResponseStatus.SERVER_ERROR.getCode());
            response.setResponseMessage(ClientResponseStatus.SERVER_ERROR.getMessage() + e.getMessage());
            response.setSbu(sbu);
        }

        return response;
    }

    @Override
    public Map<String, Object> findSbuPagination(String SbuId, String SbuDesc, Integer pageNumber, Integer size, Integer draw) {

        Map<String, Object> findResult = sbuRepository.findSbuForPagination(SbuId, SbuDesc, pageNumber, size);

        Map<String, Object> map = new HashMap<>();
        map.put("draw", draw);
        map.put("recordsTotal", findResult.get("totalCount"));
        map.put("recordsFiltered", findResult.get("totalCount"));
        map.put("data", findResult.get("sbus"));

        return map;
    }

    @Override
    public ClientSbuResponse updateCodeDesc(SbuEntity sbu, String sbuId) {
        ClientSbuResponse response = new ClientSbuResponse();
        try {
            sbuRepository.updateCodeDescbySbuId(sbu,sbuId);
            response.setResponseCode(ClientResponseStatus.SBU_CODEDESC_CHANGED.getCode());
            response.setResponseMessage(ClientResponseStatus.SBU_CODEDESC_CHANGED.getMessage());
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();

            response.setResponseCode(ClientResponseStatus.SQL_ERROR.getCode());
            response.setResponseMessage(ClientResponseStatus.SQL_ERROR.getMessage());
            response.setSbu(null);
        }
        return response;
    }

    @Override
    public ClientSbuResponse updateRule(String rule, String sbuId){
        ClientSbuResponse response = new ClientSbuResponse();
        try {
            sbuRepository.updateRulebySbuId(rule,sbuId);
            response.setResponseCode(ClientResponseStatus.SBU_RULE_CHANGED.getCode());
            response.setResponseMessage(ClientResponseStatus.SBU_RULE_CHANGED.getMessage());
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();

            response.setResponseCode(ClientResponseStatus.SQL_ERROR.getCode());
            response.setResponseMessage(ClientResponseStatus.SQL_ERROR.getMessage());
            response.setSbu(null);
        }
        return response;
    }
}
