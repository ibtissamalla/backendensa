package com.example.api.controllers;

import com.example.api.models.*;
import com.example.api.services.MatiereService;
import com.example.api.repositories.EtudiantRepository;
import com.example.api.repositories.EnseignantRepository;
import com.example.api.repositories.DirecteurRepository;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Value("${security.jwt.secret-key}")
    private String jwtSecretKey;

    @Value("${security.jwt.issuer}")
    private String jwtIssuer;

    @Autowired
    private EtudiantRepository etudiantRepository;

    @Autowired
    private EnseignantRepository enseignantRepository;

    @Autowired
    private DirecteurRepository directeurRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MatiereService matiereService;

    // Inscription de l'étudiant
    @PostMapping("/register/etudiant")
    public ResponseEntity<Object> registerEtudiant(@Valid @RequestBody Etudiant etudiant) {
        etudiant.setPassword(new BCryptPasswordEncoder().encode(etudiant.getPassword()));
        etudiantRepository.save(etudiant);
        return ResponseEntity.ok("Étudiant inscrit avec succès");
    }

    // Inscription de l'enseignant
    @PostMapping("/register/enseignant")
    public ResponseEntity<Object> registerEnseignant(@Valid @RequestBody Enseignant enseignant) {
        enseignant.setPassword(new BCryptPasswordEncoder().encode(enseignant.getPassword()));
        enseignantRepository.save(enseignant);
        return ResponseEntity.ok("Enseignant inscrit avec succès");
    }

    // Inscription du directeur
    @PostMapping("/register/directeur")
    public ResponseEntity<Object> registerDirecteur(@Valid @RequestBody Directeur directeur) {
        directeur.setPassword(new BCryptPasswordEncoder().encode(directeur.getPassword()));
        directeurRepository.save(directeur);
        return ResponseEntity.ok("Directeur inscrit avec succès");
    }

    // Connexion
    @PostMapping("/login")
    public ResponseEntity<Object> login(@Valid @RequestBody LoginDto loginDto) {
        try {
            // Authentification de l'utilisateur
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword())
            );

            // Génération du token JWT
            String jwtToken = createJwtToken(authentication);
            String username = authentication.getName();


            // Préparer la réponse avec le token et les informations de l'utilisateur
            Map<String, Object> response = new HashMap<>();
            response.put("token", jwtToken);
            response.put("username", authentication.getName());
            response.put("roles", authentication.getAuthorities());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Authentification échouée: " + e.getMessage());
        }
    }

    // Récupérer les noms des matières de l'enseignant connecté
    @GetMapping("/matieres")
    public ResponseEntity<List<Map<String, Object>>> getMatieresForConnectedEnseignant(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body(null);
        }

        String username = authentication.getName();
        Enseignant enseignant = enseignantRepository.findByUsername(username);
        if (enseignant == null) {
            return ResponseEntity.status(404).body(null);
        }

        // Récupérer les ID et noms des matières par ID de l'enseignant
        List<Map<String, Object>> matieres = matiereService.findMatiereIdsAndNamesByEnseignantId(enseignant.getId());

        return ResponseEntity.ok(matieres);
    }



    // Méthode pour générer un token JWT
    private String createJwtToken(Authentication authentication) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(jwtIssuer)
                .issuedAt(now)
                .expiresAt(now.plusSeconds(24 * 3600)) // Expire dans 24 heures
                .subject(authentication.getName())
                .claim("roles", authentication.getAuthorities())
                .claim("id", getUserIdFromAuthentication(authentication)) // Ajout de l'ID de l'utilisateur
                .build();

        var secretKey = new ImmutableSecret<>(jwtSecretKey.getBytes());
        var encoder = new NimbusJwtEncoder(secretKey);
        var params = JwtEncoderParameters.from(
                JwsHeader.with(MacAlgorithm.HS256).build(),
                claims
        );

        return encoder.encode(params).getTokenValue();
    }

    // Méthode pour récupérer l'ID de l'utilisateur à partir de l'authentification
    private int getUserIdFromAuthentication(Authentication authentication) {
        String username = authentication.getName();

        // Vérification si l'utilisateur est un enseignant
        Enseignant enseignant = enseignantRepository.findByUsername(username);
        if (enseignant != null) {
            return enseignant.getId();
        }

        // Vérification si l'utilisateur est un étudiant
        Etudiant etudiant = etudiantRepository.findByUsername(username);
        if (etudiant != null) {
            return Math.toIntExact(etudiant.getId());
        }

        // Vérification si l'utilisateur est un directeur
        Directeur directeur = directeurRepository.findByUsername(username);
        if (directeur != null) {
            return directeur.getId();
        }

        // Retourne -1 si l'utilisateur n'est pas trouvé dans les trois catégories
        return -1;
    }

}