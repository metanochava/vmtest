package mz.co.diasmetano.vm.resource;

import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import mz.co.diasmetano.vm.db.Store;
import mz.co.diasmetano.vm.service.NumberService;


@Path("")
public class NumberResource {

	NumberService numberService = new NumberService();

	@POST
	@Path("/random")
	public Response random(@HeaderParam("X-Max-Wait") String x_max_wait) {
		long request_enter = new Date().getTime();
		
		return Response.ok(numberService.random(x_max_wait)).header("X-Request-Duration", ((new Date().getTime() - request_enter) /1000)).build();

	}

	@GET
	@Path("/history")
	public Response history() {
		long request_enter = new Date().getTime();
		return Response.ok(Store.getNumbers()).header("X-Request-Duration", ((new Date().getTime() - request_enter) /1000)).build();
	}

	@PUT
	@Path("/{requestid}/cancel")
	public Response cancel(@PathParam("requestid") String requestid) {
		long request_enter = new Date().getTime();
		return Response.ok(numberService.cancel(requestid)).header("X-Request-Duration", ((new Date().getTime() - request_enter) /1000)).build();

	}

	@GET
	@Path("/stats")
	public Response stats() {
		long request_enter = new Date().getTime();
		return Response.ok(numberService.stats()).header("X-Request-Duration", ((new Date().getTime() - request_enter) /1000)).build();
	}

	@GET
	@Path("/pending")
	public Response pending() {
		long request_enter = new Date().getTime();
		return Response.ok(numberService.pending()).header("X-Request-Duration", ((new Date().getTime() - request_enter) /1000)).build();
	}

	@PUT
	@Path("/threads")
	@Produces("application/json")
	@Consumes("application/json")
	public Response threads(String value) {
		long request_enter = new Date().getTime();
		return Response.ok(numberService.threads(value)).header("X-Request-Duration", ((new Date().getTime() - request_enter) /1000)).build();

	}

	

}
