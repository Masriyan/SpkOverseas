package id.co.map.spk.configs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * Mail configuration based on active profile
 */
@Configuration
class MailConfig {

    private static final Logger logger = LoggerFactory.getLogger(MailConfig.class);

    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.port}")
    private Integer port;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;

    @Profile("awie")
    @Bean
    public JavaMailSender awieJavaMailSender(){
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(host);
        javaMailSender.setPort(port);
        javaMailSender.setUsername(username);
        javaMailSender.setPassword(password);

        logger.info("================================ Awie's Mail ================================");
        logger.info("Host           : " + javaMailSender.getHost());
        logger.info("Port           : " + javaMailSender.getPort());
        logger.info("Username       : " + javaMailSender.getUsername());
        logger.info("================================ Awie's Mail ================================");

        return  javaMailSender;
    }

    @Profile("dev")
    @Bean
    public JavaMailSender devJavaMailSender(){
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(host);
        javaMailSender.setPort(port);
        javaMailSender.setUsername(username);
        javaMailSender.setPassword(password);

        logger.info("================================ Development's Mail ================================");
        logger.info("Host           : " + javaMailSender.getHost());
        logger.info("Port           : " + javaMailSender.getPort());
        logger.info("Username       : " + javaMailSender.getUsername());
        logger.info("================================ Development's Mail ================================");

        return  javaMailSender;
    }

    @Profile("prod")
    @Bean
    public JavaMailSender prodJavaMailSender(){
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(host);
        javaMailSender.setPort(port);
        javaMailSender.setUsername(username);
        javaMailSender.setPassword(password);

        logger.info("================================ Production's Mail ================================");
        logger.info("Host           : " + javaMailSender.getHost());
        logger.info("Port           : " + javaMailSender.getPort());
        logger.info("Username       : " + javaMailSender.getUsername());
        logger.info("================================ Production's Mail ================================");

        return  javaMailSender;
    }

}
