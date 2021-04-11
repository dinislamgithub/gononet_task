package com.din.gononet.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.din.gononet.api_resoponse.AuthorApiReponse;
import com.din.gononet.dto.AuthorDTO;
import com.din.gononet.entity.Author;
import com.din.gononet.service.AuthorService;
import com.din.gononet.service.BookService;

@RestController
@RequestMapping("/api")
public class AuthorController {

	@Autowired
	private AuthorService authorService;

	@Autowired
	private AuthorApiReponse authorApiResponse;

	@RequestMapping(value = "/v1/create_author", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<AuthorApiReponse> createAuthor(@Valid @RequestBody AuthorDTO authorDTO) {

		if (authorDTO != null) {
			AuthorDTO reponse = authorService.createAuthor(authorDTO);
			if (reponse.getErrorMessage() == null || reponse.getErrorMessage().isEmpty()) {
				authorApiResponse.setResponseObject(reponse);
				authorApiResponse.setStatusCode(HttpStatus.CREATED);
				authorApiResponse.setErrorMessage(null);//
				return new ResponseEntity<AuthorApiReponse>(authorApiResponse, HttpStatus.CREATED);
			} else {
				authorApiResponse.setErrorMessage(reponse.getErrorMessage());
				authorApiResponse.setStatusCode(HttpStatus.FORBIDDEN);
				return new ResponseEntity<AuthorApiReponse>(authorApiResponse, HttpStatus.FORBIDDEN);
			}
		}
		return null;
	}

	@RequestMapping(value = "/v1/get_author", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<AuthorApiReponse> getAuthor(@RequestBody AuthorDTO authorDTO) {

		if (authorDTO != null) {
			AuthorDTO reponse = authorService.getSpeceficAuthor(authorDTO.getName());

			if (reponse != null) {
				authorApiResponse.setResponseObject(reponse);
				authorApiResponse.setStatusCode(HttpStatus.OK);
				return new ResponseEntity<AuthorApiReponse>(authorApiResponse, HttpStatus.CREATED);
			} else {
				authorApiResponse.setErrorMessage(List.of("This author doesn't existed."));
				authorApiResponse.setStatusCode(HttpStatus.FORBIDDEN);
				return new ResponseEntity<AuthorApiReponse>(authorApiResponse, HttpStatus.FORBIDDEN);
			}
		}
		return null;
	}

	@RequestMapping(value = "/v1/update_author", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<AuthorApiReponse> updateAuthor(@Valid @RequestBody AuthorDTO authorDTO) {

		if (authorDTO != null) {
			AuthorDTO reponse = authorService.updateAuthor(authorDTO);

			if (reponse.getErrorMessage() == null || reponse.getErrorMessage().isEmpty()) {
				authorApiResponse.setResponseObject(reponse);
				authorApiResponse.setStatusCode(HttpStatus.CREATED);
				return new ResponseEntity<AuthorApiReponse>(authorApiResponse, HttpStatus.CREATED);
			} else {
				authorApiResponse.setErrorMessage(reponse.getErrorMessage());
				authorApiResponse.setStatusCode(HttpStatus.FORBIDDEN);
				return new ResponseEntity<AuthorApiReponse>(authorApiResponse, HttpStatus.FORBIDDEN);
			}
		}
		return null;
	}

	@RequestMapping(value = "/v1/delete_author", method = RequestMethod.GET)
	public ResponseEntity<String> deleteAuthor(AuthorDTO authorDTO) {

		if (authorDTO != null) {
			AuthorDTO reponse = authorService.deleteAuthor(authorDTO.getAuthorId());
			return new ResponseEntity<String>(reponse.getDeleteMessage(), HttpStatus.OK);
		}
		return null;
	}

	@RequestMapping(value = "/v1/get_authors_by_booktitle", method = RequestMethod.POST)
	public ResponseEntity<List<Author>> getAuthorsByBookTitle(@RequestBody AuthorDTO authorDTO) {
		List<Author> responseList = new ArrayList();

		if (authorDTO.getBookDTO().getTitle() != null && !authorDTO.getBookDTO().getTitle().isEmpty()) {
			responseList = authorService.getAuthorsByBookTitle(authorDTO.getBookDTO().getTitle());
			return new ResponseEntity<List<Author>>(responseList, HttpStatus.OK);
		} else {
			return new ResponseEntity<List<Author>>(responseList, HttpStatus.FORBIDDEN);
		}
	}

}
