package org.apx.utils;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;

/**
 * All reflection operations are handled in here. Mainly for POJO's
 */
public abstract class ReflectionUtils {

	static Logger LOG = LoggerFactory.getLogger(ReflectionUtils.class);

	public static Field field(Class target, String name) {
		return field(target, name, null, null);
	}

	public static Field field(Class target, String name, Class type) {
		return field(target, name, type, null);
	}

	public static Field field(Class target, String name, Class type, Class stopClass) {

		if (target == null) {
			LOG.warn("Target class is null.");
			return null;
		}

		if (StringUtils.isBlank(name)) {
			LOG.warn("Target field name is empty.");
			return null;
		}

		if (stopClass == null) {
			stopClass = Object.class;
		}

		Field result = null;

		while (!target.equals(stopClass)) {
			Field[] fields = target.getDeclaredFields();
			if (fields != null) {
				for (Field f : fields) {
					if (f.getName().equals(name)) {
						if (type != null && !ClassUtils.isAssignable(f.getType(), type, true)) {
							continue;
						}
						result = f;
						break;
					}
				}
			}
			target = target.getSuperclass();
		}

		if (result == null) {
			LOG.warn("No field with name '{}' and type '{}' was found.", name, type);
		}

		return result;
	}


	public static Class<?> getParameterClassSimple(Class<?> target, int parameterIndex) {
		if (target == null) {
			LOG.warn("No target class passed to get parameter class.");
			return null;
		}
		return (Class) ((ParameterizedType) target.getGenericSuperclass()).getActualTypeArguments()[parameterIndex];
	}


	public static <E> E newInstance(Class<E> target) {
		if (target == null) {
			LOG.warn("No target class passed.");
			return null;
		}

		E result = null;

		try {
			result = target.newInstance();
		} catch (InstantiationException e) {
			LOG.error("Cannot create instance of class '{}'", target.getSimpleName(), e);
		} catch (IllegalAccessException e) {
			LOG.error("Cannot access default constructor for class '{}'", target.getSimpleName(), e);
		}
		return result;
	}


	public static <E extends Annotation> E getClassAnnotation(Class<?> target, Class<E> annClass, boolean deepSearch) {
		Class<?> stopClass = Object.class;
		if (target == null || annClass == null || stopClass.equals(target)) {
			LOG.warn("Target class is '{}' . Annotation class is '{}'. Cannot proceed.");
			return null;
		}

		E result = null;
		if (!deepSearch) {
			result = target.getAnnotation(annClass);
		} else {
			List<E> res = getClassAnnotations(target, annClass, new StopCondition<E>() {
				@Override
				public boolean doStop(E currentObject, Class<?> currentClass, Collection<E> currentResult) {
					if(currentObject != null){
						return true;
					}
					return false;
				}
			});
			if(!res.isEmpty()){
				result = res.get(0);
			}
		}

		return result;
	}

	public static <E extends Annotation> List<E> getClassAnnotations(Class<?> target, Class<E> annClass) {
		return getClassAnnotations(target, annClass, null);
	}

	public static <E extends Annotation> List<E> getClassAnnotations(Class<?> target, Class<E> annClass, StopCondition<E> condition) {
		Class<?> stopClass = Object.class;
		if (target == null || annClass == null || stopClass.equals(target)) {
			LOG.warn("Target class is '{}' . Annotation class is '{}'. Cannot proceed.");
			return null;
		}

		List<E> list = new ArrayList<E>();
		while (target != stopClass) {
			E result = target.getAnnotation(annClass);
			if (result != null) {
				list.add(result);
				if(condition != null && condition.doStop(result,target,list)){
					break;
				}
			}
			target = target.getSuperclass();
		}
		Collections.reverse(list);
		return list;
	}


}
