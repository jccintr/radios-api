package com.jcsoftware.radios.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jcsoftware.radios.entities.Category;
import com.jcsoftware.radios.entities.RadioList;
import com.jcsoftware.radios.entities.dtos.CategoryDTO;
import com.jcsoftware.radios.entities.dtos.NewRadioListDTO;
import com.jcsoftware.radios.entities.dtos.RadioListDTO;
import com.jcsoftware.radios.repositories.ListItemRepository;
import com.jcsoftware.radios.repositories.RadioListRepository;
import com.jcsoftware.radios.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class RadioListService {
	
	@Autowired
	private RadioListRepository repository;
	
	@Autowired 
	private UserService userService;
	@Autowired
	private AuthService authService;

	
	public RadioListDTO findById(Long id) {
		Optional<RadioList> radioListO = repository.findById(id);
		RadioList radioList = radioListO.orElseThrow(() -> new ResourceNotFoundException("Radio List not found id: "+id));
		authService.isAdminOrOwner(radioList.getOwner().getId(),radioList.getId());
		return new RadioListDTO(radioList);
	}
	

	public RadioListDTO insert(NewRadioListDTO dto) {
		RadioList newRadioList = new RadioList();
		newRadioList.setName(dto.name());
		newRadioList.setOwner(userService.me());
		newRadioList = repository.save(newRadioList);
		return new RadioListDTO(newRadioList);
	}

	public List<RadioListDTO> findAll() {
		List<RadioList> lists = repository.findAll();
		return lists.stream().map(RadioListDTO::new).toList();
	}

	public List<RadioListDTO> findAllByOwner() {
		List<RadioList> lists = repository.findByOwnerId(userService.me().getId());
		return lists.stream().map(RadioListDTO::new).toList();
	}

	 @Transactional
	public void delete(Long id) {
		RadioList radioList = repository.findById(id)
	    	        .orElseThrow(() -> new ResourceNotFoundException("Radio List not found id: " + id));
		authService.isOwner(radioList.getOwner().getId(),radioList.getId());
		
		repository.delete(radioList);
	}

/*
	public RadioListDTO update(Long id, NewRadioListDTO dto) {
		try {
			RadioList list = repository.getReferenceById(id);
			list.setName(dto.name());
			list = repository.save(list);
			return new RadioListDTO(list);
		} catch (EntityNotFoundException e) {
			throw (new ResourceNotFoundException("Radio List not found id: " + id));
		}
	}
	*/
	@Transactional
	public RadioListDTO update(Long id, NewRadioListDTO dto) {
	    RadioList radioList = repository.findById(id)
	        .orElseThrow(() -> new ResourceNotFoundException("Radio List not found id: " + id));

	    authService.isOwner(radioList.getOwner().getId(),radioList.getId());
	    radioList.setName(dto.name());

	    // Não precisa chamar save(), pois a entidade está gerenciada e @Transactional cuida disso
	    return new RadioListDTO(radioList);
	}

}
