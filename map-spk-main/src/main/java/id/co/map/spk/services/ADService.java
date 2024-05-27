package id.co.map.spk.services;

import id.co.map.spk.enums.ClientResponseStatus;
import id.co.map.spk.integrator.impl.ADIntegrator;
import id.co.map.spk.model.response.ActDirResponse;
import id.co.map.spk.model.response.ClientUserResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
public class ADService
{
    private static final Logger logger = LogManager.getLogger(ADService.class);
    private final ADIntegrator adIntegrator;

    public ADService(ADIntegrator adIntegrator) {
        this.adIntegrator = adIntegrator;
    }

    public ClientUserResponse checkUname(Map<String, String> map) {
        ClientUserResponse response = new ClientUserResponse();

        try {
            ActDirResponse resp = adIntegrator.checkUser(map);
            if(resp.getCode() == 200)
            {
                response.setResponseCode(ClientResponseStatus.USER_AD_ACTIVATED.getCode());
                response.setResponseMessage(ClientResponseStatus.USER_AD_ACTIVATED.getMessage());
            }
            else
            {
                response.setResponseCode(ClientResponseStatus.USER_AD_NOT_FOUND.getCode());
                response.setResponseMessage(ClientResponseStatus.USER_AD_NOT_FOUND.getMessage());
            }
        }
        catch (IOException e){
            e.printStackTrace();
            response.setResponseCode(ClientResponseStatus.SERVER_ERROR.getCode());
            response.setResponseMessage(ClientResponseStatus.SERVER_ERROR.getMessage() + " " +e.getMessage());
        }

        return response;
    }
}
