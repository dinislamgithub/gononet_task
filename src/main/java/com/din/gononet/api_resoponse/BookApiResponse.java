package com.din.gononet.api_resoponse;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.din.gononet.dto.BookDTO;

import lombok.Data;

@Data
@Component
public class BookApiResponse {
	BookDTO responseObject = new BookDTO();
	private List<String> errorMessage;
	private HttpStatus statusCode;
}
