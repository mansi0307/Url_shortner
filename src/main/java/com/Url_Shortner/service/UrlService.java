package com.Url_Shortner.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Url_Shortner.common.ShorteningUtil;
import com.Url_Shortner.dto.FullUrl;
import com.Url_Shortner.dto.ShortUrl;
import com.Url_Shortner.model.UrlEntity;
import com.Url_Shortner.repository.UrlRepository;
@Service
public class UrlService {

    Logger logger = LoggerFactory.getLogger(UrlService.class);

    private final UrlRepository urlRepository;

    @Autowired
    public UrlService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    private UrlEntity get(Long id) {
        logger.info(String.format("Fetching Url from database for Id %d", id));
        UrlEntity urlEntity = urlRepository.findById(id).get();
        return urlEntity;
    }

    /**
     * Uses the Base62 encoded to convert to Base10 number and fetches the corresponding record from the database
     * @param shortenString Base62 encoded string
     * @return FullUrl object
     */
    public FullUrl getFullUrl(String shortenString) {
        
        Long id = ShorteningUtil.strToId(shortenString);
        logger.info(String.format("Retrieving full url for %d", id));
        return new FullUrl(this.get(id).getFullUrl());
    }

    private UrlEntity save(String fullUrl) {
        return urlRepository.save(new UrlEntity(fullUrl));
    }

    /**
     * It saves the full url to database and uses the autogenerated id to convert to Base62 string
     * @param fullUrl FullUrl object to be converted to shortened url
     * @return ShortUrl object
     */
    public ShortUrl getShortUrl(String fullUrl) {

        logger.info("Checking if the url already exists");
        List<UrlEntity> savedUrls = null;
        savedUrls = checkFullUrlAlreadyExists(fullUrl);

        UrlEntity savedUrl = null;

        if (savedUrls.isEmpty()) {
            logger.info(String.format("Saving Url %s to database", fullUrl));
            savedUrl = this.save(fullUrl);
            System.out.println(savedUrl.toString());
        }
        else {
            savedUrl = savedUrls.get(0);
        }
        String shortUrlText = ShorteningUtil.idToStr(savedUrl.getId());
        savedUrl.setShortUrl(shortUrlText);
        System.out.println(shortUrlText);
        System.out.println(savedUrl.toString());
        
        return new ShortUrl(shortUrlText);
    }

    /**
     * Check if Url already exists in the database to prevent having same url stored multiple times
     *
     * @param fullUrl
     * @return list of UrlEntity objects, list will be empty if no results found
     */
    private List<UrlEntity> checkFullUrlAlreadyExists(String fullUrl) {
        return urlRepository.findUrlByFullUrl(fullUrl);
    }
}