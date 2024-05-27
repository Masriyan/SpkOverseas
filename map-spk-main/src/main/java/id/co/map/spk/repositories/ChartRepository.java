package id.co.map.spk.repositories;

import id.co.map.spk.model.chart.SpkCompany;

import java.util.List;

/**
 * @author Awie on 11/19/2019
 */
public interface ChartRepository {

    public List<SpkCompany> getPendingSpkGroupedByCompany(String userName);

}
