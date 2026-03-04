package com.vkdev.urlshortner.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class ShortCodeRequestDTO {
    private String url;
    private String alias;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date expiryDate;
}
