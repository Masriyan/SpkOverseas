package id.co.map.spk.model.sap.po;

import com.fasterxml.jackson.annotation.JsonProperty;
import id.co.map.spk.model.sap.ItabBase;

/**
 * @author Awie on 10/2/2019
 */
public class PurchaseOrderItab extends ItabBase {

    @JsonProperty("POSERVICE")
    private String poService;

    public String getPoService() {
        return poService;
    }

    @JsonProperty("POSERVICE")
    public void setPoService(String po) {
        this.poService = po;
    }
}
