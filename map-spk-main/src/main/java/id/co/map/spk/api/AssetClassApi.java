package id.co.map.spk.api;

import id.co.map.spk.entities.AssetClassEntity;
import id.co.map.spk.repositories.AssetClassRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Awie on 11/14/2019
 */
@RestController
@RequestMapping(AssetClassApi.BASE_URL)
class AssetClassApi {

    static final String BASE_URL = "api/asset-class";
    private final AssetClassRepository assetClassRepository;

    public AssetClassApi(AssetClassRepository assetClassRepository) {
        this.assetClassRepository = assetClassRepository;
    }

    @GetMapping(value = "", params = {"assetTypeId"})
    public List<AssetClassEntity> findSameTypeById(@RequestParam("assetTypeId") String assetTypeId){
        return assetClassRepository.findSameType(assetTypeId);
    }

}
