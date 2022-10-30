package com.fullmugu.nanumeal.entity.thkmsg;

import com.fullmugu.nanumeal.entity.restaurant.Restaurant;
import com.fullmugu.nanumeal.entity.user.User;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ThxMsg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id")
//    @JsonIgnore
    private User childId;

    @ManyToOne
    @JoinColumn(name = "id")
//    @JsonIgnore
    private Restaurant resId;

    @Column(nullable = false, length = 255)
    private String message;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime regDate;



}
