package com.fullmugu.nanumeal.api.entity.menu;

import com.fullmugu.nanumeal.api.entity.restaurant.Restaurant;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menuId")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "restaurantId")
    @JsonIgnore
    private Restaurant restaurantId;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false)
    private Long price;

    @Column(nullable = false, length = 255)
    private String image;

}
