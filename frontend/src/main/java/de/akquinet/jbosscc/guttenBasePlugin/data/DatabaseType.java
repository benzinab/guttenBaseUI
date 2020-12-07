package de.akquinet.jbosscc.guttenBasePlugin.data;

public enum DatabaseType {
    MICROSOFT_SQL_SERVER("Microsoft SQL Server"),
    MS_ACCESS("MS-Access"),
    MYSQL ("MySQL"),
    POSTGRESQL ("PostgreSQL"),
    HSQLDB ("HSQLDB"),
    H2_DERBY ("H2 Derby"),
    ORACLE ("Oracle"),
    DB2 ("DB2"),
    SYBASE ("Sybase");


    private final String name;

    DatabaseType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
