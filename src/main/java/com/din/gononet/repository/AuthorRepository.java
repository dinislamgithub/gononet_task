package com.din.gononet.repository;
 

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.din.gononet.entity.Author;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long>{

	public Author findByName(String name);
	
	@Modifying(clearAutomatically = true) 
	@Query(value = "update author set name = ?2 WHERE author_id = ?1", nativeQuery = true)
	void updateAuthor(Long author_id, String name);
	
	public Author findByAuthorIdOrName(Long authorId, String name);
	
	public Author findByAuthorId(Long authorId);
	
	public List<Author> findAllByAuthorId(long authorId);
}
