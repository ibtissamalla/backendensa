package com.example.api.services;

import com.example.api.models.Etudiant;
import com.example.api.models.Enseignant;
import com.example.api.models.Directeur;
import com.example.api.repositories.EtudiantRepository;
import com.example.api.repositories.EnseignantRepository;
import com.example.api.repositories.DirecteurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private EtudiantRepository etudiantRepository;

    @Autowired
    private EnseignantRepository enseignantRepository;

    @Autowired
    private DirecteurRepository directeurRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Etudiant etudiant = etudiantRepository.findByUsername(username);
        if (etudiant != null) {
            return org.springframework.security.core.userdetails.User.withUsername(etudiant.getUsername())
                    .password(etudiant.getPassword())
                    .roles("ETUDIANT")
                    .build();
        }

        Enseignant enseignant = enseignantRepository.findByUsername(username);
        if (enseignant != null) {
            return org.springframework.security.core.userdetails.User.withUsername(enseignant.getUsername())
                    .password(enseignant.getPassword())
                    .roles("ENSEIGNANT")
                    .build();
        }

        Directeur directeur = directeurRepository.findByUsername(username);
        if (directeur != null) {
            return org.springframework.security.core.userdetails.User.withUsername(directeur.getUsername())
                    .password(directeur.getPassword())
                    .roles("DIRECTEUR")
                    .build();
        }

        throw new UsernameNotFoundException("Utilisateur non trouvé");
    }
}
