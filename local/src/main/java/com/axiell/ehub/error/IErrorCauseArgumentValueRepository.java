package com.axiell.ehub.error;

import com.axiell.ehub.ErrorCauseArgumentType;
import com.axiell.ehub.ErrorCauseArgumentValue;
import org.springframework.data.repository.CrudRepository;

public interface IErrorCauseArgumentValueRepository extends CrudRepository<ErrorCauseArgumentValue, Long> {

    ErrorCauseArgumentValue findOneByType(ErrorCauseArgumentType type);
}
