package com.zimaberlin.zimasocial.service.report.dto;

import com.zimaberlin.zimasocial.entity.report.ReportReason;
import com.zimaberlin.zimasocial.entity.report.ReportType;
import com.zimaberlin.zimasocial.repository.PostRepository;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReportRequest {
    @NotNull
    @PositiveOrZero
    private Long resourceId;
    @NotBlank
    private ReportReason reason;
    @NotBlank
    private ReportType reportType;
    private String description;
}
