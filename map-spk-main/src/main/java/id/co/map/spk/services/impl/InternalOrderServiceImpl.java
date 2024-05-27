package id.co.map.spk.services.impl;

import id.co.map.spk.emails.IoSpkMailService;
import id.co.map.spk.emails.SpkClosedMailService;
import id.co.map.spk.entities.SpkHistoryEntity;
import id.co.map.spk.entities.SuratPerintahKerjaEntity;
import id.co.map.spk.enums.ClientResponseStatus;
import id.co.map.spk.enums.SpkStatus;
import id.co.map.spk.integrator.InternalOrderIntegrator;
import id.co.map.spk.model.response.ClientSpkResponse;
import id.co.map.spk.model.sap.internalorder.InternalOrderItab;
import id.co.map.spk.model.sap.internalorder.InternalOrderResponse;
import id.co.map.spk.repositories.*;
import id.co.map.spk.services.EmailService;
import id.co.map.spk.services.InternalOrderService;
import id.co.map.spk.utils.AppProperty;
import id.co.map.spk.utils.TextFormatter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@Service
public class InternalOrderServiceImpl implements InternalOrderService {

    private static final Logger logger = LogManager.getLogger(InternalOrderServiceImpl.class);

    private final InternalOrderIntegrator internalOrderIntegrator;
    private final SpkRepository spkRepository;
    private final AppUserRepository appUserRepository;
    private final SpkHistoryRepository spkHistoryRepository;
    private final AppProperty appProperty;
    private final TextFormatter textFormatter;
    private final EmailService emailService;
    private final VendorRepository vendorRepository;
    private final StoreRepository storeRepository;
    private final SpkNextRoleRepository spkNextRoleRepository;

    public InternalOrderServiceImpl(InternalOrderIntegrator internalOrderIntegrator, SpkRepository spkRepository, AppUserRepository appUserRepository, SpkHistoryRepository spkHistoryRepository, AppProperty appProperty, TextFormatter textFormatter, EmailService emailService, VendorRepository vendorRepository, StoreRepository storeRepository, SpkNextRoleRepository spkNextRoleRepository) {
        this.internalOrderIntegrator = internalOrderIntegrator;
        this.spkRepository = spkRepository;
        this.appUserRepository = appUserRepository;
        this.spkHistoryRepository = spkHistoryRepository;
        this.appProperty = appProperty;
        this.textFormatter = textFormatter;
        this.emailService = emailService;
        this.vendorRepository = vendorRepository;
        this.storeRepository = storeRepository;
        this.spkNextRoleRepository = spkNextRoleRepository;
    }

    @Override
    public ClientSpkResponse createInternalOrder(SpkHistoryEntity spkHistory) {

        ClientSpkResponse clientSpkResponse = new ClientSpkResponse();
        SuratPerintahKerjaEntity spk =  spkRepository.findById(spkHistory.getSpkId());
        boolean created, released, inputed;
        String internalOrder, spkNote;

        if(spk.getStatus() == SpkStatus.VERIFIED || spk.getStatus() == SpkStatus.INTERNAL_ORDER_FAILED){
            try {
                //post internal order to SAP
                logger.debug("POST IO TO SAP");
                List<InternalOrderItab> responseSap = internalOrderIntegrator.postInternalOrder(spk);

                /*
                  Internal order creation in SAP tooks 3 step (Create, release, and input budget). Internal order success if this 3 steps are success in SAP.
                 */
                logger.debug("GET RESPONSE IO");
                created = responseSap.stream().anyMatch(r -> r.getCode().equals("1001"));
                released = responseSap.stream().anyMatch(r -> r.getCode().equals("1002"));
                inputed = responseSap.stream().anyMatch(r -> r.getCode().equals("1003"));

                //internal order creation in SAP success
                if(created && released && inputed){

                    internalOrder = responseSap.get(0).getInternalOrder();
                    spkNote = "Internal Order creation success with number " + internalOrder;

                    //update internal order number, status, datatimestamp & insert into spkhistory
                    spkRepository.updateStatusAndInternalOrder(spkHistory.getSpkId(), SpkStatus.INTERNAL_ORDER_CREATED.name(), internalOrder, spkNote, spkHistory.getUserName());
                    spk.setInternalOrder(internalOrder);

                    //set clientSpkResponse
                    clientSpkResponse.setResponseCode(ClientResponseStatus.INTERNAL_ORDER_CREATED.getCode());
                    clientSpkResponse.setResponseMessage(ClientResponseStatus.INTERNAL_ORDER_CREATED.getMessage());
                    clientSpkResponse.setSuratPerintahKerja(spk);

                    //update spk next role
                    spkNextRoleRepository.update(spkHistory.getSpkId(), 2);

                    //send email notification to operation after internal order created.
                    Thread IoSpkMailThread = new Thread(
                            new IoSpkMailService(appUserRepository, spk, spkHistory.getUserName(), spkHistoryRepository, appProperty, textFormatter, emailService, vendorRepository, storeRepository)
                    );
                    IoSpkMailThread.start();

                }
                //internal order creation ins SAP Failed
                else{

                    StringBuilder errMessage = new StringBuilder();
                    responseSap.stream()
                            .filter(r -> r.getCode().equals("2001"))
                            .forEach(r -> errMessage.append(r.getMessage()));

                    //update internal order number, status, datatimestamp & insert into spkhistory
                    spkRepository.updateStatusAndInternalOrder(spkHistory.getSpkId(), SpkStatus.INTERNAL_ORDER_FAILED.name(), "", errMessage.toString(), spkHistory.getUserName());

                    //set client spk response
                    clientSpkResponse.setResponseCode(ClientResponseStatus.INTERNAL_ORDER_FAILED.getCode());
                    clientSpkResponse.setResponseMessage(ClientResponseStatus.INTERNAL_ORDER_FAILED.getMessage() + " " + errMessage);
                    clientSpkResponse.setSuratPerintahKerja(spk);
                }
            }
            //Error while create internal order
            catch (IOException e) {

                logger.error("******************* ERROR CREATE INTERNAL ORDER IO *******************");
                logger.debug("SPK ID        : {}", spkHistory.getSpkId());
                logger.debug("User          : {}", spkHistory.getUserName());
                logger.debug("Error Message : {}", e.getMessage());
                e.printStackTrace();
                logger.error("******************* ERROR CREATE INTERNAL ORDER IO *******************");

                clientSpkResponse.setResponseCode(ClientResponseStatus.SERVER_ERROR.getCode());
                clientSpkResponse.setResponseMessage(ClientResponseStatus.SERVER_ERROR.getMessage() + " " +e.getMessage());
                clientSpkResponse.setSuratPerintahKerja(null);
            }
            //Error in SQL Server.
            catch (SQLException | ClassNotFoundException e) {
                logger.error("******************* ERROR CREATE INTERNAL ORDER SQL *******************");
                logger.debug("SPK ID        : {}", spkHistory.getSpkId());
                logger.debug("User          : {}", spkHistory.getUserName());
                logger.debug("Error Message : {}", e.getMessage());
                e.printStackTrace();
                logger.error("******************* ERROR CREATE INTERNAL ORDER SQL *******************");

                clientSpkResponse.setResponseCode(ClientResponseStatus.SQL_ERROR.getCode());
                clientSpkResponse.setResponseMessage(ClientResponseStatus.SQL_ERROR.getMessage() + " " +e.getMessage());
                clientSpkResponse.setSuratPerintahKerja(null);
            //Error Parsing JSON.
            } catch (JSONException e) {
                logger.error("******************* ERROR CREATE INTERNAL ORDER JSON *******************");
                logger.debug("SPK ID        : {}", spkHistory.getSpkId());
                logger.debug("User          : {}", spkHistory.getUserName());
                logger.debug("Error Message : {}", e.getMessage());
                e.printStackTrace();
                logger.error("******************* ERROR CREATE INTERNAL ORDER JSON *******************");

                clientSpkResponse.setResponseCode(ClientResponseStatus.SQL_ERROR.getCode());
                clientSpkResponse.setResponseMessage(ClientResponseStatus.SQL_ERROR.getMessage() + " " +e.getMessage());
                clientSpkResponse.setSuratPerintahKerja(null);
            }
        }
        //can create Internal order if status equals to Verified
        else {
            clientSpkResponse.setResponseCode(ClientResponseStatus.INTERNAL_ORDER_CREATION_NOT_ALLOWED.getCode());
            clientSpkResponse.setResponseMessage(ClientResponseStatus.INTERNAL_ORDER_CREATION_NOT_ALLOWED.getMessage());
            clientSpkResponse.setSuratPerintahKerja(null);
        }

        return clientSpkResponse;
    }

    @Override
    public ClientSpkResponse closeInternalOrder(SpkHistoryEntity spkHistory, SuratPerintahKerjaEntity spk) {

        ClientSpkResponse clientSpkResponse = new ClientSpkResponse();
        InternalOrderResponse responseSap = null;
        boolean closed = false;
        try
        {
            //InternalOrderResponse responseSap = internalOrderIntegrator.closeInternalOrder(spkHistory.getSpkId(), "123456987", spk.getClosedAmount());
            if(spk.getSpkType().equalsIgnoreCase("Asset"))
            {
                responseSap = internalOrderIntegrator.closeInternalOrder(spkHistory.getSpkId(), spk.getInternalOrder(), spk.getActualAmount());
                closed = responseSap.getItabs().stream().anyMatch(r -> r.getCode().equals("1001"));
            }
            else if(spk.getSpkType().equalsIgnoreCase("Expense"))
            {
                responseSap = new InternalOrderResponse();
                closed = true;
            }

            if(closed)
            {
                spk.setStatus(SpkStatus.CLOSED);
                spkRepository.updateStatus(spkHistory.getSpkId(), SpkStatus.CLOSED.name(), spkHistory.getSpkNote(), spkHistory.getUserName());

                clientSpkResponse.setResponseCode(ClientResponseStatus.INTERNAL_ORDER_CLOSED_SUCCESS.getCode());
                clientSpkResponse.setResponseMessage(ClientResponseStatus.INTERNAL_ORDER_CLOSED_SUCCESS.getMessage());
                clientSpkResponse.setSuratPerintahKerja(spk);

                //update spk next role
                spkNextRoleRepository.update(spkHistory.getSpkId(), 0);

                //send email notification to accountant
                Thread closedSpkMailService = new Thread(
                        new SpkClosedMailService(
                                spk,
                                vendorRepository,
                                storeRepository,
                                appProperty,
                                textFormatter,
                                spkHistoryRepository,
                                appUserRepository,
                                emailService
                        ));
                closedSpkMailService.start();
            }
            else
            {
                spkHistory.setStatus(SpkStatus.INTERNAL_ORDER_CLOSE_FAILED);
                StringBuilder error = new StringBuilder();

                responseSap.getItabs().stream().forEach(r -> {
                    error.append(r.getMessage());
                });

                //update spk status
                spkRepository.updateStatus(spkHistory.getSpkId(), spkHistory.getStatus().name(), error.toString(), spkHistory.getUserName());

                //update spk next role
                spkNextRoleRepository.update(spkHistory.getSpkId(), 2);

                clientSpkResponse.setResponseCode(ClientResponseStatus.INTERNAL_ORDER_CLOSED_FAILED.getCode());
                clientSpkResponse.setResponseMessage(ClientResponseStatus.INTERNAL_ORDER_CLOSED_FAILED.getMessage() + ". " + error.toString());
            }
        }
        catch (IOException e) {
            logger.error("******************* ERROR CLOSE INTERNAL ORDER *******************");
            logger.debug("SPK ID        : {}", spkHistory.getSpkId());
            logger.debug("Error Message : {}", e.getMessage());
            e.printStackTrace();
            logger.error("******************* ERROR CLOSE INTERNAL ORDER *******************");

            clientSpkResponse.setResponseCode(ClientResponseStatus.SERVER_ERROR.getCode());
            clientSpkResponse.setResponseMessage(ClientResponseStatus.SERVER_ERROR.getMessage() + " " +e.getMessage());
            clientSpkResponse.setSuratPerintahKerja(null);
        }
        catch (ClassNotFoundException | SQLException e){
            logger.error("******************* ERROR CLOSE INTERNAL ORDER *******************");
            logger.debug("SPK ID        : {}", spkHistory.getSpkId());
            logger.debug("Error Message : {}", e.getMessage());
            e.printStackTrace();
            logger.error("******************* ERROR CLOSE INTERNAL ORDER *******************");

            clientSpkResponse.setResponseCode(ClientResponseStatus.SQL_ERROR.getCode());
            clientSpkResponse.setResponseMessage(ClientResponseStatus.SQL_ERROR.getMessage() + " " +e.getMessage());
            clientSpkResponse.setSuratPerintahKerja(null);
        }

        return clientSpkResponse;
    }
}
