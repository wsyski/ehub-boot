package com.axiell.ehub.lms.arena.controller.portalsites;


import com.axiell.ehub.lms.arena.controller.portalsites.dto.DecorationAdvice;
import com.axiell.ehub.lms.arena.controller.portalsites.dto.OpenEntityFieldCollection;
import com.axiell.ehub.lms.arena.controller.portalsites.dto.RecordsDTO;
import com.axiell.ehub.lms.arena.controller.portalsites.dto.SortAdvice;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.Set;

@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public interface IPortalSitesResource {
    @GET
    @Path("/agencymembers/{agencyMemberId}/records")
    @Produces({MediaType.APPLICATION_JSON})
    RecordsDTO search(
            @PathParam("agencyMemberId") long arenaMemberId,
            @DefaultValue("*:*") @QueryParam("query") String query,
            @DefaultValue("0") @QueryParam("start") int start,
            @DefaultValue("10") @QueryParam("count") int count,
            @DefaultValue("Basic") @QueryParam("fieldCollections") Set<OpenEntityFieldCollection> fieldCollections,
            @DefaultValue("Descending") @QueryParam("sortDirection") SortAdvice.Direction direction,
            @DefaultValue("Relevance") @QueryParam("sortField") String sortField,
            @QueryParam("patronId") String patronId,
            @QueryParam("decorationNames") Set<DecorationAdvice.Type> decorationAdviceTypes);
}
