package image.images.service;

import image.images.entity.Image;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.FileSystemUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.function.Function;
import java.util.function.Supplier;

@AllArgsConstructor
@Service
public class ImageService {
    private final static String UPLOAD_URL="upload-dir";
    private final ResourceLoader resourceLoader;

    public Flux<Image> findAllImages()throws IOException {
     try{

       return   Flux.fromIterable(
                 Files.newDirectoryStream(Paths.get(UPLOAD_URL))
         ).map(path -> new Image(path.hashCode()+"",path.getFileName().toString()));
        }catch (Exception e){
           return Flux.empty();
        }

    }

    public Mono<Resource> findOneImage(String fileName){

        return Mono.fromSupplier(() -> resourceLoader.getResource("file:"+UPLOAD_URL+"/"+fileName));
    }

    @Bean
    CommandLineRunner setUp(){
        return args -> {
            FileSystemUtils.deleteRecursively(new File(UPLOAD_URL));
            Files.createDirectory(Paths.get(UPLOAD_URL));
            FileCopyUtils.copy("test file",new FileWriter(UPLOAD_URL+ "/learning-spring-boot-cover.jpg"));
            FileCopyUtils.copy("test file2",new FileWriter(UPLOAD_URL+ "/learning-spring-boot-2nd-edition-cover.jpg"));
            FileCopyUtils.copy("test file3",new FileWriter(UPLOAD_URL+ "/bazinga.png"));

        };

    }
}
