package id.co.map.spk.model;

import id.co.map.spk.entities.CompanyEntity;
import id.co.map.spk.entities.RoleEntity;
import id.co.map.spk.entities.SbuEntity;
import id.co.map.spk.jpa.AppUserJpa;

import java.util.List;

/**
 * @author Awie on 10/31/2019
 */
public class AppUser extends AppUserJpa {

    private List<CompanyEntity> companies;
    private List<RoleEntity> roles;
    private List<SbuEntity> sbus;

    public List<CompanyEntity> getCompanies() {
        return companies;
    }

    public void setCompanies(List<CompanyEntity> companies) {
        this.companies = companies;
    }

    public List<RoleEntity> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleEntity> roles) {
        this.roles = roles;
    }

    public List<SbuEntity> getSbus() {
        return sbus;
    }

    public void setSbus(List<SbuEntity> sbus) {
        this.sbus = sbus;
    }
}
