package br.com.am.rest;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Provider
@Produces(MediaType.APPLICATION_JSON)
public class ObjectMapperResolver implements ContextResolver<ObjectMapper> {
	private final ObjectMapper mapper;

	public ObjectMapperResolver() {
		System.out.println("ObjectMapperResolver");
		mapper = new ObjectMapper();
	}

	@Override
	public ObjectMapper getContext(Class<?> type) {
		return mapper;
	}
}
