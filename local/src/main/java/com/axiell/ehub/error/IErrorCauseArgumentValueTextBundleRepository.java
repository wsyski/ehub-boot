package com.axiell.ehub.error;

import com.axiell.ehub.ErrorCauseArgumentValueTextBundle;
import com.axiell.ehub.language.Language;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IErrorCauseArgumentValueTextBundleRepository extends CrudRepository<ErrorCauseArgumentValueTextBundle, Long> {

    List<ErrorCauseArgumentValueTextBundle> findByLanguage(Language language);
}
