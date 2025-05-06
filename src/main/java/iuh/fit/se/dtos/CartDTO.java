package iuh.fit.se.dtos;

import java.util.Map;

import lombok.Data;

@Data
public class CartDTO {
	private Map<Long, Integer> items;

}
