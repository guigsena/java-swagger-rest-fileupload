package br.com.backend.service;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import br.com.backend.rest.ArquivoResource;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;

@ApplicationPath("/v1")
public class ArquivoService extends Application {
	/* ------------------------------
	 * CONSTRUTOR
	 * ------------------------------
	 */	
	public ArquivoService() {
		BeanConfig conf = new BeanConfig();
	    conf.setTitle("API atividade");
	    conf.setDescription("Realiza upload e download de arquivos ale�m de listar todos inseridos");
	    conf.setVersion("1.0.0");
	    conf.setHost("localhost:8080");
	    conf.setBasePath("/backend/rest/arquivo/v1");
	    conf.setSchemes(new String[] { "http" });
	    conf.setResourcePackage("br.com.backend.rest");
	    conf.setScan(true);
	}
	
	/* ------------------------------
	 * MÉTODOS
	 * ------------------------------
	 */	
	 @Override
	  public Set<Class<?>> getClasses() {
	      Set<Class<?>> resources = new HashSet<>();
	      resources.add(ArquivoResource.class);
	       
	      resources.add(ApiListingResource.class);
	      resources.add(SwaggerSerializers.class);
	      return resources;
	   }
}
