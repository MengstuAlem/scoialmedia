package image.images.service;

import image.images.entity.Image;
import image.images.repository.ImageRepository;
import lombok.AllArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.codec.multipart.FilePart;
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
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

@AllArgsConstructor
@Service
public class ImageService {
    private final static String UPLOAD_URL="upload-dir";
    private final ResourceLoader resourceLoader;
    private final ImageRepository imageRepository;

    public Flux<Image> findAllImages(){
        return imageRepository.findAll();
    }

    public Mono<Resource> findOneImage(String fileName){
        return Mono.fromSupplier(() -> resourceLoader.getResource("file:"+UPLOAD_URL+"/"+fileName));
    }

    public Mono<Void> createImage(Flux<FilePart> files){
        return files
                .flatMap(filePart -> {
                    Mono<Image> saveData = imageRepository.save(new Image(UUID.randomUUID().toString(), filePart.filename()));
                    Mono<Void> copyFile = Mono.just(Paths.get(UPLOAD_URL,filePart.filename()).toFile()).map(file -> {
                        try {
                            file.createNewFile();
                        }catch (Exception e){
                            throw new RuntimeException(e);
                        }
                        return file;
                    }).log("created new file")
                            .flatMap(filePart::transferTo);
                    return Mono.when(saveData,copyFile);
                })
                .then();
    }

    public Mono<Void> deleteImage(String fileName){
        Mono<Void> deleteFromDataBase = imageRepository.findByName(fileName).
                flatMap(imageRepository::delete);
        Mono<Void> deletFile = Mono.fromRunnable(() -> {
            try {
                Files.deleteIfExists(Paths.get(UPLOAD_URL, fileName));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
       return Mono.when(deleteFromDataBase,deletFile).then();
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
