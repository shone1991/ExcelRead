package process;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelReader {
    FileInputStream excelFile=null;
    List<String> waglist=null;

    public List<String> numwag(int startFrom, int endTo, String namefile) throws IOException {
        try{
            waglist=new ArrayList<>();
            excelFile = new FileInputStream(new File(namefile));
            Workbook workbook = new XSSFWorkbook(excelFile);
            Sheet datatypeSheet = workbook.getSheetAt(0);
            Row row;
            for(int i=startFrom - 1; i<=endTo; i++) {
                //Thread.sleep(2000);
                row = datatypeSheet.getRow(i);
                Cell curcell=row.getCell(0);
                curcell.setCellType(CellType.STRING);
                String numv=curcell.getStringCellValue();
                waglist.add(numv);
            }

            }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }  finally {
            // closes the stream and releases resources associated
            if(excelFile!=null)
                excelFile.close();

        }
        return waglist;
    }
}
