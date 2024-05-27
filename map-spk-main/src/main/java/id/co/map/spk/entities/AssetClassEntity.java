package id.co.map.spk.entities;

public class AssetClassEntity {

    private String assetClassId;
    private String assetTypeId;
    private String assetClassName;
    private String dataTimeStamp;

    public AssetClassEntity(){

    }

    public AssetClassEntity(String assetClassId, String assetTypeId, String assetClassName, String dataTimeStamp) {
        this.assetClassId = assetClassId;
        this.assetTypeId = assetTypeId;
        this.assetClassName = assetClassName;
        this.dataTimeStamp = dataTimeStamp;
    }

    public String getAssetClassId() {
        return assetClassId;
    }

    public void setAssetClassId(String assetClassId) {
        this.assetClassId = assetClassId;
    }

    public String getAssetTypeId() {
        return assetTypeId;
    }

    public void setAssetTypeId(String assetTypeId) {
        this.assetTypeId = assetTypeId;
    }

    public String getAssetClassName() {
        return assetClassName;
    }

    public void setAssetClassName(String assetClassName) {
        this.assetClassName = assetClassName;
    }

    public String getDataTimeStamp() {
        return dataTimeStamp;
    }

    public void setDataTimeStamp(String dataTimeStamp) {
        this.dataTimeStamp = dataTimeStamp;
    }
}
