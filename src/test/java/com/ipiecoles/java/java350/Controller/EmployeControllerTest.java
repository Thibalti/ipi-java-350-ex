package com.ipiecoles.java.java350.Controller;

import com.ipiecoles.java.java350.model.Employe;
import com.ipiecoles.java.java350.repository.EmployeRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class EmployeControllerTest {
   /* @Autowired
    private MockMvc mockMvc;

    @MockBean
    EmployeRepository employeRepository;

    @Test
    public void testGetEmploye() throws Exception {
        //Given
        Employe employe = new Employe("Doe", "John", "T0001", LocalDate.now(),
                1500d, 1, 1.0);
        employe.setId(5l);
        Mockito.when(employeRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(employe));

        //When
        ResultActions result =
                mockMvc.perform(MockMvcRequestBuilders.get("/employes/5"));

        //Then
        result.andExpect(status().isOk())
                .andExpect((ResultMatcher) content().json("{'id' : 5, 'nom' : 'Doe',  'prenom' : 'John', 'matricule' : 'T0001', 'dateEmbauche' : '2020-03-12', 'salaire' : 1500.0, 'performance' : 1, 'tempsPartiel' : 1.0 }"));
    }

*/}