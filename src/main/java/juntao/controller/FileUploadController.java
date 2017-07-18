package juntao.controller;

import juntao.service.iStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by juntaomei on 7/17/17.
 */

@Controller
public class FileUploadController {

    private final iStorageService storageService;

    @Autowired
    public FileUploadController(iStorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/")
    public String listUploadedFiles(Model model) {

        model.addAttribute("files", storageService.loadAll().map(path -> MvcUriComponentsBuilder.fromMethodName(this.getClass(), "responseFile",path.getFileName().toString()).build().toString()).collect(Collectors.toList()));

        return "uploadFile";
    }

    @GetMapping("/files/{fileName:.+}")
    @ResponseBody
    public ResponseEntity<Resource> responseFile(@PathVariable String fileName) {
        Resource file = storageService.loadAsResource(fileName);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; fileName=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping("/")
    public String fileUpload(@RequestParam("file") MultipartFile file, @RequestParam(value="metaData", required=false) String metaData,
                             RedirectAttributes redirectAttributes) {
        if(metaData != null && metaData.length() != 0) {
            storageService.store(metaData, ("" + file.hashCode()));
        }
        storageService.store(file);
        redirectAttributes.addFlashAttribute("message",
                "Successfully uploaded " + file.getOriginalFilename() + "!");

        return "redirect:/";
    }

}
