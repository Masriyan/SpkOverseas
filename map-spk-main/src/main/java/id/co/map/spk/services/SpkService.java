package id.co.map.spk.services;

import id.co.map.spk.entities.SpkHistoryEntity;
import id.co.map.spk.entities.SuratPerintahKerjaEntity;
import id.co.map.spk.model.response.ClientSpkResponse;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public interface SpkService {

    ClientSpkResponse addSpk(SuratPerintahKerjaEntity spk, String userName);
    ClientSpkResponse editSpk(SuratPerintahKerjaEntity spk, String userName);
    ClientSpkResponse updateSpkStatus(SpkHistoryEntity spkHistory);
    Map<String, Object> findSpkPagination(String username, String spkId, String spkDescription, String companyId, String sbuId, String agreementDate, String storeId, String vendorId, String status, Integer pageNumber, Integer size, Integer draw);
    void createExc(HttpServletResponse response,  String username, String spkId, String spkDescription, String companyId, String sbuId, String approvedQuotationDate, String storeId, String vendorId, String status) throws IOException;
}
