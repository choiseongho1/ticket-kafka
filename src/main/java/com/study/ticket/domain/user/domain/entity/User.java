package com.study.ticket.domain.user.domain.entity;

import com.study.ticket.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

/**
 * 사용자 엔티티
 */
@Entity
@Table(name = "USERS")
@Builder
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    @Comment("사용자 ID")
    private Long id;

    @Column(name = "EMAIL", nullable = false, unique = true)
    @Comment("이메일")
    private String email;

    @Column(name = "PASSWORD", nullable = false)
    @Comment("비밀번호")
    private String password;

    @Column(name = "NAME", nullable = false)
    @Comment("이름")
    private String name;

    @Column(name = "PHONE")
    @Comment("전화번호")
    private String phone;

    @Column(name = "ROLE", nullable = false)
    @Comment("역할")
    private String role;

    @Column(name = "ENABLED", nullable = false)
    @Comment("활성화 여부")
    private boolean enabled;
}
