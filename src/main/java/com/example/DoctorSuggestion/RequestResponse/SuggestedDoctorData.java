package com.example.DoctorSuggestion.RequestResponse;

import lombok.Data;

@Data
public class SuggestedDoctorData {
    private String name;

    private String city;

    private String email;

    private String phoneNo;

    private String speciality;
}
