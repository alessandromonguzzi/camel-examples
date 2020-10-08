package org.monguzzi.examples.route;

import javax.inject.Named;

import org.apache.camel.LoggingLevel;
import org.apache.camel.PropertyInject;
import org.apache.camel.builder.RouteBuilder;

/**
 * Intentionally thrown a {@link RuntimeException} to rollback the transaction.
 * @author amonguzz
 *
 */
@Named
public class RollbackExample extends RouteBuilder {
	
	@PropertyInject("route.maximumRedeliveries")
	int maximumRedeliveries;
	@PropertyInject("route.redeliveryDelay")
	int redeliveryDelay;
	
	@Override
	public void configure() throws Exception {

		onException(RuntimeException.class, java.io.IOException.class, java.sql.SQLException.class, org.springframework.jdbc.CannotGetJdbcConnectionException.class)
		.handled(false)
		.logRetryStackTrace(true).maximumRedeliveries(maximumRedeliveries).redeliveryDelay(redeliveryDelay)
		.log(LoggingLevel.INFO, "Error RollbackExample");
    	
		from("direct:rollbackExample").transacted().to("sql:DELETE FROM myTable?transacted=true")
			.log(LoggingLevel.INFO, "RollbackExample- ${header.CamelSqlUpdateCount} myTable")
			.throwException(new RuntimeException())
			.to("mock:endpoint");
	}
	
}
