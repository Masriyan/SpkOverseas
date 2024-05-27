package id.co.map.spk.model.chart;

/**
 * @author Awie on 11/19/2019
 */
public class SpkCompany {

    private String companyId;
    private int numOfSpk;

    public SpkCompany(String companyId, int numOfSpk) {
        this.companyId = companyId;
        this.numOfSpk = numOfSpk;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public int getNumOfSpk() {
        return numOfSpk;
    }

    public void setNumOfSpk(int numOfSpk) {
        this.numOfSpk = numOfSpk;
    }
}
