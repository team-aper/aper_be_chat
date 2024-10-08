package com.sparta.aper_chat_back.global.security.user;

import com.sparta.aper_chat_back.global.security.user.Entity.BaseSoftDeleteEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class DeleteAccount extends BaseSoftDeleteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @OneToOne(cascade = CascadeType.MERGE, orphanRemoval = true)
    @JoinColumn
    private User user;

    public DeleteAccount(User user) {
        this.user = user;
    }

    public DeleteAccount() {
    }
}