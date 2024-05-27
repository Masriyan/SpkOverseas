package id.co.map.spk.repositories;

import id.co.map.spk.entities.RoleEntity;

import java.util.List;

/**
 * @author Awie on 10/31/2019
 */
public interface RoleRepository {

    List<RoleEntity> findAll();
    List<RoleEntity> findByUserName(String username);
}
