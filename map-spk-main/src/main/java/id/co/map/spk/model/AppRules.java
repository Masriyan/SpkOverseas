package id.co.map.spk.model;

import id.co.map.spk.entities.ApprovalRulesEntity;
import id.co.map.spk.entities.LvlRoleEntity;

import java.util.List;

public class AppRules extends ApprovalRulesEntity {
    private String roleName;

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    private String countryName;
    private List<LvlRoleEntity> appruleList;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public List<LvlRoleEntity> getAppruleList() {
        return appruleList;
    }

    public void setAppruleList(List<LvlRoleEntity> appruleList) {
        this.appruleList = appruleList;
    }

}
