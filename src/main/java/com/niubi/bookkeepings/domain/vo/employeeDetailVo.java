package com.niubi.bookkeepings.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class employeeDetailVo {
    private Integer id;
    private String name;
    List<employeeMonthSalaryVo> employeeMonthSalaryVoList;
}
