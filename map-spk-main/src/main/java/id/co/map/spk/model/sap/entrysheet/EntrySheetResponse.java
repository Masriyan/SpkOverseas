package id.co.map.spk.model.sap.entrysheet;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @author Awie on 10/11/2019
 *
 * Response Object after post entry sheet to SAP
 */
public class EntrySheetResponse {

    @JsonProperty("ITAB")
    private List<EntrySheetItab> itabs;

    public List<EntrySheetItab> getItabs() {
        return itabs;
    }
}
