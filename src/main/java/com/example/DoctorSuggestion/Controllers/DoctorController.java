package com.example.DoctorSuggestion.Controllers;

import com.example.DoctorSuggestion.RequestResponse.DoctorDTO;
import com.example.DoctorSuggestion.Service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/doctor")
public class DoctorController {

    @Autowired
    DoctorService doctorService;

    @PostMapping("/addDoctor")
    public String addDoctor(@RequestBody DoctorDTO doctorDTO){
        return doctorService.addDoctor(doctorDTO);
    }

    @DeleteMapping("/deleteDoctor")
    public String deleteDoctor(@RequestParam Integer doctorId){
        return doctorService.deleteDoctor(doctorId);
    }
}
