package id.co.map.spk.entities;

public class SbuEntity {
    private int sbuId;
    private String sbuCode;
    private String sbuDesc;
    private String companyId;
    private String approvalRulesId;

    public int getSbuId() {
        return sbuId;
    }

    public void setSbuId(int SbuId) {
        sbuId = SbuId;
    }

    public String getSbuCode() {
        return sbuCode;
    }

    public void setSbuCode(String SbuCode) {
        sbuCode = SbuCode;
    }

    public String getSbuDesc() {
        return sbuDesc;
    }

    public void setSbuDesc(String SbuDesc) {
        sbuDesc = SbuDesc;
    }

    public String getCompanyId() {return companyId;    }

    public void setCompanyId(String companyId) {this.companyId = companyId;    }

    public String getApprovalRulesId() {return approvalRulesId;    }

    public void setApprovalRulesId(String approvalRulesId) {this.approvalRulesId = approvalRulesId;    }
}
