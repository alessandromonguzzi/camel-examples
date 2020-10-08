package org.monguzzi.examples.route;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.camel.CamelContext;
import org.apache.camel.LoggingLevel;
import org.apache.camel.PropertyInject;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.cdi.ContextName;
import org.apache.camel.model.rest.RestBindingMode;

@ApplicationScoped
@ContextName("camel-sql-mysql")
public class RestConfiguration extends RouteBuilder {
	
	@PropertyInject("rest.svc.host")
	String host;
	
	@PropertyInject("rest.svc.port")
	int port;
	
	@Inject
	private CamelContext camelctx;
	
    @Override
    public void configure() {
    	
        // Enable Stream caching
        camelctx.setStreamCaching(true);
        // Enable MDC logging
        camelctx.setUseMDCLogging(true);
        // Enable use of breadcrumbId
		camelctx.setUseBreadcrumb(true);  
		
        /*
            Configure we want to use servlet as the component for the rest DSL
            and we enable json binding mode
         */
    	restConfiguration()
    		.bindingMode(RestBindingMode.off).component("servlet")
    		.endpointProperty("servletName", "CamelServlet-camel-sql-mysql")
    		.contextPath("/camel-sql-mysql/camel").host(host).port(port)
    		.dataFormatProperty("prettyPrint", "true")
    		.apiProperty("api.version", "1.0.0")
    		.apiProperty("api.title", "Launch service for camel-sql-mysql flow");
    	
    	rest("/restsvc")
    		.id("restsvc").produces("application/json")
    		.get("/insert")
    		.route()
			.to("direct:insert")
			.log(LoggingLevel.INFO, "camel-sql-mysql insert (By rest service)");
    	
    	rest("/restsvc")
		.id("restsvc").produces("application/json")
		.get("/deleteAllTable")
		.route()
		.to("direct:deleteAllTable")
		.log(LoggingLevel.INFO, "camel-sql-mysql deleteAllTable (By rest service)");
    	
    	rest("/restsvc")
		.id("restsvc").produces("application/json")
		.get("/rollbackExample")
		.route()
		.to("direct:rollbackExample")
		.log(LoggingLevel.INFO, "camel-sql-mysql rollbackExample (By rest service)");
    }
}
