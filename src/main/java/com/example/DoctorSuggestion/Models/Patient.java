package com.example.DoctorSuggestion.Models;

import com.example.DoctorSuggestion.Enums.Symptom;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "patient")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String city;

    private String email;

    private String phoneNo;

    @Enumerated(value = EnumType.STRING)
    private Symptom symptom;
}
