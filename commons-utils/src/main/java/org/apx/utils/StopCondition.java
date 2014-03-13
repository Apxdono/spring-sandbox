package org.apx.utils;

import java.util.Collection;
import java.util.Stack;

/**
 * Created by oleg on 3/13/14.
 */
public interface StopCondition<T> {
	public boolean doStop(T currentObject,Class<?> currentClass,Collection<T> currentResult);
}
