package com.app.beatbong.model.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.persistence.*;

@Builder
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "mobile")
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class Mobile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "details", nullable = false)
    private String details;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "spec_id", nullable = false)
    ProductSpecification specification;

}
