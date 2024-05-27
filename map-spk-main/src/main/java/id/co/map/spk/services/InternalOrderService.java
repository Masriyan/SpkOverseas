package id.co.map.spk.services;

import id.co.map.spk.entities.SpkHistoryEntity;
import id.co.map.spk.entities.SuratPerintahKerjaEntity;
import id.co.map.spk.model.response.ClientSpkResponse;

/**
 * @author Awie on 9/13/2019
 */
public interface InternalOrderService {

    ClientSpkResponse createInternalOrder(SpkHistoryEntity spkHistoryEntity);
    ClientSpkResponse closeInternalOrder(SpkHistoryEntity spkHistoryEntity, SuratPerintahKerjaEntity spk);
}
