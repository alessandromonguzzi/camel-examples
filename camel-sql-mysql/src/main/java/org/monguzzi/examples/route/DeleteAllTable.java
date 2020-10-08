package org.monguzzi.examples.route;

import javax.inject.Named;

import org.apache.camel.LoggingLevel;
import org.apache.camel.PropertyInject;
import org.apache.camel.builder.RouteBuilder;

@Named
public class DeleteAllTable extends RouteBuilder {
	
	@PropertyInject("route.maximumRedeliveries")
	int maximumRedeliveries;
	@PropertyInject("route.redeliveryDelay")
	int redeliveryDelay;
	
	@Override
	public void configure() throws Exception {
    
		onException(java.io.IOException.class, java.sql.SQLException.class, org.springframework.jdbc.CannotGetJdbcConnectionException.class)
		.handled(false)
		.logRetryStackTrace(true).maximumRedeliveries(maximumRedeliveries).redeliveryDelay(redeliveryDelay)
		.log(LoggingLevel.INFO, "Error deleteAllTable");
    	
		from("direct:deleteAllTable").transacted().to("sql:DELETE FROM myTable")
			.log(LoggingLevel.INFO, "DeleteAllTable- ${header.CamelSqlUpdateCount} myTable").id("log-deleteAllTable");
	}
	
}
