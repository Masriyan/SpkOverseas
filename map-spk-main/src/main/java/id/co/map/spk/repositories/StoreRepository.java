package id.co.map.spk.repositories;

import id.co.map.spk.entities.StoreEntity;

import java.util.List;

public interface StoreRepository {

    List<StoreEntity> findByCompany(String companyId);
    StoreEntity findByStoreIdAndCompanyId(String storeId, String companyId);
    List<StoreEntity> findByUsername(String username);
}
