package org.apx.spring.jsf.impl;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationScopeMetadataResolver;
import org.springframework.context.annotation.ScopeMetadata;

import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import java.util.Set;

public class JavaxFacesScopeMetadataResolver
		extends AnnotationScopeMetadataResolver {
	@Override
	public ScopeMetadata resolveScopeMetadata(BeanDefinition definition) {
		ScopeMetadata metadata = new ScopeMetadata();
		if (definition instanceof AnnotatedBeanDefinition) {
			AnnotatedBeanDefinition annDef = (AnnotatedBeanDefinition) definition;
			Set<String> annotationTypes = annDef.getMetadata().getAnnotationTypes();

			if (annotationTypes.contains(
					RequestScoped.class.getName())
					) {
				metadata.setScopeName("request");
			} else if (annotationTypes.contains(
					SessionScoped.class.getName())
					) {
				metadata.setScopeName("session");
			} else if (annotationTypes.contains(
					ViewScoped.class.getName())
					) {
				metadata.setScopeName("view");
			} else {
				return super.resolveScopeMetadata(definition);
			}
		}
		return metadata;
	}


}