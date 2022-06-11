package com.hugo.urlshortner.controller;

import com.hugo.urlshortner.model.dto.UrlDTO;
import com.hugo.urlshortner.model.entities.UrlEntity;
import com.hugo.urlshortner.service.Base64UrlCommonService;
import com.hugo.urlshortner.service.UrlService;
import com.hugo.urlshortner.service.exception.UrlException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/v1/api")
public class UrlShortnerController {

    @Autowired
    private UrlService urlService;

    @GetMapping(value = "/url")
    public ResponseEntity<Page<UrlDTO>> getAllUrls(@PageableDefault(size = 15,
            sort = "id", direction = Sort.Direction.ASC) Pageable pageable, HttpServletRequest request) {
        log.info("Fetching urls.");
        Page<UrlEntity> urls = urlService.getAllClients(pageable);

        if (urls.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        String localURl = request.getRequestURL().toString();
        Page<UrlDTO> map = urls.map(u -> u.toDTO(localURl));
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PostMapping(value = "/url")
    public ResponseEntity<UrlDTO> createClient(@RequestBody @Valid UrlDTO urlDTO, HttpServletRequest request) {
        log.info("Creating shortned url.");

        String encodedOriginalUrl = Base64UrlCommonService.encodeString(urlDTO.getOriginalUrl());
        return new ResponseEntity<>(urlService.createUrl(request.getRequestURL().toString(), encodedOriginalUrl), HttpStatus.CREATED);
    }

    @GetMapping(value = "/url/{shortUrl}")
    public RedirectView redirectUrl(@PathVariable String shortUrl) throws UrlException {
        log.info("Shortned url to be accessed: " + shortUrl);
        UrlDTO urlDto = urlService.getOriginalUrlFromShortUrl(shortUrl);
        log.info("Original URL: " + urlDto.getOriginalUrl());
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(urlDto.getOriginalUrl());
        return redirectView;
    }

    @DeleteMapping(value = "/url/{id}")
    public ResponseEntity<Object> deleteUrlById(@PathVariable("id") long id) {
        log.info("Deleting url.");
        try {
            urlService.deleteUrl(id);
        } catch (UrlException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/url")
    public ResponseEntity<Object> deleteAllUrls() {
        log.info("Deleting every single url.");
        try {
            urlService.deleteAllUrls();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_MODIFIED);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
