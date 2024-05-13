package org.example.computer_storebe.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class User {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private Date birthDate;

    @Column
    private int height;

    @Column
    private Gender gender;

    @Column
    private String email;
}

enum Gender {
    MALE, FEMALE
}