package image.images;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@AllArgsConstructor
public class ImageController {
    private ImageRepository imageRepository;



    @GetMapping("/image")

    public Flux<Image> images(){
        return imageRepository.findAll();
    }
}
