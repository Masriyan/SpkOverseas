package id.co.map.spk.services;

import id.co.map.spk.entities.ForgotPasswordEntity;
import id.co.map.spk.model.response.ClientResponse;

import java.util.Map;

/**
 * @author Awie on 3/2/2020
 */
public interface ForgotPasswordService {

    ClientResponse forgotPassword(String email);
    ForgotPasswordEntity findByKey(String key);
    ClientResponse updateForgotPassword(Map<String, String> map, String forgotPasswordKey);
}
