package id.co.map.spk.utils;

/**
 * @author Awie on 9/13/2019
 */
public class SapEndpoint {

    private String internalOrderUri;
    private String purchaseOrderUri;
    private String purchaseOrderUri2;
    private String entrySheetUri;
    private String closeInternalOrderUri;

    public String getInternalOrderUri() {
        return internalOrderUri;
    }

    public void setInternalOrderUri(String internalOrderUri) {
        this.internalOrderUri = internalOrderUri;
    }

    public String getPurchaseOrderUri() {
        return purchaseOrderUri;
    }

    public void setPurchaseOrderUri(String purchaseOrderUri) {
        this.purchaseOrderUri = purchaseOrderUri;
    }

    public String getPurchaseOrderUri2() { return purchaseOrderUri2;}

    public void setPurchaseOrderUri2(String purchaseOrderUri2) {
        this.purchaseOrderUri2 = purchaseOrderUri2;
    }

    public String getEntrySheetUri() {
        return entrySheetUri;
    }

    public void setEntrySheetUri(String entrySheetUri) {
        this.entrySheetUri = entrySheetUri;
    }

    public String getCloseInternalOrderUri() {
        return closeInternalOrderUri;
    }

    public void setCloseInternalOrderUri(String closeInternalOrderUri) {
        this.closeInternalOrderUri = closeInternalOrderUri;
    }
}
