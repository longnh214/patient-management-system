package com.example.patient.constant;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class PageInfo {
    int pageNo;
    int pageSize;
}