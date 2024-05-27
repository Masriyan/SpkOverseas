package id.co.map.spk.entities;

/**
 * @author Awie on 8/26/2019
 */
public class ApprovalRulesEntity {

    private String approvalRulesId;
    private String assetTypeId;
    private String poType;
    private Integer approvalLevel;
    private Integer roleId;
    private Double minAmount;
    private Double maxAmount;
    private String dataTimeStamp;

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    private Integer countryId;

    public String getApprovalRulesId() {
        return approvalRulesId;
    }

    public void setApprovalRulesId(String approvalRulesId) {
        this.approvalRulesId = approvalRulesId;
    }

    public String getAssetTypeId() {
        return assetTypeId;
    }

    public void setAssetTypeId(String assetTypeId) {
        this.assetTypeId = assetTypeId;
    }

    public String getPoType() {
        return poType;
    }

    public void setPoType(String poType) {
        this.poType = poType;
    }

    public Integer getApprovalLevel() {
        return approvalLevel;
    }

    public void setApprovalLevel(Integer approvalLevel) {
        this.approvalLevel = approvalLevel;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Double getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(Double minAmount) {
        this.minAmount = minAmount;
    }

    public Double getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(Double maxAmount) {
        this.maxAmount = maxAmount;
    }

    public String getDataTimeStamp() {
        return dataTimeStamp;
    }

    public void setDataTimeStamp(String dataTimeStamp) {
        this.dataTimeStamp = dataTimeStamp;
    }
}
