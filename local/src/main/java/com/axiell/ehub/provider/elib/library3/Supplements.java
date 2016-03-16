package com.axiell.ehub.provider.elib.library3;

import com.axiell.ehub.checkout.SupplementLink;
import com.axiell.ehub.checkout.SupplementLinks;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Supplements extends ArrayList<SupplementDTO> {
    List<SupplementLink> getSupplementLinks() {
        return new SupplementLinks(
                stream().map(supplementDTO -> new SupplementLink(supplementDTO.getName(), supplementDTO.getHref())).collect(Collectors.toList()))
                .getSupplementLinks();
    }
}
