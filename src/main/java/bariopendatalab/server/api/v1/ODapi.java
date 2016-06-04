/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bariopendatalab.server.api.v1;

import bariopendatalab.server.DBWrapper;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

/**
 *
 * @author pierpaolo
 */
@Path("v1")
public class ODapi {

    @GET
    @Path("hello/{name}")
    public Response hello(@PathParam("name") String name) {
        return Response.ok("Hello " + name).build();
    }

    @GET
    @Path("findByType/{type}/{offset: [0-9]+}/{size: [0-9]+}")
    public Response findByType(@PathParam("type") String type, @PathParam("offset") int offset, @PathParam("size") int size) {
        try {
            String response = DBWrapper.getInstance().getDba().findByType(type, offset, size);
            return Response.ok(response).build();
        } catch (Exception ex) {
            Logger.getLogger(ODapi.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        }
    }

    @GET
    @Path("municipi")
    public Response getMunicipi() {
        try {
            String response = DBWrapper.getInstance().getDba().getMunicipi();
            return Response.ok(response).build();
        } catch (Exception ex) {
            Logger.getLogger(ODapi.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        }
    }

    @GET
    @Path("findByMunicipio/{id: [0-9]+}")
    public Response findByMunicipio(@PathParam("id") int id) {
        try {
            String response = DBWrapper.getInstance().getDba().poiByMunicipio(id);
            return Response.ok(response).build();
        } catch (Exception ex) {
            Logger.getLogger(ODapi.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        }
    }

    @GET
    @Path("score/{id: [0-9]+}/{servizi}/{ambiente}/{cultura}/{salute}")
    public Response score(@PathParam("id") int id, @PathParam("servizi") double servizi, @PathParam("ambiente") double ambiente, @PathParam("cultura") double cultura, @PathParam("salute") double salute) {
        try {
            double score = DBWrapper.getInstance().getLife().score(new double[]{servizi, ambiente, cultura, salute}, id);
            return Response.ok(score).build();
        } catch (Exception ex) {
            Logger.getLogger(ODapi.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        }
    }

}
