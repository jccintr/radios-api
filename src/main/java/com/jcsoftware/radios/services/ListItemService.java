package com.jcsoftware.radios.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jcsoftware.radios.entities.ListItem;
import com.jcsoftware.radios.entities.Radio;
import com.jcsoftware.radios.entities.RadioList;
import com.jcsoftware.radios.entities.dtos.ListItemDTO;
import com.jcsoftware.radios.entities.dtos.NewListItemDTO;
import com.jcsoftware.radios.repositories.ListItemRepository;
import com.jcsoftware.radios.repositories.RadioListRepository;
import com.jcsoftware.radios.repositories.RadioRepository;
import com.jcsoftware.radios.services.exceptions.RadioAlreadyInListException;
import com.jcsoftware.radios.services.exceptions.ResourceNotFoundException;

@Service
public class ListItemService {
	
	@Autowired
	private ListItemRepository repository;
	@Autowired
	private RadioRepository radioRepository;
	@Autowired
	private RadioListRepository radioListRepository;
	@Autowired
	private AuthService authService;
	

    @Transactional
	public ListItemDTO insert(NewListItemDTO dto) {
		
		// verificar se a lista existe
		Optional<RadioList> radioListO = radioListRepository.findById(dto.listId());
		RadioList radioList = radioListO.orElseThrow(() -> new ResourceNotFoundException("Radio List not found id: " + dto.listId()));
		
		// verificar se o auth é dono da lista
		authService.isOwner(radioList.getOwner().getId(),radioList.getId());
		
		// verificar se a radio existe
		Optional<Radio> radioO = radioRepository.findById(dto.radioId());
		Radio radio = radioO.orElseThrow(() -> new ResourceNotFoundException("Radio not found id: " + dto.radioId()));
		// verificar se a rádio já não está na lista
		if(radioList.containsRadio(radio)) throw new RadioAlreadyInListException("Radio already in list id: "+ radio.getId());
		// salva o ListItem
		ListItem newListItem = new ListItem();
		newListItem.setList(radioList);
		newListItem.setRadio(radio);
		newListItem = repository.save(newListItem);
		return new ListItemDTO(newListItem);
	}
    

	public void delete(Long id) {
		
    	ListItem item = repository.findById(id)
    	        .orElseThrow(() -> new ResourceNotFoundException("List Item not found id: " + id));
    	authService.isOwner(item.getList().getOwner().getId(),item.getList().getId());
    	repository.deleteById(item.getId());
	}

}
