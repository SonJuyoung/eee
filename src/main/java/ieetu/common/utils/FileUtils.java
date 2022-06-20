package ieetu.common.utils;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class FileUtils {
    /**
     * fileMap에서 데이터가 없는 파일 제외
     *
     * @param files
     * @return
     */
    public static Map<String, MultipartFile> parseFileMap(Map<String, MultipartFile> files) {
        Iterator<Map.Entry<String, MultipartFile>> itr = files.entrySet().iterator();
        while (itr.hasNext()) {
            Map.Entry<String, MultipartFile> entry = itr.next();
            long size = entry.getValue().getSize();
            if (size == 0L) {
                itr.remove();
            }
        }
        return files;
    }

    public static boolean isEmpty(List<MultipartFile> files) {
        boolean result = false;

        if(files == null || files.size() == 0) {
            return true;
        }

        for(MultipartFile file : files) {
            if(file.isEmpty()) {
                result = true;
                break;
            }
        }
        return result;
    }

    public static Boolean isMultipartRequest(HttpServletRequest request) {
        return request.getContentType() != null && request.getContentType().toLowerCase().contains("multipart/form-data");
    }

}
