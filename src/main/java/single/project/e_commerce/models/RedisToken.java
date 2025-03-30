package single.project.e_commerce.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;


@RedisHash("Token")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RedisToken {
    @Id
    private String jti;
    private String username;

    @TimeToLive
    private Long timeToLive;
}
