package com.din.gononet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.din.gononet.entity.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>{

   public Book findByTitle(String title);

	@Modifying(clearAutomatically = true) 
	@Query(value = "update book set title = ?2 WHERE book_id = ?1", nativeQuery = true)
	void updateBook(Long bookId, String title);
	
	public Book findByBookId(Long bookId);
	
}
