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

import com.din.gononet.dto.AuthorDTO;
import com.din.gononet.dto.BookDTO;
import com.din.gononet.entity.Author;
import com.din.gononet.entity.Author_Book;
import com.din.gononet.entity.Book;
import com.din.gononet.repository.AuthorBookRepository;
import com.din.gononet.repository.AuthorRepository;
import com.din.gononet.repository.BookRepository;
import com.din.gononet.serviceImpl.BookServiceImpl;

@Service
@Transactional
public class BookService implements BookServiceImpl {

	@Autowired
	private AuthorRepository authorRepository;

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private AuthorBookRepository authorBookRepository;
	private boolean isValueDuplicate = false;
	private boolean isAuthorCreate = false;

	@Override
	public BookDTO createBook(BookDTO bookDTO) {
		BookDTO bookResponse = new BookDTO();
		Book book = new Book();
		List<Author> authorList = authorRepository.findAll();
		Author author = authorRepository.findByAuthorIdOrName(bookDTO.getAuthorDTO().getAuthorId(),
				bookDTO.getAuthorDTO().getName());
		List<Book> bookList = bookRepository.findAll();
		AuthorDTO authorDTO = new AuthorDTO();
		Author_Book authorBook = new Author_Book();

		if (bookList.size() > 0) {
			for (Book b : bookList) {
				if (b.getTitle().contains(bookDTO.getTitle())) {
					isValueDuplicate = true;
					bookResponse.setErrorMessage(List.of("Book title must be unique."));
				}
			}
		}

		if (bookDTO != null && !isValueDuplicate) {
			book.setTitle(bookDTO.getTitle());

			List<String> list = requiredFieldValidate(book);
			if (list.isEmpty()) {
				for (Author aList : authorList) {
					if (((aList.getAuthorId() == author.getAuthorId()) || (aList.getName().matches(author.getName())))
							&& (!isValueDuplicate)) {
						book = bookRepository.save(book);

						authorBook.setAuthorId(aList.getAuthorId());
						authorBook.setBookId(book.getBookId());
						authorBook.setExtraValue(bookDTO.getAuthorBook().getExtraValue());
						authorBook = authorBookRepository.save(authorBook);

						bookResponse.setBookId(book.getBookId());
						bookResponse.setTitle(book.getTitle());

						authorDTO.setAuthorId(aList.getAuthorId());
						authorDTO.setName(aList.getName());
						bookResponse.setAuthorDTO(authorDTO);
					} else {
						isAuthorCreate = false;
					}
				}

				if (isAuthorCreate == true) {
					bookResponse.setErrorMessage(List.of("You must create Author first."));
				}

			} else {
				bookResponse.setErrorMessage(list);
			}

		}

		return bookResponse;
	}

	@Override
	public BookDTO getSpeceficBook(String title) {
		BookDTO dto = new BookDTO();
		Book book = new Book();
		Author_Book authorBook = new Author_Book();
		Author author = new Author();
		AuthorDTO authorDTO = new AuthorDTO();

		if (title != null || title.isEmpty() || title.isBlank()) {
			book = bookRepository.findByTitle(title);
			if (book != null) {
				authorBook = authorBookRepository.findByBookId(book.getBookId());
				author = authorRepository.findByAuthorId(authorBook.getAuthorId());

				dto.setBookId(book.getBookId());
				dto.setTitle(book.getTitle());
				authorDTO.setAuthorId(author.getAuthorId());
				authorDTO.setName(author.getName());
				dto.setAuthorDTO(authorDTO);
			} else {
				dto.setErrorMessage(List.of("Desire title doesn't exist."));
			}

		} else {
			dto.setErrorMessage(List.of("The given title is emmpty."));
		}

		return dto;
	}

	@Override
	public BookDTO updateBook(BookDTO bookDTO) {
		BookDTO bookDto = new BookDTO();
		List<Book> books = bookRepository.findAll();
		Book book = new Book();
		boolean matchedBookId = false;

		if (bookDTO.getTitle() != null || bookDTO.getTitle().isEmpty() || bookDTO.getTitle().isBlank()) {
			book.setTitle(bookDTO.getTitle());
			List<String> list = requiredFieldValidate(book);
			if (list.isEmpty()) {
				for (Book b : books) {
					if (b.getBookId() == bookDTO.getBookId()) {
						bookRepository.updateBook(b.getBookId(), bookDTO.getTitle());
						book = bookRepository.findByBookId(b.getBookId());
						bookDto.setBookId(book.getBookId());
						bookDto.setTitle(book.getTitle());
						matchedBookId = true;
					}
				}
				if (matchedBookId == false) {
					bookDto.setErrorMessage(List.of("Desire title is empty."));
				}
			} else {
				bookDto.setErrorMessage(list);
			}

		} else {
			bookDto.setErrorMessage(List.of("Desire title is empty."));
		}
		return bookDto;
	}

	@Override
	public BookDTO deleteBook(BookDTO bookDTO) {
		BookDTO bookDto = new BookDTO();
		List<Book> bookList = bookRepository.findAll();
		boolean matchedBookId = false;

		if (bookDTO.getBookId() != 0) {
			for (Book b : bookList) {
				if (b.getBookId() == bookDTO.getBookId()) {
					bookRepository.deleteById(bookDTO.getBookId());
					authorBookRepository.deleteAuthorBookByBookId(bookDTO.getBookId());
					bookDto.setDeleteMessage(" Data is deleted successfully.");
					matchedBookId = true;
				} else {
					matchedBookId = false;

				}
			}
			if (matchedBookId == false) {
				bookDto.setDeleteMessage("Desire bookId isn't valid.");
			}

		}
		return bookDto;
	}

	@Override
	public List<Book> getBooksByAuthorName(String name) {
		Author author = authorRepository.findByName(name);
		List<Book> responseBooks = new ArrayList<>();
		if (author != null) {
			List<Author_Book> authorBooks = (List<Author_Book>) authorBookRepository
					.findAllByAuthorId(author.getAuthorId());
			List<Book> bookList = bookRepository.findAll();

			if ((name != null || name.isEmpty()) && !authorBooks.isEmpty()) {
				for (Author_Book ab : authorBooks) {
					for (Book b : bookList) {
						if (ab.getBookId() == b.getBookId()) {
							responseBooks.add(b);
						}
					}
				}
			}
		}
		return responseBooks;
	}

	public List<String> requiredFieldValidate(Book book) {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<Book>> violations = validator.validate(book);

		List<String> validationList = new ArrayList<>();
		for (ConstraintViolation<Book> violation : violations) {
			validationList = List.of(violation.getMessage());
		}
		return validationList;
	}

}
