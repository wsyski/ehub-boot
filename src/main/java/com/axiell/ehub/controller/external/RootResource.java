package com.axiell.ehub.controller.external;


import com.axiell.ehub.controller.external.v5_0.IV5_0_Resource;
import com.axiell.ehub.controller.external.v5_0.V5_0_Resource;

public class RootResource implements IRootResource {

    @Override
    public IV5_0_Resource getIV5_0_Resource() {
        return new V5_0_Resource();
    }
}
