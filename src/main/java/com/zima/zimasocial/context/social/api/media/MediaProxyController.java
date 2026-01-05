package com.zima.zimasocial.context.social.api.media;

import com.zima.zimasocial.context.social.media.MediaProxyService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/proxy")
@RequiredArgsConstructor
public class MediaProxyController {
    private final MediaProxyService proxyService;

    @RequestMapping
    public ResponseEntity<byte[]> proxy(
            HttpMethod method,
            HttpServletRequest httpServletRequest,
            @RequestParam(name = "url") String url,
            @RequestParam MultiValueMap<String, String> params,
            @RequestBody(required = false) byte[] body) {
        return proxyService.proxy(method, httpServletRequest, url, params, body);
    }
}
