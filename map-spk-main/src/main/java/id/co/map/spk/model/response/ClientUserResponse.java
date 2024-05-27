package id.co.map.spk.model.response;

import id.co.map.spk.model.AppUser;

/**
 * @author Awie on 10/31/2019
 */
public class ClientUserResponse extends ClientResponse {

    private AppUser user;

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }
}
