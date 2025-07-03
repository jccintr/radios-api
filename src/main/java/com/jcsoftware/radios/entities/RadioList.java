package com.jcsoftware.radios.entities;

import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "lists")
public class RadioList {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private User owner;
	
	@OneToMany(mappedBy = "list", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<ListItem> radios = new HashSet<>();

	@Column(columnDefinition="TIMESTAMP WITHOUT TIME ZONE")
	private Instant createdAt;
	@Column(columnDefinition="TIMESTAMP WITHOUT TIME ZONE")
	private Instant updatedAt;
	
	public RadioList() {
		
	}
	
	public RadioList(Long id, String name, User owner) {
		super();
		this.id = id;
		this.name = name;
		this.owner = owner;
	}
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}
	
	

	public Set<ListItem> getRadios() {
		return radios;
	}

	public void setItems(Set<ListItem> radios) {
		this.radios = radios;
	}
	
	

	public Instant getCreatedAt() {
		return createdAt;
	}

	public Instant getUpdatedAt() {
		return updatedAt;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RadioList other = (RadioList) obj;
		return Objects.equals(id, other.id);
	}

	@PrePersist
	public void prePersist() {
		this.createdAt = Instant.now();
	}
	
	@PreUpdate
	public void preUpdate() {
		this.updatedAt = Instant.now();
	}
	
	public boolean containsRadio(Radio radio) {
	    if (radio == null) {
	        return false;
	    }
	    return radios.stream().anyMatch(item -> radio.equals(item.getRadio()));
	}
}
