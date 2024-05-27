package id.co.map.spk.controllers;

import id.co.map.spk.entities.UserRegistrationEntity;
import id.co.map.spk.enums.RegistrationStatus;
import id.co.map.spk.repositories.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Awie on 10/30/2019
 */
@Controller
@RequestMapping(UsersController.BASE_URL)
class UsersController {

    public static final String BASE_URL = "users";

    private final CompanyRepository companyRepository;
    private final RoleRepository roleRepository;
    private final UserRegistrationRepository userRegistrationRepository;
    private final AppUserRepository appUserRepository;
    private final SbuRepository sbuRepository;

    public UsersController(CompanyRepository companyRepository, RoleRepository roleRepository, UserRegistrationRepository userRegistrationRepository, AppUserRepository appUserRepository,SbuRepository sbuRepository) {
        this.companyRepository = companyRepository;
        this.roleRepository = roleRepository;
        this.userRegistrationRepository = userRegistrationRepository;
        this.appUserRepository = appUserRepository;
        this.sbuRepository = sbuRepository;
    }

    @GetMapping({"", "/"})
    public ModelAndView showListOfSpk(ModelAndView m){
        m.addObject("roles", roleRepository.findAll());
        m.setViewName("users/list-of-users");

        return  m;
    }

    @GetMapping("/create-new-user")
    public ModelAndView showCreateNewUser(){

        ModelAndView m = new ModelAndView();
        m.addObject("companies", companyRepository.findAll());
        m.addObject("roles", roleRepository.findAll());

        m.setViewName("users/create-new-user");

        return  m;
    }

    @GetMapping("/change-password")
    public String showChangePassword(){
        return "users/change-password";
    }

    @GetMapping("/activation")
    public ModelAndView showActivationPage(ModelAndView m, @RequestParam(value = "registrationKey") String registrationKey){

        UserRegistrationEntity registration = userRegistrationRepository.findByKey(registrationKey);

        if(registration.getRegistrationStatus().equals(RegistrationStatus.ACTIVE.name())){
            m.setViewName("redirect:/auth/login");
        }else{
            m.addObject("userRegistration", registration);
            m.setViewName("users/activation");
        }
        return  m;
    }

    @GetMapping("/{userName}/")
    public ModelAndView showUserDetail(ModelAndView m, @PathVariable("userName") String userName){

        m.addObject("user", appUserRepository.findByUsername(userName));
        m.addObject("companies", companyRepository.findByUserName(userName));
        m.addObject("role", roleRepository.findByUserName(userName).stream().findFirst().get());
        m.addObject("sbus", sbuRepository.findByUsername(userName));
        m.setViewName("users/user-details");
        return m;
    }

}
