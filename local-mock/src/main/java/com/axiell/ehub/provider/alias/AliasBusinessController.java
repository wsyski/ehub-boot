package com.axiell.ehub.provider.alias;

import com.axiell.ehub.AbstractBusinessController;
import com.axiell.ehub.error.IEhubExceptionFactory;
import com.axiell.ehub.util.EhubMessageUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
class AliasBusinessController extends AbstractBusinessController implements IAliasBusinessController {
    @Autowired
    private IEhubExceptionFactory ehubExceptionFactory;

    @Autowired
    private EhubMessageUtility ehubMessageUtility;

    @Override
    public Set<AliasMapping> getAliasMappings() {
        return ehubMessageUtility.getEhubMessage(AliasMappingsDTO.class, "alias-mappings").getAliasMappings();
    }

    public String getName(final String alias) {
        return "ELIB3";
    }

}
