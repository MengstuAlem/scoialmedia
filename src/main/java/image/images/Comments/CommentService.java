package image.images.Comments;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@AllArgsConstructor
@Service

public class CommentService {

private CommentReaderRepository repository;
private MeterRegistry meterRegistry;
    @RabbitListener(bindings =@QueueBinding(
            value = @Queue,
            exchange = @Exchange(value = "learning"),
            key="comments.new"))

public void save(Comment newComment){
    repository.save(newComment)
            .log("comment service saved")
            .subscribe(comment -> meterRegistry
                    .counter("comments.consumed",	"imageId",comment.getImageId()));
}

    @Bean
    Jackson2JsonMessageConverter jackson2JsonMessageConverter()	{
        return	new	Jackson2JsonMessageConverter();
    }

@Bean
    CommandLineRunner setup(MongoOperations operations){
        return args -> {
            operations.dropCollection(Comment.class);
        };
}
}
