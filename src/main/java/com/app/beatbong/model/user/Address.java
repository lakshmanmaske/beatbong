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
@Table(name = "address")
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Integer addressId;

    @Column(name = "house_no")
    private String houseNumber;

    @Column(name = "street")
    private String street;

    @Column(name = "landmark")
    private String landmark;

    @Column(name = "village")
    private String village;

    @Column(name = "city")
    private String city;

    @Column(name = "district")
    private String district;

    @Column(name = "pin_code")
    private Integer pinCode;

    @Column(name = "state", nullable = false)
    private String state;

    @Column(name = "country", nullable = false)
    private String country;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    AppUser user;
}
