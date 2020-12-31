package de.akquinet.jbosscc.gbplugin.data.gbactions;

import de.akquinet.jbosscc.gbplugin.data.nodes.MyDataNode;
import de.akquinet.jbosscc.guttenbase.connector.DatabaseType;
import de.akquinet.jbosscc.guttenbase.mapping.DefaultColumnTypeMapper;

public class ChangeTypeGBAction extends GBAction{
    private String sourceTypeName;
    private String targetTypeName;
    private DefaultColumnTypeMapper mapper;
    private DatabaseType sourceDBType;
    private DatabaseType targetDBType;
    public ChangeTypeGBAction(String name, GBActionType gbActionType, String sourceTypeName, String targetTypeName) {
        super(name, gbActionType);
        this.sourceTypeName = sourceTypeName;
        this.targetTypeName = targetTypeName;
    }

    public ChangeTypeGBAction(String name, GBActionType gbActionType, String description,
                              String sourceTypeName, String targetTypeName) {
        super(name, gbActionType, description);
    }

    public ChangeTypeGBAction(GBAction gbAction, MyDataNode node, String sourceTypeName, String targetTypeName) {
        super(gbAction, node);
    }

    @Override
    public boolean matches(MyDataNode node) {
        return node.getType().equals(sourceTypeName);
    }

    @Override
    public void execute() {
        //TODO check names before adding mapping.
        //TODO get database type!!! (in Overview ;))
        mapper.addMapping(DatabaseType.POSTGRESQL, DatabaseType.MYSQL, sourceTypeName,
                targetTypeName);
    }

    public void setMapper(DefaultColumnTypeMapper mapper) {
        this.mapper = mapper;
    }

    public String getSourceTypeName() {
        return sourceTypeName;
    }

    public void setSourceTypeName(String sourceTypeName) {
        this.sourceTypeName = sourceTypeName;
    }

    public String getTargetTypeName() {
        return targetTypeName;
    }

    public void setTargetTypeName(String targetTypeName) {
        this.targetTypeName = targetTypeName;
    }

    public DatabaseType getSourceDBType() {
        return sourceDBType;
    }

    public void setSourceDBType(DatabaseType sourceDBType) {
        this.sourceDBType = sourceDBType;
    }

    public DatabaseType getTargetDBType() {
        return targetDBType;
    }

    public void setTargetDBType(DatabaseType targetDBType) {
        this.targetDBType = targetDBType;
    }
}
