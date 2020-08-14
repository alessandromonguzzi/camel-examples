# Camel-paho example with AMQ Broker
This example creates two camel routes, one creating a message and pushing it to a queue and the other one consuming the messages from the same queue.

## Requirements
1. AMQ Broker running 
2. Maven 3.x compatible with Spring Boot 2

## Procedure
1. Start AMQ Broker:
```
./bin/artemis run
```

2. Run the Spring Boot application from mvn:
```
mvn spring-boot:run
```

The output will show one line to the output for each message in the queue:
```
2020-08-14 10:51:53.978  INFO 31880 --- [timer://trigger] route2                                   : sent
2020-08-14 10:51:54.070  INFO 31880 --- [ad #2 - Threads] route1                                   : received
2020-08-14 10:51:54.956  INFO 31880 --- [timer://trigger] route2                                   : sent
2020-08-14 10:51:54.965  INFO 31880 --- [ad #3 - Threads] route1                                   : received
2020-08-14 10:51:55.956  INFO 31880 --- [timer://trigger] route2                                   : sent
2020-08-14 10:51:56.023  INFO 31880 --- [ad #4 - Threads] route1                                   : received
2020-08-14 10:51:56.955  INFO 31880 --- [timer://trigger] route2                                   : sent
2020-08-14 10:51:57.025  INFO 31880 --- [ad #5 - Threads] route1                                   : received
```

Please note the different Thread names that depend on the amount of threads specified in the 
MqttConsumer route.

Now it's possible to play with the PARALLEL_THREADS and DELAY constants. While delay is 0, the consumer keep the pace with the producer even if there is a `sleep()`
call in the Processor. If a delay > 0 is introduced, the consumer is not able to keep the pace of the producer.
