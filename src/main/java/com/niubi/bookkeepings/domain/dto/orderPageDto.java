package com.niubi.bookkeepings.domain.dto;

import com.niubi.bookkeepings.domain.query.PageDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class orderPageDto extends PageDto {
    private String name;
    private Integer bagName;
    private LocalDate endTime;
    private LocalDate startTime;

}
