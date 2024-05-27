package id.co.map.spk.integrator;

import id.co.map.spk.entities.SuratPerintahKerjaEntity;
import id.co.map.spk.model.sap.po.PurchaseOrderItab;
import id.co.map.spk.model.sap.po.PurchaseOrderResponse;
import org.json.JSONException;

import java.io.IOException;
import java.util.List;

/**
 * @author Awie on 10/2/2019
 *
 *  Act as communicator between web apps and SAP web services
 */
public interface PurchaseOrderIntegrator {

    List<PurchaseOrderItab> postPurchaseOrder(SuratPerintahKerjaEntity spk) throws IOException, JSONException;
}
