package com.hugo.urlshortner.service;

import com.hugo.urlshortner.service.exception.UrlException;
import com.hugo.urlshortner.model.dto.UrlDTO;
import com.hugo.urlshortner.model.entities.UrlEntity;
import com.hugo.urlshortner.repository.URLRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class UrlService {

    private static final String SLASH = "/";

    private final URLRepository urlRepository;

    @Autowired
    public UrlService(URLRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    /**
     * @param pageable
     * @return
     */
    public Page<UrlEntity> getAllClients(Pageable pageable) {
        return urlRepository.findAll(pageable);
    }

    /**
     * @param id
     * @throws UrlException
     */
    @Transactional
    public void deleteUrl(Long id) throws UrlException {
        Optional<UrlEntity> url = urlRepository.findById(id);
        if (!url.isPresent()) {
            throw new UrlException(String.format("Url id %s not found!", id));
        }
        deleteUrl(url.get());
    }

    /**
     * @param urlEntity
     */
    public void deleteUrl(UrlEntity urlEntity) {
        urlRepository.delete(urlEntity);
    }

    @Transactional
    public void deleteAllUrls() {
        urlRepository.deleteAll();
    }

    /**
     * @param originalUrl
     * @return
     * @throws UrlException
     */
    @Transactional
    public UrlEntity findUrlByOriginalUrl(String originalUrl) throws UrlException {
        return urlRepository.findByOriginalUrl(originalUrl).orElseThrow(() -> new UrlException("original URL not found!"));
    }

    /**
     * @param shortnedUrl
     * @return
     * @throws UrlException
     */
    @Transactional
    public UrlEntity findUrlsByShortnedUrl(String shortnedUrl) throws UrlException {
        return urlRepository.findByShortnedUrl(shortnedUrl).orElseThrow(() -> new UrlException("shortned URL not found!"));
    }

    /**
     * @param encodedOriginalUrl
     * @return
     */
    @Transactional
    @Cacheable(value = "encodedOriginalUrl", key = "#encodedOriginalUrl")
    public UrlDTO createUrl(String localUrl, String encodedOriginalUrl) {
        Optional<UrlEntity> url = urlRepository.findByOriginalUrl(encodedOriginalUrl);
        if (url.isPresent()) {
            return url.get().toDTO(localUrl + SLASH);
        }

        UrlEntity savedUrlEntity = urlRepository.save(UrlEntity.builder().originalUrl(encodedOriginalUrl).build());
        return defineShortnedUrl(savedUrlEntity, localUrl);
    }

    private UrlDTO defineShortnedUrl(UrlEntity urlEntity, String localUrl) {
        String idAsString = urlEntity.getId().toString();

        String encodedString = Base64UrlCommonService.encodeString(idAsString);

        urlEntity.setShortnedUrl(encodedString);
        return urlRepository.save(urlEntity).toDTO(localUrl + SLASH);
    }

    @Cacheable(value = "shortUrl", key = "#shortUrl")
    public UrlDTO getOriginalUrlFromShortUrl(String shortUrl) throws UrlException {
        UrlEntity urlEntity = urlRepository.findByShortnedUrl(shortUrl).orElseThrow(() -> new UrlException("shortned URL not found!"));
        return urlEntity.toDTO();
    }
}
