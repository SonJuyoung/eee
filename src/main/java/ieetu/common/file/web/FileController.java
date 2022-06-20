package ieetu.common.file.web;

import ieetu.common.file.entity.FileEntity;
import ieetu.common.file.service.FileService;
import ieetu.common.file.vo.FileVO;
import ieetu.common.utils.BrowserUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;

@Controller
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;


    /**
     * 파일 다운로드
     */
    @GetMapping("/file/{fileSeq}/{storeFileNm}")
    public void download(@PathVariable Long fileSeq, @PathVariable String storeFileNm, HttpServletRequest request, HttpServletResponse response) throws Exception {
//        FileEntity file = fileService.getFile(fileSeq); // JPA
        FileVO file = fileService.getFileWithMyBatis(fileSeq); // MyBatis
        if(!file.getStoreFileNm().equals(storeFileNm)) {
            throw new IllegalArgumentException();
        }

        File storedFile = new File(file.getFilePath(), file.getStoreFileNm());
        long fileSize = storedFile.length();
        if(!storedFile.exists() || fileSize <= 0) {
            response.setContentType("application/x-msdownload");

            PrintWriter printwriter = response.getWriter();

            printwriter.println("<html>");
            printwriter.println("<br><br><br><h2>Could not get file name:<br>" + file.getOriginalFileNm() + "</h2>");
            printwriter.println("<br><br><br><center><h3><a href='javascript: history.go(-1)'>Back</a></h3></center>");
            printwriter.println("<br><br><br>&copy; webAccess");
            printwriter.println("</html>");

            printwriter.flush();
            printwriter.close();
            throw new IllegalArgumentException("File Not Exist");
        }

        String mimetype = "application/x-msdownload";

        String userAgent = request.getHeader("User-Agent");
        HashMap<String,String> result = BrowserUtil.getBrowser(userAgent);
        if ( !BrowserUtil.MSIE.equals(result.get(BrowserUtil.TYPEKEY)) ) {
            mimetype = "application/x-stuff";
        }

        String contentDisposition = BrowserUtil.getDisposition(file.getOriginalFileNm(), userAgent, "UTF-8");
        response.setContentType(mimetype);
        response.setHeader("Content-Disposition", contentDisposition);
        response.setContentLengthLong(fileSize);

        BufferedInputStream in = null;
        BufferedOutputStream out = null;

        try {
            in = new BufferedInputStream(Files.newInputStream(storedFile.toPath()));
            out = new BufferedOutputStream(response.getOutputStream());

            FileCopyUtils.copy(in, out);
            out.flush();
        } finally {
            close(in, out);
        }
    }

    private final String[] images = {"jpg", "jpeg", "png", "bmp"};
    @GetMapping("/image/{fileSeq}/{storeFileNm}")
    public void previewImage(@PathVariable Long fileSeq, @PathVariable String storeFileNm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        FileEntity file = fileService.getFile(fileSeq);

        if(!file.getStoreFileNm().equals(storeFileNm)) {
            throw new IllegalArgumentException();
        }

        File storedFile = new File(file.getFilePath(), file.getStoreFileNm());
        long fileSize = storedFile.length();

        String ext = FilenameUtils.getExtension(file.getOriginalFileNm());

        // 파일이 없는 경우 기본 이미지 출력
        if(!storedFile.exists() || fileSize <= 0 || StringUtils.isEmpty(ext)) {
            storedFile = new File(request.getServletContext().getRealPath("/images/fileNotFound.png"));
        } else if (!Arrays.asList(images).contains(ext)) {
            storedFile = new File(request.getServletContext().getRealPath("/images/fileNotFound.png"));
        }

        BufferedInputStream in = null;
        ByteArrayOutputStream out = null;

        try {

            in = new BufferedInputStream(Files.newInputStream(storedFile.toPath()));
            out = new ByteArrayOutputStream();

            int imgByte;
            while ((imgByte = in.read()) != -1) {
                out.write(imgByte);
            }

            String type = "";
            if ("jpg".equals(ext)) {
                type = "image/jpeg";
            } else {
                type = "image/" + ext.toLowerCase();
            }

            type = type.replaceAll("\r", "").replaceAll("\n", "");
            response.setHeader("Content-Type", type);
            response.setContentLength(out.size());

            out.writeTo(response.getOutputStream());

            response.getOutputStream().flush();
            response.getOutputStream().close();

        } finally {
            close(out, in);
        }
    }

    public static void close(Closeable  ... resources) {
        for (Closeable resource : resources) {
            if (resource != null) {
                try {
                    resource.close();
                } catch (Exception ignore) {

                }
            }
        }
    }
}
