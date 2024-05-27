package id.co.map.spk.services.impl;

import id.co.map.spk.model.chart.SpkCompany;
import id.co.map.spk.repositories.ChartRepository;
import id.co.map.spk.services.ChartService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Awie on 11/19/2019
 */
@Service
public class ChartServiceImpl implements ChartService {

    private final ChartRepository chartRepository;

    public ChartServiceImpl(ChartRepository chartRepository) {
        this.chartRepository = chartRepository;
    }

    @Override
    public Map<String, Object> getOnProcessSpkGroupedByCompany(String userName) {

        Map<String, Object> map = new HashMap<>();
        List<SpkCompany> spkCompanies = chartRepository.getPendingSpkGroupedByCompany(userName);

        List<String> labels = spkCompanies.stream()
                .map(SpkCompany::getCompanyId)
                .collect(Collectors.toList());
        map.put("labels", labels);

        List<Integer> numberOfSpks = spkCompanies.stream()
                .map(SpkCompany::getNumOfSpk)
                .collect(Collectors.toList());
        map.put("numberOfSpks", numberOfSpks);

        return map;
    }
}
