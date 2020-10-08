package org.monguzzi.examples.route;

import javax.inject.Named;

import org.apache.camel.LoggingLevel;
import org.apache.camel.PropertyInject;
import org.apache.camel.builder.RouteBuilder;

@Named
public class Insert extends RouteBuilder {
	
	@PropertyInject("route.maximumRedeliveries")
	int maximumRedeliveries;
	@PropertyInject("route.redeliveryDelay")
	int redeliveryDelay;
	
	@Override
	public void configure() throws Exception {
    		
		onException(java.io.IOException.class, java.sql.SQLException.class, org.apache.commons.dbcp.SQLNestedException.class, org.springframework.jdbc.CannotGetJdbcConnectionException.class)
		.handled(false)
		.logRetryStackTrace(true).maximumRedeliveries(maximumRedeliveries).redeliveryDelay(redeliveryDelay)
		.log(LoggingLevel.INFO, "Error insert");
    	
		from("direct:insert").transacted().to("sql:INSERT INTO myTable VALUES ('first')?transacted=true")
			.log(LoggingLevel.INFO, "Insert- ${header.CamelSqlUpdateCount} myTable");
	}
	
}
