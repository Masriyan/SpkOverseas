package id.co.map.spk.model.sap.internalorder;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @author Awie on 9/16/2019
 *
 * Object returned after post internal order from SAP
 */
public class InternalOrderResponse {

    @JsonProperty("ITAB")
    private List<InternalOrderItab> itabs;

    public List<InternalOrderItab> getItabs() {
        return itabs;
    }
}