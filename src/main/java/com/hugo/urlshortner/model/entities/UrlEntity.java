package com.hugo.urlshortner.model.entities;

import com.hugo.urlshortner.model.dto.UrlDTO;
import com.hugo.urlshortner.model.interfaces.BaseEntity;
import com.hugo.urlshortner.service.Base64UrlCommonService;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "url", uniqueConstraints = {
        @UniqueConstraint(columnNames = "original_url")})
public class UrlEntity implements BaseEntity {

    private static final long serialVersionUID = 9148589425359955100L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "shortened_url", length = 400)
    private String shortnedUrl;

    @Column(name = "original_url", length = 1500, nullable = false)
    private String originalUrl;

    public UrlDTO toDTO() {
        return toDTO(StringUtils.EMPTY);
    }

    public UrlDTO toDTO(String localUrl) {
        String originalUrlDecoded = Base64UrlCommonService.decodeString(this.originalUrl);
        return UrlDTO.builder()
                .originalUrl(originalUrlDecoded)
                .shortnedUrl(localUrl + this.shortnedUrl).build();
    }

}
