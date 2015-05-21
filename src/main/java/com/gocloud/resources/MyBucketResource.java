package com.gocloud.resources;

import com.gocloud.model.MyBucket;
import com.gocloud.model.MyBucketException;
import com.google.gson.Gson;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Created by marcofunaro on 18/05/15.
 */
@Path("/bucket")
public class MyBucketResource {

    private static final Gson gson = new Gson();

    @Context
    UriInfo uriInfo;

    @GET
    @Produces("application/json")
    public Response getAll() {
        Set<String> maps = new HashSet<>();
        MyBucket.INSTANCE.getAll().forEach( s -> maps.add(uriInfo.getAbsolutePath().toString() + s));
        return Response.ok(gson.toJson(maps)).build();
    }

    @POST
    @Consumes("application/json")
    @Produces("text/plain")
    public Response create(String entity) {
        try {
            URI uri = new URI(uriInfo.getAbsolutePath().toString() + "/" + MyBucket.INSTANCE.createEl(entity));
            return Response.created(uri).build();
        } catch (URISyntaxException e) {
            return Response.serverError().build();
        }
    }

    @PUT
    @Consumes("application/json")
    @Path("/{entryId}")
    public Response updateEntry(@PathParam("entryId")  String entryId, String entity) {
        try {
            MyBucket.INSTANCE.updateEl(entryId, entity);
            return Response.noContent().build();
        } catch (MyBucketException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{entryId}")
    public Response deleteEntry(@PathParam("entryId")  String entryId){
        MyBucket.INSTANCE.removeEl(entryId);
        return Response.noContent().build();
    }

    @GET
    @Path("/{entryId}")
    @Produces("application/json")
    public Response getEntry(@PathParam("entryId")  String entryId){
        Optional<String> result = MyBucket.INSTANCE.getById(entryId);
        if(result.isPresent()){
            return Response.ok(result.get()).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

}
