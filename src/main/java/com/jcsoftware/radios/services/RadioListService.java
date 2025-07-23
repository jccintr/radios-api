package com.jcsoftware.radios.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jcsoftware.radios.entities.RadioList;
import com.jcsoftware.radios.entities.dtos.NewRadioListDTO;
import com.jcsoftware.radios.entities.dtos.RadioListDTO;
import com.jcsoftware.radios.repositories.RadioListRepository;
import com.jcsoftware.radios.services.exceptions.ResourceNotFoundException;

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

	public Page<RadioListDTO> findAll(Pageable pageable) {
		Pageable customPageable = PageRequest.of(
	            pageable.getPageNumber(),
	            10,                     
	            Sort.by("name").ascending() 
	        );
		Page<RadioList> lists = repository.findAll(customPageable);
		return lists.map(RadioListDTO::new);
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
