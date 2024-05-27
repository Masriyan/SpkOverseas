package id.co.map.spk.integrator.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import id.co.map.spk.entities.SuratPerintahKerjaEntity;
import id.co.map.spk.integrator.PurchaseOrderIntegrator;
import id.co.map.spk.model.sap.internalorder.InternalOrderItab;
import id.co.map.spk.model.sap.po.PurchaseOrderItab;
import id.co.map.spk.model.sap.po.PurchaseOrderResponse;
import id.co.map.spk.utils.SapEndpoint;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Awie on 10/2/2019
 */
@Service
public class PurchaseOrderIntegratorImpl implements PurchaseOrderIntegrator {

    private static final Logger logger = LogManager.getLogger(PurchaseOrderIntegratorImpl.class);

    private final SapEndpoint sapEndpoint;
    private final DecimalFormat decimalFormat;

    public PurchaseOrderIntegratorImpl(SapEndpoint sapEndpoint, DecimalFormat decimalFormat) {
        this.sapEndpoint = sapEndpoint;
        this.decimalFormat = decimalFormat;
    }

    @Override
    public List<PurchaseOrderItab> postPurchaseOrder(SuratPerintahKerjaEntity spk) throws IOException, JSONException {

        String uri = "";
        if(spk.getSpkType().equalsIgnoreCase("Asset")) {
            uri = sapEndpoint.getPurchaseOrderUri();
        }else if(spk.getSpkType().equalsIgnoreCase("Expense")){
            uri = sapEndpoint.getPurchaseOrderUri2();
        }
        ObjectMapper objectMapper = new ObjectMapper();
        String inputLine;
        StringBuffer responseString = new StringBuffer();
        BufferedReader in;
        int responseCode;
        List<PurchaseOrderItab> response;

        //request object
        Map<String, Object> map = new HashMap<>();
        map.put("CompanyId", spk.getCompanyId());
        map.put("VendorId", spk.getVendorId());
        map.put("AgreementDate", spk.getApprovedQuotationDate().replace("-", ""));
        map.put("SpKId", spk.getSpkId());
        map.put("AssetClassId", spk.getAssetClassId());
        map.put("StoreId", spk.getStoreId());
        map.put("InternalOrder", spk.getInternalOrder());
        map.put("CostCenter", spk.getCostCenterId());

        //convert to amount, if spk payment use percentage,
        spk.setPercentageToAmount();

        if(spk.getTop1Amount() > 0) map.put("Top1Amount", decimalFormat.format(spk.getTop1Amount()));
        if(spk.getTop2Amount() > 0) map.put("Top2Amount", decimalFormat.format(spk.getTop2Amount()));
        if(spk.getTop3Amount() > 0) map.put("Top3Amount", decimalFormat.format(spk.getTop3Amount()));
        if(spk.getTop4Amount() > 0) map.put("Top4Amount", decimalFormat.format(spk.getTop4Amount()));
        if(spk.getTop5Amount() > 0) map.put("Top5Amount", decimalFormat.format(spk.getTop5Amount()));

        String json = objectMapper.writeValueAsString(map);

        URL url = new URL(uri);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        // Set Request Header
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setReadTimeout(90 * 1000);

        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.write(json.getBytes());
        wr.flush();
        wr.close();

        logger.debug("===========================REQUEST - POST Purchase Order to SAP================================================");
        logger.debug("URI          : {}", uri);
        logger.debug("Method       : {}", con.getRequestMethod());
        logger.debug("Request Body : {}", json);
        logger.debug("==========================REQUEST - POST Purchase Order to SAP================================================");

        responseCode = con.getResponseCode();
        in = new BufferedReader(new InputStreamReader(con.getInputStream()));

        while ((inputLine = in.readLine()) != null) {
            responseString.append(inputLine);
        }
        in.close();

        logger.debug("===========================RESPONSE - POST Purchase Order to SAP================================================");
        logger.debug("URI          : {}", url);
        logger.debug("Method       : {}", con.getRequestMethod());
        logger.debug("Status code  : {}", responseCode);
        logger.debug("Request Body : {}", json);
        logger.debug("Respose Body : {}", responseString);
        logger.debug("==========================RESPONSE - POST Purchase Order to SAP================================================");

        //Convert json string to object
        //response = objectMapper.readValue(responseString.toString(), new TypeReference<List<PurchaseOrderItab>>(){});

        JSONObject rp = new JSONObject(responseString.toString());
        JSONArray js = rp.getJSONArray("ITAB");
        List<PurchaseOrderItab> resp = new ArrayList<>();

        for (int i = 0; i < js.length(); i++) {
            JSONObject res = js.getJSONObject(i);
            PurchaseOrderItab poitab = new PurchaseOrderItab();
            poitab.setPoService(res.getString("POSERVICE"));
            poitab.setMessage(res.getString("MESSAGE"));
            poitab.setCode(res.getString("CODE"));
            /*
            if (i % 2 == 0) {
                poitab.setCode("1001");
            } else {
                poitab.setCode("1002");
            }*/
            resp.add(poitab);
        }

        response = resp;

        con.disconnect();

        return response;
    }
}
