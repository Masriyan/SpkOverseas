package id.co.map.spk.api;

import id.co.map.spk.services.ChartService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

/**
 * @author Awie on 11/19/2019
 */
@RestController
@RequestMapping(ChartApi.BASE_URL)
public class ChartApi {

    static final String BASE_URL = "api/chart";
    private final ChartService chartService;

    public ChartApi(ChartService chartService) {
        this.chartService = chartService;
    }

    @GetMapping("/spk-on-process-grouped-by-company")
    public Map<String, Object> getSpkOnProcessGroupedByCompany(Principal principal){

        User user = (User) ((Authentication) principal).getPrincipal();
        return chartService.getOnProcessSpkGroupedByCompany(user.getUsername());
    }

}
