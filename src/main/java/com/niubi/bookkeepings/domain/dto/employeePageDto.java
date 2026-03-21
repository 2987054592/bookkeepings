package com.niubi.bookkeepings.domain.dto;

import com.niubi.bookkeepings.domain.query.PageDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class employeePageDto extends PageDto {
    private String name;
}
