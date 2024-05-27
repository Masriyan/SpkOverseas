package id.co.map.spk.model.sap.entrysheet;

import com.fasterxml.jackson.annotation.JsonProperty;
import id.co.map.spk.model.sap.ItabBase;

/**
 * @author Awie on 10/11/2019
 */
public class EntrySheetItab extends ItabBase {

    @JsonProperty("ENTRYSHEET")
    private String entrySheet;

    public EntrySheetItab(){

    }

    public String getEntrySheet() {
        return entrySheet;
    }

}
