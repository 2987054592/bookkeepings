package com.niubi.bookkeepings.domain.vo;

import com.niubi.bookkeepings.domain.query.PageVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@Data
@NoArgsConstructor
public class bagPageVo extends PageVo {
    private List<bagVo> bagList;
}
