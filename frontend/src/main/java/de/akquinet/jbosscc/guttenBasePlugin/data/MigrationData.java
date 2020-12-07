package de.akquinet.jbosscc.guttenBasePlugin.data;

import de.akquinet.jbosscc.guttenbase.connector.DatabaseType;

public class MigrationData {

    private String url;

    private String user;

    private String password;

    private String driverClass;

    private DatabaseType databaseType;

    public MigrationData(String url, String user, String password, String driverClass, DatabaseType databaseType) {
        this.url = url;
        this.user = user;
        this.password = password;
        this.driverClass = driverClass;
        this.databaseType = databaseType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDriverClass() {
        return driverClass;
    }

    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }

    public DatabaseType getDatabaseType() {
        return databaseType;
    }

    public void setDatabaseType(DatabaseType databaseType) {
        this.databaseType = databaseType;
    }
}
