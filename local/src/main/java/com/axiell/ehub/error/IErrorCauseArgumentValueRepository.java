package com.axiell.ehub.error;

import com.axiell.ehub.ErrorCauseArgumentValue;
import org.springframework.data.repository.CrudRepository;

public interface IErrorCauseArgumentValueRepository extends CrudRepository<ErrorCauseArgumentValue, Long> {

    ErrorCauseArgumentValue findByType(ErrorCauseArgumentValue.Type type);
}
