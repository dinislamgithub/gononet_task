package com.din.gononet.api_resoponse;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.din.gononet.dto.AuthorDTO;

import lombok.Data;

@Data
@Component
public class AuthorApiReponse {
	 AuthorDTO responseObject = new AuthorDTO();
	 private List<String> errorMessage; 
	 private HttpStatus statusCode;
	 
}
