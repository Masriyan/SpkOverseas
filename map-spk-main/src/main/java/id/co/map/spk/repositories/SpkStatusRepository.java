package id.co.map.spk.repositories;

import id.co.map.spk.entities.SpkStatusEntity;
import java.util.List;

public interface SpkStatusRepository {

    List<SpkStatusEntity> findAll();
}
