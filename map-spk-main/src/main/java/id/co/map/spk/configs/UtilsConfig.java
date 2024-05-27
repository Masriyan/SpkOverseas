package id.co.map.spk.configs;

import id.co.map.spk.utils.BahasaNumberToWords;
import id.co.map.spk.utils.TextFormatter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import java.text.DecimalFormat;

@Configuration
class UtilsConfig {

    @Bean(name = "textFormatter")
    public TextFormatter getTextFormatter(){
        return  new TextFormatter();
    }

    @Bean(name = "bahasaNumberToWords")
    public BahasaNumberToWords getBahasaNumberToWords(){ return  new BahasaNumberToWords(); }

    @Bean(name = "decimalFormatter")
    public DecimalFormat getDecimalFormatter(){
        return new DecimalFormat("###");
    }

}
