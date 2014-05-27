package org.apx.repo.handler;

import org.apx.repo.CommonRepo;
import org.apx.utils.ReflectionUtils;
import org.apx.utils.StopCondition;

import java.util.Collection;
import java.util.List;

/**
 * Created by oleg on 3/13/14.
 */
public abstract class HandlerProcessor {

	public static <E> E processLazyObjects(E entity, CommonRepo repo){
		List<LazyInitHandler> annotations = ReflectionUtils.getClassAnnotations(entity.getClass(),LazyInitHandler.class,new StopCondition<LazyInitHandler>() {
			@Override
			public boolean doStop(LazyInitHandler lazyInitHandler, Class<?> aClass, Collection<LazyInitHandler> lazyInitHandlers) {
				if(lazyInitHandler != null && lazyInitHandler.override()){
					return true;
				}
				return false;
			}
		});

		if(annotations != null && !annotations.isEmpty()){
			for (LazyInitHandler annotation : annotations) {
				IDBHandler<E> handler = ReflectionUtils.newInstance(annotation.handlerClass());
				if(handler != null){
					handler.setContext(repo);
					entity = handler.process(entity);
				}
			}
		}
		return entity;
	}

}
