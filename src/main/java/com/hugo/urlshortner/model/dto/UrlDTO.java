package com.hugo.urlshortner.model.dto;

import com.hugo.urlshortner.service.validation.constraints.Url;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
@Data
public class UrlDTO implements Serializable {

    private static final long serialVersionUID = -5044249260947239230L;

    private String shortnedUrl;

    @Url(message = "Invalid URL!")
    private String originalUrl;

}
