package id.co.map.spk.repositories;

import id.co.map.spk.entities.VendorEntity;

import java.util.List;

public interface VendorRepository {

    List<VendorEntity> findByUserAndCompany(String userName, String companyId);
    VendorEntity findByVendorIdAndCompanyId(String vendorId, String companyId);
    List<VendorEntity> findByUsername(String userName);
}
