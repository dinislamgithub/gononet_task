package com.din.gononet.serviceImpl;

import java.util.List;

import com.din.gononet.dto.AuthorDTO;
import com.din.gononet.entity.Author;

public interface AuthorServiceImpl {

	public AuthorDTO createAuthor(AuthorDTO authorDTO);
	
	 public AuthorDTO getSpeceficAuthor(String name);
	
	 public AuthorDTO updateAuthor(AuthorDTO authorDTO);
	
	 public List<Author> getAuthorsByBookTitle(String title);
}
