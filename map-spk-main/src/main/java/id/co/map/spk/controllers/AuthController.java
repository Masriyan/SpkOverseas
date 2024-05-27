package id.co.map.spk.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(AuthController.BASE_URL)
class AuthController {

    public static final String BASE_URL = "/auth";

    @GetMapping("/login")
    public String showLogin(){
        return "auth/login";
    }



}
