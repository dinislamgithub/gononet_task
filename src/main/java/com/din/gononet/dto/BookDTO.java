package com.din.gononet.dto; 

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO { 
	private long bookId; 
	private String title; 
	private AuthorBook authorBook;	
	private List<String> errorMessage; 
	
	private AuthorDTO authorDTO;
	private String deleteMessage;
}
