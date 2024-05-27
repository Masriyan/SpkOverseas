package id.co.map.spk.api;

import id.co.map.spk.model.AppUser;
import id.co.map.spk.model.response.ClientResponse;
import id.co.map.spk.services.ForgotPasswordService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author Awie on 3/2/2020
 */
@RestController
@RequestMapping(ForgotPasswordApi.BASE_URL)
public class ForgotPasswordApi {

    static final String BASE_URL = "api/forgot-account-or-password";

    private final ForgotPasswordService forgotPasswordService;

    public ForgotPasswordApi(ForgotPasswordService forgotPasswordService) {
        this.forgotPasswordService = forgotPasswordService;
    }

    @PostMapping("")
    public ClientResponse requestNewForgotPass(@RequestBody AppUser user){
        return forgotPasswordService.forgotPassword(user.getEmail());
    }

    @PatchMapping(value = "", params = {"forgotPasswordKey"})
    public ClientResponse changeForgotPassword(
            @RequestBody Map<String, String> map,
            @RequestParam(value = "forgotPasswordKey") String forgotPasswordKey){

        return forgotPasswordService.updateForgotPassword(map, forgotPasswordKey);
    }

}
