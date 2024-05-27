package id.co.map.spk.repositories;

import java.sql.SQLException;

/**
 * @author Awie on 11/18/2019
 */
public interface SpkNextRoleRepository {

    void insert(String spkId, Integer nextRoleId) throws ClassNotFoundException, SQLException;
    void update(String spkId, Integer nextRoleId);

}
