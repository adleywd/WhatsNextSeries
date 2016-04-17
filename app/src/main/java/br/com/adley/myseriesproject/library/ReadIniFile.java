package br.com.adley.myseriesproject.library;

import org.ini4j.Ini;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;

/**
 * Created by Adley on 17/04/2016.
 * Read a ini file base on keys
 */
public class ReadIniFile {
    //--- Get Key or Value from a .ini File ---//
    private String configurationFilePath;
    private final String defaultConfigPath = "apiconfig.ini"; //Doesn't Work
    private HashMap<String, String> iniFile;

    public HashMap<String, String> getIniFile() {
        return iniFile;
    }

    public void setIniFile(HashMap<String, String> iniFile) {
        this.iniFile = iniFile;
    }

    public ReadIniFile(String iniConfFilePath, String... keys) {
        configurationFilePath = iniConfFilePath == null ? defaultConfigPath : iniConfFilePath;
        Ini ini = loadIni();
        if (ini != null) {
            if (keys.length > 0) {
                for (String key : keys) {
                    iniFile.put(key, ini.get(key).toString());
                }
            }
        }
    }

    public ReadIniFile(String iniConfFilePath, String key) {
        configurationFilePath = iniConfFilePath == null ? defaultConfigPath : iniConfFilePath;
        Ini ini = loadIni();
        if (ini != null) {
            iniFile.put(key, ini.get(key).toString());
        }
    }

    public ReadIniFile(String... keys) {
        configurationFilePath = defaultConfigPath;
        Ini ini = loadIni();
        if (ini != null) {
            if (keys.length > 0) {
                for (String key : keys) {
                    iniFile.put(key, ini.get(key).toString());
                }
            }
        }
    }

    public ReadIniFile(String key) {
        configurationFilePath = defaultConfigPath;
        Ini ini = loadIni();
        if (ini != null) {
            iniFile.put(key, ini.get(key).toString());
        }
    }

    public Ini loadIni() {
        try {
            File file = new File(configurationFilePath);
            Ini ini = new Ini(new File(file.getAbsolutePath()));
            return ini;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public HashMap<String, String> getAllIni() {
        return iniFile;
    }

    public String getSpecificValueIni(String key) {
        return iniFile.get(key);
    }

    public Collection<String> getAllValues() {
        return iniFile.values();
    }

    public String getSpecificKeyValueIniToString(String key) {
        return key + " = " + iniFile.get(key);
    }
}
