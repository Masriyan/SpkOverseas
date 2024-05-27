package id.co.map.spk.services.impl;

import id.co.map.spk.emails.NewUserMailService;
import id.co.map.spk.entities.AppUserEntity;
import id.co.map.spk.entities.CompanyEntity;
import id.co.map.spk.entities.SbuEntity;
import id.co.map.spk.entities.UserRegistrationEntity;
import id.co.map.spk.enums.ClientResponseStatus;
import id.co.map.spk.model.AppUser;
import id.co.map.spk.model.response.ClientUserResponse;
import id.co.map.spk.repositories.AppUserRepository;
import id.co.map.spk.repositories.CompanyRepository;
import id.co.map.spk.repositories.SbuRepository;
import id.co.map.spk.repositories.UserRegistrationRepository;
import id.co.map.spk.services.EmailService;
import id.co.map.spk.services.UserService;
import id.co.map.spk.utils.AppProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Awie on 10/31/2019
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);

    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRegistrationRepository userRegistrationRepository;
    private final AppProperty appProperty;
    private final EmailService emailService;
    private final CompanyRepository companyRepository;
    private final SbuRepository sbuRepository;

    public UserServiceImpl(AppUserRepository appUserRepository, BCryptPasswordEncoder passwordEncoder, UserRegistrationRepository userRegistrationRepository, AppProperty appProperty, EmailService emailService, CompanyRepository companyRepository,SbuRepository sbuRepository) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRegistrationRepository = userRegistrationRepository;
        this.appProperty = appProperty;
        this.emailService = emailService;
        this.companyRepository = companyRepository;
        this.sbuRepository = sbuRepository;
    }

    @Override
    public ClientUserResponse add(AppUser newUser, String registeredBy) {

        ClientUserResponse response = new ClientUserResponse();

        //set datatimestamp
        String currentDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        newUser.setDataTimeStamp(currentDateTime);

        try {
            appUserRepository.add(newUser, registeredBy);

            UserRegistrationEntity registration = userRegistrationRepository.findByUserName(newUser.getUserName());

            response.setResponseCode(ClientResponseStatus.USER_ADDED.getCode());
            response.setResponseMessage(ClientResponseStatus.USER_ADDED.getMessage());
            response.setUser(newUser);

            //send email notification to user for activate
//            Thread newUserSpkMailService = new Thread(
//                    new NewUserMailService(appProperty, registration.getRegistrationKey(), newUser.getFirstName(), newUser.getEmail(), emailService, newUser.getUserName())
//            );
//            newUserSpkMailService.start();

        } catch (ClassNotFoundException | SQLException e) {
            logger.error("============================== Error Add New User ==============================");
            logger.error(e.getMessage());
            e.printStackTrace();
            logger.error("============================== Error Add New User ==============================");

            response.setResponseCode(ClientResponseStatus.SQL_ERROR.getCode());
            response.setResponseMessage(ClientResponseStatus.SQL_ERROR.getMessage() + " " +e.getMessage());
        }

        return response;
    }

    @Override
    public ClientUserResponse changePassword(Map<String, Object> map, String username) {

        ClientUserResponse response = new ClientUserResponse();
        AppUserEntity userEntity = appUserRepository.findByUsername(username);
        String currentPassword = (String) map.get("currentPassword");
        String newPassword = (String) map.get("newPassword");
        String confirmPassword = (String) map.get("confirmPassword");

        //current password did not match
        if(!passwordEncoder.matches(currentPassword, userEntity.getEncryptedPassword())){
            response.setResponseCode(ClientResponseStatus.USER_INVALID_CURRENT_PASSWORD.getCode());
            response.setResponseMessage(ClientResponseStatus.USER_INVALID_CURRENT_PASSWORD.getMessage());
            response.setUser(null);
        }
        //new password did not match with confirm password
        else if (!newPassword.equals(confirmPassword)){
            response.setResponseCode(ClientResponseStatus.USER_NEW_PASSWORD_NOT_MATCH.getCode());
            response.setResponseMessage(ClientResponseStatus.USER_NEW_PASSWORD_NOT_MATCH.getMessage());
            response.setUser(null);
        }
        else{
            try {
                appUserRepository.changePassword(username, passwordEncoder.encode(newPassword));
                response.setResponseCode(ClientResponseStatus.USER_PASSWORD_UPDATED.getCode());
                response.setResponseMessage(ClientResponseStatus.USER_PASSWORD_UPDATED.getMessage());
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();

                response.setResponseCode(ClientResponseStatus.SQL_ERROR.getCode());
                response.setResponseMessage(ClientResponseStatus.SQL_ERROR.getMessage());
                response.setUser(null);
            }
        }
        return response;
    }



    @Override
    public ClientUserResponse replacePassword(String password, String username) {

        ClientUserResponse response = new ClientUserResponse();
        AppUserEntity userEntity = appUserRepository.findByUsername(username);
        String newPassword = password;

        try {
            appUserRepository.changePassword(username, passwordEncoder.encode(newPassword));
            response.setResponseCode(ClientResponseStatus.USER_PASSWORD_UPDATED.getCode());
            response.setResponseMessage(ClientResponseStatus.USER_PASSWORD_UPDATED.getMessage());
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();

            response.setResponseCode(ClientResponseStatus.SQL_ERROR.getCode());
            response.setResponseMessage(ClientResponseStatus.SQL_ERROR.getMessage());
            response.setUser(null);
        }

        return response;
    }



    @Override
    public Map<String, Object> findUserPagination(String username, String name, Integer roleId, Integer pageNumber, Integer size, Integer draw) {
        Map<String, Object> findResult = appUserRepository.findUserForPagination(username, name, roleId, pageNumber, size);
        Map<String, Object> map = new HashMap<>();
        map.put("draw", draw);
        map.put("recordsTotal", findResult.get("totalCount"));
        map.put("recordsFiltered", findResult.get("totalCount"));
        map.put("data", findResult.get("users"));

        return map;
    }

    @Override
    public ClientUserResponse activateResponse(String registrationKey, Map<String, Object> map) {

        ClientUserResponse response = new ClientUserResponse();
        String password = (String) map.get("password");
        String confirmPassword = (String) map.get("confirmPassword");
        String encryptedPassword = passwordEncoder.encode(password);
        UserRegistrationEntity userRegistration = userRegistrationRepository.findByKey(registrationKey);

        if(userRegistration == null){
            response.setResponseCode(ClientResponseStatus.USER_INVALID_REGISTRATION_KEY.getCode());
            response.setResponseMessage(ClientResponseStatus.USER_INVALID_REGISTRATION_KEY.getMessage());
        }
        else if(!password.equals(confirmPassword)){
            response.setResponseCode(ClientResponseStatus.USER_NEW_PASSWORD_NOT_MATCH.getCode());
            response.setResponseMessage(ClientResponseStatus.USER_NEW_PASSWORD_NOT_MATCH.getMessage());

        }else{
            try {
                appUserRepository.activateUser(userRegistration.getRegistrationKey(), userRegistration.getUserName(), encryptedPassword );
                response.setResponseCode(ClientResponseStatus.USER_ACTIVATED.getCode());
                response.setResponseMessage(ClientResponseStatus.USER_ACTIVATED.getMessage());

            } catch (ClassNotFoundException | SQLException e) {
                logger.error("============================== Error Add New User ==============================");
                logger.error(e.getMessage());
                e.printStackTrace();
                logger.error("============================== Error Add New User ==============================");

                response.setResponseCode(ClientResponseStatus.SQL_ERROR.getCode());
                response.setResponseMessage(ClientResponseStatus.SQL_ERROR.getMessage() + " " +e.getMessage());
            }
        }

        return response;
    }

    @Override
    public ClientUserResponse delete(String username) {
        ClientUserResponse response = new ClientUserResponse();
        try {
            appUserRepository.delete(username);
            response.setResponseCode(ClientResponseStatus.USER_DELETED.getCode());
            response.setResponseMessage(ClientResponseStatus.USER_DELETED.getMessage());
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();

            response.setResponseCode(ClientResponseStatus.SQL_ERROR.getCode());
            response.setResponseMessage(ClientResponseStatus.SQL_ERROR.getMessage());
            response.setUser(null);
        }
        return response;
    }

    @Override
    public ClientUserResponse updateEmail(String email, String userName) {
        ClientUserResponse response = new ClientUserResponse();

        try {
            appUserRepository.updateEmail(email, userName);
            response.setResponseCode(ClientResponseStatus.USER_EMAIL_CHANGED.getCode());
            response.setResponseMessage(ClientResponseStatus.USER_EMAIL_CHANGED.getMessage());

        } catch (ClassNotFoundException | SQLException e) {
            response.setResponseCode(ClientResponseStatus.SQL_ERROR.getCode());
            response.setResponseMessage(ClientResponseStatus.SQL_ERROR.getMessage());
            e.printStackTrace();
        }

        return response;
    }

    @Override
    public ClientUserResponse removeCompany(String companyId, String userName) {
        ClientUserResponse response = new ClientUserResponse();

        try {
            appUserRepository.removeCompany(companyId, userName);
            response.setResponseCode(ClientResponseStatus.USER_COMPANY_DELETED.getCode());
            response.setResponseMessage(ClientResponseStatus.USER_COMPANY_DELETED.getMessage());

        } catch (ClassNotFoundException | SQLException e) {
            response.setResponseCode(ClientResponseStatus.SQL_ERROR.getCode());
            response.setResponseMessage(ClientResponseStatus.SQL_ERROR.getMessage());
            e.printStackTrace();
        }

        return response;
    }

    @Override
    public ClientUserResponse removeSbu(String sbuId, String userName) {
        ClientUserResponse response = new ClientUserResponse();

        try {
            appUserRepository.removeSbu(sbuId, userName);
            response.setResponseCode(ClientResponseStatus.USER_SBU_DELETED.getCode());
            response.setResponseMessage(ClientResponseStatus.USER_SBU_DELETED.getMessage());

        } catch (ClassNotFoundException | SQLException e) {
            response.setResponseCode(ClientResponseStatus.SQL_ERROR.getCode());
            response.setResponseMessage(ClientResponseStatus.SQL_ERROR.getMessage());
            e.printStackTrace();
        }

        return response;
    }

    @Override
    public ClientUserResponse addNewCompanies(String userName, List<CompanyEntity> newCompanies) {

        ClientUserResponse response = new ClientUserResponse();
        List<CompanyEntity> currCompanies = companyRepository.findByUserName(userName);
        String currentDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        //removing company that already exist in UserCompany
        for(int i=0; i <newCompanies.size(); i++){

            String companyId = newCompanies.get(i).getCompanyId();
            boolean exist = currCompanies.stream()
                            .filter(c -> c.getCompanyId().equals(companyId))
                            .findFirst().isPresent();

            if(exist) newCompanies.remove(i);
        }

        try {
            appUserRepository.addNewCompanies(userName, newCompanies, currentDateTime);
            response.setResponseCode(ClientResponseStatus.USER_COMPANY_ADDED.getCode());
            response.setResponseMessage(ClientResponseStatus.USER_COMPANY_ADDED.getMessage());
        } catch (ClassNotFoundException | SQLException e) {
            response.setResponseCode(ClientResponseStatus.SQL_ERROR.getCode());
            response.setResponseMessage(ClientResponseStatus.SQL_ERROR.getMessage());
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public ClientUserResponse addNewSbus(String userName, List<SbuEntity> newsbus) {

        ClientUserResponse response = new ClientUserResponse();
        List<SbuEntity> currSbus = sbuRepository.findByUsername(userName);

        //removing company that already exist in UserCompany
        for(int i=0; i <newsbus.size(); i++){

            int sbuId = newsbus.get(i).getSbuId();
            boolean exist = currSbus.stream()
                    .filter(c -> c.getSbuId() == sbuId)
                    .findFirst().isPresent();

            if(exist) newsbus.remove(i);
        }

        try {
            appUserRepository.addNewSbus(userName, newsbus);
            response.setResponseCode(ClientResponseStatus.USER_SBU_ADDED.getCode());
            response.setResponseMessage(ClientResponseStatus.USER_SBU_ADDED.getMessage());
        } catch (ClassNotFoundException | SQLException e) {
            response.setResponseCode(ClientResponseStatus.SQL_ERROR.getCode());
            response.setResponseMessage(ClientResponseStatus.SQL_ERROR.getMessage());
            e.printStackTrace();
        }
        return response;
    }
}
