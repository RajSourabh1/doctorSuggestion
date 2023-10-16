package com.example.DoctorSuggestion.Service;

import com.example.DoctorSuggestion.Enums.Speciality;
import com.example.DoctorSuggestion.Enums.Symptom;
import com.example.DoctorSuggestion.Models.Doctor;
import com.example.DoctorSuggestion.Models.Patient;
import com.example.DoctorSuggestion.Repository.DoctorRepository;
import com.example.DoctorSuggestion.Repository.PatientRepository;
import com.example.DoctorSuggestion.RequestResponse.PatientDTO;
import com.example.DoctorSuggestion.RequestResponse.SuggestedDoctorData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PatientService {

    @Autowired
    PatientRepository patientRepository;

    @Autowired
    DoctorRepository doctorRepository;

    public String addPatient(PatientDTO patientDTO){

        try{
            String symptom = patientDTO.getSymptom();

            // checking name's char is greater than 3 or not
            if(patientDTO.getName().length()<3)
                return "Name should be at least 3 characters";

            // cheking length of city
            if(patientDTO.getCity().length()>20)
                return "City should be at max 20 characters";

            // checking number of digit in phoneNo
            if(patientDTO.getPhoneNo().length()<10)
                return "Number should be at least 10 digits";

            // checking email validation
            if(!emailValidation(patientDTO.getEmail()))
                return "Email should be valid  or please do not use any special characters in username part";

            // checking if symptoms are of these types then add otherwise not
            if(symptom.equalsIgnoreCase("arthritis") || symptom.equalsIgnoreCase("back pain") ||
                    symptom.equalsIgnoreCase("tissue injuries") || symptom.equalsIgnoreCase("dysmenorrhea") ||
                    symptom.equalsIgnoreCase("skin infection") || symptom.equalsIgnoreCase("skin burn") ||
                    symptom.equalsIgnoreCase("ear pain")){

                Patient patient = new Patient();
                patient.setName(patientDTO.getName());
                patient.setCity(patientDTO.getCity());
                patient.setPhoneNo(patientDTO.getPhoneNo());

                if(symptom.equalsIgnoreCase("arthritis"))
                    patient.setSymptom(Symptom.Arthritis);
                if(symptom.equalsIgnoreCase("back pain"))
                    patient.setSymptom(Symptom.Back_Pain);
                if(symptom.equalsIgnoreCase("tissue injuries"))
                    patient.setSymptom(Symptom.Tissue_injuries);
                if(symptom.equalsIgnoreCase("dysmenorrhea"))
                    patient.setSymptom(Symptom.Dysmenorrhea);
                if(symptom.equalsIgnoreCase("skin infection"))
                    patient.setSymptom(Symptom.Skin_infection);
                if(symptom.equalsIgnoreCase("skin burn"))
                    patient.setSymptom(Symptom.Skin_burn);
                if(symptom.equalsIgnoreCase("ear pain"))
                    patient.setSymptom(Symptom.Ear_pain);

                patient.setEmail(patientDTO.getEmail());

                int id = patientRepository.save(patient).getId();

                return "successfully added and your id is :- "+ id;
            }
            return "please enter only these symptoms i.e. arthritis, back pain, tissue injuries, dysmenorrhea, skin infection, skin burn, ear pain";
        }catch (Exception e){
            return "Internal server error";
        }
    }

    public String deletePatient(Integer patientId){
        if(patientRepository.existsById(patientId)){
            patientRepository.deleteById(patientId);
            return "successfully deleted";
        }
        else{
            return "not found of this id";
        }
    }

    public List suggestions(Integer patientId) throws Exception {
        List<SuggestedDoctorData> suggestedDoctorDataList = new ArrayList<>();
        List<String> errorMessage = new ArrayList<>();

        StringBuilder suggestedDoctors = new StringBuilder();

        SuggestedDoctorData doctorData = new SuggestedDoctorData();

         try{

             Patient patient = patientRepository.findById(patientId).get();
             Symptom symptom = patient.getSymptom();

             List<Doctor> doctors = doctorRepository.findAll();

             // checking that doctor's availability based on location and symptom using flag as 0-1;
             int doctorAvailableOnThatLocation = 0;
             int doctorForThatSymptomOnThatLocation = 0;

             for(Doctor doctor: doctors){

                 // If doctor is not available on patient's location then not updating the value
                 if(doctor.getCity().name().equalsIgnoreCase(patient.getCity())){
                     doctorAvailableOnThatLocation = 1;
                 }

                 // if doctor is not available for patient's symptom on patient's location then not updating the value
                 if(
                    (
                     ((symptom==Symptom.Arthritis || symptom==Symptom.Back_Pain
                         || symptom==Symptom.Tissue_injuries) && doctor.getSpeciality()== Speciality.Orthopedic
                     )
                    || (symptom==Symptom.Dysmenorrhea && doctor.getSpeciality()==Speciality.Gynecology)
                    || ((symptom==Symptom.Skin_infection || symptom==Symptom.Skin_burn)
                         && doctor.getSpeciality()==Speciality.Dermatology)
                    || (symptom==Symptom.Ear_pain && doctor.getSpeciality()==Speciality.ENT)
                   ) && doctor.getCity().name().equalsIgnoreCase(patient.getCity())) {
                     doctorForThatSymptomOnThatLocation = 1;
                 }
             }

             if(doctorAvailableOnThatLocation==0){
                 errorMessage.add("We are still waiting to expend to your location");
                 return errorMessage;
             }

             if(doctorForThatSymptomOnThatLocation==0){
                 errorMessage.add("There isn't any doctor present at your location for your symptom");
                 return errorMessage;
             }

             // now finding doctors based on the symptom after all checkings
             for(Doctor doctor: doctors){
                 if((symptom==Symptom.Arthritis || symptom==Symptom.Back_Pain
                 || symptom==Symptom.Tissue_injuries) && doctor.getSpeciality()==Speciality.Orthopedic){

                     doctorData.setName(doctor.getName());
                     doctorData.setCity(doctor.getCity().name());
                     doctorData.setEmail(doctor.getEmail());
                     doctorData.setPhoneNo(doctor.getPhoneNo());
                     doctorData.setSpeciality(Speciality.Orthopedic.name());

                     suggestedDoctorDataList.add(doctorData);

                     suggestedDoctors.append(" "+doctorData);
                 }

                 if(symptom==Symptom.Dysmenorrhea && doctor.getSpeciality()==Speciality.Gynecology){

                     doctorData.setName(doctor.getName());
                     doctorData.setCity(doctor.getCity().name());
                     doctorData.setEmail(doctor.getEmail());
                     doctorData.setPhoneNo(doctor.getPhoneNo());
                     doctorData.setSpeciality(Speciality.Gynecology.name());

                     suggestedDoctorDataList.add(doctorData);

                     suggestedDoctors.append(" "+doctorData);
                 }

                 if((symptom==Symptom.Skin_infection || symptom==Symptom.Skin_burn) && doctor.getSpeciality()==Speciality.Dermatology){

                     doctorData.setName(doctor.getName());
                     doctorData.setCity(doctor.getCity().name());
                     doctorData.setEmail(doctor.getEmail());
                     doctorData.setPhoneNo(doctor.getPhoneNo());
                     doctorData.setSpeciality(Speciality.Dermatology.name());

                     suggestedDoctorDataList.add(doctorData);

                     suggestedDoctors.append(" "+doctorData);
                 }

                 if(symptom==Symptom.Ear_pain && doctor.getSpeciality()==Speciality.ENT){

                     doctorData.setName(doctor.getName());
                     doctorData.setCity(doctor.getCity().name());
                     doctorData.setEmail(doctor.getEmail());
                     doctorData.setPhoneNo(doctor.getPhoneNo());
                     doctorData.setSpeciality(Speciality.ENT.name());

                     suggestedDoctorDataList.add(doctorData);

                     suggestedDoctors.append(" " + doctorData);
                 }
             }

             return suggestedDoctorDataList;
         }catch (Exception e){
             errorMessage.add("not able to fetch suggested doctor");
             return errorMessage;
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
