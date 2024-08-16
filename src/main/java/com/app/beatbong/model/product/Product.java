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
@Table(name = "product")
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Integer productId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price", nullable = false)
    private Float price;

    @Column(name = "image_folder_id", nullable = false)
    private String imageFolderId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "spec_id", nullable = true)
    private ProductSpecification specification;
}
