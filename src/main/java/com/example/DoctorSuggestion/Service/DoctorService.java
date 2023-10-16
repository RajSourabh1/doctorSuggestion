package com.example.DoctorSuggestion.Service;

import com.example.DoctorSuggestion.Enums.DoctorsCity;
import com.example.DoctorSuggestion.Enums.Speciality;
import com.example.DoctorSuggestion.Models.Doctor;
import com.example.DoctorSuggestion.Repository.DoctorRepository;
import com.example.DoctorSuggestion.RequestResponse.DoctorDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class DoctorService {

    @Autowired
    DoctorRepository doctorRepository;

    public String addDoctor(DoctorDTO doctorDTO){

        try{
            String city = doctorDTO.getCity();
            String speciality = doctorDTO.getSpeciality();

            // checking name's char is greater than 3 or not
            if(doctorDTO.getName().length()<3)
                return "Name should be at least 3 characters";

            // checking email validation
            if(!emailValidation(doctorDTO.getEmail()))
                return "Email should be valid or please do not use any special characters in username part";

            // checking number of digit in phoneNo
            if(doctorDTO.getPhoneNo().length()<10)
                return "number should be at least 10 digits";

            // checking if city are of these types then proceed otherwise not
            if(city.equalsIgnoreCase("delhi") || city.equalsIgnoreCase("noida") || city.equalsIgnoreCase("faridabad")){

                // // checking if speciality are of these types then add otherwise not
                if(speciality.equalsIgnoreCase("orthopedic") || speciality.equalsIgnoreCase("gynecology")
                        || speciality.equalsIgnoreCase("dermatology") || speciality.equalsIgnoreCase("ent")){
                    Doctor doctor = new Doctor();

                    doctor.setName(doctorDTO.getName());
                    doctor.setEmail(doctorDTO.getEmail());

                    if(speciality.equalsIgnoreCase("orthopedic"))
                        doctor.setSpeciality(Speciality.Orthopedic);
                    if(speciality.equalsIgnoreCase("gynecology"))
                        doctor.setSpeciality(Speciality.Gynecology);
                    if(speciality.equalsIgnoreCase("dermatology"))
                        doctor.setSpeciality(Speciality.Dermatology);
                    if(speciality.equalsIgnoreCase("ent"))
                        doctor.setSpeciality(Speciality.ENT);

                    doctor.setPhoneNo(doctorDTO.getPhoneNo());

                    if(city.equalsIgnoreCase("delhi"))
                        doctor.setCity(DoctorsCity.Delhi);
                    if(city.equalsIgnoreCase("noida"))
                        doctor.setCity(DoctorsCity.Noida);
                    if(city.equalsIgnoreCase("faridabad"))
                        doctor.setCity(DoctorsCity.Faridabad);

                    int id = doctorRepository.save(doctor).getId();

                    return "successfully added and your id is :- "+ id;
                }
                else
                    return "please enter only orthopedic, gynecology, dermatology or ent";
            }

            else
                return "enter only city name Delhi, Noida, and Faridabad";
        }catch (Exception e){
            return "Internal server error";
        }
    }

    public String deleteDoctor(Integer doctorId){
        if(doctorRepository.existsById(doctorId)){
            doctorRepository.deleteById(doctorId);
            return "successfully deleted";
        }else {
            return "doctor of this id is not found";
        }
    }

    // code for email validation
    public Boolean emailValidation(String email){

        Set<Character> bad = new HashSet<>();

        char[] specialChars = new char[] {'!', '#', '$', '%', '^', '&', '(', ')', '-', '/', '~', '[', ']', '{', '}', '+', '*', '.', '@'};

        for(char c: specialChars){
            bad.add(c);
        }

        // finding the last occurance of @
        int index = -1;
        for(int i=0;i<email.length();i++){
            if(email.charAt(i)=='@')
                index = i;
        }

        if(index == -1)
            return false;

        String name = email.substring(0,index);
        String domain = email.substring(index+1,email.length());

        for(int i=0;i<name.length();i++){
            if(bad.contains(name.charAt(i)))
                return false;
        }
        if(bad.contains(domain.charAt(domain.length()-1)))
            return false;

        return true;

    }
}
