package cc.sofast.infrastructure.customization.script;

import org.springframework.util.Assert;

/**
 * @author xielong.wang
 * groovy脚本缓存
 */
public class GroovyScriptEntry {

    /**
     * 脚本名称（需要保证唯一）
     */
    private String name;

    /**
     * 脚本内容
     */
    private final String scriptContext;

    /**
     * 脚本指纹
     */
    private final String fingerprint;

    /**
     * 最近修改时间
     */
    private Long lastModifiedTime;

    /**
     * 脚本code对应的Class
     */
    private Class<?> clazz;

    public GroovyScriptEntry(String name, String scriptContext, String fingerprint, Long lastModifiedTime) {
        Assert.notNull(name, "name can not be null.");
        Assert.notNull(scriptContext, "scriptContext can not be null.");
        Assert.notNull(fingerprint, "fingerprint can not be null.");
        Assert.notNull(lastModifiedTime, "lastModifiedTime can not be null.");
        this.name = name;
        this.scriptContext = scriptContext;
        this.fingerprint = fingerprint;
        this.lastModifiedTime = lastModifiedTime;
    }

    /**
     * 指纹是否相同
     */
    public boolean fingerprintIsEquals(String otherFingerprint) {
        return fingerprint.equals(otherFingerprint);
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setLastModifiedTime(Long lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public String getName() {
        return name;
    }

    public String getScriptContext() {
        return scriptContext;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public Long getLastModifiedTime() {
        return lastModifiedTime;
    }

    public Class<?> getClazz() {
        return clazz;
    }
}
