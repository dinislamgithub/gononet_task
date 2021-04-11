package com.din.gononet.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "author")
public class Author {
	private static final long serialVersionUID = 4910225916550731448L;
	
	@Id
	@Column(name = "author_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long authorId;

	@Column(name = "name")
	@NotNull(message = "Name cann't be null.")
	@NotEmpty(message = "Name cann't be empty.")
	private String name;

	@Transient
	private Author_Book authorBook;
	
	
	

}
