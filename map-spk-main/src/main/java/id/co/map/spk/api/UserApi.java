package id.co.map.spk.api;

import id.co.map.spk.entities.CompanyEntity;
import id.co.map.spk.entities.SbuEntity;
import id.co.map.spk.model.AppUser;
import id.co.map.spk.model.response.ClientUserResponse;
import id.co.map.spk.services.ADService;
import id.co.map.spk.services.UserRoleService;
import id.co.map.spk.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;

/**
 * @author Awie on 10/31/2019
 */
@RestController
@RequestMapping(UserApi.BASE_URL)
class UserApi {

    static final String BASE_URL = "api/users";
    private final UserService userService;
    private final UserRoleService userRoleService;
//    private final ADService AdService;

//    public UserApi(UserService userService, UserRoleService userRoleService,ADService AdService) {
//        this.userService = userService;
//        this.userRoleService = userRoleService;
//        this.AdService = AdService;
//    }
    
    public UserApi(UserService userService, UserRoleService userRoleService) {
        this.userService = userService;
        this.userRoleService = userRoleService;
    }

    @PostMapping("")
    public ClientUserResponse addUser(@RequestBody AppUser newUser, Principal principal){
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) ((Authentication) principal).getPrincipal();
        return userService.add(newUser, user.getUsername());
    }

    @PutMapping("/{username}/")
    public ClientUserResponse changePassword(@RequestBody Map<String, Object> map, @PathVariable("username") String username){
        ClientUserResponse response = new ClientUserResponse();
        String action = (String) map.get("action");

        switch (action){
            case "changePassword":
                response = userService.changePassword(map, username);
                break;
            default:
                break;
        }

        return  response;
    }

    @GetMapping("")
    public Map<String, Object> findSpkPagination(
            @RequestParam(name ="userName", required = false) String userName,
            @RequestParam(name ="name", required = false) String name,
            @RequestParam(name ="roleId", required = false) Integer roleId,
            @RequestParam(name = "draw") Integer draw, //required to avoid paging bugs!
            @RequestParam(name = "pageNumber") Integer pageNumber
    ){
        return userService.findUserPagination(userName, name, roleId, pageNumber, 10, draw);
    }

    @PutMapping("/activation")
    public ClientUserResponse activateUser(@RequestParam(value="activationKey") String activationKey, @RequestBody Map<String,Object> map){

        return userService.activateResponse(activationKey, map);
    }

    @DeleteMapping("/{username}/")
    public ClientUserResponse deleteUser(@PathVariable("username") String username){
        return userService.delete(username);
    }

    @PatchMapping("/{userName}/email")
    public ClientUserResponse changeEmail(@RequestBody Map<String, String> map, @PathVariable("userName") String userName){
        String email = map.get("email");
        return userService.updateEmail(email, userName);
    }

    @PatchMapping("/{userName}/role")
    public ClientUserResponse changeRole(@RequestBody Map<String, Integer> map, @PathVariable("userName") String userName){
        Integer roleId = map.get("roleId");
        return userRoleService.updateUserRole(roleId, userName);
    }

    @PostMapping("/{userName}/companies")
    public ClientUserResponse addCompaniesToUser(@PathVariable("userName") String userName, @RequestBody List<CompanyEntity> companies){
        return userService.addNewCompanies(userName, companies);
    }

    @PostMapping("/{userName}/sbus")
    public ClientUserResponse addSbusToUser(@PathVariable("userName") String userName, @RequestBody List<SbuEntity> sbus){
        return userService.addNewSbus(userName, sbus);
    }

    @DeleteMapping("/{userName}/companies")
    public ClientUserResponse deleteUserCompany(@RequestBody Map<String, String> map, @PathVariable("userName") String userName){

        String companyId = map.get("companyId");
        return userService.removeCompany(companyId, userName);
    }

    @DeleteMapping("/{userName}/sbus")
    public ClientUserResponse deleteUserSbu(@RequestBody Map<String, String> map, @PathVariable("userName") String userName){

        String sbuId = map.get("sbuId");
        return userService.removeSbu(sbuId, userName);
    }

//    @PutMapping("/check-ad")
//    public ClientUserResponse checkUsernameAD(@RequestBody Map<String, String> map) {
//        return AdService.checkUname(map);
//    }

}
