package id.co.map.spk.integrator.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.co.map.spk.integrator.EntrySheetIntegrator;
import id.co.map.spk.model.sap.entrysheet.EntrySheetResponse;
import id.co.map.spk.utils.SapEndpoint;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * @author Awie on 10/11/2019
 */
@Service
public class EntrySheetIntegratorImpl implements EntrySheetIntegrator {

    private static final Logger logger = LogManager.getLogger(EntrySheetIntegratorImpl.class);

    private final SapEndpoint sapEndpoint;

    public EntrySheetIntegratorImpl(SapEndpoint sapEndpoint) {
        this.sapEndpoint = sapEndpoint;
    }

    @Override
    public EntrySheetResponse postEntrySheet(Map<String, Object> map) throws IOException {
        String uri = sapEndpoint.getEntrySheetUri();
        ObjectMapper objectMapper = new ObjectMapper();
        String inputLine;
        StringBuffer responseString = new StringBuffer();
        BufferedReader in;
        int responseCode;
        EntrySheetResponse response;
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

        logger.debug("===========================REQUEST - POST Entry Sheet to SAP================================================");
        logger.debug("URI          : {}", uri);
        logger.debug("Method       : {}", con.getRequestMethod());
        logger.debug("Request Body : {}", json);
        logger.debug("==========================REQUEST - POST Entry Sheet to SAP================================================");

        responseCode = con.getResponseCode();
        in = new BufferedReader(new InputStreamReader(con.getInputStream()));

        while ((inputLine = in.readLine()) != null) {
            responseString.append(inputLine);
        }
        in.close();

        logger.debug("===========================RESPONSE - POST Entry Sheet to SAP================================================");
        logger.debug("URI          : {}", url);
        logger.debug("Method       : {}", con.getRequestMethod());
        logger.debug("Status code  : {}", responseCode);
        logger.debug("Request Body : {}", json);
        logger.debug("Respose Body : {}", responseString);
        logger.debug("==========================RESPONSE - POST Entry Sheet to SAP================================================");

        //Convert json string to object
        response = objectMapper.readValue(responseString.toString(), EntrySheetResponse.class);
        con.disconnect();

        return response;
    }
}
