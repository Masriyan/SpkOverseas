package id.co.map.spk.api;

import id.co.map.spk.entities.StoreEntity;
import id.co.map.spk.repositories.StoreRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(StoreApi.BASE_URL)
class StoreApi {

    static final String BASE_URL = "api/store";

    private final StoreRepository storeRepository;

    public StoreApi(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    @GetMapping("")
    public List<StoreEntity> getStoreByCompany(@RequestParam(value = "companyId") String companyId){

        return storeRepository.findByCompany(companyId);
    }
}
