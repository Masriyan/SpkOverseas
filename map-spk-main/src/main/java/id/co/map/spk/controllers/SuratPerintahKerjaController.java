package id.co.map.spk.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.org.apache.xpath.internal.operations.Bool;
import id.co.map.spk.entities.*;
import id.co.map.spk.enums.PoType;
import id.co.map.spk.enums.SpkStatus;
import id.co.map.spk.model.response.ClientSpkResponse;
import id.co.map.spk.repositories.*;
import id.co.map.spk.services.ApprovalRulesService;
import id.co.map.spk.services.SpkService;
import id.co.map.spk.services.UserService;
import id.co.map.spk.utils.AppProperty;
import id.co.map.spk.utils.BahasaNumberToWords;
import id.co.map.spk.utils.TextFormatter;
import id.co.map.spk.utils.ZXingHelper;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(SuratPerintahKerjaController.BASE_URL)
class SuratPerintahKerjaController {

    public static final String BASE_URL = "surat-perintah-kerja";

    private final CompanyRepository companyRepository;
    private final SbuRepository sbuRepository;
    private final AssetClassRepository assetClassRepository;
    private final SpkRepository spkRepository;
    private final SpkHistoryRepository spkHistoryRepository;
    private final VendorRepository vendorRepository;
    private final StoreRepository storeRepository;
    private final CostCenterRepository costCenterRepository;
    private final ApprovalRulesService approvalRulesService;
    private final AppProperty appProperty;
    private final UserCompanyRepository userCompanyRepository;
    private final SpkStatusRepository spkStatusRepository;
    private final SpkService spkService;
    private final UserService userService;

    public SuratPerintahKerjaController(SpkService spkService,CompanyRepository companyRepository,
                                        SbuRepository sbuRepository, AssetClassRepository assetClassRepository,
                                        SpkRepository spkRepository, SpkHistoryRepository spkHistoryRepository,
                                        VendorRepository vendorRepository, StoreRepository storeRepository,
                                        CostCenterRepository costCenterRepository, ApprovalRulesService approvalRulesService,
                                        AppProperty appProperty, UserCompanyRepository userCompanyRepository,
                                        SpkStatusRepository spkStatusRepository, UserService userService) {
        this.spkService = spkService;
        this.companyRepository = companyRepository;
        this.sbuRepository = sbuRepository;
        this.assetClassRepository = assetClassRepository;
        this.spkRepository = spkRepository;
        this.spkHistoryRepository = spkHistoryRepository;
        this.vendorRepository = vendorRepository;
        this.storeRepository = storeRepository;
        this.costCenterRepository = costCenterRepository;
        this.approvalRulesService = approvalRulesService;
        this.appProperty = appProperty;
        this.userCompanyRepository = userCompanyRepository;
        this.spkStatusRepository = spkStatusRepository;
        this.userService = userService;
    }

    @GetMapping("/create-new-spk")
    public ModelAndView showCreateNewSpk(ModelAndView m, Principal principal){

        User user = (User) ((Authentication) principal).getPrincipal();

        m.addObject("companies", companyRepository.findByUserName(user.getUsername()));
        m.addObject("assets", assetClassRepository.findAllAssetClass());
        m.setViewName("surat-perintah-kerja/create-new-spk");

        return m;
    }

    @GetMapping("/create-new-spk2")
    public ModelAndView showCreateNewSpk2(ModelAndView m, Principal principal){

        User user = (User) ((Authentication) principal).getPrincipal();

        m.addObject("companies", companyRepository.findByUserName(user.getUsername()));
        m.addObject("assets", assetClassRepository.findAllAssetClass());
        m.setViewName("surat-perintah-kerja/create-new-spk2");

        return m;
    }

    @GetMapping("/{spkId}")
    public ModelAndView showSpkDetailById(ModelAndView m, Principal principal, @PathVariable(name="spkId") String spkId){

        User user = (User) ((Authentication) principal).getPrincipal();
        SuratPerintahKerjaEntity spk = spkRepository.findById(spkId);
        List<UserCompanyEntity> userCompanies = userCompanyRepository.findByUserName(user.getUsername());

        if(userCompanies.stream().filter(uc -> uc.getSbuId().equals(spk.getSbuId())).findFirst().isPresent()){

            List<SpkHistoryEntity> spkHistories = spkHistoryRepository.findBySpkId(spkId);
            CompanyEntity company = companyRepository.findByCompanyId(spk.getCompanyId());
            VendorEntity vendor = vendorRepository.findByVendorIdAndCompanyId(spk.getVendorId(), spk.getCompanyId());
            StoreEntity store = storeRepository.findByStoreIdAndCompanyId(spk.getStoreId(), spk.getCompanyId());
            SbuEntity sbu = sbuRepository.findBySbuId(spk.getSbuId());
            CostCenterEntity costCenter = costCenterRepository.findByIdAndCompany(spk.getCostCenterId(), spk.getCompanyId());
            AssetClassEntity assetClass = assetClassRepository.findById(spk.getAssetClassId());
            String spkAction;

            if(spk.getStatus() == SpkStatus.CLOSE_REQUEST || spk.getStatus() == SpkStatus.CLOSE_APPROVED)
                spkAction = approvalRulesService.getSpkCloseAction(user.getUsername(), spk, spkHistories, assetClass.getAssetTypeId());
            else
                spkAction = approvalRulesService.getSpkAction(user.getUsername(), spk, spkHistories, assetClass.getAssetTypeId(), PoType.APPROVAL);

            spk.setPercentageToAmount();

            m.addObject("spk", spk);
            m.addObject("spkHistories", spkHistories);
            m.addObject("company", company);
            m.addObject("sbu", sbu);
            m.addObject("vendor", vendor);
            m.addObject("store", store);
            m.addObject("costCenter", costCenter);
            m.addObject("assetClass", assetClass);
            m.addObject("spkAction", spkAction);
            m.setViewName("surat-perintah-kerja/spk-by-id");
        }
        else{
            return new ModelAndView("redirect:/errors/403");
        }

        return  m;
    }

    @GetMapping("/edit/{spkId}")
    public ModelAndView editSpkDetailById(ModelAndView m, Principal principal, @PathVariable(name="spkId") String spkId){

        User user = (User) ((Authentication) principal).getPrincipal();
        SuratPerintahKerjaEntity spk = spkRepository.findById(spkId);
        List<UserCompanyEntity> userCompanies = userCompanyRepository.findByUserName(user.getUsername());

        if(userCompanies.stream().filter(uc -> uc.getSbuId().equals(spk.getSbuId())).findFirst().isPresent()){

            CompanyEntity company = companyRepository.findByCompanyId(spk.getCompanyId());
            VendorEntity vendor = vendorRepository.findByVendorIdAndCompanyId(spk.getVendorId(), spk.getCompanyId());
            StoreEntity store = storeRepository.findByStoreIdAndCompanyId(spk.getStoreId(), spk.getCompanyId());
            SbuEntity sbu = sbuRepository.findBySbuId(spk.getSbuId());
            CostCenterEntity costCenter = costCenterRepository.findByIdAndCompany(spk.getCostCenterId(), spk.getCompanyId());
            AssetClassEntity assetClass = assetClassRepository.findById(spk.getAssetClassId());

            List<AssetClassEntity> assts = assetClassRepository.findAllAssetClass()
                    .stream()
                    .filter(r -> r.getAssetClassId().equalsIgnoreCase(assetClass.getAssetClassId()) == false)
                    .collect(Collectors.toList());

            spk.setPercentageToAmount();

            m.addObject("comps", companyRepository.findByUserName(user.getUsername()));
            m.addObject("assets", assts);
            m.addObject("spk", spk);
            m.addObject("company", company);
            m.addObject("sbu", sbu);
            m.addObject("vendor", vendor);
            m.addObject("store", store);
            m.addObject("costCenter", costCenter);
            m.addObject("assetClass", assetClass);
            m.setViewName("surat-perintah-kerja/update-spk");
        }
        else{
            return new ModelAndView("redirect:/errors/403");
        }

        return  m;
    }
    

    @GetMapping("/upd/{datareq}")
    public ModelAndView updateSpkGet(
            @PathVariable(name ="datareq") String datareq
    ){
        datareq = datareq.replace('^','.');
        String[] req = datareq.split(",",5);

        SpkHistoryEntity spkHistoryEntity = new SpkHistoryEntity();
        spkHistoryEntity.setUserName(req[0]);
        spkHistoryEntity.setSpkId(req[1]);
        spkHistoryEntity.setSpkNote("");

        SpkStatus stat = SpkStatus.valueOf(req[2]);
        spkHistoryEntity.setStatus(stat);

        ClientSpkResponse clrsp = spkService.updateSpkStatus(spkHistoryEntity);

        ModelAndView mod = new ModelAndView();
        if(clrsp.getResponseCode() == 1001)
        {
            //mod.setViewName("/update-spk-success");
        	//---- Update Milton 20221220
            mod.setViewName("redirect:/update-spk-success");
        }
        else if(clrsp.getResponseCode() == 4012)
        {
            //mod.setViewName("/update-spk-fail-has-approv");
        	//---- Update Milton 20221220
            mod.setViewName("redirect:/update-spk-fail-has-approv");
        }
        else
        {
            //mod.setViewName("/update-spk-fail");
        	//---- Update Milton 20221220
            mod.setViewName("redirect:/update-spk-fail");
        }

        return mod;
    }

    @GetMapping("/{spkId}/print")
    public ModelAndView printSpkById(ModelAndView m, @PathVariable(name="spkId") String spkId){

        SuratPerintahKerjaEntity spk = spkRepository.findById(spkId);
        List<SpkHistoryEntity> spkHistories = spkHistoryRepository.findByIdForPrint(spkId);

        //get first approver and put it on left signature
        SpkHistoryEntity firstApprover = spkHistories.get(spkHistories.size()-1);

        CountryEntity dataPrint = companyRepository.findCountryByCompanyId(spk.getCompanyId());
        boolean isEn = dataPrint.getLabel1().contains("@en@");
        boolean isId = dataPrint.getLabel1().contains("@id@");
        String untukLabel1 = "";
        TextFormatter textformat = new TextFormatter();
        BahasaNumberToWords bahasaNumberToWords = new BahasaNumberToWords();
        untukLabel1 = dataPrint.getLabel1().replace("@quotation@",textformat.toBahasaDateFormat(spk.getApprovedQuotationDate()));
        untukLabel1 = untukLabel1.replace("@desc@" , spk.getSpkDescription());
        untukLabel1 = untukLabel1.replace("@amount@" , textformat.toRupiahFormat(spk.getAmount()));

        if(isId){
            untukLabel1 = untukLabel1.replace("@amounttext@" , bahasaNumberToWords.convert(spk.getAmount()));
        }else{
            untukLabel1 = untukLabel1.replace("@amounttext@" , bahasaNumberToWords.convertInggris(spk.getAmount()));
        }

        untukLabel1 = untukLabel1.replace("@en@" , "");
        untukLabel1 = untukLabel1.replace("@id@" , "");
        dataPrint.setLabel1(untukLabel1);

        String untukLabel8 = dataPrint.getLabel8().replace("@desc@" , spk.getSpkDescription());
        dataPrint.setLabel8(untukLabel8);

        //another approver on right signature
        spkHistories.remove(firstApprover);
        Collections.reverse(spkHistories);
        m.addObject("spk", spk);
        m.addObject("company", companyRepository.findByCompanyId(spk.getCompanyId()));
        m.addObject("print", dataPrint);
        m.addObject("asset", assetClassRepository.findById(spk.getAssetClassId()));
        m.addObject("store", storeRepository.findByStoreIdAndCompanyId(spk.getStoreId(), spk.getCompanyId()));
        m.addObject("vendor", vendorRepository.findByVendorIdAndCompanyId(spk.getVendorId(), spk.getCompanyId()));
        m.addObject("firstApprover", firstApprover);
        m.addObject("spkHistories", spkHistories);
        m.addObject("qrCodeUri", appProperty.getBaseUrl() + "/surat-perintah-kerja/" + spkId + "/qr-code");
        if(spk.getSpkType().equalsIgnoreCase("Asset")) {
            m.setViewName("surat-perintah-kerja/spk-print");
        }else{
            m.setViewName("surat-perintah-kerja/spk-print-ex");
        }
        return m;
    }

    @GetMapping("/{spkId}/quotation")
    public ResponseEntity<byte[]> viewQuotationBySpk(@PathVariable(name="spkId") String spkId){

        SuratPerintahKerjaEntity spk = spkRepository.findById(spkId);
        byte[] pdfBytes = new byte[0];
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        String quotationFile = spk.getQuotationFile();

        try {
            pdfBytes = Files.readAllBytes(Paths.get(appProperty.getSpkQuotationPath() + quotationFile));
        } catch (IOException e) {
            e.printStackTrace();
        }

        headers.add("content-disposition", "inline;filename=" + quotationFile);

        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(pdfBytes, headers, HttpStatus.OK);
        return response;
    }

    @GetMapping({"", "/"})
    public ModelAndView showListOfSpk(ModelAndView m, Principal principal){
        User user = (User) ((Authentication) principal).getPrincipal();
        String username = user.getUsername();

        m.addObject("companies", companyRepository.findByUserName(username));
        m.addObject("stores", storeRepository.findByUsername(username));
        m.addObject("vendors", vendorRepository.findByUsername(username));
        m.addObject("sbus", sbuRepository.findByUsername(username));
        m.addObject("status", spkStatusRepository.findAll());

        m.setViewName("surat-perintah-kerja/list-of-spk");
        return m;
    }

    @GetMapping("LoginSSO")
    public ModelAndView untuksso(
        ModelAndView m,
        Principal principal,
        @RequestParam(name ="token", required = true) String Token,
        @RequestParam(name ="urlVerified", required = true) String UrlVerified
    ){
        //hit api jika 200 redirect ke home page
        RestTemplate restTemplate = new RestTemplate();

        // Mengirim permintaan POST dan mendapatkan respons
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(UrlVerified + "?token=" + Token, HttpEntity.EMPTY, String.class);

        // Mengambil status kode HTTP
        int statusCode = responseEntity.getStatusCodeValue();
        System.err.println(responseEntity.getBody());
        System.err.println(statusCode);
        // Mengambil body dari respons API
        String responseBody = responseEntity.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(responseBody);
        } catch (Exception e) {
            System.err.println("masuk ke sini jika error");
            e.printStackTrace();
        }
        System.err.println(jsonNode);
        String password = "";
        String username = "";
        if(statusCode == 200){
            System.err.println("masuk ke sini");
            System.err.println(jsonNode.get("objectValue").get("email").asText());
            m.addObject("username", jsonNode.get("objectValue").get("email").asText());
            username = jsonNode.get("objectValue").get("email").asText();
            m.addObject("password", jsonNode.get("objectValue").get("id").asText());
            password = jsonNode.get("objectValue").get("id").asText();
            m.addObject("darisso", true);
        }


        //update kolom password di table appuser
        userService.replacePassword(password, username);

        m.setViewName("auth/login");
        return m;
    }

    @GetMapping("/{spkId}/qr-code")
    public void genQrCode(@PathVariable(name="spkId") String spkId, HttpServletResponse response) throws Exception{

        response.setContentType("image/png");
        String spkUri = appProperty.getBaseUrl() + "/surat-perintah-kerja/" + spkId;
        OutputStream outputStream = response.getOutputStream();
        outputStream.write(ZXingHelper.getQRCodeImage(spkUri, 150, 150));
        outputStream.flush();
        outputStream.close();
    }
}
