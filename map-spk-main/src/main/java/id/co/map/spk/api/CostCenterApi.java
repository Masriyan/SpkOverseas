package id.co.map.spk.api;

import id.co.map.spk.entities.CostCenterEntity;
import id.co.map.spk.repositories.CostCenterRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(CostCenterApi.BASE_URL)
class CostCenterApi {

    static final String BASE_URL = "api/costCenter";

    private final CostCenterRepository costCenterRepository;

    public CostCenterApi(CostCenterRepository costCenterRepository) {
        this.costCenterRepository = costCenterRepository;
    }

    @GetMapping(value = "", params = {"companyId"})
    public List<CostCenterEntity> getCostCenterByCompany(@RequestParam(name = "companyId") String companyId){

        return costCenterRepository.findByCompany(companyId);
    }

    @GetMapping(value = "", params = {"companyId", "storeId"})
    public List<CostCenterEntity> getCostCenterByCompanyAndStore(
            @RequestParam(name = "companyId") String companyId,
            @RequestParam(name = "storeId") String storeId){

        return costCenterRepository.findByCompanyAndStore(companyId, storeId);
    }

}
