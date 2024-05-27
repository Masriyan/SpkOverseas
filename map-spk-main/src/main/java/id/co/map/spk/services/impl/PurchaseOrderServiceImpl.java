package id.co.map.spk.services.impl;

import id.co.map.spk.entities.SpkHistoryEntity;
import id.co.map.spk.entities.SuratPerintahKerjaEntity;
import id.co.map.spk.enums.ClientResponseStatus;
import id.co.map.spk.enums.SpkStatus;
import id.co.map.spk.integrator.PurchaseOrderIntegrator;
import id.co.map.spk.model.response.ClientSpkResponse;
import id.co.map.spk.model.sap.po.PurchaseOrderItab;
import id.co.map.spk.model.sap.po.PurchaseOrderResponse;
import id.co.map.spk.repositories.SpkRepository;
import id.co.map.spk.services.PurchaseOrderService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Awie on 10/2/2019
 */
@Service
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

    private static final Logger logger = LogManager.getLogger(PurchaseOrderServiceImpl.class);

    private final SpkRepository spkRepository;
    private final PurchaseOrderIntegrator purchaseOrderIntegrator;

    public PurchaseOrderServiceImpl(SpkRepository spkRepository, PurchaseOrderIntegrator purchaseOrderIntegrator) {
        this.spkRepository = spkRepository;
        this.purchaseOrderIntegrator = purchaseOrderIntegrator;
    }

    @Override
    public ClientSpkResponse createPurchaseOrder(SpkHistoryEntity spkHistory) {
        ClientSpkResponse clientSpkResponse = new ClientSpkResponse();
        SuratPerintahKerjaEntity spk =  spkRepository.findById(spkHistory.getSpkId());
        boolean created, released;
        String purchaseOrder, spkNote;

        if(spk.getStatus() == SpkStatus.INTERNAL_ORDER_CREATED || spk.getStatus() == SpkStatus.PURCHASE_ORDER_FAILED
                ||  ( spk.getSpkType().equalsIgnoreCase("Expense") && spk.getStatus() == SpkStatus.DONE_APPROVED)){
            try {
                //post purchase order to SAP
                List<PurchaseOrderItab> resp = purchaseOrderIntegrator.postPurchaseOrder(spk);
                //PurchaseOrderResponse responseSap = (PurchaseOrderResponse) purchaseOrderIntegrator.postPurchaseOrder(spk);
                PurchaseOrderResponse responseSap = new PurchaseOrderResponse();

                responseSap.setItabs(resp);

                //Purchase order creation in SAP takes 2 step (Create and release). Purchase order success if this 2 steps are success in SAP.
                created = responseSap.getItabs().stream().anyMatch(r -> r.getCode().equals("1001"));
                released = responseSap.getItabs().stream().anyMatch(r -> r.getCode().equals("1002"));

                //internal order creation in SAP success
                if(created && released){

                    purchaseOrder = responseSap.getItabs().get(0).getPoService();
                    spkNote = "Purchase Order creation success with number " + purchaseOrder;

                    //update purchase order number, status, datatimestamp & insert into spkhistory
                    spkRepository.updateStatusAndPurchaseOrder(spkHistory.getSpkId(), SpkStatus.PURCHASE_ORDER_CREATED.name(), purchaseOrder, spkNote, spkHistory.getUserName());
                    spk.setPurchaseOrder(purchaseOrder);

                    //set clientSpkResponse
                    clientSpkResponse.setResponseCode(ClientResponseStatus.PURCHASE_ORDER_CREATED.getCode());
                    clientSpkResponse.setResponseMessage(ClientResponseStatus.PURCHASE_ORDER_CREATED.getMessage());
                    clientSpkResponse.setSuratPerintahKerja(spk);

                }
                //purchase order creation failed in SAP
                else{

                    StringBuilder errMessage = new StringBuilder();
                    responseSap.getItabs().stream()
                            .filter(r -> r.getCode().equals("2001"))
                            .forEach(r -> errMessage.append(r.getMessage()));

                    //update purchase order number, status, datatimestamp & insert into spkhistory
                    spkRepository.updateStatusAndPurchaseOrder(spkHistory.getSpkId(), SpkStatus.PURCHASE_ORDER_FAILED.name(), "", errMessage.toString(), spkHistory.getUserName());

                    //set client spk response
                    clientSpkResponse.setResponseCode(ClientResponseStatus.PURCHASE_ORDER_FAILED.getCode());
                    clientSpkResponse.setResponseMessage(ClientResponseStatus.PURCHASE_ORDER_FAILED.getMessage() + " " + errMessage);
                    clientSpkResponse.setSuratPerintahKerja(spk);
                }
            }
            catch (ClassCastException e) {

                logger.error("******************* ERROR CREATE PURCHASE ORDER *******************");
                logger.debug("SPK ID        : {}", spkHistory.getSpkId());
                logger.debug("User          : {}", spkHistory.getUserName());
                logger.debug("Error Message : {}", e.getMessage());
                e.printStackTrace();
                logger.error("******************* ERROR CREATE PURCHASE ORDER *******************");

                clientSpkResponse.setResponseCode(ClientResponseStatus.SERVER_ERROR.getCode());
                clientSpkResponse.setResponseMessage(ClientResponseStatus.SERVER_ERROR.getMessage() + " " +e.getMessage());
                clientSpkResponse.setSuratPerintahKerja(null);
            }
            //Error while create purchase order
            catch (IOException e) {

                logger.error("******************* ERROR CREATE PURCHASE ORDER PO *******************");
                logger.debug("SPK ID        : {}", spkHistory.getSpkId());
                logger.debug("User          : {}", spkHistory.getUserName());
                logger.debug("Error Message : {}", e.getMessage());
                e.printStackTrace();
                logger.error("******************* ERROR CREATE PURCHASE ORDER PO *******************");

                clientSpkResponse.setResponseCode(ClientResponseStatus.SERVER_ERROR.getCode());
                clientSpkResponse.setResponseMessage(ClientResponseStatus.SERVER_ERROR.getMessage() + " " +e.getMessage());
                clientSpkResponse.setSuratPerintahKerja(null);
            }
            //Error in SQL Server.
            catch (SQLException | ClassNotFoundException e) {
                logger.error("******************* ERROR CREATE PURCHASE ORDER SQL *******************");
                logger.debug("SPK ID        : {}", spkHistory.getSpkId());
                logger.debug("User          : {}", spkHistory.getUserName());
                logger.debug("Error Message : {}", e.getMessage());
                e.printStackTrace();
                logger.error("******************* ERROR CREATE PURCHASE ORDER SQL *******************");

                clientSpkResponse.setResponseCode(ClientResponseStatus.SQL_ERROR.getCode());
                clientSpkResponse.setResponseMessage(ClientResponseStatus.SQL_ERROR.getMessage() + " " +e.getMessage());
                clientSpkResponse.setSuratPerintahKerja(null);
            //Error in JSON Parses.
            } catch (JSONException e) {
                logger.error("******************* ERROR CREATE PURCHASE ORDER JSON *******************");
                logger.debug("SPK ID        : {}", spkHistory.getSpkId());
                logger.debug("User          : {}", spkHistory.getUserName());
                logger.debug("Error Message : {}", e.getMessage());
                e.printStackTrace();
                logger.error("******************* ERROR CREATE PURCHASE ORDER JSON *******************");

                clientSpkResponse.setResponseCode(ClientResponseStatus.SQL_ERROR.getCode());
                clientSpkResponse.setResponseMessage(ClientResponseStatus.SQL_ERROR.getMessage() + " " +e.getMessage());
                clientSpkResponse.setSuratPerintahKerja(null);
            }
        }
        //can create Purchase order if status equals to Internal Order Created or Purchase Order Failed
        else {
            clientSpkResponse.setResponseCode(ClientResponseStatus.PURCHASE_ORDER_CREATION_NOT_ALLOWED.getCode());
            clientSpkResponse.setResponseMessage(ClientResponseStatus.PURCHASE_ORDER_CREATION_NOT_ALLOWED.getMessage());
            clientSpkResponse.setSuratPerintahKerja(null);
        }

        return clientSpkResponse;
    }
}
