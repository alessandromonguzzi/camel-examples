package org.monguzzi.examples.bean;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.sql.DataSource;

import org.apache.camel.CamelContext;
import org.apache.camel.PropertyInject;
import org.apache.camel.component.sql.SqlComponent;
import org.apache.camel.spi.TransactedPolicy;
import org.apache.camel.spring.spi.SpringTransactionPolicy;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

public class DataSourceConfiguration {

	@PropertyInject("ds.driverClassName")
	String driverClassName;
	@PropertyInject("ds.username")
	String username;
	@PropertyInject("ds.password")
	String password;
	@PropertyInject("ds.url")
	String url;
		
	@Inject
	private CamelContext context;
	
	@Produces
	@Singleton
	@Named("myDataSource")
	public BasicDataSource getDS() {
		
		BasicDataSource bds = new BasicDataSource();
		
		bds.setDriverClassName(driverClassName);
		bds.setUsername(username);
		bds.setPassword(password);
		bds.setUrl(url);
		/*
		 * This settings disables the auto commit and makes the transaction behavior effective
		 */
		bds.setDefaultAutoCommit(false);

		return bds;
	}
	
	@Produces
	@Named("springTransactionPolicy") 
	public TransactedPolicy transactedPolicy(PlatformTransactionManager ptm) {
		return new SpringTransactionPolicy(ptm);
	}
	
	@Produces
	@Named("transactionManager")
	public PlatformTransactionManager transactionManager (DataSource ds) {
		return new DataSourceTransactionManager(ds);
    }
	
    @Produces
    @Named("sql")
    public SqlComponent createSqlComponent(DataSource ds){
        SqlComponent sql= new SqlComponent(context);
        sql.setDataSource(ds);
        return sql;
    }
	
}
