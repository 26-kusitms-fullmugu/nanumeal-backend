package com.fullmugu.nanumeal.api.entity.user;

import com.fullmugu.nanumeal.api.entity.donation.Donation;
import com.fullmugu.nanumeal.api.entity.favorite.Favorite;
import com.fullmugu.nanumeal.api.entity.history.History;
import com.fullmugu.nanumeal.api.entity.thkmsg.ThkMsg;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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
    @Column()
    private Long kakaoId; // 카카오에서 주는 시퀀스 넘버

    @Column(length = 255, unique = true)
    private String loginId;

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
    private String nickName;

    @Enumerated(EnumType.STRING)
    private Type type;

    @CreationTimestamp
    private Timestamp regDate;

    @LastModifiedDate
    @Column(name = "moddate")
    private LocalDateTime modDate;
    @Enumerated(EnumType.STRING)
    private Role role;

    private String provider;

    //    연관관계의 주인은 FK를 가진 쪽.
    @OneToMany(mappedBy = "childId", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ThkMsg> thkMsgs = new ArrayList<>();

    @OneToMany(mappedBy = "userId", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<History> histories = new ArrayList<>();

    @OneToMany(mappedBy = "userId", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Favorite> Favorites = new ArrayList<>();

    @OneToMany(mappedBy = "donateUserId", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Donation> donations = new ArrayList<>();


    @Builder(builderClassName = "OAuth2Register", builderMethodName = "oauth2Register")
    public User(Long kakaoId, String name, String email, String password, Type type, Role role, String provider) {
        this.kakaoId = kakaoId;
        this.name = name;
        this.password = password;
        this.email = email;
        this.type = type;
        this.role = role;
        this.provider = provider;
    }

    @Builder(builderClassName = "FormSignup", builderMethodName = "formSignup")
    public User(String loginId, String email, String password, Type type, String name, String nickName, Long age, String location, Role role) {
        this.loginId = loginId;
        this.email = email;
        this.password = password;
        this.type = type;
        this.name = name;
        this.nickName = nickName;
        this.age = age;
        this.location = location;
        this.role = role;
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

    public void generatePassword() {
        this.password += this.id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<String> roles = new ArrayList<>();
        roles.add(role.toString());

        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

}
