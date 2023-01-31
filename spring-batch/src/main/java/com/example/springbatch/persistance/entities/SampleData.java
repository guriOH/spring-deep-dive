package com.example.springbatch.persistance.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "sample_data")
public class SampleData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id; // 대표 상품 ID

    @Column
    private String firstName;

    @Column
    private String lastName;


}
