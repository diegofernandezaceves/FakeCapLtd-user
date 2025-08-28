package com.fakecap.controller;

import com.fakecap.dto.*;
import com.fakecap.service.TradeService;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/trade")
public class TradeController {

    private final TradeService tradeService;

    public TradeController(TradeService tradeService) {
        this.tradeService = tradeService;
    }

    @POST
    @Path("{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> trade(@PathParam("userId") String userId, ShareRequestDto shareRequestDto) {
        return this.tradeService.buyOrSellShare(userId, shareRequestDto)
                .map(Response::ok)
                .map(Response.ResponseBuilder::build);
    }

}
