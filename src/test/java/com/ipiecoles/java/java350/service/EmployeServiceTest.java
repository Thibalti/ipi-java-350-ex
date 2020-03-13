package com.ipiecoles.java.java350.service;

import com.ipiecoles.java.java350.exception.EmployeException;
import com.ipiecoles.java.java350.model.Employe;
import com.ipiecoles.java.java350.model.Entreprise;
import com.ipiecoles.java.java350.model.NiveauEtude;
import com.ipiecoles.java.java350.model.Poste;
import com.ipiecoles.java.java350.repository.EmployeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityExistsException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class EmployeServiceTest {

    @InjectMocks
    EmployeService employeService;

    @Mock
    EmployeRepository employeRepository;

    @Autowired
    private EmployeRepository employeRepository2;
    @Autowired
    private EmployeService employeService2;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.initMocks(this.getClass());
    }

    @Test
    public void testEmbaucheEmployeTechnicienPleinTempsBts() throws EmployeException {
        //Given
        String nom = "Doe";
        String prenom = "John";
        Poste poste = Poste.TECHNICIEN;
        NiveauEtude niveauEtude = NiveauEtude.BTS_IUT;
        Double tempsPartiel = 1.0;
        when(employeRepository.findLastMatricule()).thenReturn("00345");
        when(employeRepository.findByMatricule("T00346")).thenReturn(null);

        //When
        employeService.embaucheEmploye(nom, prenom, poste, niveauEtude, tempsPartiel);

        //Then
        ArgumentCaptor<Employe> employeArgumentCaptor = ArgumentCaptor.forClass(Employe.class);
        verify(employeRepository, times(1)).save(employeArgumentCaptor.capture());
        Assertions.assertEquals(nom, employeArgumentCaptor.getValue().getNom());
        Assertions.assertEquals(prenom, employeArgumentCaptor.getValue().getPrenom());
        Assertions.assertEquals(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")), employeArgumentCaptor.getValue().getDateEmbauche().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        Assertions.assertEquals("T00346", employeArgumentCaptor.getValue().getMatricule());
        Assertions.assertEquals(tempsPartiel, employeArgumentCaptor.getValue().getTempsPartiel());

        //1521.22 * 1.2 * 1.0
        Assertions.assertEquals(1825.46, employeArgumentCaptor.getValue().getSalaire().doubleValue());
    }

    @Test
    public void testEmbaucheEmployeManagerMiTempsMaster() throws EmployeException {
        //Given
        String nom = "Doe";
        String prenom = "John";
        Poste poste = Poste.MANAGER;
        NiveauEtude niveauEtude = NiveauEtude.MASTER;
        Double tempsPartiel = 0.5;
        when(employeRepository.findLastMatricule()).thenReturn("00345");
        when(employeRepository.findByMatricule("M00346")).thenReturn(null);

        //When
        employeService.embaucheEmploye(nom, prenom, poste, niveauEtude, tempsPartiel);

        //Then
        ArgumentCaptor<Employe> employeArgumentCaptor = ArgumentCaptor.forClass(Employe.class);
        verify(employeRepository, times(1)).save(employeArgumentCaptor.capture());
        Assertions.assertEquals(nom, employeArgumentCaptor.getValue().getNom());
        Assertions.assertEquals(prenom, employeArgumentCaptor.getValue().getPrenom());
        Assertions.assertEquals(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")), employeArgumentCaptor.getValue().getDateEmbauche().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        Assertions.assertEquals("M00346", employeArgumentCaptor.getValue().getMatricule());
        Assertions.assertEquals(tempsPartiel, employeArgumentCaptor.getValue().getTempsPartiel());

        //1521.22 * 1.4 * 0.5
        Assertions.assertEquals(1064.85, employeArgumentCaptor.getValue().getSalaire().doubleValue());
    }

    @Test
    public void testEmbaucheEmployeManagerMiTempsMasterNoLastMatricule() throws EmployeException {
        //Given
        String nom = "Doe";
        String prenom = "John";
        Poste poste = Poste.MANAGER;
        NiveauEtude niveauEtude = NiveauEtude.MASTER;
        Double tempsPartiel = 0.5;
        when(employeRepository.findLastMatricule()).thenReturn(null);
        when(employeRepository.findByMatricule("M00001")).thenReturn(null);

        //When
        employeService.embaucheEmploye(nom, prenom, poste, niveauEtude, tempsPartiel);

        //Then
        ArgumentCaptor<Employe> employeArgumentCaptor = ArgumentCaptor.forClass(Employe.class);
        verify(employeRepository, times(1)).save(employeArgumentCaptor.capture());
        Assertions.assertEquals("M00001", employeArgumentCaptor.getValue().getMatricule());
    }

    @Test
    public void testEmbaucheEmployeManagerMiTempsMasterExistingEmploye(){
        //Given
        String nom = "Doe";
        String prenom = "John";
        Poste poste = Poste.MANAGER;
        NiveauEtude niveauEtude = NiveauEtude.MASTER;
        Double tempsPartiel = 0.5;
        when(employeRepository.findLastMatricule()).thenReturn(null);
        when(employeRepository.findByMatricule("M00001")).thenReturn(new Employe());

        //When/Then
        EntityExistsException e = Assertions.assertThrows(EntityExistsException.class, () -> employeService.embaucheEmploye(nom, prenom, poste, niveauEtude, tempsPartiel));
        Assertions.assertEquals("L'employé de matricule M00001 existe déjà en BDD", e.getMessage());
    }

    @Test
    public void testEmbaucheEmployeManagerMiTempsMaster99999(){
        //Given
        String nom = "Doe";
        String prenom = "John";
        Poste poste = Poste.MANAGER;
        NiveauEtude niveauEtude = NiveauEtude.MASTER;
        Double tempsPartiel = 0.5;
        when(employeRepository.findLastMatricule()).thenReturn("99999");

        //When/Then
        EmployeException e = Assertions.assertThrows(EmployeException.class, () -> employeService.embaucheEmploye(nom, prenom, poste, niveauEtude, tempsPartiel));
        Assertions.assertEquals("Limite des 100000 matricules atteinte !", e.getMessage());
    }

    @Test
    public void testCalculSalaireMoyenTP() throws Exception{
        //Given
        Mockito.when(employeRepository.sumSalaire()).thenReturn(10000d);
        Mockito.when(employeRepository.count()).thenReturn(10l);
        Mockito.when(employeRepository.sumTempsPartiel()).thenReturn(10d);

        //When
        Double salaireMoyen = employeService.calculSalaireMoyenETP();

        //Given
        Assertions.assertEquals(1000, salaireMoyen);

    }

    @Test
    public void calculPerformanceCommercialTest() throws EmployeException{
        //Given
        Employe e1 = new Employe("Doe", "John", "C12345", LocalDate.now(), Entreprise.SALAIRE_BASE, 1, 1.0);

        when(employeRepository.findByMatricule("C98765")).thenReturn(null);
        when(employeRepository.findByMatricule("C12345")).thenReturn(e1);

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

        //Cas 2
        String matricule5 = "C12345";
        Long caTraite5 = 80l;
        Long objectifCa5 = 100l;



        //When
        //Cas 2
         employeService.calculPerformanceCommercial(matricule5, caTraite5, objectifCa5);


        //Then
        EmployeException employe1 = Assertions.assertThrows(EmployeException.class, () -> employeService.calculPerformanceCommercial(matricule1, caTraite1, objectifCa1));
        Assertions.assertEquals("Le matricule "+matricule1+" n'existe pas !", employe1.getMessage());

        EmployeException employe2 = Assertions.assertThrows(EmployeException.class, () -> employeService.calculPerformanceCommercial(matricule2, caTraite2, objectifCa2));
        Assertions.assertEquals("Le chiffre d'affaire traité ne peut être négatif ou null !", employe2.getMessage());

        EmployeException employe3 = Assertions.assertThrows(EmployeException.class, () -> employeService.calculPerformanceCommercial(matricule3, caTraite3, objectifCa3));
        Assertions.assertEquals("L'objectif de chiffre d'affaire ne peut être négatif ou null !", employe3.getMessage());

        EmployeException employe4 = Assertions.assertThrows(EmployeException.class, () -> employeService.calculPerformanceCommercial(matricule4, caTraite4, objectifCa4));
        Assertions.assertEquals("Le matricule ne peut être null et doit commencer par un C !", employe4.getMessage());

        //Cas2
        Assertions.assertEquals(2, employeRepository.findByMatricule("C12345").getPerformance());


    }

    @Test
    public void calculPerformanceCommercialTestCas3() throws EmployeException{
        //Given
        Employe e1 = new Employe("Doe", "John", "C12345", LocalDate.now(), Entreprise.SALAIRE_BASE, 1, 1.0);

        when(employeRepository.findByMatricule("C12345")).thenReturn(e1);

        //Cas 3
        String matricule6 = "C12345";
        Long caTraite6 = 100l;
        Long objectifCa6 = 100l;

        //When
        //Cas 3
        employeService.calculPerformanceCommercial(matricule6, caTraite6, objectifCa6);

        //Then
        //Cas 3
        Assertions.assertEquals(2, employeRepository.findByMatricule("C12345").getPerformance());

    }

    @Test
    public void calculPerformanceCommercialTestCas4() throws EmployeException {
        //Given
        Employe e1 = new Employe("Doe", "John", "C12345", LocalDate.now(), Entreprise.SALAIRE_BASE, 1, 1.0);

        when(employeRepository.findByMatricule("C12345")).thenReturn(e1);

        //Cas 4
        String matricule7 = "C12345";
        Long caTraite7 = 110l;
        Long objectifCa7 = 100l;

        //When
        //Cas 4
        employeService.calculPerformanceCommercial(matricule7, caTraite7, objectifCa7);

        //Then
        //Cas 4
        Assertions.assertEquals(3, employeRepository.findByMatricule("C12345").getPerformance());

    }

    @Test
    public void calculPerformanceCommercialTestCas5() throws EmployeException {
        //Given
        employeRepository2.save(new Employe("Doe", "John", "C12345", LocalDate.now(), Entreprise.SALAIRE_BASE, 1, 1.0));


        //Cas 4
        String matricule = "C12345";
        Long caTraite = 125l;
        Long objectifCa = 100l;

        //When
        //Cas 4
        employeService2.calculPerformanceCommercial(matricule, caTraite, objectifCa);

        //Then
        //Cas 4
        Assertions.assertEquals(6, employeRepository2.findByMatricule("C12345").getPerformance());

    }
}