package cc.sofast.infrastructure.extension.register;


import cc.sofast.infrastructure.extension.*;
import jakarta.annotation.Resource;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;


/**
 * ExtensionRegister
 *
 * @author fulan.zjf 2017-11-05
 */
@Component
public class ExtensionRegister {

    /**
     * 扩展点接口名称不合法
     */
    private static final String EXTENSION_INTERFACE_NAME_ILLEGAL = "extension_interface_name_illegal";
    /**
     * 扩展点不合法
     */
    private static final String EXTENSION_ILLEGAL = "extension_illegal";
    /**
     * 扩展点定义重复
     */
    private static final String EXTENSION_DEFINE_DUPLICATE = "extension_define_duplicate";

    @Resource
    private ExtensionRepository extensionRepository;

    public final static String EXTENSION_EXTPT_NAMING = "ExtPt";


    public void doRegistration(ExtensionPointI extensionObject) {
        Class<?> extensionClz = extensionObject.getClass();
        if (AopUtils.isAopProxy(extensionObject)) {
            extensionClz = ClassUtils.getUserClass(extensionObject);
        }
        Extension extensionAnn = AnnotationUtils.findAnnotation(extensionClz, Extension.class);
        Assert.notNull(extensionAnn, "extensionAnn is null");
        BizScenario bizScenario = BizScenario.valueOf(extensionAnn.bizId(), extensionAnn.tenant(), extensionAnn.scenario());
        ExtensionCoordinate extensionCoordinate = new ExtensionCoordinate(calculateExtensionPoint(extensionClz), bizScenario.getUniqueIdentity());
        ExtensionPointI preVal = extensionRepository.getExtensionRepo().put(extensionCoordinate, extensionObject);
        if (preVal != null) {
            String errMessage = "Duplicate registration is not allowed for :" + extensionCoordinate;
            throw new ExtensionException(EXTENSION_DEFINE_DUPLICATE, errMessage);
        }
    }

    public void doRegistrationExtensions(ExtensionPointI extensionObject) {
        Class<?> extensionClz = extensionObject.getClass();
        if (AopUtils.isAopProxy(extensionObject)) {
            extensionClz = ClassUtils.getUserClass(extensionObject);
        }

        Extensions extensionsAnnotation = AnnotationUtils.findAnnotation(extensionClz, Extensions.class);
        Assert.notNull(extensionsAnnotation, "extensionsAnnotation is null");
        Extension[] extensions = extensionsAnnotation.value();
        if (!ObjectUtils.isEmpty(extensions)) {
            for (Extension extensionAnn : extensions) {
                BizScenario bizScenario = BizScenario.valueOf(extensionAnn.bizId(), extensionAnn.tenant(), extensionAnn.scenario());
                ExtensionCoordinate extensionCoordinate = new ExtensionCoordinate(calculateExtensionPoint(extensionClz), bizScenario.getUniqueIdentity());
                ExtensionPointI preVal = extensionRepository.getExtensionRepo().put(extensionCoordinate, extensionObject);
                if (preVal != null) {
                    String errMessage = "Duplicate registration is not allowed for :" + extensionCoordinate;
                    throw new ExtensionException(EXTENSION_DEFINE_DUPLICATE, errMessage);
                }
            }
        }

        //
        String[] bizIds = extensionsAnnotation.bizId();
        String[] tenant = extensionsAnnotation.tenant();
        String[] scenarios = extensionsAnnotation.scenario();
        for (String bizId : bizIds) {
            for (String ta : tenant) {
                for (String scenario : scenarios) {
                    BizScenario bizScenario = BizScenario.valueOf(bizId, ta, scenario);
                    ExtensionCoordinate extensionCoordinate = new ExtensionCoordinate(calculateExtensionPoint(extensionClz), bizScenario.getUniqueIdentity());
                    ExtensionPointI preVal = extensionRepository.getExtensionRepo().put(extensionCoordinate, extensionObject);
                    if (preVal != null) {
                        String errMessage = "Duplicate registration is not allowed for :" + extensionCoordinate;
                        throw new ExtensionException(EXTENSION_DEFINE_DUPLICATE, errMessage);
                    }
                }
            }
        }
    }

    /**
     * @param targetClz
     * @return
     */
    private String calculateExtensionPoint(Class<?> targetClz) {
        Class<?>[] interfaces = ClassUtils.getAllInterfacesForClass(targetClz);
        if (interfaces.length == 0) {
            throw new ExtensionException(EXTENSION_ILLEGAL, "Please assign a extension point interface for " + targetClz);
        }
        for (Class<?> intf : interfaces) {
            String extensionPoint = intf.getSimpleName();
            if (extensionPoint.contains(EXTENSION_EXTPT_NAMING)) {
                return intf.getName();
            }
        }
        String errMessage = "Your name of ExtensionPoint for " + targetClz +
                " is not valid, must be end of " + EXTENSION_EXTPT_NAMING;
        throw new ExtensionException(EXTENSION_INTERFACE_NAME_ILLEGAL, errMessage);
    }

}
