package com.sdu127.Data.DTO;

import lombok.Data;

import java.util.List;

@Data
public class ReportParams {
    Integer type;
    String content;
    List<String> images;
}
