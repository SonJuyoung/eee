package ieetu.common.utils;

import org.springframework.core.io.ClassPathResource;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.LinkedHashMap;


public class PropertiesLoader {
    public static Object getProperties(String key) {
        Yaml yaml = new Yaml();
        LinkedHashMap<String, Object> yamlMaps = new LinkedHashMap<>();
        ClassPathResource resource = new ClassPathResource("application.yml");
        try (InputStream inputStream = resource.getInputStream()){
            yamlMaps = yaml.load(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getValue(yamlMaps, key);
    }

    private static Object getValue(LinkedHashMap yamlMaps, String key) {
        int index = key.indexOf(".");
        if(index == -1) {
            return yamlMaps.get(key);
        }

        Object obj = yamlMaps.get(key.substring(0, index));
        if(obj instanceof LinkedHashMap) {
            return getValue((LinkedHashMap) obj, key.substring(index + 1));
        }

        return obj;
    }
}
