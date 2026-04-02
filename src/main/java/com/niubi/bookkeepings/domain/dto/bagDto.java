package com.niubi.bookkeepings.domain.dto;

import com.niubi.bookkeepings.domain.po.Process;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class bagDto {
    private Integer id;
    private String name;
    private List<processDto> processList;
}
