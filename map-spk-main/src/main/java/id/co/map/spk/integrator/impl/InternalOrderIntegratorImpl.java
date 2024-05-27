package id.co.map.spk.integrator.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonArrayFormatVisitor;
import id.co.map.spk.entities.SbuEntity;
import id.co.map.spk.entities.SuratPerintahKerjaEntity;
import id.co.map.spk.integrator.InternalOrderIntegrator;
import id.co.map.spk.model.sap.internalorder.InternalOrderItab;
import id.co.map.spk.model.sap.internalorder.InternalOrderResponse;
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
import java.util.*;

/**
 * @author Awie on 9/16/2019
 */
@Service
public class InternalOrderIntegratorImpl implements InternalOrderIntegrator {

    private static final Logger logger = LogManager.getLogger(InternalOrderIntegratorImpl.class);

    private final SapEndpoint sapEndpoint;
    private final DecimalFormat decimalFormat;

    public InternalOrderIntegratorImpl(SapEndpoint sapEndpoint, DecimalFormat decimalFormat) {
        this.sapEndpoint = sapEndpoint;
        this.decimalFormat = decimalFormat;
    }

    @Override
    public List<InternalOrderItab> postInternalOrder(SuratPerintahKerjaEntity spk) throws IOException, JSONException {
        String uri = sapEndpoint.getInternalOrderUri();
        ObjectMapper objectMapper = new ObjectMapper();
        String inputLine;
        StringBuffer responseString = new StringBuffer();
        BufferedReader in;
        int responseCode;
        List<InternalOrderItab> response = new ArrayList<>();

        //request object
        Map<String, Object> map = new HashMap<>();
        map.put("SpKId", spk.getSpkId());
        map.put("Coa", spk.getCoa());
        map.put("CompanyId", spk.getCompanyId());
        map.put("StoreId", spk.getStoreId());
        map.put("CostCenter", spk.getCostCenterId());
        map.put("AgreementDate", spk.getApprovedQuotationDate().replace("-", ""));
        map.put("AssetClassId", spk.getAssetClassId());
        map.put("Amount",  decimalFormat.format(spk.getAmount())); //removing decimal in number
        map.put("ProcessingGroup", spk.getProcessingGroup());
        map.put("AssetNo", spk.getAssetNo());

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

        logger.debug("===========================REQUEST - POST Internal Order to SAP================================================");
        logger.debug("URI          : {}", uri);
        logger.debug("Method       : {}", con.getRequestMethod());
        logger.debug("Request Body : {}", json);
        logger.debug("==========================REQUEST - POST Internal Order to SAP================================================");

        responseCode = con.getResponseCode();
        in = new BufferedReader(new InputStreamReader(con.getInputStream()));

        while ((inputLine = in.readLine()) != null) {
            responseString.append(inputLine);
        }
        in.close();

        logger.debug("===========================RESPONSE - POST Internal Order to SAP================================================");
        logger.debug("URI          : {}", url);
        logger.debug("Method       : {}", con.getRequestMethod());
        logger.debug("Status code  : {}", responseCode);
        logger.debug("Request Body : {}", json);
        logger.debug("Respose Body : {}", responseString);
        logger.debug("==========================RESPONSE - POST Internal Order to SAP================================================");

        //Convert json string to object
        //response = objectMapper.readValue(responseString.toString(), new TypeReference<List<InternalOrderItab>>(){});

        JSONObject rp = new JSONObject(responseString.toString());
        JSONArray js = rp.getJSONArray("ITAB");
        List<InternalOrderItab> resp = new ArrayList<>();

        for (int i = 0; i < js.length(); i++) {
            JSONObject res = js.getJSONObject(i);
            InternalOrderItab ioitab = new InternalOrderItab();
            ioitab.setInternalOrder(res.getString("INTERNALORDER"));
            ioitab.setCode(res.getString("CODE"));
            ioitab.setMessage(res.getString("MESSAGE"));
            resp.add(ioitab);
        }

        response = resp;

        con.disconnect();

        return response;
    }

    @Override
    public InternalOrderResponse closeInternalOrder(String spkId, String internalOrder, Double closedAmount) throws IOException {
        String uri = sapEndpoint.getCloseInternalOrderUri();
        ObjectMapper objectMapper = new ObjectMapper();
        String inputLine;
        StringBuffer responseString = new StringBuffer();
        BufferedReader in;
        int responseCode;
        InternalOrderResponse response;

        //request object
        Map<String, Object> map = new HashMap<>();
        map.put("SpKId", spkId);
        map.put("InternalOrder", internalOrder);
        map.put("Amount",  decimalFormat.format(closedAmount)); //removing decimal in number

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

        logger.debug("===========================REQUEST - Close Internal Order to SAP================================================");
        logger.debug("URI          : {}", uri);
        logger.debug("Method       : {}", con.getRequestMethod());
        logger.debug("Request Body : {}", json);
        logger.debug("==========================REQUEST - Close Internal Order to SAP================================================");

        responseCode = con.getResponseCode();
        in = new BufferedReader(new InputStreamReader(con.getInputStream()));

        while ((inputLine = in.readLine()) != null) {
            responseString.append(inputLine);
        }
        in.close();

        logger.debug("===========================RESPONSE - Close Internal Order to SAP================================================");
        logger.debug("URI          : {}", url);
        logger.debug("Method       : {}", con.getRequestMethod());
        logger.debug("Status code  : {}", responseCode);
        logger.debug("Request Body : {}", json);
        logger.debug("Respose Body : {}", responseString);
        logger.debug("==========================RESPONSE - Close Internal Order to SAP================================================");

        //Convert json string to object
        response = objectMapper.readValue(responseString.toString(), InternalOrderResponse.class);
        con.disconnect();

        return response;
    }
}
