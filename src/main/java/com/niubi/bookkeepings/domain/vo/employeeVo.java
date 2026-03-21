package com.niubi.bookkeepings.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class employeeVo {
    private String name;
    private Integer id;
    private List<employeeMonthSalary> Salary;
}
