package br.com.am.rest;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/teste")
public class TesteApp {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response teste() {
        return Response.status(200).entity("Teste").build();
    }
}
