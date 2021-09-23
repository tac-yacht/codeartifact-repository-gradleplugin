package com.iyher.gradle.plugin.codeartifact_repository;

import static org.apache.commons.lang3.ObjectUtils.*;

import java.util.ArrayList;
import java.util.List;

import org.gradle.api.Project;
import org.gradle.api.artifacts.dsl.RepositoryHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSCredentialsProviderChain;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.codeartifact.AWSCodeArtifact;
import com.amazonaws.services.codeartifact.AWSCodeArtifactClient;
import com.amazonaws.services.codeartifact.model.GetAuthorizationTokenRequest;
import com.amazonaws.services.codeartifact.model.GetAuthorizationTokenResult;

import groovy.lang.Closure;

public class CredentialsResolver {
	private static final Logger log = LoggerFactory.getLogger(CredentialsResolver.class);
	
	@SuppressWarnings("unused")
	private final RepositoryHandler repositories;
	private final Project project;

	public CredentialsResolver(Project project) {
		this.project = project;
		this.repositories = project.getRepositories();
	}
	
	public String resolve(CodeArtifactRepository repo) {
		AWSCredentialsProvider credentials = parseResource(repo.getAwsCredentialsClosure());
		
		AWSCodeArtifact client = AWSCodeArtifactClient.builder()
			.withCredentials(credentials)
			.withRegion(repo.getRegion())
		.build();
		
		GetAuthorizationTokenResult tokenResult = client.getAuthorizationToken(new GetAuthorizationTokenRequest()
			.withDomain(repo.getDomainName())
			.withDomainOwner(repo.getDomainOwner())
			.withDurationSeconds(900L)
		);
		
		log.debug("token expiration: {}", tokenResult.getExpiration());
		return tokenResult.getAuthorizationToken();
	}
	
	AWSCredentialsProvider parseResource(Closure<?> closure) {
		AwsCredentialsCompositProfile origin = (AwsCredentialsCompositProfile) project.configure(new AwsCredentialsCompositProfile(), closure);

		List<AWSCredentialsProvider> providers = new ArrayList<>();
		if(allNotNull(origin.getAccessKey(), origin.getSecretKey())) {
			AWSCredentials awsCreds;
			if (allNotNull(origin.getSessionToken())) {
				awsCreds = new BasicSessionCredentials(origin.getAccessKey(), origin.getSecretKey(), origin.getSessionToken());
			} else {
				awsCreds = new BasicAWSCredentials(origin.getAccessKey(), origin.getSecretKey());
			}
			providers.add(new AWSStaticCredentialsProvider(awsCreds));
		}
		
		if(allNotNull(origin.getProfile())) {
			providers.add(new ProfileCredentialsProvider(origin.getProfile()));
		}
		
		return new AWSCredentialsProviderChain(providers);
	}

}
