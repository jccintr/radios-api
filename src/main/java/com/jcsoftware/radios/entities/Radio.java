package com.jcsoftware.radios.entities;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "radios")
public class Radio {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	private String shortName;
	private String streamUrl;
	private String imageUrl;
	private Boolean hsl;
	private Boolean webRadio;
	@ManyToOne
	@JoinColumn(name = "city_id")
	private City city;
	@ManyToMany
	@JoinTable(name = "radio_category", joinColumns = @JoinColumn(name = "radio_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
	private Set<Category> categories = new HashSet<>();

	
	public Radio() {
		
	}

	

	public Radio(Long id, String name, String shortName, String streamUrl, String imageUrl, Boolean hsl,
			Boolean webRadio, City city) {
		super();
		this.id = id;
		this.name = name;
		this.shortName = shortName;
		this.streamUrl = streamUrl;
		this.imageUrl = imageUrl;
		this.hsl = hsl;
		this.webRadio = webRadio;
		this.city = city;
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

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getStreamUrl() {
		return streamUrl;
	}

	public void setStreamUrl(String streamUrl) {
		this.streamUrl = streamUrl;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public Boolean getHsl() {
		return hsl;
	}

	public void setHsl(Boolean hsl) {
		this.hsl = hsl;
	}

	public Boolean getWebRadio() {
		return webRadio;
	}

	public void setWebRadio(Boolean webRadio) {
		this.webRadio = webRadio;
	}
	
	
	public City getCity() {
		return city;
	}



	public void setCity(City city) {
		this.city = city;
	}
	
	 @JsonIgnore
	 public Set<Category> getCategories() {
		return categories;
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
		Radio other = (Radio) obj;
		return Objects.equals(id, other.id);
	}
	
	

}
