package com.iyher.gradle.plugin.codeartifact_repository;

import org.gradle.internal.credentials.DefaultAwsCredentials;

public class AwsCredentialsCompositProfile extends DefaultAwsCredentials {
	private String profile;

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}
}
