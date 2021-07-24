package com.iyher.gradle.plugin.codeartifact_repository;
import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CodeArtifactURLParser {
	private final class GroupNames {
		static final String DOMAIN_NAME = "a";
		static final String DOMAIN_OWNER = "b";
		static final String REGION = "c";
		static final String REPOSITORY = "d";
	}
	
	private static final String namedPattern(String name, String regex) {
		return "(?<" + name + ">" + regex + ")";
	}
	private static final String DOMAIN_NAME = namedPattern(GroupNames.DOMAIN_NAME, ".*");
	private static final String DOMAIN_OWNER = namedPattern(GroupNames.DOMAIN_OWNER, "\\d{12}");
	private static final String REGION = namedPattern(GroupNames.REGION, ".*");
	private static final Pattern HOST_PATTERN = Pattern.compile(String.join(""
		, DOMAIN_NAME
		, "-"
		, DOMAIN_OWNER
		, Pattern.quote(".d.codeartifact.")
		, REGION
		, Pattern.quote(".amazonaws.com")
	));  
	
	private static final String REPOSITORY = namedPattern(GroupNames.REPOSITORY, ".*");
	private static final Pattern PATH_PATTERN = Pattern.compile(String.join(""
		, Pattern.quote("/maven/")
		, REPOSITORY
		, Pattern.quote("/")
	));

	public static CodeArtifactRepository parse(URI url) {
		CodeArtifactRepository result = new CompositRepositoryExclusiveToCodeartifact();
		parse(url, result);
		
		return result;
	}

	public static CodeArtifactRepository parse(URI url, CodeArtifactRepository result) {
		CodeArtifactURLParser.hostParse(url.getHost(), result);
		CodeArtifactURLParser.pathParse(url.getPath(), result);
		
		return result;
	}
	
	static void hostParse(String host, CodeArtifactRepository result) {
		Matcher matcher = HOST_PATTERN.matcher(host);
		if (!matcher.matches()) {
			throw new IllegalArgumentException("not CodeArtifact URL");
		}
	
		result.setDomainName(matcher.group(GroupNames.DOMAIN_NAME));
		result.setDomainOwner(matcher.group(GroupNames.DOMAIN_OWNER));
		result.setRegion(matcher.group(GroupNames.REGION));
	}
	
	static void pathParse(String path, CodeArtifactRepository result) {
		Matcher matcher = PATH_PATTERN.matcher(path);
		if (!matcher.matches()) {
			throw new IllegalArgumentException("not CodeArtifact URL");
		}
	
		result.setRepository(matcher.group(GroupNames.REPOSITORY));
	}
}
