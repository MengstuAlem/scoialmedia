package image.images.Comments;

import image.images.entity.Image;
import image.images.repository.ImageRepository;
import lombok.AllArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Supplier;


@Profile("simulator")
@Component
public class CommentSimulator {
    private CommentController commentController;
    private ImageRepository imageRepository;
    private AtomicInteger atomicInteger;


    @EventListener
    public void onApplicarionReadEvent(ApplicationReadyEvent event){
        Flux.interval(Duration.ofMillis(100))
                .flatMap( tick -> imageRepository.findAll())
                .map(image -> {
                    Comment comment= new Comment();
                    comment.setImageId(image.getId());
                    comment.setComment(
                            "comment # "+atomicInteger.getAndIncrement()
                    );
                    return Mono.just(comment);
                }).flatMap(newComment -> Mono.defer( () -> commentController.addComment(newComment)))
                .subscribe();
    }
}
