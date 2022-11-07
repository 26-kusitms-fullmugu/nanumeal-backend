package com.fullmugu.nanumeal.api.entity.badge;

import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Badge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "badgeId")
    private Long id;

    @Column(nullable = false)
    private Long totalPrice;

    @Column(nullable = false, length = 255)
    private String badgeImage;


}
