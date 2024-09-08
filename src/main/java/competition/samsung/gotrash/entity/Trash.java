package competition.samsung.gotrash.entity;

import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Document(collection = "trashes")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Trash {

    @Transient
    public static final String SEQUENCE_NAME = "trash_sequence";

    @Id
    private Integer id;
    private Integer category;
    private String description;
    private BigInteger coin;
    private BigInteger rating;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Version
    private Long version;
}
