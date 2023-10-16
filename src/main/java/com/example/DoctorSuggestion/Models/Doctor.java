package com.example.DoctorSuggestion.Models;

import com.example.DoctorSuggestion.Enums.DoctorsCity;
import com.example.DoctorSuggestion.Enums.Speciality;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "doctor")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @Enumerated(value = EnumType.STRING)
    private DoctorsCity city;

    private String email;

    private String phoneNo;

    @Enumerated(value = EnumType.STRING)
    private Speciality speciality;
}
