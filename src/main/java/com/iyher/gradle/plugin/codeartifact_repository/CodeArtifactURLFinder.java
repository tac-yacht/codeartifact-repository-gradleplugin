package com.iyher.gradle.plugin.codeartifact_repository;
import java.net.URI;

public class CodeArtifactURLFinder {
	
	//sample url
	//https://my-domain-111122223333.d.codeartifact.us-west-2.amazonaws.com/maven/my_repo/
	private static String URL_FORMAT = "https://%1$s-%2$s.d.codeartifact.%3$s.amazonaws.com/maven/%4$s/";
	
	public static URI find(CodeArtifactRepository repo) {
		String url = String.format(URL_FORMAT, 
			repo.getDomainName(),
			repo.getDomainOwner(),
			repo.getRegion(),
			repo.getRepository()
		);

		return URI.create(url);
	}
}
