package cc.sofast.infrastructure.customization;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.AbstractRefreshableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

/**
 * @author xielong.wang
 */
public class ApplicationContextHelper implements ApplicationContextAware {

    private static ApplicationContext context;

    private static DefaultListableBeanFactory springFactory;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextHelper.setContext(applicationContext);
        if (applicationContext instanceof AbstractRefreshableApplicationContext) {
            AbstractRefreshableApplicationContext springContext =
                    (AbstractRefreshableApplicationContext) applicationContext;
            ApplicationContextHelper.setFactory((DefaultListableBeanFactory) springContext.getBeanFactory());
        } else if (applicationContext instanceof GenericApplicationContext) {
            GenericApplicationContext springContext = (GenericApplicationContext) applicationContext;
            ApplicationContextHelper.setFactory(springContext.getDefaultListableBeanFactory());
        }
    }

    private static void setContext(ApplicationContext applicationContext) {

        ApplicationContextHelper.context = applicationContext;
    }

    private static void setFactory(DefaultListableBeanFactory springFactory) {

        ApplicationContextHelper.springFactory = springFactory;
    }

    public static ApplicationContext getContext() {

        return context;
    }

    public static DefaultListableBeanFactory getSpringFactory() {

        return springFactory;
    }

}

