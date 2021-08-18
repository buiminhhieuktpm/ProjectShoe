package com.project.domain;

import org.springframework.data.domain.Page;

public interface GenericPagination<T> {<T> PaginationResult<T> pagination(Page<T> page, Integer pageNo, Integer limit, String sortField, String sortDir);
}
