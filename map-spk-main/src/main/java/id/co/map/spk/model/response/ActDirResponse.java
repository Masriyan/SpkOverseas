package id.co.map.spk.model.response;

public class ActDirResponse {
    private String Id;
    private String IsActived;
    private String msg;
    private String detail;
    private int code;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getIsActived() {
        return IsActived;
    }

    public void setIsActived(String isActived) {
        IsActived = isActived;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
