package com.example.demo.entities;
import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data @AllArgsConstructor @NoArgsConstructor
@Entity
public class Employee {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(unique = true)
	private String matricule;
	@Column(unique = true)
	private String cin;
	private String nomComplet;
	@Temporal(TemporalType.DATE)
	private Date dateDeNaissance;
	@Temporal(TemporalType.DATE)
	private Date dateEmbauche;
	@Column(unique = true)
	private String numAssurance;
	private double soldeConge;
	@Column(unique = true)
	private String cnrps;
	private Gender genre; 
	private Fonction fonction;
	@OneToOne(cascade = CascadeType.ALL)

	private Account account;
	public Employee(Long id, String matricule, String cin, String nomComplet, Date dateDeNaissance, Date dateEmbauche,
			String numAssurance, double soldeConge, String cnrps, Gender genre, Fonction fonction) {
		super();
		this.id = id;
		this.matricule = matricule;
		this.cin = cin;
		this.nomComplet = nomComplet;
		this.dateDeNaissance = dateDeNaissance;
		this.dateEmbauche = dateEmbauche;
		this.numAssurance = numAssurance;
		this.soldeConge = soldeConge;
		this.cnrps = cnrps;
		this.genre = genre;
		this.fonction = fonction;
	}
	
}
