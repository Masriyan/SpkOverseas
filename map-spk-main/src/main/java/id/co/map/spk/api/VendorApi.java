package id.co.map.spk.api;

import id.co.map.spk.entities.VendorEntity;
import id.co.map.spk.repositories.VendorRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(VendorApi.BASE_URL)
class VendorApi {

    static final String BASE_URL = "api/vendor";

    private final VendorRepository vendorRepository;

    public VendorApi(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    @GetMapping("")
    public List<VendorEntity> getVendorByUserAndCompany(Principal principal,
                                                        @RequestParam(value = "companyId") String companyId){

        User user = (User) ((Authentication) principal).getPrincipal();

        return vendorRepository.findByUserAndCompany(user.getUsername(), companyId);
    }
}
