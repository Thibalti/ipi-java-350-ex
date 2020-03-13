package com.ipiecoles.java.java350.service;


import com.ipiecoles.java.java350.exception.EmployeException;
import com.ipiecoles.java.java350.model.*;
import com.ipiecoles.java.java350.repository.EmployeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityExistsException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class EmployeServiceIntegrationTest {

    @Autowired
    EmployeService employeService;

    @Autowired
    private EmployeRepository employeRepository;

    @BeforeEach
    @AfterEach
    public void setup(){
        employeRepository.deleteAll();
    }

    @Test
    public void integrationEmbaucheEmploye() throws EmployeException {
        //Given
        employeRepository.save(new Employe("Doe", "John", "T12345", LocalDate.now(), Entreprise.SALAIRE_BASE, 1, 1.0));
        String nom = "Doe";
        String prenom = "John";
        Poste poste = Poste.TECHNICIEN;
        NiveauEtude niveauEtude = NiveauEtude.BTS_IUT;
        Double tempsPartiel = 1.0;

        //When
        employeService.embaucheEmploye(nom, prenom, poste, niveauEtude, tempsPartiel);

        //Then
        Employe employe = employeRepository.findByMatricule("T12346");
        Assertions.assertNotNull(employe);
        Assertions.assertEquals(nom, employe.getNom());
        Assertions.assertEquals(prenom, employe.getPrenom());
        Assertions.assertEquals(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")), employe.getDateEmbauche().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        Assertions.assertEquals("T12346", employe.getMatricule());
        Assertions.assertEquals(1.0, employe.getTempsPartiel().doubleValue());

        //1521.22 * 1.2 * 1.0
        Assertions.assertEquals(1825.46, employe.getSalaire().doubleValue());
    }

    @Test
    public void calculPerformanceCommercialTest() throws EmployeException{
        //Given
        employeRepository.save(new Employe("Doe", "John", "C12345", LocalDate.now(), Entreprise.SALAIRE_BASE, 1, 1.0));
        employeRepository.save(new Employe("Doe", "Jane", "T00001", LocalDate.now(), Entreprise.SALAIRE_BASE, 1, 1.0));

        //matricule non présent en BDD
        String matricule1 = "C98765";
        Long caTraite1 = 100l;
        Long objectifCa1 = 100l;

        //caTraite null
        String matricule2 = "C12345";
        Long caTraite2 = null;
        Long objectifCa2 = 100l;

        //obejctifCa null
        String matricule3 = "C12345";
        Long caTraite3 = 100l;
        Long objectifCa3 = null;

        //matricule ne commençant pas par C
        String matricule4 = "T00001";
        Long caTraite4 = 100l;
        Long objectifCa4 = 100l;



        //When


        //Then
        EmployeException employe1 = Assertions.assertThrows(EmployeException.class, () -> employeService.calculPerformanceCommercial(matricule1, caTraite1, objectifCa1));
        Assertions.assertEquals("Le matricule "+matricule1+" n'existe pas !", employe1.getMessage());

        EmployeException employe2 = Assertions.assertThrows(EmployeException.class, () -> employeService.calculPerformanceCommercial(matricule2, caTraite2, objectifCa2));
        Assertions.assertEquals("Le chiffre d'affaire traité ne peut être négatif ou null !", employe2.getMessage());

        EmployeException employe3 = Assertions.assertThrows(EmployeException.class, () -> employeService.calculPerformanceCommercial(matricule3, caTraite3, objectifCa3));
        Assertions.assertEquals("L'objectif de chiffre d'affaire ne peut être négatif ou null !", employe3.getMessage());

        EmployeException employe4 = Assertions.assertThrows(EmployeException.class, () -> employeService.calculPerformanceCommercial(matricule4, caTraite4, objectifCa4));
        Assertions.assertEquals("Le matricule ne peut être null et doit commencer par un C !", employe4.getMessage());

    }

}