package cc.sofast.infrastructure.customization.script.groovy;

/**
 * @author apple
 */
public class Md5Script {
    private String md5;
    private groovy.lang.Script script;

    public Md5Script(String md5, groovy.lang.Script script) {
        this.md5 = md5;
        this.script = script;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public groovy.lang.Script getScript() {
        return script;
    }

    public void setScript(groovy.lang.Script script) {
        this.script = script;
    }
}
