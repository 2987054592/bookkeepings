package com.niubi.bookkeepings.domain.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PageVo {
    private long totalData;
    private long totalPage;
}
