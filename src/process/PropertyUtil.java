package process;

import java.io.*;
import java.util.Properties;

public class PropertyUtil {
    public Properties getProperties(String filename){
        Properties prop = null;
        try (InputStream input = this.getClass().getClassLoader().getResourceAsStream(filename)) {
            prop = new Properties();

            if (input == null) {
                System.out.println("Sorry, unable to find " + filename);
                return null;
            }

            prop.load(input);
        } catch (IOException ex){
            ex.printStackTrace();
        }
        return prop;
    }
    public void updateValue(String key, String value) throws IOException {
        InputStream ip =  this.getClass().getClassLoader().getResourceAsStream("conf.properties");
        Properties propp=new Properties();
        propp.load(ip);
        ip.close();

        String fileName = "conf.properties";
        ClassLoader classLoader = this.getClass().getClassLoader();

        File file = new File(classLoader.getResource(fileName).getFile());
        FileOutputStream fout=new FileOutputStream(file);
        propp.setProperty(key,value);
        propp.store(fout,null);
        fout.close();
    }
}
