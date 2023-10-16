package com.example.DoctorSuggestion.RequestResponse;

import lombok.Data;

@Data
public class PatientDTO {

    private String name;

    private String city;

    private String email;

    private String phoneNo;

    private String symptom;
}
