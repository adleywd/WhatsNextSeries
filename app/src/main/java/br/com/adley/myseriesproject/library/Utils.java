package br.com.adley.myseriesproject.library;

import android.net.Uri;

import org.ini4j.Ini;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by adley on 16/04/16.
 * Class with utilities to project.
 */
public class Utils {

    //--- Append URI ---//
    public static Uri appendUri(String uri, HashMap<String, String> queryParams) {
        Uri newUri = Uri.parse(uri);
        if (!queryParams.isEmpty()) {
            for (Map.Entry<String, String> entry : queryParams.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                newUri.buildUpon().appendQueryParameter(key, value).build();
            }
            return newUri;
        }
        return null;
    }

    //--- Get Key or Value from a .ini File ---//
    public class ReadIniConfigFile {
        private String configurationFilePath;
        private final String defaultConfigPath = "apiconfig.ini"; //Doesn't Work
        private HashMap<String, String> iniFile;

        public HashMap<String, String> getIniFile() {
            return iniFile;
        }

        public void setIniFile(HashMap<String, String> iniFile) {
            this.iniFile = iniFile;
        }

        public ReadIniConfigFile(String iniConfFilePath, String... keys) {
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

        public ReadIniConfigFile(String iniConfFilePath, String key) {
            configurationFilePath = iniConfFilePath == null ? defaultConfigPath : iniConfFilePath;
            Ini ini = loadIni();
            if (ini != null) {
                iniFile.put(key, ini.get(key).toString());
            }
        }

        public ReadIniConfigFile(String... keys) {
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

        public ReadIniConfigFile(String key) {
            //Path path = Paths.get("loremipsum.txt").toRealPath(LinkOption.NOFOLLOW_LINKS);
            configurationFilePath = defaultConfigPath;
            Ini ini = loadIni();
            if (ini != null) {
                iniFile.put(key, ini.get(key).toString());
            }
        }

        public Ini loadIni() {
            try {
                URL fileURL = getClass().getClassLoader().getResource(configurationFilePath);
                Ini ini = new Ini(new File(fileURL.toString()));
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
}
