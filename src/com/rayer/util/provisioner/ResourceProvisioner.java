package com.rayer.util.provisioner;

public interface ResourceProvisioner<T> {
	T getResource(String identificator);
	boolean setResource(String identificator, T targetResource);
	boolean dereferenceResource(String identificator);
}