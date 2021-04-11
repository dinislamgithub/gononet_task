package com.din.gononet.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.din.gononet.dto.AuthorBook;
import com.din.gononet.dto.AuthorDTO;
import com.din.gononet.entity.Author;
import com.din.gononet.entity.Author_Book;
import com.din.gononet.entity.Book;
import com.din.gononet.repository.AuthorBookRepository;
import com.din.gononet.repository.AuthorRepository;
import com.din.gononet.repository.BookRepository;
import com.din.gononet.serviceImpl.AuthorServiceImpl;

@Service
@Transactional
public class AuthorService implements AuthorServiceImpl {

	@Autowired
	private AuthorRepository authorRepository;

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private AuthorBookRepository authorBookRepository;

	private boolean isValueDuplicate = false;

	@Override
	public AuthorDTO createAuthor(AuthorDTO authorDTO) {
		AuthorDTO authorResponse = new AuthorDTO();
		Author_Book authorBook = new Author_Book();
		Author author = new Author();

		List<Author> authorList = authorRepository.findAll();

		if (authorList.isEmpty()) {

			if (authorDTO != null) {
				author.setName(authorDTO.getName());
				authorBook.setAuthorId(1);
				authorBook.setBookId(0);
				authorBook.setExtraValue(0);
				author.setAuthorBook(authorBook);

				List<String> list = requiredFieldValidate(author);
				if (list.isEmpty()) {
					author = authorRepository.save(author);
					authorBook = authorBookRepository.save(authorBook);
					authorResponse = copyAuthorDTOFromAuthor(author, authorBook);
				} else {
					authorResponse.setErrorMessage(list);
				}

			}
		} else {
			for (Author auth : authorList) {
				if (auth.getName().matches(authorDTO.getName())) {
					authorResponse.setErrorMessage(List.of("Duplicate Author Name Not Allowed."));
					isValueDuplicate = false;
					return authorResponse;
				} else {
					isValueDuplicate = true;
				}
			}

			if (isValueDuplicate) {
				author.setName(authorDTO.getName());
				authorBook.setAuthorId(authorList.size() + 1);
				authorBook.setBookId(0);
				authorBook.setExtraValue(0);
				author.setAuthorBook(authorBook);

				List<String> list = requiredFieldValidate(author);
				if (list.isEmpty()) {
					author = authorRepository.save(author);
					authorBook = authorBookRepository.save(authorBook);
					authorResponse = copyAuthorDTOFromAuthor(author, authorBook);
				} else {
					authorResponse.setErrorMessage(list);
				}
			}

		}

		return authorResponse;
	}

	public AuthorDTO copyAuthorDTOFromAuthor(Author author, Author_Book authorBook) {
		AuthorDTO dto = new AuthorDTO();
		AuthorBook authorBookDto = new AuthorBook();

		if (author != null) {
			dto.setAuthorId(author.getAuthorId());
			dto.setName(author.getName());

			authorBookDto.setId(authorBook.getId());
			authorBookDto.setAuthorId(authorBook.getAuthorId());
			authorBookDto.setBookId(authorBook.getBookId());
			authorBookDto.setExtraValue(authorBook.getExtraValue());

			dto.setAuthorBook(authorBookDto);
		}
		return dto;
	}

	public List<String> requiredFieldValidate(Author autho) {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<Author>> violations = validator.validate(autho);

		List<String> validationList = new ArrayList<>();
		for (ConstraintViolation<Author> violation : violations) {
			validationList = List.of(violation.getMessage());
		}
		return validationList;
	}

	@Override
	public AuthorDTO getSpeceficAuthor(String name) {
		AuthorDTO authorDTO = new AuthorDTO();
		Author author = new Author();
		Author_Book authorBook = new Author_Book();
		if (name != null) {
			author = authorRepository.findByName(name);
			authorBook = authorBookRepository.findByAuthorId(author.getAuthorId());
			authorDTO = this.copyAuthorDTOFromAuthor(author, authorBook);

		} else {
			authorDTO.setErrorMessage(List.of("This author doesn't existed."));
		}
		return authorDTO;
	}

	@Override
	public AuthorDTO updateAuthor(AuthorDTO authorDTO) {
		Author author = new Author();
		Author_Book authorBook = new Author_Book();
		AuthorDTO authorResponse = new AuthorDTO();

		if (authorDTO != null) {
			if (authorDTO.getAuthorId() != 0 && authorDTO.getName() != null) {
				author.setName(authorDTO.getName());
				List<String> list = requiredFieldValidate(author); // name validation
				if (list.isEmpty()) {
					authorRepository.updateAuthor(authorDTO.getAuthorId(), authorDTO.getName());
					author = authorRepository.findById(authorDTO.getAuthorId()).get();

					authorBook = authorBookRepository.findByAuthorId(author.getAuthorId());
					authorResponse = this.copyAuthorDTOFromAuthor(author, authorBook);
				} else {
					authorResponse.setErrorMessage(list);
				}
			}
		} else {
			authorResponse.setErrorMessage(List.of("Requested Object property isn't valid."));
		}
		return authorResponse;
	}

	public AuthorDTO deleteAuthor(Long authorId) {
		AuthorDTO authorDto = new AuthorDTO();
		List<Author> list = authorRepository.findAll();

		if (authorId != 0) {
			for (Author a : list) {
				if (a.getAuthorId() == authorId) {
					authorRepository.deleteById(authorId);
					authorBookRepository.deleteById(authorId);
					authorDto.setAuthorId(authorId);
					authorDto.setDeleteMessage(" Data is deleted successfully.");
				} else {
					authorDto.setDeleteMessage("Desire authorId isn't valid.");
				}
			}
		}
		return authorDto;
	}

	@Override
	public List<Author> getAuthorsByBookTitle(String title) {
		Book book = bookRepository.findByTitle(title);
		List<Author> responseAuthors = new ArrayList<>();

		if (book != null) {
			List<Author_Book> authorBooks = (List<Author_Book>) authorBookRepository.findAllBybookId(book.getBookId());
			List<Author> authorList = authorRepository.findAll();

			if ((title != null || title.isEmpty()) && !authorBooks.isEmpty()) {
				for (Author_Book ab : authorBooks) {
					for (Author a : authorList) {
						if (a.getAuthorId() == ab.getAuthorId()) {
							responseAuthors.add(a);
						}
					}
				}
			}
		}

		return responseAuthors;
	}

}
