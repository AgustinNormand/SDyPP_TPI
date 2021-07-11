package com.TPI.Server.Servidor;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class UploadController {
    private static String UPLOADED_FOLDER = "/tmp/";
    private KubectlController kubectl = new KubectlController();

    @GetMapping("/")
    public String index(){
        return "upload";
    }

    @PostMapping("/upload") // //new annotation since 4.3
    public String singleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {

        String returnResult = "";
        if (file.isEmpty()) {
            returnResult = "File is emtpy";
        }

        try {
            byte[] bytes = new byte[0];
            try {
                bytes = file.getBytes();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            Files.write(path, bytes);


            returnResult = kubectl.applyCommand("kubectl apply -f "+UPLOADED_FOLDER + file.getOriginalFilename());

            File fileCreated = new File(UPLOADED_FOLDER + file.getOriginalFilename());
            fileCreated.delete();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return returnResult;
    }

    @GetMapping("/uploadStatus")
    public String uploadStatus() {
        return "uploadStatus";
    }
}
