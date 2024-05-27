package id.co.map.spk.configs;

import id.co.map.spk.utils.DbConProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * create DbConProperty bean based on active profile.
 */
@Configuration
class DbConConfig {

    private static final Logger logger = LoggerFactory.getLogger(DbConConfig.class);

    @Value("${spring.datasource.url}")
    private String connectionUrl;

    @Value("${spring.datasource.username}")
    private String userName;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    @Profile("awie")
    @Bean
    public DbConProperty awieDbConProperties(){

        DbConProperty dbConProperty = new DbConProperty(this.connectionUrl, this.userName, this.password, this.driverClassName);

        showDbConfig(dbConProperty, "awie");

        return  dbConProperty;
    }

    @Profile("dev")
    @Bean
    public DbConProperty devDbConProperties(){

        DbConProperty dbConProperty = new DbConProperty(this.connectionUrl, this.userName, this.password, this.driverClassName);

        showDbConfig(dbConProperty, "dev");

        return  dbConProperty;
    }

    @Profile("prod")
    @Bean
    public DbConProperty prodDbConProperties(){

        DbConProperty dbConProperty = new DbConProperty(this.connectionUrl, this.userName, this.password, this.driverClassName);

        showDbConfig(dbConProperty, "prod");

        return  dbConProperty;
    }

    @Profile("fnbLocal")
    @Bean
    public DbConProperty fnbLocalDbConProperties(){

        DbConProperty dbConProperty = new DbConProperty(this.connectionUrl, this.userName, this.password, this.driverClassName);

        showDbConfig(dbConProperty, "fnbLocal");

        return  dbConProperty;
    }

    private void showDbConfig(DbConProperty dbConProperty, String profile) {
        logger.info("================================ {}'s Database Connection ================================", profile);
        logger.info("Database Connection              : " + dbConProperty.getConnectionUrl());
        logger.info("Database User                    : " + dbConProperty.getUserName());
        logger.info("Database Password                : " + dbConProperty.getPassword());
        logger.info("Database Driver                  : " + dbConProperty.getDriverClassName());
        logger.info("================================ {}}'s Database Connection ================================", profile);
    }

}
