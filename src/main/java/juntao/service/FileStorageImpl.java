package juntao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * Created by juntaomei on 7/17/17.
 */

@Service
public class FileStorageImpl implements iStorageService {

    private final Path rootLocation;



    @Autowired
    public FileStorageImpl(ServiceProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }

    @Override
    public void init() {
        try {
            Files.createDirectory(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Can't initialize directory", e);
        }
    }

    @Override
    public void store(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("Can't store empty file: " + file.getOriginalFilename());
            }
            Files.copy(file.getInputStream(), this.rootLocation.resolve(file.getOriginalFilename()));
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file " + file.getOriginalFilename(), e);
        }

    }

    @Override
    public void store(String metaData, String fileName) {
//        if (metaData == null || metaData.length() == 0) {
//            return;
//        }
        try(PrintWriter out = new PrintWriter(this.rootLocation.resolve("meta_" + fileName).toString())){
            out.println(metaData);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Can't find file directory", e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public Path load(String fileName) {
        return rootLocation.resolve(fileName);

    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(path -> this.rootLocation.relativize(path));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read stored files", e);
        }
    }

    @Override
    public Resource loadAsResource(String fileName) {
        try {
            Path file = load(fileName);
            Resource resource = new UrlResource(file.toUri());
            if(resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new RuntimeException("Can't read file: " + fileName);

            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Could not read file: " + fileName, e);
        }
    }
}
