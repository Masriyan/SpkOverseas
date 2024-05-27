package id.co.map.spk.api;

import id.co.map.spk.entities.SpkHistoryEntity;
import id.co.map.spk.entities.SuratPerintahKerjaEntity;
import id.co.map.spk.enums.SpkStatus;
import id.co.map.spk.model.response.ClientResponse;
import id.co.map.spk.model.response.ClientSpkResponse;
import id.co.map.spk.repositories.SpkRepository;
import id.co.map.spk.services.EntrySheetService;
import id.co.map.spk.services.InternalOrderService;
import id.co.map.spk.services.PurchaseOrderService;
import id.co.map.spk.services.SpkService;
import id.co.map.spk.utils.AppProperty;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(SpkApi.BASE_URL)
class SpkApi {

    static final String BASE_URL = "api/surat-perintah-kerja";
    private final SpkService spkService;
    private final InternalOrderService internalOrderService;
    private final AppProperty appProperty;
    private final PurchaseOrderService purchaseOrderService;
    private final EntrySheetService entrySheetService;
    private final SpkRepository spkRepository;

    public SpkApi(SpkService spkService, InternalOrderService internalOrderService, AppProperty appProperty, PurchaseOrderService purchaseOrderService, EntrySheetService entrySheetService, SpkRepository spkRepository) {
        this.spkService = spkService;
        this.internalOrderService = internalOrderService;
        this.appProperty = appProperty;
        this.purchaseOrderService = purchaseOrderService;
        this.entrySheetService = entrySheetService;
        this.spkRepository = spkRepository;
    }

    @PostMapping("")
    public ClientSpkResponse addSpk (@RequestBody SuratPerintahKerjaEntity spk, Principal principal){

        User user = (User) ((Authentication) principal).getPrincipal();
        return spkService.addSpk(spk, user.getUsername());
    }

    @PutMapping("/upd")
    public ClientSpkResponse editSpk (@RequestBody SuratPerintahKerjaEntity spk, Principal principal){

        User user = (User) ((Authentication) principal).getPrincipal();
        return spkService.editSpk(spk, user.getUsername());
    }

    @PutMapping("")
    public ClientSpkResponse updateSpk(@RequestBody SpkHistoryEntity spkHistoryEntity, Principal principal){
        User user = (User) ((Authentication) principal).getPrincipal();
        spkHistoryEntity.setUserName(user.getUsername());

        return spkService.updateSpkStatus(spkHistoryEntity);
    }

    @PutMapping("/internal-order")
    public ClientSpkResponse createInternalOrder(@RequestBody SpkHistoryEntity spkHistoryEntity, Principal principal){
        User user = (User) ((Authentication) principal).getPrincipal();
        spkHistoryEntity.setUserName(user.getUsername());

        return internalOrderService.createInternalOrder(spkHistoryEntity);
    }

    @PostMapping("/quotation")
    public ResponseEntity<?> uploadSpkQuotation(@RequestParam("quotationFile") MultipartFile quotationFile){

        String savedFileName, extension;
        String[] fileFrags;

        if (quotationFile.isEmpty()) {
            return new ResponseEntity("please select a file!", HttpStatus.OK);
        }

        try {

            fileFrags = quotationFile.getOriginalFilename().split("\\.");
            extension = fileFrags[fileFrags.length-1];

            savedFileName = UUID.randomUUID().toString() + "." + extension;

            // Get the file and save it somewhere
            byte[] bytes = quotationFile.getBytes();
            Path path = Paths.get(appProperty.getSpkQuotationPath() + savedFileName);
            Files.write(path, bytes);

            return new ResponseEntity(savedFileName, new HttpHeaders(), HttpStatus.OK);
        } catch (IOException e) {

            return new ResponseEntity(e.getMessage(), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("")
    public Map<String, Object> findSpkPagination(
            Principal principal,
            @RequestParam(name ="spkId", required = false) String spkId,
            @RequestParam(name ="spkDescription", required = false) String spkDescription,
            @RequestParam(name ="companyId", required = false) String companyId,
            @RequestParam(name ="sbuId", required = false) String sbuId,
            @RequestParam(name ="approvedQuotationDate", required = false) String approvedQuotationDate,
            @RequestParam(name ="storeId", required = false) String storeId,
            @RequestParam(name ="vendorId", required = false) String vendorId,
            @RequestParam(name ="status", required = false) String status,
            @RequestParam(name = "draw") Integer draw, //required to avoid paging bugs!
            @RequestParam(name = "pageNumber") Integer pageNumber
    ){
        User user = (User) ((Authentication) principal).getPrincipal();
        return spkService.findSpkPagination(user.getUsername(), spkId, spkDescription, companyId, sbuId, approvedQuotationDate, storeId, vendorId, status , pageNumber, 10, draw);
    }



    @GetMapping("/excel")
    public void createExcel(
            HttpServletResponse response,
            Principal principal,
            @RequestParam(name ="spkId", required = false) String spkId,
            @RequestParam(name ="spkDescription", required = false) String spkDescription,
            @RequestParam(name ="companyId", required = false) String companyId,
            @RequestParam(name ="sbuId", required = false) String sbuId,
            @RequestParam(name ="approvedQuotationDate", required = false) String approvedQuotationDate,
            @RequestParam(name ="storeId", required = false) String storeId,
            @RequestParam(name ="vendorId", required = false) String vendorId,
            @RequestParam(name ="status", required = false) String status
    ) throws IOException {

        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=spk.xlsx";

        response.setHeader(headerKey,headerValue);

        User user = (User) ((Authentication) principal).getPrincipal();
        spkService.createExc(response, user.getUsername(), spkId, spkDescription, companyId, sbuId, approvedQuotationDate, storeId, vendorId, status);
    }

    @PutMapping("/purchase-order")
    public ClientSpkResponse createPurchaseOrder(@RequestBody SpkHistoryEntity spkHistoryEntity, Principal principal){

        User user = (User) ((Authentication) principal).getPrincipal();
        spkHistoryEntity.setUserName(user.getUsername());

        return purchaseOrderService.createPurchaseOrder(spkHistoryEntity);
    }

    @PutMapping("/entry-sheet")
    public ClientResponse createEntrySheet(@RequestBody SpkHistoryEntity spkHistoryEntity, Principal principal){
        User user = (User) ((Authentication) principal).getPrincipal();
        spkHistoryEntity.setUserName(user.getUsername());

        return  entrySheetService.createEntrySheet(spkHistoryEntity);
    }

    @PutMapping("/asset-class")
    public ClientSpkResponse changeAssetClass(@RequestBody SpkHistoryEntity spkHistoryEntity, Principal principal){
        User user = (User) ((Authentication) principal).getPrincipal();
        spkHistoryEntity.setUserName(user.getUsername());

        return spkService.updateSpkStatus(spkHistoryEntity);
    }

    @PutMapping("close-internal-order")
    public ClientSpkResponse closeInternalOrder(@RequestBody SpkHistoryEntity spkHistoryEntity, Principal principal){
        User user = (User) ((Authentication) principal).getPrincipal();
        spkHistoryEntity.setUserName(user.getUsername());
        SuratPerintahKerjaEntity spk = spkRepository.findById(spkHistoryEntity.getSpkId());

        return internalOrderService.closeInternalOrder(spkHistoryEntity, spk);
    }

}