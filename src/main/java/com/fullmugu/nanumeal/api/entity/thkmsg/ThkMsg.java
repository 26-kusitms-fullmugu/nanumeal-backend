package com.fullmugu.nanumeal.api.entity.thkmsg;

import com.fullmugu.nanumeal.api.entity.restaurant.Restaurant;
import com.fullmugu.nanumeal.api.entity.user.User;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ThkMsg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "thxMsgId")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userId")
    @JsonIgnore
    private User childId;

    @ManyToOne
    @JoinColumn(name = "restaurantId")
    @JsonIgnore
    private Restaurant resId;

    @Column(nullable = false)
    private String feeling;

    @Column(length = 255)
    private String message;

    @CreationTimestamp
    private Timestamp regDate;


}
