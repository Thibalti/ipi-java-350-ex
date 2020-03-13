package com.ipiecoles.java.java350.repository;

import com.ipiecoles.java.java350.model.Employe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeRepository extends JpaRepository<Employe, Long> {
    @Query("select max(substring(matricule,2)) from Employe")
    String findLastMatricule();

    Employe findByMatricule(String matricule);

    @Query("select avg(performance) from Employe where SUBSTRING(matricule,0,1) = ?1 ")
    Double avgPerformanceWhereMatriculeStartsWith(String premiereLettreMatricule);

    @Query("select e2 from Employe e2 where e2.salaire < (select e.salaire from Employe e where e.matricule = ?1)")
    List<Employe> findEmployeGagnantMoinsQue(String matricule);

    @Query("select sum(tempsPartiel) from Employe")
    Double sumTempsPartiel();

    @Query("select sum(salaire) from Employe")
    Double sumSalaire();
}
