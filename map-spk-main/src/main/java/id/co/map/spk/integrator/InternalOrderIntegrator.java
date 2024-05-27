package id.co.map.spk.integrator;

import id.co.map.spk.entities.SuratPerintahKerjaEntity;
import id.co.map.spk.model.sap.internalorder.InternalOrderItab;
import id.co.map.spk.model.sap.internalorder.InternalOrderResponse;
import org.json.JSONException;

import java.io.IOException;
import java.util.List;

/**
 * @author Awie on 9/16/2019
 *
 * Act as communicator between web apps and SAP web services
 */
public interface InternalOrderIntegrator {

    List<InternalOrderItab> postInternalOrder(SuratPerintahKerjaEntity spk) throws IOException, JSONException;
    InternalOrderResponse closeInternalOrder(String spkId, String internalOrder, Double closedAmount) throws IOException;
}
