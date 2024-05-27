package id.co.map.spk.controllers;

import id.co.map.spk.entities.SuratPerintahKerjaEntity;
import id.co.map.spk.repositories.SpkRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;

@Controller
class IndexController {

    private final SpkRepository spkRepository;

    public IndexController(SpkRepository spkRepository) {
        this.spkRepository = spkRepository;
    }

    @GetMapping({"", "/", "/index", "index.html"})
    public ModelAndView showIndexPage(ModelAndView m, Principal principal){

        User user = (User) ((Authentication) principal).getPrincipal();
        List<SuratPerintahKerjaEntity> listOfSpk = spkRepository.findUserShouldTakeAction(user.getUsername());
        Integer totalSpk = listOfSpk.size();

        m.addObject("totalSpk", totalSpk);
        m.addObject("listOfSpk", listOfSpk);
        m.setViewName("index");

        return m;
    }

    @GetMapping("/update-spk-success")
    public String showUpdateSuccess(){ return "update-resp/update-spk-success";}

    @GetMapping("/update-spk-fail-has-approv")
    public String showUpdateFailAppr(){ return "update-resp/update-spk-fail-has-approv";}

    @GetMapping("/update-spk-fail")
    public String showUpdateFail(){ return "update-resp/update-spk-fail";}

}
