### Microservice Architecture


#### Service Registry Logic
    we register all the service in the discovery server.
    There all the client services (Eureka clients) are registered
    Earlier we used to annotate client with @EnableEurekaClient but
    now, we replace with @EnableDiscoveryClient

    POM.XML
        This should contain the appropriate dependency and dependency
        management since Eureka is a part of spring cloud and not 
        springBoot, make sure to add the Eureka version in the property
        --- like the example shown below

            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
            </dependency>
    
        </dependencies>

        --- dependency management
            <dependencyManagement>
                <dependencies>
                    <dependency>
                        <groupId>org.springframework.cloud</groupId>
                        <artifactId>spring-cloud-dependencies</artifactId>
                        <version>${spring-cloud.version}</version>
                        <type>pom</type>
                        <scope>import</scope>
                    </dependency>
                </dependencies>
            </dependencyManagement>

        --- properties
            <properties>
                <maven.compiler.source>17</maven.compiler.source>
                <maven.compiler.target>17</maven.compiler.target>
                <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
                <spring-cloud.version>2023.0.3</spring-cloud.version>
            </properties>
        ---properties

    for client use this:
        eureka.client.serviceUrl.defaultZone=http://localhost:8761/eureka 
    for discovery use this:
        eureka.client.register-with-eureka=false
        eureka.client.fetch-registry=false
    --- what happens when service goes down?
        The client gets its own copy of the services and the ip and port
        number it runs on,
        to decipher the same use 
        webClientBuilder.build().get()
        .uri("http://inventory-service/api/inventory",
        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
        .retrieve()
        .bodyToMono(InventoryResponse[].class)
        .block();
        

        and this
    @Bean
    @LoadBalanced
    public WebClient.Builder webClientBuilder(){
        return WebClient.builder();
    }

    Note that even if discovery service is down, since the client has saved the details of the services
    and its corresponding ip and port number, it would still be able to figure out where to contact.    
    But somehow, when say, client 1 is fetching client 2's api but client 2 now has reconfigured it's port to something
    different, it will now not be able to fetch which port it is looking for until discovery server comes up and 
    the client 2 re-registers itself to the discovery server