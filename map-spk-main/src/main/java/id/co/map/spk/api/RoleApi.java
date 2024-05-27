package id.co.map.spk.api;

import id.co.map.spk.entities.RoleEntity;
import id.co.map.spk.repositories.RoleRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Awie on 11/7/2019
 */
@RestController
@RequestMapping(RoleApi.BASE_URL)
class RoleApi {

    static final String BASE_URL = "api/roles";
    private final RoleRepository roleRepository;

    public RoleApi(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @GetMapping("")
    public List<RoleEntity> getAllRoles(){
        return roleRepository.findAll();
    }
}
