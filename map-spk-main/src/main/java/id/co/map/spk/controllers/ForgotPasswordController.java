package id.co.map.spk.controllers;

import id.co.map.spk.entities.ForgotPasswordEntity;
import id.co.map.spk.enums.ClientResponseStatus;
import id.co.map.spk.enums.ForgotPasswordStatus;
import id.co.map.spk.services.ForgotPasswordService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Awie on 3/4/2020
 */
@Controller
@RequestMapping(ForgotPasswordController.BASE_URL)
public class ForgotPasswordController {

    static final String BASE_URL = "/forgot-account-or-password";
    private final String ERR_VIEW_NAME = "errors/error-forgot-password";
    private final ForgotPasswordService forgotPasswordService;

    public ForgotPasswordController(ForgotPasswordService forgotPasswordService) {
        this.forgotPasswordService = forgotPasswordService;
    }

    @GetMapping("")
    public String showForgotPassword(){ return "forgot-password/forgot-account-or-password";}

    @GetMapping(value = "", params = {"forgotPasswordKey"})
    public ModelAndView showChangeForgotPassword(
            @RequestParam(value = "forgotPasswordKey", required = true) String forgotPasswordKey,
            ModelMap modelMap){

        ForgotPasswordEntity entity = forgotPasswordService.findByKey(forgotPasswordKey);
        ModelAndView modelAndView = new ModelAndView();

        if(entity == null){
            modelMap.addAttribute("errMessage", ClientResponseStatus.FP_INVALID_KEY.getMessage());
            return new ModelAndView(ERR_VIEW_NAME, modelMap);
        }

        if(entity.getForgotPasswordStatus().endsWith(ForgotPasswordStatus.REPLACED.toString())){
            modelMap.addAttribute("errMessage", ClientResponseStatus.FP_REPLACED.getMessage());
            return new ModelAndView(ERR_VIEW_NAME, modelMap);
        }

        if(entity.getForgotPasswordStatus().endsWith(ForgotPasswordStatus.COMPLETED.toString())){
            modelMap.addAttribute("errMessage", ClientResponseStatus.FP_LINK_COMPLETED.getMessage());
            return new ModelAndView(ERR_VIEW_NAME, modelMap);
        }

        modelAndView.setViewName("forgot-password/change-forgot-password");
        return modelAndView;

    }

}
