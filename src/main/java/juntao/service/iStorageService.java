package juntao.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * Created by juntaomei on 7/17/17.
 */
public interface iStorageService {

    void init();

    void store(MultipartFile file);

    void store(String metaData, String fileName);

    void deleteAll();

    Path load(String fileName);

    Stream<Path> loadAll();

    Resource loadAsResource(String filename);



}
