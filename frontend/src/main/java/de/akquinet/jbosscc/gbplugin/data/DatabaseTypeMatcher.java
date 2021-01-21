package de.akquinet.jbosscc.gbplugin.data;

public enum DatabaseTypeMatcher {
    MSSQL("MSSQL"),
    MYSQL ("MYSQL"),
    POSTGRES ("POSTGRESQL"),
    HSQL ("HSQLDB"),
    H2 ("H2DB"),
    DERBY ("DERBY"),
    ORACLE ("ORACLE"),
    DB2 ("DB2"),
    SYBASE ("SYBASE"),
    GENERIC ("GENERIC"),
    MOCK ("MOCK"),
    EXPORT_DUMP ("EXPORT_DUMP"),
    IMPORT_DUMP ("IMPORT_DUMP"),
    MS_ACCESS ("MS_ACCESS");


    private final String gbType;

    DatabaseTypeMatcher(String gbType) {
        this.gbType = gbType;
    }

    public String getGbType() {
        return gbType;
    }

    @Override
    public String toString() {
        return gbType;
    }
}
