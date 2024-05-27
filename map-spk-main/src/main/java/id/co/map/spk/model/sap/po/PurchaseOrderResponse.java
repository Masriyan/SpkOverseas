package id.co.map.spk.model.sap.po;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @author Awie on 10/2/2019
 */
public class PurchaseOrderResponse {

    @JsonProperty("ITAB")
    private List<PurchaseOrderItab> itabs;

    public void setItabs(List<PurchaseOrderItab> itab) {this.itabs=itab;}

    public List<PurchaseOrderItab> getItabs() {return itabs;}
}
