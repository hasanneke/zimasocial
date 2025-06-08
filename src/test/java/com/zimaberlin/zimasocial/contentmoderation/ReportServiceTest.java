package com.zimaberlin.zimasocial.contentmoderation;

import com.zimaberlin.zimasocial.context.contentmoderation.report.Report;
import com.zimaberlin.zimasocial.context.contentmoderation.report.ReportAlreadyMadeException;
import com.zimaberlin.zimasocial.context.contentmoderation.report.ReportRepository;
import com.zimaberlin.zimasocial.context.contentmoderation.report.ReportService;
import com.zimaberlin.zimasocial.context.social.author.Author;
import com.zimaberlin.zimasocial.context.social.author.AuthorRepository;
import com.zimaberlin.zimasocial.context.social.comment.CommentRepository;
import com.zimaberlin.zimasocial.context.social.post.Post;
import com.zimaberlin.zimasocial.context.social.post.PostRepository;
import com.zimaberlin.zimasocial.entity.PostType;
import com.zimaberlin.zimasocial.entity.report.ReportReason;
import com.zimaberlin.zimasocial.entity.report.ResourceType;
import com.zimaberlin.zimasocial.service.report.dto.ReportRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReportServiceTest {
    @Mock
    private AuthorRepository authorRepository;
    @Mock
    private PostRepository postRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private ReportRepository reportRepository;
    @InjectMocks
    private ReportService reportService;

    private Author testAuthor = new Author(0L, "", "", LocalDateTime.now());
    private Report testReport = new Report(0L, 0L, ResourceType.post, ReportReason.spam);
    private ReportRequest testRequest = new ReportRequest(0L, ReportReason.spam, ResourceType.post, "");
    private Post dummyPost = new Post(0L,"", PostType.any, 0, 0,  LocalDateTime.now(), LocalDateTime.now(), 0L);
    @Test
    void testThrowReportAlreadyMadeException_WhenReportExists() {
        when(authorRepository.getAuthenticatedAuthor()).thenReturn(testAuthor);
        when(reportRepository.checkReportExists(0L, 0L, ResourceType.post)).thenReturn(true);

        Assertions.assertThrows(ReportAlreadyMadeException.class, ()-> reportService.report(testRequest));
    }

    @Test
    void testWhenSuccess() {
        ReportRequest request = new ReportRequest(0L, ReportReason.spam, ResourceType.post, "");

        when(authorRepository.getAuthenticatedAuthor()).thenReturn(testAuthor);
        when(reportRepository.checkReportExists(any(), any(), any())).thenReturn(false);
        when(postRepository.findById(any())).thenReturn(Optional.of(dummyPost));
        reportService.report(request);
        verify(reportRepository, times(1)).save(new Report(0L, 0L, ResourceType.post, ReportReason.spam));
    }
}
