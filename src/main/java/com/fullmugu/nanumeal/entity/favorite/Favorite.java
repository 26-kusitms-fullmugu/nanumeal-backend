package com.fullmugu.nanumeal.entity.favorite;

import com.fullmugu.nanumeal.entity.restaurant.Restaurant;
import com.fullmugu.nanumeal.entity.user.User;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id")
//    @JsonIgnore
    private User userId;

    @ManyToOne
    @JoinColumn(name = "id")
    private Restaurant restaurantId;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime regDate;


}
