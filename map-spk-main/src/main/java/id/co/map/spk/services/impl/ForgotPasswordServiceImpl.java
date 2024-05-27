package id.co.map.spk.services.impl;

import id.co.map.spk.emails.ForgotPasswordMailService;
import id.co.map.spk.entities.AppUserEntity;
import id.co.map.spk.entities.ForgotPasswordEntity;
import id.co.map.spk.enums.ClientResponseStatus;
import id.co.map.spk.enums.ForgotPasswordStatus;
import id.co.map.spk.model.response.ClientResponse;
import id.co.map.spk.repositories.AppUserRepository;
import id.co.map.spk.repositories.ForgotPasswordRepository;
import id.co.map.spk.services.EmailService;
import id.co.map.spk.services.ForgotPasswordService;
import id.co.map.spk.utils.AppProperty;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Map;

/**
 * @author Awie on 3/2/2020
 */
@Service
public class ForgotPasswordServiceImpl implements ForgotPasswordService {

    private final ForgotPasswordRepository forgotPasswordRepository;
    private final AppUserRepository appUserRepository;
    private final EmailService emailService;
    private final AppProperty appProperty;
    private final BCryptPasswordEncoder passwordEncoder;

    public ForgotPasswordServiceImpl(ForgotPasswordRepository forgotPasswordRepository, AppUserRepository appUserRepository, EmailService emailService, AppProperty appProperty, BCryptPasswordEncoder passwordEncoder) {
        this.forgotPasswordRepository = forgotPasswordRepository;
        this.appUserRepository = appUserRepository;
        this.emailService = emailService;
        this.appProperty = appProperty;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public ClientResponse forgotPassword(String email) {

        ClientResponse response = new ClientResponse();

        try {

            AppUserEntity appUserEntity = appUserRepository.findByEmail(email);

            if(appUserEntity == null){
                response.setResponseCode(ClientResponseStatus.USER_NOT_FOUND.getCode());
                response.setResponseMessage(ClientResponseStatus.USER_NOT_FOUND.getMessage());
                return response;
            }

            if(!appUserEntity.isEnbaled()){
                response.setResponseCode(ClientResponseStatus.USER_INACTIVE.getCode());
                response.setResponseMessage(ClientResponseStatus.USER_INACTIVE.getMessage());
                return response;
            }

            ForgotPasswordEntity incompleteOld = forgotPasswordRepository.findByUserNameAndStatus(appUserEntity.getUserName(), ForgotPasswordStatus.INCOMPLETE.toString());

            if(incompleteOld != null){
                forgotPasswordRepository.updateStatusByKey(incompleteOld.getForgotPasswordKey(), ForgotPasswordStatus.REPLACED.toString());
            }

            ForgotPasswordEntity incompleteNew = forgotPasswordRepository.save(appUserEntity.getUserName(), email, ForgotPasswordStatus.INCOMPLETE.toString());

            sendForgotPasswordEmail(incompleteNew.getForgotPasswordKey(), appUserEntity.getFirstName(), email, appUserEntity.getUserName());

            response.setResponseCode(ClientResponseStatus.FP_CREATED.getCode());
            response.setResponseMessage(ClientResponseStatus.FP_CREATED.getMessage());
            return response;
            
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();

            response.setResponseCode(ClientResponseStatus.SQL_ERROR.getCode());
            response.setResponseMessage(ClientResponseStatus.SQL_ERROR.getMessage());

            return response;
        }
    }

    @Override
    public ForgotPasswordEntity findByKey(String key) {
        try {
            return forgotPasswordRepository.findByKey(key);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ClientResponse updateForgotPassword(Map<String, String> map, String forgotPasswordKey) {

        ClientResponse response = new ClientResponse();
        ForgotPasswordEntity forgotPasswordEntity;
        String newPassword = (String) map.get("newPassword");
        String confirmPassword = (String) map.get("confirmPassword");

        try {
            forgotPasswordEntity = forgotPasswordRepository.findByKey(forgotPasswordKey);

            if(forgotPasswordEntity == null){
                response.setResponseCode(ClientResponseStatus.FP_INVALID_KEY.getCode());
                response.setResponseMessage(ClientResponseStatus.FP_INVALID_KEY.getMessage());
            }

            if(forgotPasswordEntity.getForgotPasswordStatus().endsWith(ForgotPasswordStatus.REPLACED.toString())){
                response.setResponseCode(ClientResponseStatus.FP_REPLACED.getCode());
                response.setResponseMessage(ClientResponseStatus.FP_REPLACED.getMessage());
            }

            if(forgotPasswordEntity.getForgotPasswordStatus().endsWith(ForgotPasswordStatus.COMPLETED.toString())){
                response.setResponseCode(ClientResponseStatus.FP_LINK_COMPLETED.getCode());
                response.setResponseMessage(ClientResponseStatus.FP_LINK_COMPLETED.getMessage());
            }

            if (!newPassword.equals(confirmPassword)){
                response.setResponseCode(ClientResponseStatus.USER_NEW_PASSWORD_NOT_MATCH.getCode());
                response.setResponseMessage(ClientResponseStatus.USER_NEW_PASSWORD_NOT_MATCH.getMessage());
            }

            appUserRepository.changePassword(forgotPasswordEntity.getUserName(), passwordEncoder.encode(newPassword));
            forgotPasswordRepository.updateStatusByKey(forgotPasswordKey, ForgotPasswordStatus.COMPLETED.toString());
            response.setResponseCode(ClientResponseStatus.FP_UPDATED.getCode());
            response.setResponseMessage(ClientResponseStatus.FP_UPDATED.getMessage());

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            response.setResponseCode(ClientResponseStatus.SQL_ERROR.getCode());
            response.setResponseMessage(ClientResponseStatus.SQL_ERROR.getMessage());
        }

        return response;
    }

    private void sendForgotPasswordEmail(String forgotPasswordKey, String firstName, String recepientEmail, String userName){

        Thread forgotPassWordMail = new Thread(
                new ForgotPasswordMailService(appProperty, forgotPasswordKey, firstName, recepientEmail, emailService, userName)
        );

        forgotPassWordMail.start();
    }
}
