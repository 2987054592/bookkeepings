package com.niubi.bookkeepings.domain.vo;

import com.niubi.bookkeepings.domain.dto.processDto;
import com.niubi.bookkeepings.domain.po.Process;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class bagVo {
    private Integer id;
    private String  name;
    private List<processDto> processList;

}
