package com.nishant.code_it.docker;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;

@Configuration
public class DockerConfiguration {
	
	@Value("${docker.host}")
    private String host;
	@Value("${docker.registryUsername}")
	private String registryUsername;
	@Value("${docker.registryPassword}")
	private String registryPassword;
	@Value("${docker.registryEmail}")
	private String registryEmail;
	@Value("${docker.registryUrl}")
	private String registryUrl;
	
	
	@Bean 
	public DockerClient dockerClient()
	{
		
		DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
				       .withDockerHost(host)
				       .withDockerTlsVerify(false)
				       .withRegistryUsername(registryUsername)
				       .withRegistryPassword(registryPassword)
				       .withRegistryEmail(registryEmail)
				       .withRegistryUrl(registryUrl)
				       .build();
		
		DockerHttpClient httpClient = new ApacheDockerHttpClient.Builder()
				.dockerHost(config.getDockerHost())
				.sslConfig(config.getSSLConfig())
				.maxConnections(100)
				.connectionTimeout(Duration.ofSeconds(30))
				.responseTimeout(Duration.ofSeconds(45))
				.build();
		
		DockerClient dockerClient = DockerClientImpl.getInstance(config, httpClient);
		return dockerClient;
				
	}

}
