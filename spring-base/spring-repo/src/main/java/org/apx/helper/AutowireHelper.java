package org.apx.helper;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Helper class which is able to autowire a specified class. It holds a static reference to the {@link org
 * .springframework.context.ApplicationContext}.
 */
public final class AutowireHelper implements ApplicationContextAware {

    private static final AutowireHelper INSTANCE = new AutowireHelper();
    private static ApplicationContext applicationContext;

    private AutowireHelper() {
    }

    /**
     * Tries to autowire the specified instance of the class
     * @param classToAutowire the instance of the class which holds @Autowire annotations
     */
    public static void autowire(Object classToAutowire) {
        applicationContext.getAutowireCapableBeanFactory().autowireBean(classToAutowire);
    }

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) {
        AutowireHelper.applicationContext = applicationContext;
    }

    /**
     * @return the singleton instance.
     */
    public static AutowireHelper getInstance() {
        return INSTANCE;
    }

}