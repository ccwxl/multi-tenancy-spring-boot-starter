package cc.sofast.infrastructure.jdbc.schema;

import lombok.Getter;

/**
 * @author apple
 */
@Getter
public class SchemaInfo {

    private String username;

    private String password;

    private String host;

    private int port;

    private String db;

    private String schema;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setDb(String db) {
        this.db = db;
    }
}
