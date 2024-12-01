package com.sdu127.Data.Objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeBlock {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
