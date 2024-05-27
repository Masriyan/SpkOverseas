package id.co.map.spk.api;

import id.co.map.spk.entities.SbuEntity;
import id.co.map.spk.model.response.ClientSbuResponse;
import id.co.map.spk.repositories.SbuRepository;
import id.co.map.spk.services.SbuService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(SbuApi.BASE_URL)
class SbuApi {

    static final String BASE_URL = "api/sbu";
    private static final Logger logger = LogManager.getLogger(SbuApi.class);

    private final SbuService sbuService;
    private final SbuRepository sbuRepository;

    public SbuApi(SbuService sbuService,SbuRepository sbuRepository) {

        this.sbuService = sbuService;
        this.sbuRepository = sbuRepository;
    }

    @GetMapping("/comp")
    public List<SbuEntity> getSbuByCompany(@RequestParam(value = "companyId") String companyId){
        return sbuRepository.findByCompany(companyId);
    }

    @GetMapping("/{comp}/")
    public List<SbuEntity> getSbuByComp(@PathVariable("comp") String comp){

        return sbuRepository.findByCompany(comp);
    }

    @GetMapping(value = "/comp", params = {"username", "companyId"})
    public List<SbuEntity> getSbuByUserandCompany(
                @RequestParam(name = "username") String username,
                @RequestParam(name = "companyId") String companyId){
        return sbuRepository.findByUserandCompany(username,companyId);
    }

    @GetMapping("/listcomp")
    public List<SbuEntity> getSbuByListCompany(@RequestParam(value = "companyId") String company){
        return sbuRepository.findByListCompany(company);
    }

    @GetMapping("/listsbusn")
    public List<SbuEntity> getSbuByUsername(@RequestParam(value = "username") String username){
        return sbuRepository.findByUsernamebyComp(username);
    }

    @PostMapping("")
    public ClientSbuResponse addNewSbu(@RequestBody SbuEntity sbu){
        return sbuService.add(sbu);
    }

    @GetMapping("")
    public Map<String, Object> findCompaniesPagination(
            @RequestParam(name ="sbuId", required = false) String sbuId,
            @RequestParam(name ="sbuDesc", required = false) String sbuName,
            @RequestParam(name = "draw") Integer draw, //required to avoid paging bugs!
            @RequestParam(name = "pageNumber") Integer pageNumber
    ){
        return sbuService.findSbuPagination(sbuId, sbuName, pageNumber, 10, draw);
    }

    @PatchMapping("/{sbuId}/edit")
    public ClientSbuResponse editCodeDesc(@RequestBody SbuEntity sbu, @PathVariable("sbuId") String sbuId){
        return sbuService.updateCodeDesc(sbu, sbuId);
    }

    @PatchMapping("/{sbuId}/rule")
    public ClientSbuResponse changeRule(@RequestBody Map<String, String> map, @PathVariable("sbuId") String sbuId){
        String rule = map.get("approvalRulesId");
        return sbuService.updateRule(rule, sbuId);
    }
}
