package id.co.map.spk.model.sap;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Awie on 10/2/2019
 */
public class ItabBase {

    @JsonProperty("CODE")
    private String code;

    @JsonProperty("MESSAGE")
    private String message;

    public String getCode() {
        return code;
    }

    @JsonProperty("CODE")
    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    @JsonProperty("MESSAGE")
    public void setMessage(String msg) {
        this.message = msg;
    }
}
