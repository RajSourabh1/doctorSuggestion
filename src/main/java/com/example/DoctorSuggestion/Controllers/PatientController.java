package com.example.DoctorSuggestion.Controllers;

import com.example.DoctorSuggestion.RequestResponse.PatientDTO;
import com.example.DoctorSuggestion.RequestResponse.SuggestedDoctorData;
import com.example.DoctorSuggestion.Service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patient")
public class PatientController {

    @Autowired
    PatientService patientService;

    @PostMapping("/addPatient")
    public String addPatient(@RequestBody PatientDTO patientDTO){
        return patientService.addPatient(patientDTO);
    }

    @DeleteMapping("/deletePatient")
    public String deletePatient(@RequestParam Integer patientId){
        return patientService.deletePatient(patientId);
    }

    @GetMapping("/suggestions")
    public ResponseEntity suggestions(@RequestParam Integer patientId) throws Exception {

        List response =  patientService.suggestions(patientId);

        return new ResponseEntity<>(response, HttpStatus.OK);

//         try{
//             List<SuggestedDoctorData> response =  patientService.suggestions(patientId);
//         }catch (Exception e){
//             String error = patientService.suggestions(patientId);
//         }
    }
}
