package id.co.map.spk.integrator.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import id.co.map.spk.enums.ClientResponseStatus;
import id.co.map.spk.model.response.ActDirResponse;
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

@Service
public class ADIntegrator
{
    private static final Logger logger = LogManager.getLogger(ADIntegrator.class);

    public ActDirResponse checkUser(Map<String, String> map) throws IOException
    {

        ActDirResponse resp = new ActDirResponse();

            String uri = "https://claim.map.co.id:1994/public/user/get/accountcheck";
            ObjectMapper objectMapper = new ObjectMapper();
            String inputLine;
            StringBuffer responseString = new StringBuffer();
            BufferedReader in;
            int responseCode;

            String json = objectMapper.writeValueAsString(map);

            URL url = new URL(uri);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setReadTimeout(90 * 1000);

            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.write(json.getBytes());
            wr.flush();
            wr.close();

            logger.debug("===========================REQUEST - POST Check Username on AD================================================");
            logger.debug("URI          : {}", uri);
            logger.debug("Method       : {}", con.getRequestMethod());
            logger.debug("Request Body : {}", json);
            logger.debug("==========================REQUEST - POST Check Username on AD================================================");

            responseCode = con.getResponseCode();
            in = new BufferedReader(new InputStreamReader(con.getInputStream()));

            while ((inputLine = in.readLine()) != null) {
                responseString.append(inputLine);
            }
            in.close();

            logger.debug("===========================RESPONSE - POST Check Username on AD================================================");
            logger.debug("Status code  : {}", responseCode);
            logger.debug("Respose Body : {}", responseString);
            logger.debug("==========================RESPONSE - POST Check Username on AD================================================");

            resp.setCode(responseCode);

            con.disconnect();

        return resp;
    }
}
