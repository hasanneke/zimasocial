package com.zimaberlin.zimasocial.controller;
import com.zimaberlin.zimasocial.service.notification.NotificationService;
import com.zimaberlin.zimasocial.utility.CurrentUser;
import com.zimaberlin.zimasocial.views.notification.NotificationView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkRelation;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController()
@RequestMapping(path = "/api/v1/notifications")
public class NotificationController {
    private NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public HttpEntity<PagedModel<NotificationView>> getNotifications(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                                         @RequestParam(name = "size", defaultValue = "20") Integer size) throws NoSuchMethodException {
        Long userId = CurrentUser.getCurrentUserProfile().getId();
        Pageable pageable = PageRequest.of(page, size);
        Page<NotificationView> notificationsPage = notificationService.getNotifications(userId, pageable);

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
