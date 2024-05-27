package id.co.map.spk.services;

import id.co.map.spk.dao.AppRoleDao;
import id.co.map.spk.dao.AppUserDao;
import id.co.map.spk.jpa.AppUserJpa;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger logger = LogManager.getLogger(UserDetailsServiceImpl.class);
    private final AppUserDao appUserDao;
    private final AppRoleDao appRoleDao;

    public UserDetailsServiceImpl(AppUserDao appUserDao, AppRoleDao appRoleDao) {
        this.appUserDao = appUserDao;
        this.appRoleDao = appRoleDao;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        AppUserJpa appUser = appUserDao.findUserAccount(userName);

        if(appUser == null){
            throw new UsernameNotFoundException("User " + userName + " was not found in the database");
        }

        List<String> roleNames = appRoleDao.getRoleNames(userName);

        List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();

        if(roleNames != null){
            for(String role:roleNames){
                GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role);
                grantList.add(grantedAuthority);
            }
        }

        UserDetails userDetails = (UserDetails) new User(appUser.getUserName(), appUser.getEncryptedPassword(), grantList);

        return userDetails;
    }

    public UsernamePasswordAuthenticationToken loadUserByUname(String userName) throws UsernameNotFoundException {
        AppUserJpa appUser = appUserDao.findUserAccount(userName);

        if(appUser == null){
            throw new UsernameNotFoundException("User "+userName+" does not have Role in System !! ");
        }

        List<String> roleNames = appRoleDao.getRoleNames(userName);
        List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();

        if(roleNames != null){
            for(String role:roleNames){
                GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role);
                grantList.add(grantedAuthority);
                logger.info("Role "+role);
            }
        }

        UserDetails userDet = (UserDetails) new User(appUser.getUserName(), appUser.getEncryptedPassword(), grantList);
        UsernamePasswordAuthenticationToken userDetails = new UsernamePasswordAuthenticationToken(userDet, "", grantList);

        return userDetails;
    }
}
