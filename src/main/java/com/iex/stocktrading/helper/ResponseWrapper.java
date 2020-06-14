package com.iex.stocktrading.helper;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseWrapper {

	private List<?> payload;
	
	public ResponseWrapper(Page<?> page) {
		this.payload = page.getContent();
	}

}
