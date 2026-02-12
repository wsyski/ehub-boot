package com.axiell.ehub.local.error;

import com.axiell.ehub.common.ErrorCauseArgumentType;
import com.axiell.ehub.common.ErrorCauseArgumentValue;
import org.springframework.data.repository.CrudRepository;

public interface IErrorCauseArgumentValueRepository extends CrudRepository<ErrorCauseArgumentValue, Long> {

    ErrorCauseArgumentValue findOneByType(ErrorCauseArgumentType type);
}
