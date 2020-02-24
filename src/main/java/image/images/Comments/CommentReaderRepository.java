package image.images.Comments;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface CommentReaderRepository extends ReactiveMongoRepository<Comment,String> {
    Flux<Comment> findByImageId(String id);
}
