package com.axiell.ehub.local.error;

import com.axiell.ehub.common.ErrorCauseArgumentValueTextBundle;
import com.axiell.ehub.common.language.Language;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IErrorCauseArgumentValueTextBundleRepository extends CrudRepository<ErrorCauseArgumentValueTextBundle, Long> {

    List<ErrorCauseArgumentValueTextBundle> findByLanguage(Language language);
}
