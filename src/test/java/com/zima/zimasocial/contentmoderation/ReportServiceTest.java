package com.zima.zimasocial.contentmoderation;

import com.zima.zimasocial.AuthorFixture;
import com.zima.zimasocial.context.contentmoderation.report.ContentRepository;
import com.zima.zimasocial.context.contentmoderation.report.ReportRepository;
import com.zima.zimasocial.context.contentmoderation.report.ReportService;
import com.zima.zimasocial.context.contentmoderation.report.content.CommentContent;
import com.zima.zimasocial.context.contentmoderation.report.content.PostContent;
import com.zima.zimasocial.context.contentmoderation.report.exception.ReportAlreadyMadeException;
import com.zima.zimasocial.context.contentmoderation.report.reports.CommentReport;
import com.zima.zimasocial.context.contentmoderation.report.reports.PostReport;
import com.zima.zimasocial.context.social.author.entity.Author;
import com.zima.zimasocial.context.social.author.value.AuthorId;
import com.zima.zimasocial.context.social.author.repository.AuthorRepository;
import com.zima.zimasocial.context.contentmoderation.ReportReason;
import com.zima.zimasocial.context.contentmoderation.ResourceType;
import com.zima.zimasocial.context.contentmoderation.api.payload.ReportRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReportServiceTest {
    @Mock
    private AuthorRepository authorRepository;
    @Mock
    private ContentRepository contentRepository;
    @Mock
    private ReportRepository reportRepository;
    @InjectMocks
    private ReportService reportService;

    private Author testAuthor = AuthorFixture.validAuthor();
    private ReportRequest testRequest = new ReportRequest(0L, ReportReason.SPAM, "");
    private PostContent dummyPostContent = new PostContent(0L,new AuthorId(0L));
    private CommentContent dummyCommentContent = new CommentContent(0L,0L, 0L, new AuthorId(0L));

    public ReportServiceTest() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
    }

    @Test
    void testReportPost_ThrowReportAlreadyMadeException_WhenReportExists() {
        when(authorRepository.getAuthenticatedAuthor()).thenReturn(testAuthor);
        when(reportRepository.checkReportExists(0L, testAuthor.getId(), ResourceType.post)).thenReturn(true);

        Assertions.assertThrows(ReportAlreadyMadeException.class,
                ()-> reportService.reportPost(testRequest.getResourceId(), testRequest.getReason(), testRequest.getDescription()));
    }

    @Test
    void testReportPost_WhenSuccess() {
        ReportRequest request = new ReportRequest(0L, ReportReason.SPAM, "");

        when(authorRepository.getAuthenticatedAuthor()).thenReturn(testAuthor);
        when(reportRepository.checkReportExists(any(), any(), any())).thenReturn(false);
        when(contentRepository.getPost(any())).thenReturn(Optional.of(dummyPostContent));
        reportService.reportPost(request.getResourceId(), request.getReason(), request.getDescription());
        verify(reportRepository, times(1)).save(new PostReport(request.getResourceId(), request.getReason(), testAuthor.getId(), dummyPostContent.authorId(), request.getDescription()));
    }

    @Test
    void testReportComment_ThrowReportAlreadyMadeException_WhenReportExists() {
        when(authorRepository.getAuthenticatedAuthor()).thenReturn(testAuthor);
        when(reportRepository.checkReportExists(0L,  testAuthor.getId(), ResourceType.comment)).thenReturn(true);

        Assertions.assertThrows(ReportAlreadyMadeException.class,
                ()-> reportService.reportComment(testRequest.getResourceId(), testRequest.getReason(), testRequest.getDescription()));
    }

    @Test
    void testReportComment_WhenSuccess() {
        ReportRequest request = new ReportRequest(0L, ReportReason.SPAM, "");

        when(authorRepository.getAuthenticatedAuthor()).thenReturn(testAuthor);
        when(reportRepository.checkReportExists(any(), any(), any())).thenReturn(false);
        when(contentRepository.getComment(any())).thenReturn(Optional.of(dummyCommentContent));
        reportService.reportComment(request.getResourceId(), request.getReason(), request.getDescription());
        verify(reportRepository, times(1)).save(new CommentReport(request.getResourceId(), request.getReason(), testAuthor.getId(), dummyPostContent.authorId(), request.getDescription()));
    }
}
