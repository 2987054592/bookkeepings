package com.niubi.bookkeepings.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class processDto {
    private Integer id;
    private String name;
    private BigDecimal price;
}
