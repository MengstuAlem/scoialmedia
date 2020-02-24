package image.images.Comments;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface CommentWriterRepository extends ReactiveMongoRepository<Comment,String> {
    Mono<Comment> save(Comment newComment);
    Mono<Comment> findById(String id);

}
