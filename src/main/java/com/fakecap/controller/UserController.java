package com.fakecap.controller;

import com.fakecap.dto.BalanceDto;
import com.fakecap.dto.UserDto;
import com.fakecap.service.UserService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @POST
    @Path("random")
    @Produces(MediaType.APPLICATION_JSON)
    public UserDto createRandomUser() {
        return this.userService.createUser();
    }

    @GET
    @Path("{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public UserDto getClient(@PathParam("userId") String userId) {
        return this.userService.getUser(userId);
    }

    @POST
    @Path("{userId}/balance")
    @Produces(MediaType.APPLICATION_JSON)
    public UserDto addBalance(@PathParam("userId") String userId, BalanceDto balanceDto) {
        return this.userService.addBalance(userId, balanceDto.amount());
    }
}
