package id.co.map.spk.model.response;

import id.co.map.spk.model.AppRules;

public class ClientApprovalResponse extends ClientResponse {
    private AppRules rule;

    public AppRules getRule() {
        return rule;
    }

    public void setRule(AppRules rule) {
        this.rule = rule;
    }
}
