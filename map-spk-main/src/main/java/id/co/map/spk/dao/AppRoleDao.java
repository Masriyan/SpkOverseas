package id.co.map.spk.dao;

import id.co.map.spk.jpa.UserRoleJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class AppRoleDao {

    @Autowired
    private EntityManager entityManager;

    public List<String> getRoleNames(String userName){
        String sql = "Select ur.appRole.roleName from " + UserRoleJpa.class.getName() + " ur where ur.appUser.userName = :userName";

        Query query = this.entityManager.createQuery(sql, String.class);
        query.setParameter("userName", userName);
        return query.getResultList();
    }
}
