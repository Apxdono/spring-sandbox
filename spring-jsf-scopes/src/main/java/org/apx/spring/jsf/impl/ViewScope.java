package org.apx.spring.jsf.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.PreDestroyViewMapEvent;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * A Custom view scope implementation. Seems to behave just the same as default jfs one.
 * At least in jProfiler no memory leaks detected.
 */
public class ViewScope implements Scope, Serializable, HttpSessionBindingListener {
	private static final Logger LOG = LoggerFactory.getLogger(ViewScope.class);
	private final WeakHashMap<HttpSession, Set<ViewScopeViewMapListener>> listeners = new WeakHashMap<HttpSession, Set<ViewScopeViewMapListener>>();

	@Override
	public Object get(String name, ObjectFactory objectFactory) {
		Map<String, Object> viewMap = FacesContext.getCurrentInstance().getViewRoot().getViewMap();
		synchronized (viewMap) {
			if (viewMap.containsKey(name)) {
				return viewMap.get(name);
			} else {
				LOG.debug("Creating bean {}", name);
				Object object = objectFactory.getObject();
				viewMap.put(name, object);

				return object;
			}
		}
	}

	@Override
	public Object remove(String name) {
//		throw new UnsupportedOperationException(); or really delete from viewMap. Can't figure out yet.
		LOG.debug("Removind bean {}",name);
		return null;
	}

	@Override
	public String getConversationId() {
		return null;
	}

	@Override
	public void registerDestructionCallback(String name, Runnable callback) {
		LOG.debug("registerDestructionCallback for bean {}", name);
		UIViewRoot viewRoot = FacesContext.getCurrentInstance().getViewRoot();
		ViewScopeViewMapListener listener =
				new ViewScopeViewMapListener(viewRoot, name, callback, this);

		viewRoot.subscribeToViewEvent(PreDestroyViewMapEvent.class, listener);

		HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
		final Set<ViewScopeViewMapListener> sessionListeners;
		synchronized (listeners) {
			if (!listeners.containsKey(httpSession)) {
				listeners.put(httpSession, new HashSet<ViewScopeViewMapListener>());
			}
			sessionListeners = listeners.get(httpSession);
		}
		//noinspection SynchronizationOnLocalVariableOrMethodParameter
		synchronized (sessionListeners) {
			Set<ViewScopeViewMapListener> toRemove = new HashSet<ViewScopeViewMapListener>();
			for (ViewScopeViewMapListener viewMapListener : sessionListeners) {
				if (viewMapListener.checkRoot()) {
					toRemove.add(viewMapListener);
				}
			}
			sessionListeners.removeAll(toRemove);
			sessionListeners.add(listener);
		}
		if (!FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("sessionBindingListener")) {
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("sessionBindingListener", this);
		}

	}

	@Override
	public Object resolveContextualObject(String key) {
		return null;
	}

	@Override
	public void valueBound(HttpSessionBindingEvent event) {
		LOG.debug("Session event bound {}", event.getName());
	}

	@Override
	public void valueUnbound(HttpSessionBindingEvent event) {
		LOG.debug("Session event unbound {}", event.getName());
		final Set<ViewScopeViewMapListener> listeners;
		synchronized (this.listeners) {
			if (this.listeners.containsKey(event.getSession())) {
				listeners = this.listeners.get(event.getSession());
				this.listeners.remove(event.getSession());
			} else {
				listeners = null;
			}
		}
		if (listeners != null) {
			for (ViewScopeViewMapListener listener : listeners) {
				listener.doCallback();
			}
		}
	}

	public void clearFromListener(ViewScopeViewMapListener listener) {
		LOG.debug("Removing listener from map");
		HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		if (httpSession != null) {
			synchronized (listeners) {
				if (listeners.containsKey(httpSession)) {
					listeners.get(httpSession).remove(listener);
				}
			}
		}
	}

}
