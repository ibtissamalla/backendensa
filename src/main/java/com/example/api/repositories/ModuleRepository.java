//package com.example.api.repositories;
//
//import com.example.api.models.Module; // Assurez-vous que ceci est présent
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//public interface ModuleRepository extends JpaRepository<Module, Integer> {
//    // Récupérer tous les modules
//    List<Module> findAll();
//}
package com.example.api.repositories;

import com.example.api.models.Module; // Assurez-vous que ceci est présent
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ModuleRepository extends JpaRepository<Module, Integer> {
    // Récupérer tous les modules
    List<Module> findAll();
    // Méthode pour trouver un module par son nom
    Optional<Module> findByNom(String nom);

}