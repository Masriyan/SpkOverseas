package id.co.map.spk.configs;

import id.co.map.spk.services.UserDetailsServiceImpl;
import id.co.map.spk.utils.custAuthProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

//    @Autowired
//    private custAuthProvider authProvider;

    @Autowired
    private DataSource dataSource;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder;
    }

//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth.authenticationProvider(authProvider);
//    }

   
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        // Setting Service to find User in the database.
        // And Setting PassswordEncoder
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }
    

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable();

        // The pages or resources do not require login
        http.authorizeRequests().antMatchers(
                "/auth/login",
                "/surat-perintah-kerja/upd/**",
                "/surat-perintah-kerja/LoginSSO",
                "/update-resp/**",
                "/forgot-account-or-password",
                "/api/forgot-account-or-password",
                "/logout",
                "/bower_components/**",
                "/build/**",
                "/dist/**",
                "/images/**",
                "/plugins/**",
                "/internet_explorer/**",
                "/css/**",
                "/users/activation",
                "/api/users/activation"
                ).permitAll();

        // index page (everything inside index controller) requires login as any ROLES.
        // If no login, it will redirect to /login page.
        //change password.
        http.authorizeRequests()
                .antMatchers("/",
                        "/index",
                        "index.html",
                        "/surat-perintah-kerja/**",
                        "/users/change-password",
                        "/users/**/")
                .access("hasAnyRole('ROLE_ADMIN'," +
                        "'ROLE_OPERATION', " +
                        "'ROLE_PROJECT COSTING MANAGER', " +
                        "'ROLE_PROJECT COSTING DIVISION MANAGER', " +
                        "'ROLE_PROJECT DESIGN DIVISION MANAGER', " +
                        "'ROLE_FINANCE CONTROLLER GENERAL MANAGER', " +
                        "'ROLE_FINANCE DIRECTOR', " +
                        "'ROLE_GENERAL MANAGER', " +
                        "'ROLE_CHIEF OPERATION OFFICER', " +
                        "'ROLE_IT MANAGER', " +
                        "'ROLE_IT GENERAL MANAGER/HEAD', " +
                        "'ROLE_IT DIVISION MANAGER', " +
                        "'ROLE_IT VICE PRESIDENT', " +
                        "'ROLE_ACCOUNTANT', " +
                        "'ROLE_SBU HEAD', " +
                        "'ROLE_FINANCE AR', " +
                        "'ROLE_OPERATION IT PROJECT', " +
                        "'ROLE_OPERATION CONTRACTOR', " +
                        "'ROLE_OPERATION BUSINESS HRD', " +
                        "'ROLE_OPERATION BUSINESS FINANCE', " +
                        "'ROLE_OPERATION BUSINESS ACCOUNTING', " +
                        "'ROLE_OPERATION BUSINESS TAX', " +
                        "'ROLE_OPERATION BUSINESS GA', " +
                        "'ROLE_OPERATION BUSINESS CM', " +
                        "'ROLE_GENERAL MANAGER OPERATION', " +
                        "'ROLE_GENERAL MANAGER HRD', " +
                        "'ROLE_TAX HEAD', " +
                        "'ROLE_GA HEAD', " +
                        "'ROLE_CM HEAD', " +
                        "'ROLE_ACCOUNTING HEAD', " +
                        "'ROLE_FINANCE HEAD', " +
                        "'ROLE_HRD HEAD', " +
                        "'ROLE_SBU HEAD', " +
                        "'ROLE_DIVISION MANAGER SBU', " +
                        "'ROLE_DIVISION MANAGER HRD', " +
                        "'ROLE_DIVISION MANAGER TAX', " +
                        "'ROLE_DIVISION MANAGER ACCOUNTING', " +
                        "'ROLE_DIVISION MANAGER FINANCE AP', " +
                        "'ROLE_DIVISION MANAGER FINANCE AR', " +
                        "'ROLE_DIVISION MANAGER CM', " +
                        "'ROLE_DIVISION MANAGER GA', " +
                        "'ROLE_MANAGER SBU', " +
                        "'ROLE_MANAGER GA', " +
                        "'ROLE_MANAGER HRD', " +
                        "'ROLE_MANAGER ACCOUNTING', " +
                        "'ROLE_MANAGER FINANCE AP', " +
                        "'ROLE_MANAGER FINANCE AR', " +
                        "'ROLE_MANAGER CM', " +
                        "'ROLE_MANAGER TAX', " +
                        "'ROLE_CHIEF FINANCIAL OFFICER', " +
                        "'ROLE_CHIEF OPERATING OFFICER', " +
                        "'ROLE_IT MANAGER SBU', " +
                        "'ROLE_IT MANAGER APPLICATION', " +
                        "'ROLE_IT MANAGER INFRASTRUCTURE', " +
                        "'ROLE_IT VP APPLICATION', " +
                        "'ROLE_IT VP INFRASTRUCTURE', " +
                        "'ROLE_IT DIVISION MANAGER APPLICATION', " +
                        "'ROLE_IT DIVISION MANAGER INFRASTRUCTURE', " +
                        "'ROLE_ACCOUNTANT', " +
                        "'ROLE_REVIEWER' )");


        // create new spk page
        http.authorizeRequests()
                .antMatchers("/surat-perintah-kerja/create-new-spk")
                .access("hasAnyRole('ROLE_ADMIN', 'ROLE_OPERATION'" +
                        "'ROLE_OPERATION IT PROJECT', " +
                        "'ROLE_OPERATION CONTRACTOR', " +
                        "'ROLE_OPERATION BUSINESS HRD', " +
                        "'ROLE_OPERATION BUSINESS FINANCE', " +
                        "'ROLE_OPERATION BUSINESS ACCOUNTING', " +
                        "'ROLE_OPERATION BUSINESS TAX', " +
                        "'ROLE_OPERATION BUSINESS GA', " +
                        "'ROLE_OPERATION BUSINESS CM', " +
                        ")");

        // create new user
        // user list
        http.authorizeRequests()
                .antMatchers("/users", "/users/", "/users/create-new-user")
                .access("hasAnyRole('ROLE_ADMIN')");

        // create new company
        // companie list
        http.authorizeRequests()
                .antMatchers("/companies", "/companies/", "/companies/create-new-company")
                .access("hasAnyRole('ROLE_ADMIN')");

        // sbu list
        http.authorizeRequests()
                .antMatchers("/rules", "/rules/", "/rules/create-new-rule")
                .access("hasAnyRole('ROLE_ADMIN')");

        // sbu list
        http.authorizeRequests()
                .antMatchers("/sbus", "/sbus/", "/sbus/create-new-company")
                .access("hasAnyRole('ROLE_ADMIN')");

        // api auth
        http.authorizeRequests()
                .antMatchers("/api/**")
                .access("hasAnyRole('ROLE_ADMIN'," +
                        "'ROLE_OPERATION', " +
                        "'ROLE_PROJECT COSTING MANAGER', " +
                        "'ROLE_PROJECT COSTING DIVISION MANAGER', " +
                        "'ROLE_PROJECT DESIGN DIVISION MANAGER', " +
                        "'ROLE_FINANCE CONTROLLER GENERAL MANAGER', " +
                        "'ROLE_FINANCE DIRECTOR', " +
                        "'ROLE_GENERAL MANAGER', " +
                        "'ROLE_CHIEF OPERATION OFFICER', " +
                        "'ROLE_IT MANAGER', " +
                        "'ROLE_IT GENERAL MANAGER/HEAD', " +
                        "'ROLE_IT DIVISION MANAGER', " +
                        "'ROLE_IT VICE PRESIDENT', " +
                        "'ROLE_ACCOUNTANT', " +
                        "'ROLE_SBU HEAD', " +
                        "'ROLE_FINANCE AR', " +
                        "'ROLE_OPERATION IT PROJECT', " +
                        "'ROLE_OPERATION CONTRACTOR', " +
                        "'ROLE_OPERATION BUSINESS HRD', " +
                        "'ROLE_OPERATION BUSINESS FINANCE', " +
                        "'ROLE_OPERATION BUSINESS ACCOUNTING', " +
                        "'ROLE_OPERATION BUSINESS TAX', " +
                        "'ROLE_OPERATION BUSINESS GA', " +
                        "'ROLE_OPERATION BUSINESS CM', " +
                        "'ROLE_GENERAL MANAGER OPERATION', " +
                        "'ROLE_GENERAL MANAGER HRD', " +
                        "'ROLE_TAX HEAD', " +
                        "'ROLE_GA HEAD', " +
                        "'ROLE_CM HEAD', " +
                        "'ROLE_ACCOUNTING HEAD', " +
                        "'ROLE_FINANCE HEAD', " +
                        "'ROLE_HRD HEAD', " +
                        "'ROLE_SBU HEAD', " +
                        "'ROLE_DIVISION MANAGER SBU', " +
                        "'ROLE_DIVISION MANAGER HRD', " +
                        "'ROLE_DIVISION MANAGER TAX', " +
                        "'ROLE_DIVISION MANAGER ACCOUNTING', " +
                        "'ROLE_DIVISION MANAGER FINANCE AP', " +
                        "'ROLE_DIVISION MANAGER FINANCE AR', " +
                        "'ROLE_DIVISION MANAGER CM', " +
                        "'ROLE_DIVISION MANAGER GA', " +
                        "'ROLE_MANAGER SBU', " +
                        "'ROLE_MANAGER GA', " +
                        "'ROLE_MANAGER HRD', " +
                        "'ROLE_MANAGER ACCOUNTING', " +
                        "'ROLE_MANAGER FINANCE AP', " +
                        "'ROLE_MANAGER FINANCE AR', " +
                        "'ROLE_MANAGER CM', " +
                        "'ROLE_MANAGER TAX', " +
                        "'ROLE_CHIEF FINANCIAL OFFICER', " +
                        "'ROLE_CHIEF OPERATING OFFICER', " +
                        "'ROLE_IT MANAGER SBU', " +
                        "'ROLE_IT MANAGER APPLICATION', " +
                        "'ROLE_IT MANAGER INFRASTRUCTURE', " +
                        "'ROLE_IT VP APPLICATION', " +
                        "'ROLE_IT VP INFRASTRUCTURE', " +
                        "'ROLE_IT DIVISION MANAGER APPLICATION', " +
                        "'ROLE_IT DIVISION MANAGER INFRASTRUCTURE', " +
                        "'ROLE_ACCOUNTANT', " +
                        "'ROLE_REVIEWER' )");

        // When the user has logged in as XX.
        // But access a page that requires role YY,
        // AccessDeniedException will be thrown.
        http.authorizeRequests().and().exceptionHandling().accessDeniedPage("/errors/403");

        // Config for Login Form
        http.authorizeRequests().and().formLogin()//
                // Submit URL of login page.
                .loginProcessingUrl("/j_spring_security_check") // Submit URL
                .loginPage("/auth/login")//
                .defaultSuccessUrl("/")//
                .failureUrl("/auth/login?error=true")//
                .usernameParameter("username")//
                .passwordParameter("password")
                // Config for Logout Page
                .and().logout().logoutUrl("/logout")
                .logoutSuccessUrl("/auth/login");

        // Config Remember Me.
        http.authorizeRequests().and() //
                .rememberMe().tokenRepository(this.persistentTokenRepository()) //
                .tokenValiditySeconds(180 * 24 * 60 * 60); // 180 days
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl db = new JdbcTokenRepositoryImpl();
        db.setDataSource(dataSource);
        return db;
    }

}
