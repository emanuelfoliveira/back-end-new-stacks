package br.com.efo.bens.common;

import java.net.URI;
import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.google.gson.JsonObject;

import br.com.efo.bens.ds.ListResponse;

/**
 * 
 * @author emanuelfoliveira
 *
 */
public class RestSupport {
	@Context
	private UriInfo uriInfo;

	public Response responseGetAllSuccess(List<?> listTarget) {
		ListResponse response = new ListResponse(listTarget);
		List<ListResponse> responseList = new LinkedList<>();
		responseList.add(response);
		return Response.ok(responseList).build();
	}

	public Response responseDeleteAllSuccess() {
		return Response.status(204).build();
	}

	public Response responseDenyAccess() {
		return Response.status(403).build();
	}

	public Response responseDeleteByIdSuccess() {
		return Response.noContent().build();
	}

	public Response responseIdNotFound(String entityName) {
		return Response.status(404).entity(generateNotFoundMessage(entityName)).build();
	}

	public Response responseBodyInvalid() {
		return Response.status(400).build();
	}

	public Response responsePostSuccess(Integer id) {
		URI location = uriInfo.getAbsolutePathBuilder().path("{id}").resolveTemplate("id", id).build();
		return Response.created(location).build();
	}

	public Response responseGetByIdSuccess(Object listTarget) {
		List<Object> firstList = new LinkedList<>();
		firstList.add(listTarget);
		ListResponse response = new ListResponse(firstList);
		List<ListResponse> responseList = new LinkedList<>();
		responseList.add(response);
		return Response.ok(responseList).build();
	}

	public Response responsePatchSuccess() {
		return Response.noContent().build();
	}

	private String generateNotFoundMessage(String entityName) {
		JsonObject json = new JsonObject();
		json.addProperty("error", entityName + " not found");
		return json.toString();
	}
}
