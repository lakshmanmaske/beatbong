package com.app.beatbong.model.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.persistence.*;

@Builder
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "user_role")
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Integer roleId;

    @Column(name = "role", nullable = false)
    private String roleName;

    // this parent for user, deleting the role will delete all users associated with role
    // mapped by role means the column will be generated in the user table but not in role table
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "role")
    AppUser user;
}
