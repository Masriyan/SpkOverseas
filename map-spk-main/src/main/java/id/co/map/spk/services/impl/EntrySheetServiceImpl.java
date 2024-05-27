package id.co.map.spk.services.impl;

import id.co.map.spk.entities.SpkHistoryEntity;
import id.co.map.spk.entities.SuratPerintahKerjaEntity;
import id.co.map.spk.enums.ClientResponseStatus;
import id.co.map.spk.enums.SpkStatus;
import id.co.map.spk.integrator.EntrySheetIntegrator;
import id.co.map.spk.model.response.ClientSpkResponse;
import id.co.map.spk.model.sap.entrysheet.EntrySheetResponse;
import id.co.map.spk.repositories.SpkHistoryRepository;
import id.co.map.spk.repositories.SpkNextRoleRepository;
import id.co.map.spk.repositories.SpkRepository;
import id.co.map.spk.services.EntrySheetService;
import id.co.map.spk.utils.TextFormatter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Awie on 10/14/2019
 */
@Service
public class EntrySheetServiceImpl implements EntrySheetService {

    private static final Logger logger = LogManager.getLogger(EntrySheetServiceImpl.class);

    private final EntrySheetIntegrator entrySheetIntegrator;
    private final SpkRepository spkRepository;
    private final TextFormatter textFormatter;
    private final SpkHistoryRepository spkHistoryRepository;
    private final DecimalFormat decimalFormat;
    private final SpkNextRoleRepository spkNextRoleRepository;

    public EntrySheetServiceImpl(EntrySheetIntegrator entrySheetIntegrator, SpkRepository spkRepository, TextFormatter textFormatter, SpkHistoryRepository spkHistoryRepository, DecimalFormat decimalFormat, SpkNextRoleRepository spkNextRoleRepository) {
        this.entrySheetIntegrator = entrySheetIntegrator;
        this.spkRepository = spkRepository;
        this.textFormatter = textFormatter;
        this.spkHistoryRepository = spkHistoryRepository;
        this.decimalFormat = decimalFormat;
        this.spkNextRoleRepository = spkNextRoleRepository;
    }

    @Override
    public ClientSpkResponse createEntrySheet(SpkHistoryEntity spkHistoryEntity) {

        String entrySheet;
        String spkId = spkHistoryEntity.getSpkId();
        String username = spkHistoryEntity.getUserName();
        ClientSpkResponse clientSpkResponse = new ClientSpkResponse();
        SuratPerintahKerjaEntity spk = spkRepository.findById(spkId);
        int termOfPayment = 0;
        boolean created;

        StringBuilder spkNote = new StringBuilder();
        double paidAmount = 0;

        Map<String, Object> map = new HashMap<>();
        map.put("SpKId", spkId);
        map.put("PONumber", spk.getPurchaseOrder());

        if(spkHistoryEntity.getGrAmount1() != null) {
            termOfPayment = 1;
            paidAmount = spkHistoryEntity.getGrAmount1();
            map.put("POItem", "10");
            map.put("GRAmount", decimalFormat.format(paidAmount));

            //set spk history status
            spkHistoryEntity.setStatus(SpkStatus.GR1);
        }
        else if(spkHistoryEntity.getGrAmount2() != null) {
            termOfPayment = 2;
            paidAmount = spkHistoryEntity.getGrAmount2();
            map.put("POItem", "20");
            map.put("GRAmount", decimalFormat.format(paidAmount));

            //set spk history status
            spkHistoryEntity.setStatus(SpkStatus.GR2);
        }
        else if(spkHistoryEntity.getGrAmount3() != null) {
            termOfPayment = 3;
            paidAmount = spkHistoryEntity.getGrAmount3();
            map.put("POItem", "30");
            map.put("GRAmount", decimalFormat.format(paidAmount));

            //set spk history status
            spkHistoryEntity.setStatus(SpkStatus.GR3);
        }
        else if(spkHistoryEntity.getGrAmount4() != null) {
            termOfPayment = 4;
            paidAmount = spkHistoryEntity.getGrAmount4();
            map.put("POItem", "40");
            map.put("GRAmount", decimalFormat.format(paidAmount));

            //set spk history status
            spkHistoryEntity.setStatus(SpkStatus.GR4);
        }
        else if(spkHistoryEntity.getGrAmount5() != null) {
            termOfPayment = 5;
            paidAmount = spkHistoryEntity.getGrAmount5();
            map.put("POItem", "50");
            map.put("GRAmount", decimalFormat.format(paidAmount));

            //set spk history status
            spkHistoryEntity.setStatus(SpkStatus.GR5);
        }

        if (isCreateable(spk, termOfPayment)) {
            try {
                EntrySheetResponse response = entrySheetIntegrator.postEntrySheet(map);

                created = response.getItabs().stream().filter(s -> s.getCode().equals("1001")).findFirst().isPresent();

                //successful create Entry Sheet OR GR in SAP
                if(created){
                    entrySheet = response.getItabs().get(0).getEntrySheet();
                    spkNote.append("Payment " + termOfPayment + " is confirmed. Paid amount is " + textFormatter.toRupiahFormat(paidAmount) + ". Entry Sheet Number is " + entrySheet+".");

                    //update entry sheet
                    SuratPerintahKerjaEntity updatedSpk = spkRepository.updateEntrySheetAndGrAmount(spkId, username, entrySheet, paidAmount, spkHistoryEntity.getStatus().name(), spkNote.toString(), termOfPayment);

                    //all term of payment already paid
                    if(isCompleted(updatedSpk)){

                        Double totalPaid = updatedSpk.getTotalGr();

                        //if total paid not equals to SPK amount. SPK need to be closed!
                        if(!totalPaid.equals(spk.getAmount())){
                            //update spk status to need to close.

                            String note = "All term of payment already confirmed, but not equals to SPK Amount. Total GR is " + textFormatter.toRupiahFormat(totalPaid) ;
                            spkRepository.updateStatus(spkId, SpkStatus.NEED_TO_CLOSE.name(), note, username);

                            updatedSpk.setStatus(SpkStatus.NEED_TO_CLOSE);

                            //set client spk response
                            clientSpkResponse.setSuratPerintahKerja(updatedSpk);
                            clientSpkResponse.setResponseCode(ClientResponseStatus.SPK_NEED_TO_CLOSE.getCode());
                            clientSpkResponse.setResponseMessage(ClientResponseStatus.SPK_NEED_TO_CLOSE.getMessage());
                        }
                        //total paid amount equals to SPK amount. set SPK To completed.
                        else{
                            //update spk status to completed.
                            //spkRepository.updateStatus(spkId, SpkStatus.COMPLETED.name(), "", username);
                            spkRepository.updateStatusAndActualAmount(spkId, SpkStatus.COMPLETED.name(), "", username, totalPaid);

                            updatedSpk.setStatus(SpkStatus.COMPLETED);

                            //update spk nextrole
                            spkNextRoleRepository.update(spkId, 0);

                            //set client spk response
                            clientSpkResponse.setSuratPerintahKerja(updatedSpk);
                            clientSpkResponse.setResponseCode(ClientResponseStatus.COMPLETED.getCode());
                            clientSpkResponse.setResponseMessage(ClientResponseStatus.COMPLETED.getMessage());
                        }
                    }
                    else{
                        //set client spk response
                        clientSpkResponse.setSuratPerintahKerja(updatedSpk);
                        clientSpkResponse.setResponseCode(ClientResponseStatus.ENTRY_SHEET_CREATED.getCode());
                        clientSpkResponse.setResponseMessage(spkNote.toString());
                    }
                }
                //error while post entry sheet.
                else {
                    StringBuilder error = new StringBuilder();

                    response.getItabs().stream().forEach(r -> {
                        error.append(r.getMessage());
                    });

                    insertErrorHistory(spkHistoryEntity, error.toString());

                    clientSpkResponse.setResponseCode(ClientResponseStatus.ENTRY_SHEET_FAILED.getCode());
                    clientSpkResponse.setResponseMessage(ClientResponseStatus.SERVER_ERROR.getMessage() + error.toString());
                }

            }
            catch (IOException e) {

                logger.error("******************* ERROR CREATE ENTRY SHEET *******************");
                logger.debug("SPK ID        : {}", spkId);
                logger.debug("User          : {}", username);
                logger.debug("Error Message : {}", e.getMessage());
                e.printStackTrace();
                logger.error("******************* ERROR CREATE ENTRY SHEET *******************");

                insertErrorHistory(spkHistoryEntity, e.getMessage());

                clientSpkResponse.setResponseCode(ClientResponseStatus.SERVER_ERROR.getCode());
                clientSpkResponse.setResponseMessage(ClientResponseStatus.SERVER_ERROR.getMessage());
                e.printStackTrace();
            }
            catch (SQLException | ClassNotFoundException e) {

                logger.error("******************* ERROR CREATE ENTRY SHEET *******************");
                logger.debug("SPK ID        : {}", spkId);
                logger.debug("User          : {}", username);
                logger.debug("Error Message : {}", e.getMessage());
                e.printStackTrace();
                logger.error("******************* ERROR CREATE ENTRY SHEET *******************");

                insertErrorHistory(spkHistoryEntity, e.getMessage());

                clientSpkResponse.setResponseCode(ClientResponseStatus.SQL_ERROR.getCode());
                clientSpkResponse.setResponseMessage(ClientResponseStatus.SQL_ERROR.getMessage());
                e.printStackTrace();
            }
        }
        else {
            clientSpkResponse.setResponseCode(ClientResponseStatus.ENTRY_SHEET_UNABLE_CREATE.getCode());
            clientSpkResponse.setResponseMessage(ClientResponseStatus.ENTRY_SHEET_UNABLE_CREATE.getMessage());
        }

        return clientSpkResponse;
    }

    private void insertErrorHistory(SpkHistoryEntity spkHistoryEntity, String error){
        //insert into spk history
        spkHistoryEntity.setAssetNo("");
        spkHistoryEntity.setInternalOrder("");
        spkHistoryEntity.setPurchaseOrder("");
        spkHistoryEntity.setEntrySheet1("");
        spkHistoryEntity.setEntrySheet2("");
        spkHistoryEntity.setEntrySheet3("");
        spkHistoryEntity.setEntrySheet4("");
        spkHistoryEntity.setEntrySheet5("");
        spkHistoryEntity.setSpkNote(error);
        spkHistoryEntity.setStatus(SpkStatus.TOP_FAILED);
        try {
            spkHistoryRepository.add(spkHistoryEntity);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Check spk allowed to create entry sheet or not.
     * @return
     */
    private boolean isCreateable(SuratPerintahKerjaEntity spk, int termOfPayment){
        boolean createable = false;

        if(spk.getStatus() == SpkStatus.PURCHASE_ORDER_CREATED || spk.getStatus() == SpkStatus.GR1 || spk.getStatus() == SpkStatus.GR2 || spk.getStatus() == SpkStatus.GR3 || spk.getStatus() == SpkStatus.GR4){

            switch (termOfPayment){
                case 1:
                    if((spk.getTop1Amount() > 0 || spk.getTop1Percentage() >0) &&  spk.getEntrySheet1().equals("")) createable = true;
                    break;
                case 2:
                    if((spk.getTop2Amount() > 0 || spk.getTop2Percentage() >0) && spk.getEntrySheet2().equals("")) createable = true;
                    break;
                case 3:
                    if((spk.getTop3Amount() > 0 || spk.getTop3Percentage() >0) && spk.getEntrySheet3().equals("")) createable = true;
                    break;
                case 4:
                    if((spk.getTop4Amount() > 0 || spk.getTop4Percentage() >0) && spk.getEntrySheet4().equals("")) createable = true;
                    break;
                case 5:
                    if((spk.getTop5Amount() > 0 || spk.getTop5Percentage() >0) && spk.getEntrySheet5().equals("")) createable = true;
                    break;
                default:
                    createable = false;
                    break;
            }
        }
        return  createable;
    }

    // check SPK completed or not!
    private boolean isCompleted(SuratPerintahKerjaEntity spk){

        boolean top1 = true, top2 = true, top3 = true, top4 = true, top5 = true;

        if((spk.getTop1Amount() > 0 || spk.getTop1Percentage() > 0) && spk.getEntrySheet1().equals("")) top1 = false;
        if((spk.getTop2Amount() > 0 || spk.getTop2Percentage() > 0) && spk.getEntrySheet2().equals("")) top2 = false;
        if((spk.getTop3Amount() > 0 || spk.getTop3Percentage() > 0) && spk.getEntrySheet3().equals("")) top3 = false;
        if((spk.getTop4Amount() > 0 || spk.getTop4Percentage() > 0) && spk.getEntrySheet4().equals("")) top4 = false;
        if((spk.getTop5Amount() > 0 || spk.getTop5Percentage() > 0) && spk.getEntrySheet5().equals("")) top5 = false;

        return top1 && top2 && top3 && top4 && top5;
    }

}
