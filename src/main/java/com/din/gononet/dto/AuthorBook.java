package com.din.gononet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorBook { 
	private Long id; 
	private long authorId; 
	private long bookId; 
	private double extraValue;
}
