package id.co.map.spk.configs;

import id.co.map.spk.utils.AppProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@AutoConfigureAfter(DispatcherServletAutoConfiguration.class)
class AppPropertyConfig {

    private static final Logger logger = LoggerFactory.getLogger(AppPropertyConfig.class);

    @Value("${app.property.email}")
    private String email;

    @Value("${app.property.baseUrl}")
    private String baseUrl;

    @Value("${app.property.compHeaderImagePath}")
    private String compHeaderImagePath;

    @Value("${app.property.spkQuotationPath}")
    private String spkQuotationPath;

    @Profile("awie")
    @Bean
    public AppProperty awieAppProperty(){

        AppProperty appProperty = getAppProperty();

        showAppProperty(appProperty, "awie");

        return  appProperty;
    }

    @Profile("dev")
    @Bean
    public AppProperty devAppProperty(){

        AppProperty appProperty = getAppProperty();

        showAppProperty(appProperty, "dev");

        return  appProperty;
    }

    @Profile("prod")
    @Bean
    public AppProperty prodAppProperty(){

        AppProperty appProperty = getAppProperty();

        showAppProperty(appProperty, "prod");

        return  appProperty;
    }

    @Profile("fnbLocal")
    @Bean
    public AppProperty fnbLocalAppProperty(){

        AppProperty appProperty = getAppProperty();

        showAppProperty(appProperty, "fnbLocal");

        return  appProperty;
    }

    private void showAppProperty(AppProperty appProperty, String profile) {
        logger.info("================================ {}'s Application Property ================================", profile);
        logger.info("Email                    : " + appProperty.getEmail());
        logger.info("BaseUrl                  : " + appProperty.getBaseUrl());
        logger.info("Image Path               : " + appProperty.getCompHeaderImagePath());
        logger.info("SPK Quotation Path       : " + appProperty.getSpkQuotationPath());
        logger.info("================================ {}'s Application Property ================================", profile);
    }

    private AppProperty getAppProperty() {
        AppProperty appProperty = new AppProperty();
        appProperty.setEmail(email);
        appProperty.setBaseUrl(baseUrl);
        appProperty.setCompHeaderImagePath(compHeaderImagePath);
        appProperty.setSpkQuotationPath(spkQuotationPath);
        return appProperty;
    }
}
