package id.co.map.spk.configs;

import id.co.map.spk.utils.SapEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author Awie on 9/13/2019
 */
@Configuration
public class SapEndpointConfig {

    private static final Logger logger = LoggerFactory.getLogger(SapEndpointConfig.class);

    @Value("${sap.endpoint.internalOrder}")
    private String internalOrderUri;

    @Value("${sap.endpoint.purchaseOrder}")
    private String purchaseOrderUri;

    @Value("${sap.endpoint.purchaseOrder2}")
    private String purchaseOrderUri2;

    @Value("${sap.endpoint.entrySheet}")
    private String entrySheetUri;

    @Value("${sap.endpoint.closeInternalOrder}")
    private String closeInternalOrderUri;

    @Profile("awie")
    @Bean
    public SapEndpoint awieSapEndpoint(){
        SapEndpoint sapEndpoint = getSapEndpoint();

        showEndpoint(sapEndpoint, "awie");

        return sapEndpoint;
    }

    @Profile("dev")
    @Bean
    public SapEndpoint devSapEndpoint(){
        SapEndpoint sapEndpoint = getSapEndpoint();

        showEndpoint(sapEndpoint, "dev");

        return sapEndpoint;
    }

    @Profile("prod")
    @Bean
    public SapEndpoint prodSapEndpoint(){
        SapEndpoint sapEndpoint = getSapEndpoint();

        showEndpoint(sapEndpoint, "prod");

        return sapEndpoint;
    }

    @Profile("fnbLocal")
    @Bean
    public SapEndpoint devFnbSapEndpoint(){
        SapEndpoint sapEndpoint = getSapEndpoint();

        showEndpoint(sapEndpoint, "fnbLocal");

        return sapEndpoint;
    }

    private SapEndpoint getSapEndpoint() {
        SapEndpoint sapEndpoint = new SapEndpoint();
        sapEndpoint.setInternalOrderUri(internalOrderUri);
        sapEndpoint.setPurchaseOrderUri(purchaseOrderUri);
        sapEndpoint.setPurchaseOrderUri2(purchaseOrderUri2);
        sapEndpoint.setEntrySheetUri(entrySheetUri);
        sapEndpoint.setCloseInternalOrderUri(closeInternalOrderUri);
        return sapEndpoint;
    }

    private void showEndpoint(SapEndpoint sapEndpoint, String profile) {
        logger.info("================================ {}'s SAP Endpoint ================================", profile);
        logger.info("Internal Order URI              : " + sapEndpoint.getInternalOrderUri());
        logger.info("Purchase Order URI Phase 1      : " + sapEndpoint.getPurchaseOrderUri());
        logger.info("Purchase Order URI Phase 2      : " + sapEndpoint.getPurchaseOrderUri2());
        logger.info("Entry Sheet URI                 : " + sapEndpoint.getEntrySheetUri());
        logger.info("Close Internal Order URI        : " + sapEndpoint.getCloseInternalOrderUri());
        logger.info("================================ {}'s SAP Endpoint ================================", profile);
    }
}
