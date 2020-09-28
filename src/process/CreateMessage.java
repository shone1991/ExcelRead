package process;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class CreateMessage {
    public static final String MESSAGE_PART_HEAD = "(:2790 73 ";
    public static final String MESSAGE_PART_TALE = ":)";
    public static boolean createFile(String numwag, String date1, String date2) throws IOException {
        boolean res=false;
        Properties p=new PropertyUtil().getProperties("conf.properties");
        String addr=p.getProperty("shared-resource-address");
        System.out.println(addr);
        String dt="dt-"+date1+"-"+date2;
        String fileData = MESSAGE_PART_HEAD+numwag+" "+dt+MESSAGE_PART_TALE;
        Path pth=Paths.get(addr);
        try (OutputStream outputStream = new FileOutputStream(pth + "\\"  + "01020000.txt");
            Writer outputStreamWriter = new OutputStreamWriter(outputStream, "Cp866")) {
            outputStreamWriter.write(fileData);
            res=true;
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
            res=false;
        } catch (IOException ex) {
            ex.printStackTrace();
            res=false;
        }
        return res;
    }
}
