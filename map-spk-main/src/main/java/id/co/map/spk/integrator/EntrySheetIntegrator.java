package id.co.map.spk.integrator;

import id.co.map.spk.model.sap.entrysheet.EntrySheetResponse;

import java.io.IOException;
import java.util.Map;

/**
 * @author Awie on 10/11/2019
 */
public interface EntrySheetIntegrator {

    EntrySheetResponse postEntrySheet(Map<String, Object> map) throws IOException;
}
