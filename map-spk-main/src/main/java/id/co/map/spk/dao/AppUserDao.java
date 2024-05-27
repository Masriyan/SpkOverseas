package id.co.map.spk.dao;

import id.co.map.spk.jpa.AppUserJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.transaction.Transactional;

/**
 * fetch data from db for spring security
 */
@Repository
@Transactional
public class AppUserDao {

    @Autowired
    private EntityManager entityManager;

    public AppUserJpa findUserAccount(String userName){
        try{
            String sql = "Select e from " + AppUserJpa.class.getName() + " e Where e.userName = :userName";

            Query query = entityManager.createQuery(sql, AppUserJpa.class);
            query.setParameter("userName", userName);

            return (AppUserJpa) query.getSingleResult();
        }catch (NoResultException e){
            return null;
        }
    }
}
