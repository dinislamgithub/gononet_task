package com.din.gononet.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorDTO { 
	
	private long authorId;
	private String name;
	private AuthorBook authorBook;	
	private List<String> errorMessage; 
	
	private BookDTO bookDTO;
	private String deleteMessage;
}
