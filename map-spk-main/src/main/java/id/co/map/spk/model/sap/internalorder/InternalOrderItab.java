package id.co.map.spk.model.sap.internalorder;

import com.fasterxml.jackson.annotation.JsonProperty;
import id.co.map.spk.model.sap.ItabBase;

/**
 * @author Awie on 9/17/2019
 */
public class InternalOrderItab extends ItabBase {

    @JsonProperty("INTERNALORDER")
    private String internalOrder;

    public InternalOrderItab(){

    }

    public String getInternalOrder() {
        return internalOrder;
    }

    @JsonProperty("INTERNALORDER")
    public void setInternalOrder(String internalOrder) {
        this.internalOrder = internalOrder;
    }
}
