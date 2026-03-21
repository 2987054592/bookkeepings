package com.niubi.bookkeepings.domain.vo;

import com.niubi.bookkeepings.domain.query.PageVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class employeePageVo extends PageVo {
    private List<employeeVo> employeeVoList;
}
