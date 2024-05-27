package id.co.map.spk.entities;

public class SuratPerintahKerjaEntity extends SpkBase{

    private String spkDescription;
    private String companyId;
    private String processingGroup;
    private String coa;
    private String approvedQuotationDate;
    private String estimateStoreOpeningDate;
    private String storeId;
    private String sbuId;
    private String sbuCode;
    private String vendorId;
    private String costCenterId;
    private Double amount;
    private String contactPerson;
    private Double top1Amount;
    private Double top2Amount;
    private Double top3Amount;
    private Double top4Amount;
    private Double top5Amount;
    private Double top1Percentage;
    private Double top2Percentage;
    private Double top3Percentage;
    private Double top4Percentage;
    private Double top5Percentage;
    private String top1Message;
    private String top2Message;
    private String top3Message;
    private String top4Message;
    private String top5Message;
    private String createdTimeStamp;
    private String quotationFile;
    private String spkType;

    /**
     * could not sent percentage to SAP. so need to round up value. and total should equals to spk amount.
     */
    public void setPercentageToAmount(){
        if(top1Percentage > 0) top1Amount = Double.valueOf(Math.round(amount * top1Percentage / 100));
        if(top2Percentage > 0) top2Amount = Double.valueOf(Math.round(amount * top2Percentage / 100));
        if(top3Percentage > 0) top3Amount = Double.valueOf(Math.round(amount * top3Percentage / 100));
        if(top4Percentage > 0) top4Amount = Double.valueOf(Math.round(amount * top4Percentage / 100));
        if(top5Percentage > 0) top5Amount = Double.valueOf(Math.round(amount * top5Percentage / 100));

        if(top5Percentage != 0) top5Amount = amount - top1Amount - top2Amount - top3Amount - top4Amount;
        else if(top4Percentage != 0) top4Amount = amount - top1Amount - top2Amount - top3Amount;
        else if(top3Percentage != 0) top3Amount = amount - top1Amount - top2Amount;
        else if(top2Percentage != 0) top2Amount = amount - top1Amount;
    }

    public String getSpkDescription(){ return  spkDescription; }

    public void setSpkDescription(String spkDescription){ this.spkDescription = spkDescription; }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getProcessingGroup() {
        return processingGroup;
    }

    public void setProcessingGroup(String processingGroup) {
        this.processingGroup = processingGroup;
    }

    public String getCoa() {
        return coa;
    }

    public void setCoa(String coa) {
        this.coa = coa;
    }

    public String getApprovedQuotationDate() {
        return approvedQuotationDate;
    }

    public void setApprovedQuotationDate(String approvedQuotationDate) {
        this.approvedQuotationDate = approvedQuotationDate;
    }

    public String getEstimateStoreOpeningDate() {
        return estimateStoreOpeningDate;
    }

    public void setEstimateStoreOpeningDate(String estimateStoreOpeningDate) {
        this.estimateStoreOpeningDate = estimateStoreOpeningDate;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getSbuId() {
        return sbuId;
    }

    public void setSbuId(String sbuId) {
        this.sbuId = sbuId;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getCostCenterId() {
        return costCenterId;
    }

    public void setCostCenterId(String costCenterId) {
        this.costCenterId = costCenterId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public Double getTop1Amount() {
        return top1Amount;
    }

    public void setTop1Amount(Double top1Amount) {
        this.top1Amount = top1Amount;
    }

    public Double getTop2Amount() {
        return top2Amount;
    }

    public void setTop2Amount(Double top2Amount) {
        this.top2Amount = top2Amount;
    }

    public Double getTop3Amount() {
        return top3Amount;
    }

    public void setTop3Amount(Double top3Amount) {
        this.top3Amount = top3Amount;
    }

    public Double getTop4Amount() {
        return top4Amount;
    }

    public void setTop4Amount(Double top4Amount) {
        this.top4Amount = top4Amount;
    }

    public Double getTop5Amount() {
        return top5Amount;
    }

    public void setTop5Amount(Double top5Amount) {
        this.top5Amount = top5Amount;
    }

    public Double getTop1Percentage() {
        return top1Percentage;
    }

    public void setTop1Percentage(Double top1Percentage) {
        this.top1Percentage = top1Percentage;
    }

    public Double getTop2Percentage() {
        return top2Percentage;
    }

    public void setTop2Percentage(Double top2Percentage) {
        this.top2Percentage = top2Percentage;
    }

    public Double getTop3Percentage() {
        return top3Percentage;
    }

    public void setTop3Percentage(Double top3Percentage) {
        this.top3Percentage = top3Percentage;
    }

    public Double getTop4Percentage() {
        return top4Percentage;
    }

    public void setTop4Percentage(Double top4Percentage) {
        this.top4Percentage = top4Percentage;
    }

    public Double getTop5Percentage() {
        return top5Percentage;
    }

    public void setTop5Percentage(Double top5Percentage) {
        this.top5Percentage = top5Percentage;
    }

    public String getTop1Message() {
        return top1Message;
    }

    public void setTop1Message(String top1Message) {
        this.top1Message = top1Message;
    }

    public String getTop2Message() {
        return top2Message;
    }

    public void setTop2Message(String top2Message) {
        this.top2Message = top2Message;
    }

    public String getTop3Message() {
        return top3Message;
    }

    public void setTop3Message(String top3Message) {
        this.top3Message = top3Message;
    }

    public String getTop4Message() {
        return top4Message;
    }

    public void setTop4Message(String top4Message) {
        this.top4Message = top4Message;
    }

    public String getTop5Message() {
        return top5Message;
    }

    public void setTop5Message(String top5Message) {
        this.top5Message = top5Message;
    }

    public String getCreatedTimeStamp() {
        return createdTimeStamp;
    }

    public void setCreatedTimeStamp(String createdTimeStamp) {
        this.createdTimeStamp = createdTimeStamp;
    }

    public String getQuotationFile() {
        return quotationFile;
    }

    public void setQuotationFile(String quotationFile) {
        this.quotationFile = quotationFile;
    }

    public String getSpkType() {
        return spkType;
    }

    public void setSpkType(String spkType) {
        this.spkType = spkType;
    }

    public String getSbuCode() {
        return sbuCode;
    }

    public void setSbuCode(String sbuCode) {
        this.sbuCode = sbuCode;
    }
}
