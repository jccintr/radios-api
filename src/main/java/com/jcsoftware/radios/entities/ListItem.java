package com.jcsoftware.radios.entities;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "list_items")
public class ListItem {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name="list_id")
	private RadioList list;
	
	@ManyToOne
	@JoinColumn(name="radio_id")
	private Radio radio;
	
	public ListItem() {
		
	}

	public ListItem(Long id, RadioList list, Radio radio) {
		super();
		this.id = id;
		this.list = list;
		this.radio = radio;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public RadioList getList() {
		return list;
	}

	public void setList(RadioList list) {
		this.list = list;
	}

	public Radio getRadio() {
		return radio;
	}

	public void setRadio(Radio radio) {
		this.radio = radio;
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
		ListItem other = (ListItem) obj;
		return Objects.equals(id, other.id);
	}
	
	
	
}
