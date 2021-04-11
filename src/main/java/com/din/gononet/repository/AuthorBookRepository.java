package com.din.gononet.repository;


import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.din.gononet.entity.Author_Book;

@Repository
public interface AuthorBookRepository extends JpaRepository<Author_Book, Long>{

	public Author_Book findByAuthorId(long authorId);
	
	public Author_Book findByBookId(long bookId);
	
	@Modifying(clearAutomatically = true) 
	@Query(value = "update author_book set author_id =?2 , book_id = ?3 WHERE author_id = ?1", nativeQuery = true)
	void updateAuthorBook(Long authorId,Long authId, Long bookId);
	
	@Transactional
	@Modifying
	@Query(value = "delete from author_book WHERE book_id = ?1", nativeQuery = true)
	void deleteAuthorBookByBookId(Long bookId);
	
	
	public List<Author_Book> findAllByAuthorId(long authorId);
	
	public List<Author_Book> findAllBybookId(long bookId);
	
}
