package com.niubi.bookkeepings.domain.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PageDto {
    private final Integer DEFAULT_PAGE_NO = 1;
    private final Integer DEFAULT_PAGE_SIZE = 10;
    private Integer pageNo=DEFAULT_PAGE_NO;
    private Integer pageSize=DEFAULT_PAGE_SIZE;

}
