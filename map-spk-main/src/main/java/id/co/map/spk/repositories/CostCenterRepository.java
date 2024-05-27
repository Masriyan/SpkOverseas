package id.co.map.spk.repositories;

import id.co.map.spk.entities.CostCenterEntity;

import java.util.List;

public interface CostCenterRepository {

    List<CostCenterEntity> findByCompany(String companyId);
    List<CostCenterEntity> findByCompanyAndStore(String companyId, String storeId);
    CostCenterEntity findByIdAndCompany(String costCenterId, String companyId);

}
