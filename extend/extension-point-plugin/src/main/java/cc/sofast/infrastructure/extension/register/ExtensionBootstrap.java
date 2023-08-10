package cc.sofast.infrastructure.extension.register;


import cc.sofast.infrastructure.extension.Extension;
import cc.sofast.infrastructure.extension.ExtensionPointI;
import cc.sofast.infrastructure.extension.Extensions;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.ServiceLoader;

/**
 * ExtensionBootstrap
 *
 * @author Frank Zhang
 * @date 2020-06-18 7:55 PM
 */
@Component
public class ExtensionBootstrap implements ApplicationContextAware {

    private static final Logger log = LoggerFactory.getLogger(ExtensionBootstrap.class);

    @Resource
    private ExtensionRegister extensionRegister;

    private ApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        //获取所有的ExtensionPointI 的实现类+并被Extension注解标记。
        ServiceLoader<ExtensionPointI> serviceLoader = ServiceLoader.load(ExtensionPointI.class);
        for (ExtensionPointI next : serviceLoader) {
            Class<? extends ExtensionPointI> extenstionClass = next.getClass();
            ObjectProvider<? extends ExtensionPointI> extensionPointBean = applicationContext.getBeanProvider(extenstionClass);
            if (extensionPointBean.getIfAvailable() != null) {
                log.info("[{}] Already exists. No duplicate registration is required", extensionPointBean);
                continue;
            }
            //动态注册bean
            AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(next.getClass()).getBeanDefinition();
            ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) applicationContext;
            DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) configurableApplicationContext.getBeanFactory();
            beanFactory.registerBeanDefinition(next.getClass().getName(), beanDefinition);
        }

        //加载Extension 的所有子类实现
        Map<String, Object> extensionBeans = applicationContext.getBeansWithAnnotation(Extension.class);
        extensionBeans.values().forEach(
                extension -> {
                    ExtensionPointI extensionPoint = (ExtensionPointI) extension;
                    extensionPoint.registerBefore(applicationContext);
                    extensionRegister.doRegistration(extensionPoint);
                }
        );

        // handle @Extensions annotation
        Map<String, Object> extensionsBeans = applicationContext.getBeansWithAnnotation(Extensions.class);
        extensionsBeans.values().forEach(extension -> {
            ExtensionPointI extensionPoint = (ExtensionPointI) extension;
            extensionPoint.registerBefore(applicationContext);
            extensionRegister.doRegistrationExtensions((ExtensionPointI) extension);
        });
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}