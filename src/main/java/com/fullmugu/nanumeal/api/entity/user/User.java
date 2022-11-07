package com.fullmugu.nanumeal.api.entity.user;

import com.fullmugu.nanumeal.api.entity.donation.Donation;
import com.fullmugu.nanumeal.api.entity.favorite.Favorite;
import com.fullmugu.nanumeal.api.entity.history.History;
import com.fullmugu.nanumeal.api.entity.thkmsg.ThxMsg;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId")
    private Long id;

    @Column(length = 255, unique = true)
    private String email;

    @Column(length = 255)
    private String password;

    @Column(length = 255)
    private String name;

    @Column()
    private Long age;

    @Column(length = 255)
    private String location;
    @Column(length = 255, unique = true)
    private String username;

    @Enumerated(EnumType.STRING)
    private Type type;

    @CreatedDate
    @Column(name = "regdate", updatable = false)
    private LocalDateTime regDate;

    @LastModifiedDate
    @Column(name = "moddate")
    private LocalDateTime modDate;
    @Enumerated(EnumType.STRING)
    private Role role;

    private String provider;

    private String providerId;

    //    연관관계의 주인은 FK를 가진 쪽.
    @OneToMany(mappedBy = "childId", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ThxMsg> thxMsgs = new ArrayList<>();

    @OneToMany(mappedBy = "userId", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<History> histories = new ArrayList<>();

    @OneToMany(mappedBy = "userId", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Favorite> Favorites = new ArrayList<>();

    @OneToMany(mappedBy = "donateUserId", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Donation> donations = new ArrayList<>();


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Builder(builderClassName = "OAuth2Register", builderMethodName = "oauth2Register")
    public User(String username, String email, String password, Role role, String provider, String providerId) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
    }

}
