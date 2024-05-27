package id.co.map.spk.services;

import id.co.map.spk.entities.CompanyEntity;
import id.co.map.spk.entities.SbuEntity;
import id.co.map.spk.model.AppUser;
import id.co.map.spk.model.response.ClientUserResponse;

import java.util.List;
import java.util.Map;

/**
 * @author Awie on 10/31/2019
 */
public interface UserService {

    ClientUserResponse add(AppUser newUser, String registeredBy);
    ClientUserResponse changePassword(Map<String, Object> map, String username);
    ClientUserResponse replacePassword(String password, String username);
    Map<String, Object> findUserPagination(String username, String name, Integer roleId, Integer pageNumber, Integer size, Integer draw);
    ClientUserResponse activateResponse(String registrationKey, Map<String, Object> map);
    ClientUserResponse delete(String username);
    ClientUserResponse updateEmail(String email, String userName);
    ClientUserResponse removeCompany(String companyId, String userName);
    ClientUserResponse removeSbu(String sbuId, String userName);
    ClientUserResponse addNewCompanies(String userName, List<CompanyEntity> newCompanies);
    ClientUserResponse addNewSbus(String userName, List<SbuEntity> sbus);
}
