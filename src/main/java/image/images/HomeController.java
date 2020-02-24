package image.images;

import image.images.Comments.CommentReaderRepository;
import image.images.service.ImageService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.HashMap;

@AllArgsConstructor
@Controller
public class HomeController {
    private static final  String BASE_PATH="/images";
    private static final  String FILENAME="{filename:.+}";
    private ImageService imageService;
    private	final CommentReaderRepository repository;

    @GetMapping(value = BASE_PATH +"/"+FILENAME +"/raw",produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public Mono<ResponseEntity<?>> oneRawImage(@PathVariable String filename){
       return imageService.findOneImage(filename).map(resource -> {
           try {
               return ResponseEntity.ok()
                       .contentLength(resource.contentLength())
                       .body(new InputStreamResource(resource.getInputStream()));
           } catch (IOException e) {
             return ResponseEntity.badRequest().body("could find" + filename+e.getMessage());
           }
       });
   }

   @PostMapping(value = BASE_PATH)
    public Mono<String> createFile(@RequestPart (value = "file")Flux<FilePart> files){
       return imageService.createImage(files).then(Mono.just("redirect:/"));
   }

   @DeleteMapping(value = BASE_PATH +"/" +FILENAME)
    public Mono<String> deleteFile(@PathVariable String filename){
       return imageService.deleteImage(filename).then(Mono.just("redirect:/"));
   }

   @GetMapping("/")
       public Mono<String> index(Model model) throws IOException {
       model.addAttribute("images",imageService.findAllImages().flatMap(
               image -> Mono.just(image).zipWith(repository.findByImageId(image.getId()).collectList()))
               .map(imageAndComments	->	new	HashMap<String,	Object>(){{
                   put("id",imageAndComments.getT1().getId());
                   put("name",imageAndComments.getT1().getName());
                   put("comments",imageAndComments.getT2());
               }})


       );
       return Mono.just("index");
       }
}
