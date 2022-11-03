package com.fullmugu.nanumeal.entity.favorite;

import com.fullmugu.nanumeal.entity.restaurant.Restaurant;
import com.fullmugu.nanumeal.entity.user.User;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;
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
    @Column(name = "favoriteId")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userId")
    @JsonIgnore
    private User userId;

    @ManyToOne
    @JoinColumn(name = "restaurantId")
    private Restaurant restaurantId;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime regDate;


}
