package id.co.map.spk.utils;

import id.co.map.spk.entities.SuratPerintahKerjaEntity;
import id.co.map.spk.services.impl.SpkServiceImpl;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ExcelExporter {
    private static final Logger logger = LoggerFactory.getLogger(ExcelExporter.class);
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<SuratPerintahKerjaEntity> listSpk;

    public ExcelExporter(List<SuratPerintahKerjaEntity> listSpk) {
        this.listSpk = listSpk;
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet("Spk");
    }

    private void writeHeaderRow(){
        Row header = sheet.createRow(0);

        Cell headerCell = header.createCell(0);
        headerCell.setCellValue("Id");
        headerCell = header.createCell(1);
        headerCell.setCellValue("Description");
        headerCell = header.createCell(2);
        headerCell.setCellValue("Company");
        headerCell = header.createCell(3);
        headerCell.setCellValue("SBU");
        headerCell = header.createCell(4);
        headerCell.setCellValue("Store");
        headerCell = header.createCell(5);
        headerCell.setCellValue("Vendor");
        headerCell = header.createCell(6);
        headerCell.setCellValue("Asset Number");
        headerCell = header.createCell(7);
        headerCell.setCellValue("Internal Order");
        headerCell = header.createCell(8);
        headerCell.setCellValue("Purchase Order");
        headerCell = header.createCell(9);
        headerCell.setCellValue("Planned Amount");
        headerCell = header.createCell(10);
        headerCell.setCellValue("Total GR");
        headerCell = header.createCell(11);
        headerCell.setCellValue("Actual Amount");
        headerCell = header.createCell(12);
        headerCell.setCellValue("Status");

    }

    private void writeHeaderData(){
        int counter = 1;

        for (SuratPerintahKerjaEntity spk : listSpk){
            Row rowdata = sheet.createRow(counter);
            //logger.info("Spk Id on ct"+counter+"="+spk.getSpkId());

            Cell celldata = rowdata.createCell(0);
            celldata.setCellValue(spk.getSpkId());

            celldata = rowdata.createCell(1);
            celldata.setCellValue(spk.getSpkDescription());

            celldata = rowdata.createCell(2);
            celldata.setCellValue(spk.getCompanyId());

            celldata = rowdata.createCell(3);
            celldata.setCellValue(spk.getSbuCode());

            celldata = rowdata.createCell(4);
            celldata.setCellValue(spk.getStoreId());

            celldata = rowdata.createCell(5);
            celldata.setCellValue(spk.getVendorId());

            celldata = rowdata.createCell(6);
            celldata.setCellValue(spk.getAssetNo());

            celldata = rowdata.createCell(7);
            celldata.setCellValue(spk.getInternalOrder());

            celldata = rowdata.createCell(8);
            celldata.setCellValue(spk.getPurchaseOrder());

            celldata = rowdata.createCell(9);
            celldata.setCellValue(spk.getAmount());

            celldata = rowdata.createCell(10);
            celldata.setCellValue(spk.getGrAmount1() + spk.getGrAmount2() + spk.getGrAmount3() + spk.getGrAmount4()+ spk.getGrAmount5());

            celldata = rowdata.createCell(11);
            celldata.setCellValue(spk.getActualAmount());

            celldata = rowdata.createCell(12);
            celldata.setCellValue(spk.getStatus().toString());

            counter+=1;
        }
    }

    public void export(HttpServletResponse response) throws IOException {
        try {
            logger.info("======================= Export to Excel Start ===================");
            writeHeaderRow();
            writeHeaderData();

            ServletOutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();
            logger.info("======================= Export to Excel Finish ===================");
        }
        catch(IOException e)
        {
            logger.error(e.getMessage());
        }
    }
}
