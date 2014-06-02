package com.axiell.ehub;

import org.springframework.data.repository.CrudRepository;

/**
 *
 */
public interface IErrorCauseArgumentValueRepository extends CrudRepository<ErrorCauseArgumentValue, Long> {

    ErrorCauseArgumentValue findByType(ErrorCauseArgumentValue.Type type);
}
