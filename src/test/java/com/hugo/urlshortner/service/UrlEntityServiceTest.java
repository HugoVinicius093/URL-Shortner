package com.hugo.urlshortner.service;

import com.hugo.urlshortner.model.entities.UrlEntity;
import com.hugo.urlshortner.repository.URLRepository;
import com.hugo.urlshortner.service.exception.UrlException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@Slf4j
@SpringBootTest
public class UrlEntityServiceTest {

    @InjectMocks
    private UrlService urlService;

    @Mock
    private URLRepository urlRepository;

    private UrlEntity urlEntity;

    @BeforeAll
    public static void setup() {
        log.info("--- STARTING TESTS ON SERVICE ---");
    }

    @BeforeEach
    public void createDummyClient() {
        urlEntity = UrlEntity.builder()
                .id(1L)
                .originalUrl("aHR0cHM6Ly9zdGFja292ZXJmbG93LmNvbQ==")
                .shortnedUrl("MQ==")
                .build();
        Mockito.when(urlRepository.findByShortnedUrl(urlEntity.getShortnedUrl())).thenReturn(Optional.ofNullable(urlEntity));
        Mockito.when(urlRepository.findByOriginalUrl(urlEntity.getOriginalUrl())).thenReturn(Optional.ofNullable(urlEntity));
    }


    @Test
    void shouldFindUrlByShortnedUrl() throws UrlException {
        Optional<UrlEntity> returnedUrl = Optional.ofNullable(urlService.findUrlsByShortnedUrl(urlEntity.getShortnedUrl()));
        returnedUrl.ifPresent(this::compareObjectFields);
    }

    @Test
    void shouldNotFindUrlByShortnedUrl() {
        Assertions.assertThrows(UrlException.class,
                () -> urlService.findUrlsByShortnedUrl("test987")
        );
    }

    @Test
    void shouldFindUrlByOriginalUrl() throws UrlException {
        Optional<UrlEntity> returnedUrl = Optional.ofNullable(urlService.findUrlByOriginalUrl(urlEntity.getOriginalUrl()));
        returnedUrl.ifPresent(this::compareObjectFields);
    }

    @Test
    void shouldNotFindUrlByOriginalUrl() {
        Assertions.assertThrows(UrlException.class,
                () -> urlService.findUrlByOriginalUrl("test1234")
        );
    }

    private void compareObjectFields(UrlEntity url) {
        Assertions.assertEquals(1L, url.getId());
        Assertions.assertEquals("MQ==", url.getShortnedUrl());
        Assertions.assertEquals("aHR0cHM6Ly9zdGFja292ZXJmbG93LmNvbQ==", url.getOriginalUrl());
    }

}
