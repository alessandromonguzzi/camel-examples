# Configure a basic REST DSL servlet and authenticate it with JAAS

## Karaf runtime
If you are using Karaf as runtime, everything can be done with configuration files: see `karaf` directory as example.

### Setup
0. Define `FUSE_BASE` env variable to the home installation of your Fuse
```
FUSE_BASE=<your home installation>
```
1. Start Fuse
```
$FUSE_BASE/bin/fuse
```
2. install camel-blueprint and camel-servlet features
```
karaf@root()> feature:install camel-blueprint camel-servlet
```
3. copy ` org.ops4j.pax.web.context-restdsl.cfg` into `etc` directory
```
cp karaf/org.ops4j.pax.web.context-restdsl.cfg $FUSE_BASE/etc
```
Note the `bundle.symbolicName` that is the same that will appear in the Fuse console after you have deployed your app in the next step.

The `security.constraint` section defines your URL to be authenticated and the role you need to complete authentication. 

4. deploy the example application
```
cp karaf/blueprint.xml $FUSE_BASE/deploy
```
5. check that everything is installed ok
```
karaf@root()> osgi:list
60 │ Active │  50 │ 2.21.0.fuse-760027-redhat-00001 │ camel-blueprint
244 │ Active │  50 │ 2.21.0.fuse-760027-redhat-00001 │ camel-servlet
243 │ Active │  80 │ 0.0.0                           │ blueprint.xml
```
Note the `Active` state for our bundles.

6. Test invoking the URL
```
curl -X GET  --basic -u admin:admin http://localhost:8181/rest/status
Hello World!
```

### Application description
1. The Camel context contains the `restConfiguration` tag that identifies the `servlet` (that means `camel-servlet`) as component to be used.
Then it configures the base endpoint (`contextPath`) and the servlet name.
The next tags defines the business logic.
```
<camelContext xmlns="http://camel.apache.org/schema/blueprint" id="simple">

            <restConfiguration component="servlet" bindingMode="off" scheme="http" contextPath="/rest"
             port="8181" enableCORS="true"  >
            <endpointProperty key="servletName" value="amIntegrationCamelServlet" />
            <dataFormatProperty key="prettyPrint" value="true" />
        </restConfiguration>
...
    </camelContext>
```
2. The real servlet is implemented by the `CamelHttpTransportServlet` that is registered to the `httpService` (the default Undertow in Fuse 7.x) using the `OsgiServletRegisterer` that acts as a glue with all the components.
Note the properties that point to the other three components.
```
    <bean id="camelServlet" class="org.apache.camel.component.servlet.CamelHttpTransportServlet" />

    <reference id="httpService" interface="org.osgi.service.http.HttpService"/>

    <bean class="org.apache.camel.component.servlet.osgi.OsgiServletRegisterer" init-method="register" destroy-method="unregister">
        <property name="alias" value="/rest" />
        <property name="servlet" ref="camelServlet" />
        <property name="httpService" ref="httpService"/>
        <property name="servletName" value="amIntegrationCamelServlet" />
    </bean>
```


