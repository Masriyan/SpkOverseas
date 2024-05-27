package id.co.map.spk.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.co.map.spk.model.response.ActDirResponse;
import id.co.map.spk.services.UserDetailsServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

@Component
public class custAuthProvider implements AuthenticationProvider {

    private static final Logger logger = LogManager.getLogger(custAuthProvider.class);
    @Autowired
    private UserDetailsServiceImpl userDetailServ;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String uname = authentication.getName();
        String password = authentication.getCredentials().toString();
        ActDirResponse resp = null;
        UsernamePasswordAuthenticationToken auth = null;

        try {
            resp = loginAD(uname,password);
        } catch (Exception e) {
            logger.error("Authentication failed for user = " + uname);
            throw new BadCredentialsException("Authentication failed for user = " + uname);
        }

        if(resp.getIsActived().equals("A"))
        {
            auth = userDetailServ.loadUserByUname(uname);
        }
        else
        {
            logger.error("User "+uname+" does not Activated in MAP Active Directory (AD) !! ");
            throw new BadCredentialsException("User "+uname+" does not Activated in MAP Active Directory (AD) !! ");
        }

        return auth;
    }

    private ActDirResponse loginAD(String uname, String password) throws Exception {

        String uri = "https://claim.map.co.id:1994/public/user/get/authentication";
        ObjectMapper objectMapper = new ObjectMapper();
        String inputLine;
        StringBuffer responseString = new StringBuffer();
        BufferedReader in;
        int responseCode;

        //request object
        Map<String, Object> map = new HashMap<>();
        map.put("Username", uname);
        map.put("Password", password);

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

        logger.debug("===========================REQUEST - Check Username in AD ================================================");
        logger.debug("URI          : {}", uri);
        logger.debug("Method       : {}", con.getRequestMethod());
        logger.debug("Request Body : {}", hashJson(json));
        logger.debug("==========================REQUEST - Check Username in AD ================================================");

        responseCode = con.getResponseCode();
        in = new BufferedReader(new InputStreamReader(con.getInputStream()));

        while ((inputLine = in.readLine()) != null) {
            responseString.append(inputLine);
        }
        in.close();

        logger.debug("===========================RESPONSE - POST Purchase Order to SAP================================================");
        logger.debug("Status code  : {}", responseCode);
        logger.debug("Respose Body : {}", responseString);
        logger.debug("==========================RESPONSE - POST Purchase Order to SAP================================================");

        JSONObject rp = new JSONObject(responseString.toString());
        ActDirResponse resp = new ActDirResponse();

        if(responseCode == 200)
        {
            JSONObject res = rp.getJSONArray("Result").getJSONObject(0);
            resp.setId(res.getString("Id"));
            resp.setIsActived(res.getString("IsActived"));
        }
        else
        {
            JSONObject res = new JSONObject(responseString.toString());
            resp.setIsActived("N");
            resp.setMsg(res.getString("msg"));
            resp.setDetail(res.getString("detail"));
        }

        con.disconnect();
        return resp;

    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(UsernamePasswordAuthenticationToken.class);
    }

    public String hashJson(String json) throws NoSuchAlgorithmException {
        // Static getInstance method is called with hashing MD5
        MessageDigest md = MessageDigest.getInstance("MD5");

        // digest() method is called to calculate message digest
        //  of an input digest() return array of byte
        byte[] messageDigest = md.digest(json.getBytes());

        // Convert byte array into signum representation
        BigInteger no = new BigInteger(1, messageDigest);

        // Convert message digest into hex value
        String hashtext = no.toString(16);
        while (hashtext.length() < 32) {
            hashtext = "0" + hashtext;
        }
        return hashtext;
    }
}
