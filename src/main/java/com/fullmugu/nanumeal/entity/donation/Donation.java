package com.fullmugu.nanumeal.entity.donation;

import com.fullmugu.nanumeal.entity.restaurant.Restaurant;
import com.fullmugu.nanumeal.entity.user.User;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Donation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "donationId")
    private Long id;

    @ManyToOne
//    @JoinColumn(name = "id")
    @JsonIgnore
    private User donateUserId;

    @ManyToOne
    @JoinColumn(name = "restaurantId")
    @JsonIgnore
    private Restaurant restaurantId;

    @Column(nullable = false)
    private Long donPrice;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime regDate;

//    메시지 수령여부
    @Column(nullable = false)
    private Boolean isThanked;

}
