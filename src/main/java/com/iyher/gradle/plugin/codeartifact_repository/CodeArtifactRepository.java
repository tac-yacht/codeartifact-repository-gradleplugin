package com.iyher.gradle.plugin.codeartifact_repository;
import java.net.URI;

import groovy.lang.Closure;

//required syncing /codeartifact-repository-gradleplugin/src/main/groovy/com.github.tac-yacht.codeartifact-repository.gradle
public interface CodeArtifactRepository {
	
	URI getUrl();
	void setUrl(URI url);
	void setUrl(Object url);
	
	String getArn();
	void setArn(String arn);
	
	String getDomainName();
	void setDomainName(String domainName);
	String getRepository();
	void setRepository(String repository);
	String getRegion();
	void setRegion(String region);
	String getDomainOwner();
	void setDomainOwner(String domainOwner);
	
	void credentials(CodeArtifactMarker marker, Closure<?> action);
	public Closure<?> getAwsCredentialsClosure();
}
