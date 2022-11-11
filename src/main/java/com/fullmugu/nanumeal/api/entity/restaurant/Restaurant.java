package com.fullmugu.nanumeal.api.entity.restaurant;

import com.fullmugu.nanumeal.api.entity.donation.Donation;
import com.fullmugu.nanumeal.api.entity.favorite.Favorite;
import com.fullmugu.nanumeal.api.entity.history.History;
import com.fullmugu.nanumeal.api.entity.menu.Menu;
import com.fullmugu.nanumeal.api.entity.thkmsg.ThkMsg;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "restaurantId")
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false, length = 255)
    private String location;

    @Column(nullable = false, length = 255)
    private String information;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime regDate;

    @Column(nullable = false)
    private Long remainDon;

    @Column(nullable = false)
    private Boolean GoB;

    @Column(nullable = false, length = 255)
    private String x;

    @Column(nullable = false, length = 255)
    private String y;

    @OneToMany(mappedBy = "restaurantId", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Menu> menus = new ArrayList<>();

    @OneToMany(mappedBy = "resId", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ThkMsg> thkMsgs = new ArrayList<>();

    @OneToMany(mappedBy = "restaurantId", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<History> histories = new ArrayList<>();

    @OneToMany(mappedBy = "restaurantId", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Favorite> favorites = new ArrayList<>();

    @OneToMany(mappedBy = "restaurantId", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Donation> donations = new ArrayList<>();








}
