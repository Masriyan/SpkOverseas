package id.co.map.spk.model.response;

import id.co.map.spk.entities.SuratPerintahKerjaEntity;

public class ClientSpkResponse extends ClientResponse {

    private SuratPerintahKerjaEntity suratPerintahKerja;

    public SuratPerintahKerjaEntity getSuratPerintahKerja() {
        return suratPerintahKerja;
    }

    public void setSuratPerintahKerja(SuratPerintahKerjaEntity suratPerintahKerja) {
        this.suratPerintahKerja = suratPerintahKerja;
    }
}
