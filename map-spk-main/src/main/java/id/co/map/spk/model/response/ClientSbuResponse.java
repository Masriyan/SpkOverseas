package id.co.map.spk.model.response;

import id.co.map.spk.entities.SbuEntity;

public class ClientSbuResponse extends ClientResponse {

    private SbuEntity sbu;

    public SbuEntity getSbu() {
        return sbu;
    }

    public void setSbu(SbuEntity sbu) {
        this.sbu = sbu;
    }
}

