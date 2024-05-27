package id.co.map.spk.repositories;

import id.co.map.spk.entities.AppUserEntity;
import id.co.map.spk.entities.CompanyEntity;
import id.co.map.spk.entities.SbuEntity;
import id.co.map.spk.model.AppUser;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface AppUserRepository {

    AppUserEntity findByUsername(String userName);
    List<AppUserEntity> findByCompanyAndRole(String company, Integer roleId);
    List<AppUserEntity> findBySbuAndRole(String sbu, Integer roleId);

    /**
     * Get list of users got rejected.
     *
     * @param spkId
     * @return
     */
    List<AppUserEntity> findRejectedUser(String spkId);
    void add(AppUser newUser, String registeredBy) throws ClassNotFoundException, SQLException;
    void changePassword(String username, String encryptedPassword) throws ClassNotFoundException, SQLException;
    Map<String, Object> findUserForPagination(String username, String name, Integer roleId, Integer pageNumber, Integer size);
    void activateUser(String registrationKey, String userName, String encryptedPassword) throws ClassNotFoundException, SQLException;
    void delete(String username) throws ClassNotFoundException, SQLException;
    void updateEmail(String email, String username) throws ClassNotFoundException, SQLException;
    void removeCompany(String companyId, String username) throws ClassNotFoundException, SQLException;
    void removeSbu(String sbuId, String userName) throws ClassNotFoundException, SQLException;
    void addNewCompanies(String userName, List<CompanyEntity> newCompanies, String currentDateTime) throws ClassNotFoundException, SQLException;
    void addNewSbus(String userName, List<SbuEntity> newsbus) throws ClassNotFoundException, SQLException;
    AppUserEntity findByEmail(String email);
}
