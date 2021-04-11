package com.din.gononet.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.din.gononet.api_resoponse.BookApiResponse;
import com.din.gononet.dto.BookDTO;
import com.din.gononet.entity.Book;
import com.din.gononet.service.BookService;

@RestController
@RequestMapping("/api")
public class BookController {

	@Autowired
	private BookService bookService;

	@Autowired
	private BookApiResponse bookApiResponse;

	@RequestMapping(value = "/v1/create_book", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<BookApiResponse> createAuthor(@Valid @RequestBody BookDTO bookDTO) {

		if (bookDTO != null) {
			BookDTO reponse = bookService.createBook(bookDTO);

			if (reponse.getErrorMessage() == null || reponse.getErrorMessage().isEmpty()) {
				bookApiResponse.setResponseObject(reponse);
				bookApiResponse.setStatusCode(HttpStatus.CREATED);
				return new ResponseEntity<BookApiResponse>(bookApiResponse, HttpStatus.CREATED);
			} else {
				bookApiResponse.setErrorMessage(reponse.getErrorMessage());
				bookApiResponse.setStatusCode(HttpStatus.FORBIDDEN);
				return new ResponseEntity<BookApiResponse>(bookApiResponse, HttpStatus.FORBIDDEN);
			}
		}
		return null;
	}

	@RequestMapping(value = "/v1/get_specific_author", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<BookApiResponse> getAuthor(BookDTO bookDTO) {

		if (bookDTO.getTitle() != null) {
			BookDTO reponse = bookService.getSpeceficBook(bookDTO.getTitle());

			if (reponse != null && reponse.getErrorMessage().isEmpty()) {
				bookApiResponse.setResponseObject(reponse);
				bookApiResponse.setStatusCode(HttpStatus.OK);
				return new ResponseEntity<BookApiResponse>(bookApiResponse, HttpStatus.CREATED);
			} else {
				bookApiResponse.setErrorMessage(reponse.getErrorMessage());
				bookApiResponse.setStatusCode(HttpStatus.FORBIDDEN);
				return new ResponseEntity<BookApiResponse>(bookApiResponse, HttpStatus.FORBIDDEN);
			}
		}
		return null;
	}

	@RequestMapping(value = "/v1/update_book", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<BookApiResponse> updateBook(@Valid @RequestBody BookDTO bookDTO) {
		if (bookDTO != null) {
			BookDTO reponse = bookService.updateBook(bookDTO);

			if (reponse.getErrorMessage() == null || reponse.getErrorMessage().isEmpty()) {
				bookApiResponse.setResponseObject(reponse);
				bookApiResponse.setStatusCode(HttpStatus.CREATED);
				return new ResponseEntity<BookApiResponse>(bookApiResponse, HttpStatus.CREATED);
			} else {
				bookApiResponse.setErrorMessage(reponse.getErrorMessage());
				bookApiResponse.setStatusCode(HttpStatus.FORBIDDEN);
				return new ResponseEntity<BookApiResponse>(bookApiResponse, HttpStatus.FORBIDDEN);
			}
		}
		return null;
	}

	@RequestMapping(value = "/v1/delete_book", method = RequestMethod.GET)
	public ResponseEntity<String> deleteBook(BookDTO bookDTO) {
		if (bookDTO.getBookId() != 0) {
			BookDTO reponse = bookService.deleteBook(bookDTO);
			return new ResponseEntity<String>(reponse.getDeleteMessage(), HttpStatus.OK);
		} else {
			return new ResponseEntity<String>(List.of("BookId is missing.").get(0), HttpStatus.FORBIDDEN);
		}
	}

	@RequestMapping(value = "/v1/get_books_by_author", method = RequestMethod.POST)
	public ResponseEntity<List<Book>> getBookByAuthor(@RequestBody BookDTO bookDTO) {
		List<Book> responseList = new ArrayList();
		if (bookDTO.getAuthorDTO().getName() != null || !bookDTO.getAuthorDTO().getName().isEmpty()) {
			responseList = bookService.getBooksByAuthorName(bookDTO.getAuthorDTO().getName());
			return new ResponseEntity<List<Book>>(responseList, HttpStatus.OK);
		} else {
			return new ResponseEntity<List<Book>>(responseList, HttpStatus.FORBIDDEN);
		}
	}
	
	
	
	
	
	
	//PATH variable
	//http://localhost:8080/api/book/2/javabook
	@GetMapping("/book/{bookId}/{title}")
	public BookDTO getBooksById(@PathVariable long bookId, @PathVariable String title) {		
		return new BookDTO();
	}	
	@GetMapping("/book/{bookIdCng}/{titleCng}")
	public BookDTO getBooksById2(@PathVariable("bookIdCng") long bookId, @PathVariable("titleCng") String title) {		
		return new BookDTO();
	}
	
	
	
	
	//request param
	//http://localhost:8080/api/book?bookId=2&title=java
		@GetMapping("/book/")
		public BookDTO getBookById(@RequestParam long bookId, @RequestParam String title) {
			return new BookDTO();
		}
		//http://localhost:8080/api/book?bookIdCng=2&titleCng=java
		@GetMapping("/book/")
		public BookDTO getBookById2(@RequestParam("bookIdCng") long bookId, @RequestParam("titleCng") String title) {
			return new BookDTO();
		}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
