package br.com.am.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/teste")
public class TesteApp {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response teste() {
        return Response.status(200).entity("Teste").build();
    }
}
