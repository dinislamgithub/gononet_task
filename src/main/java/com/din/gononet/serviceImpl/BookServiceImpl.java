package com.din.gononet.serviceImpl;

import java.util.List;

import com.din.gononet.dto.BookDTO;
import com.din.gononet.entity.Book;

public interface BookServiceImpl {
	public BookDTO createBook(BookDTO bookDTO);
	
	 public BookDTO getSpeceficBook(String title);
		
	 public BookDTO updateBook(BookDTO bookDTO);
	
	public BookDTO deleteBook(BookDTO bookDTO);
	
	public List<Book> getBooksByAuthorName(String name);
	
}
