package id.co.map.spk.repositories;

import id.co.map.spk.entities.AssetClassEntity;

import java.util.List;

public interface AssetClassRepository {

    List<AssetClassEntity> findAllAssetClass();
    AssetClassEntity findById(String assetClassId);

    /**
     * get all asset class by spk Id based on Asset type
     * @return
     */
    List<AssetClassEntity> findSameType(String assetClassId);

}
