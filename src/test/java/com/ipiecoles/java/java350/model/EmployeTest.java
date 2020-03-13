package com.ipiecoles.java.java350.model;

import com.ipiecoles.java.java350.exception.EmployeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;

public class EmployeTest {

    @Test
    public void getNombreAnneeAncienneteNow(){
        //Given
        Employe e = new Employe();
        e.setDateEmbauche(LocalDate.now());

        //When
        Integer anneeAnciennete = e.getNombreAnneeAnciennete();

        //Then
        Assertions.assertEquals(0, anneeAnciennete.intValue());
    }

    @Test
    public void getNombreAnneeAncienneteNminus2(){
        //Given
        Employe e = new Employe();
        e.setDateEmbauche(LocalDate.now().minusYears(2L));

        //When
        Integer anneeAnciennete = e.getNombreAnneeAnciennete();

        //Then
        Assertions.assertEquals(2, anneeAnciennete.intValue());
    }

    @Test
    public void getNombreAnneeAncienneteNull(){
        //Given
        Employe e = new Employe();
        e.setDateEmbauche(null);

        //When
        Integer anneeAnciennete = e.getNombreAnneeAnciennete();

        //Then
        Assertions.assertEquals(0, anneeAnciennete.intValue());
    }

    @Test
    public void getNombreAnneeAncienneteNplus2(){
        //Given
        Employe e = new Employe();
        e.setDateEmbauche(LocalDate.now().plusYears(2L));

        //When
        Integer anneeAnciennete = e.getNombreAnneeAnciennete();

        //Then
        Assertions.assertEquals(0, anneeAnciennete.intValue());
    }

    @ParameterizedTest
    @CsvSource({
            "1, 'T12345', 0, 1.0, 1000.0",
            "1, 'T12345', 2, 0.5, 600.0",
            "1, 'T12345', 2, 1.0, 1200.0",
            "2, 'T12345', 0, 1.0, 2300.0",
            "2, 'T12345', 1, 1.0, 2400.0",
            "1, 'M12345', 0, 1.0, 1700.0",
            "1, 'M12345', 5, 1.0, 2200.0",
            "2, 'M12345', 0, 1.0, 1700.0",
            "2, 'M12345', 8, 1.0, 2500.0"
    })
    public void getPrimeAnnuelle(Integer performance, String matricule, Long nbYearsAnciennete, Double tempsPartiel, Double primeAnnuelle){
        //Given
        Employe employe = new Employe("Doe", "John", matricule, LocalDate.now().minusYears(nbYearsAnciennete), Entreprise.SALAIRE_BASE, performance, tempsPartiel);

        //When
        Double prime = employe.getPrimeAnnuelle();

        //Then
        Assertions.assertEquals(primeAnnuelle, prime);

    }

    @Test
    public void passageTempsPartiel(){
        //Given
        Employe e = new Employe();
        e.setTempsPartiel(1.0);
        Employe e2 = new Employe();
        e2.setTempsPartiel(0.5);

        //When
        Double tempsPartiel = e.passageTempsPartiel(0.5);
        Double tempsPartiel2 = e2.passageTempsPartiel(1.0);

        //Then
        Assertions.assertEquals(0.5, tempsPartiel);
        Assertions.assertEquals(1.0, tempsPartiel2);

    }

    @Test
    public void augmenterSalaireTest() throws EmployeException {
        //Given
        Employe e = new Employe();
        e.setSalaire(1000d);
        Employe e2 = new Employe();
        e2.setSalaire(1000d);
        Employe e3 = new Employe();
        e3.setSalaire(1000d);

        //When
        Double newSalaire = e.augmenterSalaire(5d);
        Double newSalaire2 = e2.augmenterSalaire(-10d);



        //Then
        Assertions.assertEquals(1050d, newSalaire);
        Assertions.assertEquals(900d, newSalaire2);
        EmployeException employe = Assertions.assertThrows(EmployeException.class, () -> e3.augmenterSalaire(-100d));
        Assertions.assertEquals("Impossible d'avoir un salaire à 0.00", employe.getMessage());

    }

    /*@ParameterizedTest
    @CsvSource({

    })
    public void getNbRTTTest(){
        //Given

        //When

        //Then

    }*/

}