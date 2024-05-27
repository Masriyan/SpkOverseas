package id.co.map.spk.repositories;

import id.co.map.spk.entities.SpkHistoryEntity;

import java.sql.SQLException;
import java.util.List;

public interface SpkHistoryRepository {

    List<SpkHistoryEntity> findBySpkId(String spkId);

    /**
    * used for print SPK. changed username value in spk history to user real name.
    * */
    List<SpkHistoryEntity> findByIdForPrint(String spkId);

    void add(SpkHistoryEntity spkHistory) throws ClassNotFoundException, SQLException;
}
