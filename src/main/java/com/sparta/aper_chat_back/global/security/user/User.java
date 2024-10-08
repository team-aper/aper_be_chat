package com.sparta.aper_chat_back.global.security.user;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column
    private String fieldImage;

    @Column
    private String description;

    @Column
    private String penName;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    @Column
    private Long point;

    @Column(name = "is_exposed", columnDefinition = "boolean default false")
    private boolean isExposed;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id")
    private DeleteAccount deleteAccount;

    public User() {
    }

    @Builder
    public User(String penName, String password, String email, UserRoleEnum role) {
        this.penName = penName;
        this.password = password;
        this.email = email;
        this.role = role;
        this.description = "안녕하세요, " + penName + "입니다.";
        this.fieldImage = "https://aper-image-bucket.s3.ap-northeast-2.amazonaws.com/fieldimage/craig-manners-BNgxioIWM0Y-unsplash.png";
        this.point = 0L;
    }

    public void updatePoint(Long point) {
        this.point += point;
    }

    public void updatePassword(String encodedPassword) {
        this.password = encodedPassword;
    }

    public void updatePenName(String penName) {
        this.penName = penName;
    }

    public void updateEmail(String email) {
        this.email = email;
    }

    public void updateDescription(String description) {
        this.description = description;
    }

    public void updateFieldImage(String fieldImage) {
        this.fieldImage = fieldImage;
    }

}