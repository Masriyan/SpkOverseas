package id.co.map.spk.services;

import id.co.map.spk.entities.SpkHistoryEntity;
import id.co.map.spk.model.response.ClientSpkResponse;

/**
 * @author Awie on 10/14/2019
 */
public interface EntrySheetService {

    ClientSpkResponse createEntrySheet(SpkHistoryEntity spkHistoryEntity);

}
