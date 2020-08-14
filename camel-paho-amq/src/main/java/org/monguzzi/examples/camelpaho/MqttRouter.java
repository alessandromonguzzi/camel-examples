package org.monguzzi.examples.camelpaho;
 
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;
import org.apache.camel.component.paho.PahoConstants;


import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

@Component
class MqttRouter extends RouteBuilder {

    static final String PAHO_BROKER_CONN = "paho:queue?brokerUrl=tcp://localhost:1883";


    @Override
    public void configure() {
        from("timer://trigger").setBody(simple("Hello World!")).log("sent").to(PAHO_BROKER_CONN);    
    }
    

    

}

@Component
class MqttConsumer extends RouteBuilder {
	
	private static final int PARALLEL_THREADS = 3;
	private static final int DELAY = 1000;

	@Override
	public void configure() throws Exception {
		from(MqttRouter.PAHO_BROKER_CONN).threads(PARALLEL_THREADS).log("received").process(print);
		
	}
	
	private Processor print = new Processor() {
        @Override
        public void process(Exchange exchange) throws Exception {
	    Thread.sleep(PARALLEL_THREADS * 1000 + DELAY);
            Object header = exchange.getIn().getHeader(PahoConstants.MQTT_TOPIC);
            String topic = (String) header;
            Message camelMessage = exchange.getIn();

            byte[] body = (byte[]) camelMessage.getBody();
            String payload = new String(body, "utf-8");

            System.out.println("topic=" + topic + ", payload=" + payload);

        }
    };
	
}
