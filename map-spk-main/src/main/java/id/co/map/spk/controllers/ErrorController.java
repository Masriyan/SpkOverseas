package id.co.map.spk.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(ErrorController.BASE_URL)
class ErrorController {

    public static final String BASE_URL = "errors";

    @GetMapping("/403")
    public String showForbidden(){
        return "errors/403";
    }
}
