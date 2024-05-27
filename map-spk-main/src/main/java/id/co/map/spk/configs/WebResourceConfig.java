package id.co.map.spk.configs;

import id.co.map.spk.utils.AppProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc
class WebResourceConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private AppProperty appProperty;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/bower_components/**").addResourceLocations("classpath:/static/bower_components/");
        registry.addResourceHandler("/build/**").addResourceLocations("classpath:/static/build/");
        registry.addResourceHandler("/dist/**").addResourceLocations("classpath:/static/dist/");
        registry.addResourceHandler("/images/**").addResourceLocations("classpath:/static/images/");
        registry.addResourceHandler("/plugins/**").addResourceLocations("classpath:/static/plugins/");
        registry.addResourceHandler("/internet_explorer/**").addResourceLocations("classpath:/static/internet_explorer/");
        registry.addResourceHandler("/css/**").addResourceLocations("classpath:/static/css/");
        registry.addResourceHandler("/scripts/**").addResourceLocations("classpath:/static/scripts/");
        registry.addResourceHandler("/sweet_alert2/**").addResourceLocations("classpath:/static/sweet_alert2/");

        //should add "file:///" prefix!
        registry.addResourceHandler("/compHeaderImagePath/**").addResourceLocations("file:///"+appProperty.getCompHeaderImagePath());
    }
}
