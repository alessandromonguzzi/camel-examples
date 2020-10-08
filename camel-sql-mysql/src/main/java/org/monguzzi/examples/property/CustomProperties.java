package org.monguzzi.examples.property;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.enterprise.inject.Produces;
import javax.inject.Named;

import org.apache.camel.component.properties.PropertiesComponent;

@Named
public class CustomProperties {


	@Produces
	@Named("properties")
	PropertiesComponent propertiesComponent() {
	    Properties properties = new Properties();
	    properties.put("property", "value");
	    PropertiesComponent component = new PropertiesComponent();
	    
	    component.setInitialProperties(properties);
	    
	    List<String> propertiesFiles = new ArrayList<String>();
	    propertiesFiles.add("classpath:dbconfig.properties");    
	    
		component.setLocations(propertiesFiles);	
				
	    return component;
	}
	
}
