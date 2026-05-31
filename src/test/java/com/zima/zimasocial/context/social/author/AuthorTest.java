package com.zima.zimasocial.context.social.author;

import com.zima.zimasocial.AuthorFixture;
import com.zima.zimasocial.context.account.exception.CircularFollowException;
import com.zima.zimasocial.context.social2.domain.entity.Author;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.InvocationTargetException;

@ExtendWith(MockitoExtension.class)
public class AuthorTest {
    @Test
    void requestToFollow_WhenUserSendFollowRequestToSelf_ThrowCircularFollowException() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Author followerAuthor = AuthorFixture.validAuthor();
        Assertions.assertThrows(CircularFollowException.class, ()-> followerAuthor.requestToFollow(followerAuthor.getId()));
    }
}
