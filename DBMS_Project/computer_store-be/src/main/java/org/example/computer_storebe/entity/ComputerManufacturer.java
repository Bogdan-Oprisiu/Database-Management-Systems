package org.example.computer_storebe.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "computer_manufacturer")
public class ComputerManufacturer {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "manufacturer_name")
    private String manufacturerName;

    @Column(name = "manufacturer_email")
    private String manufacturerEmail;

    @Column(name = "manufacturer_phone")
    private Integer manufacturerPhone;

    @OneToMany(mappedBy = "manufacturer")
    private List<Computer> computers;
}
