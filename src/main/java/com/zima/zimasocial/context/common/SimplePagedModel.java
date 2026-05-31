package com.zima.zimasocial.context.common;

import java.util.Collection;

public class SimplePagedModel<T> {
    private final Collection<T> content;
    private final long totalElement;
    private final long totalPage;

    protected SimplePagedModel(Collection<T> content, long totalElement, long totalPage) {
        this.content = content;
        this.totalElement = totalElement;
        this.totalPage = totalPage;
    }
    public Collection<T> getContent() {
        return content;
    }

    public static <T> SimplePagedModel<T> of(Collection<T> content, long totalElement, long totalPage) {
        return new SimplePagedModel<>(content, totalElement, totalPage);
    }
}
