package id.co.map.spk.entities;

import id.co.map.spk.enums.SpkStatus;

/**
 * @author Awie on 10/14/2019
 */
public abstract class SpkBase {

    private String spkId;
    private String assetNo;
    private String assetClassId;
    private String internalOrder;
    private String purchaseOrder;
    private String entrySheet1;
    private String entrySheet2;
    private String entrySheet3;
    private String entrySheet4;
    private String entrySheet5;
    private Double grAmount1;
    private Double grAmount2;
    private Double grAmount3;
    private Double grAmount4;
    private Double grAmount5;
    private Double actualAmount;
    private SpkStatus status;
    private String dataTimeStamp;

    public Double getTotalGr(){

        if(grAmount1 == null) this.grAmount1 = 0D;
        if(grAmount2 == null) this.grAmount2 = 0D;
        if(grAmount3 == null) this.grAmount3 = 0D;
        if(grAmount4 == null) this.grAmount4 = 0D;
        if(grAmount5 == null) this.grAmount5 = 0D;

        return grAmount1 + grAmount2 + grAmount3 + grAmount4 + grAmount5;
    }

    public String getSpkId() {
        return spkId;
    }

    public void setSpkId(String spkId) {
        this.spkId = spkId;
    }

    public String getAssetNo() {
        return assetNo;
    }

    public void setAssetNo(String assetNo) {
        this.assetNo = assetNo;
    }

    public String getAssetClassId() {
        return assetClassId;
    }

    public void setAssetClassId(String assetClassId) {
        this.assetClassId = assetClassId;
    }

    public String getInternalOrder() {
        return internalOrder;
    }

    public void setInternalOrder(String internalOrder) {
        this.internalOrder = internalOrder;
    }

    public String getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(String purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public String getEntrySheet1() {
        return entrySheet1;
    }

    public void setEntrySheet1(String entrySheet1) {
        this.entrySheet1 = entrySheet1;
    }

    public String getEntrySheet2() {
        return entrySheet2;
    }

    public void setEntrySheet2(String entrySheet2) {
        this.entrySheet2 = entrySheet2;
    }

    public String getEntrySheet3() {
        return entrySheet3;
    }

    public void setEntrySheet3(String entrySheet3) {
        this.entrySheet3 = entrySheet3;
    }

    public String getEntrySheet4() {
        return entrySheet4;
    }

    public void setEntrySheet4(String entrySheet4) {
        this.entrySheet4 = entrySheet4;
    }

    public String getEntrySheet5() {
        return entrySheet5;
    }

    public void setEntrySheet5(String entrySheet5) {
        this.entrySheet5 = entrySheet5;
    }

    public Double getGrAmount1() {
        return grAmount1;
    }

    public void setGrAmount1(Double grAmount1) {
        this.grAmount1 = grAmount1;
    }

    public Double getGrAmount2() {
        return grAmount2;
    }

    public void setGrAmount2(Double grAmount2) {
        this.grAmount2 = grAmount2;
    }

    public Double getGrAmount3() {
        return grAmount3;
    }

    public void setGrAmount3(Double grAmount3) {
        this.grAmount3 = grAmount3;
    }

    public Double getGrAmount4() {
        return grAmount4;
    }

    public void setGrAmount4(Double grAmount4) {
        this.grAmount4 = grAmount4;
    }

    public Double getGrAmount5() {
        return grAmount5;
    }

    public void setGrAmount5(Double grAmount5) {
        this.grAmount5 = grAmount5;
    }

    public Double getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(Double actualAmount) {
        this.actualAmount = actualAmount;
    }

    public SpkStatus getStatus() {
        return status;
    }

    public void setStatus(SpkStatus status) {
        this.status = status;
    }

    public String getDataTimeStamp() {
        return dataTimeStamp;
    }

    public void setDataTimeStamp(String dataTimeStamp) {
        this.dataTimeStamp = dataTimeStamp;
    }
}
