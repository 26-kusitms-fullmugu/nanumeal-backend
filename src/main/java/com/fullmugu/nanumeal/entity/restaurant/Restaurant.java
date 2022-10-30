package com.fullmugu.nanumeal.entity.restaurant;

import com.fullmugu.nanumeal.entity.donation.Donation;
import com.fullmugu.nanumeal.entity.favorite.Favorite;
import com.fullmugu.nanumeal.entity.hsitory.History;
import com.fullmugu.nanumeal.entity.menu.Menu;
import com.fullmugu.nanumeal.entity.thkmsg.ThxMsg;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private List<ThxMsg> thxMsgs = new ArrayList<>();

    @OneToMany(mappedBy = "restaurantId", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<History> histories = new ArrayList<>();

    @OneToMany(mappedBy = "restaurantId", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Favorite> favorites = new ArrayList<>();

    @OneToMany(mappedBy = "restaurantId", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Donation> donations = new ArrayList<>();








}
