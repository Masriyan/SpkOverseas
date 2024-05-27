package id.co.map.spk.services.impl;

import id.co.map.spk.enums.ClientResponseStatus;
import id.co.map.spk.model.response.ClientUserResponse;
import id.co.map.spk.repositories.UserRoleRepository;
import id.co.map.spk.services.UserRoleService;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

/**
 * @author Awie on 11/7/2019
 */
@Service
public class UserRoleServiceImpl implements UserRoleService {

    private final UserRoleRepository userRoleRepository;

    public UserRoleServiceImpl(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public ClientUserResponse updateUserRole(Integer roleId, String userName) {

        ClientUserResponse response = new ClientUserResponse();

        try {
            userRoleRepository.updateUserRole(roleId, userName);
            response.setResponseCode(ClientResponseStatus.USER_ROLE_UPDATE.getCode());
            response.setResponseMessage(ClientResponseStatus.USER_ROLE_UPDATE.getMessage());
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            response.setResponseCode(ClientResponseStatus.SQL_ERROR.getCode());
            response.setResponseMessage(ClientResponseStatus.SQL_ERROR.getMessage());
            response.setUser(null);
        }

        return response;
    }
}
