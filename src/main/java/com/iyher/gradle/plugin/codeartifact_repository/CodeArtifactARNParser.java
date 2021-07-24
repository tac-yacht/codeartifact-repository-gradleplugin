package com.iyher.gradle.plugin.codeartifact_repository;

public class CodeArtifactARNParser {
	
	private static class Index {
		static final int REGION = 3;
		static final int DOMAIN_OWNER = 4;
		static final int DOMAIN_NAME = 6;
		static final int REPOSITORY = 7;
	}
	
	private static final String SEPARATOR = "[:/]";
	
	public static CodeArtifactRepository parse(String arn) {
		CodeArtifactRepository result = new CompositRepositoryExclusiveToCodeartifact();
		parse(arn, result);
		
		return result;
	}

	public static CodeArtifactRepository parse(String arn, CodeArtifactRepository repo) {
		String[] splited  = arn.split(SEPARATOR);

		repo.setRegion(splited[Index.REGION]);
		repo.setDomainName(splited[Index.DOMAIN_NAME]);
		repo.setDomainOwner(splited[Index.DOMAIN_OWNER]);
		repo.setRepository(splited[Index.REPOSITORY]);
		
		return repo;
	}
}
