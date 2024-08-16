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
@Table(name = "product_specification")
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class ProductSpecification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "spec_id")
    private Integer productSpecificationId;

    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER, mappedBy = "specification")
    private Product product;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", nullable = false)
    ProductCategory category;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "specification")
    Mobile mobile;
}
