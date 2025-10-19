package com.zimaberlin.zimasocial.context.communication.controller;

import com.zimaberlin.zimasocial.context.communication.NotificationService;
import com.zimaberlin.zimasocial.context.communication.infastructure.DeviceTokenPayload;
import com.zimaberlin.zimasocial.context.social.author.AuthorRepository;
import com.zimaberlin.zimasocial.views.notification.NotificationView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkRelation;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController()
@RequestMapping(path = "/api/v2/notifications")
public class NotificationControllerBeta {
    private final NotificationQuery notificationReadRepository;
    private final NotificationService notificationService;
    private final AuthorRepository authorRepository;

    @Autowired
    public NotificationControllerBeta(NotificationQuery notificationReadRepository, NotificationService notificationService, AuthorRepository authorRepository) {
        this.notificationReadRepository = notificationReadRepository;
        this.notificationService = notificationService;
        this.authorRepository = authorRepository;
    }

    @PostMapping("/device-tokens")
    public ResponseEntity<Void> sendDeviceToken(@RequestBody DeviceTokenPayload deviceToken) {
        notificationService.saveDeviceToken(deviceToken.getToken());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public HttpEntity<PagedModel<NotificationView>> getNotifications(@RequestParam(name = "page", defaultValue = "0") Integer page, @RequestParam(name = "size", defaultValue = "20") Integer size) throws NoSuchMethodException {
        Long authorId = authorRepository.getAuthenticatedAuthor().getId().getId();
        Pageable pageable = PageRequest.of(page, size);
        Page<NotificationView> notificationsPage = notificationReadRepository.findByRecipientId(authorId, pageable);

        PagedModel<NotificationView> pagedModel = PagedModel.of(
                notificationsPage.getContent(),
                new PagedModel.PageMetadata(notificationsPage.getSize(),
                        notificationsPage.getNumber(),
                        notificationsPage.getTotalElements(),
                        notificationsPage.getTotalPages()));
        Method method = this.getClass().getMethod("getNotifications", Integer.class, Integer.class);

        if(page < notificationsPage.getTotalPages()){
            Link link = linkTo(method, page + 1, size).withRel(LinkRelation.of("next"));
            pagedModel.add(link);
        }

        if(page > 0){
            Link link = linkTo(method, page - 1, size).withRel(LinkRelation.of("previous"));
            pagedModel.add(link);
        }
        return new HttpEntity<>(pagedModel);

    }
}
