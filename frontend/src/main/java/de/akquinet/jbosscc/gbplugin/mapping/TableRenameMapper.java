package de.akquinet.jbosscc.gbplugin.mapping;

import de.akquinet.jbosscc.guttenbase.hints.CaseConversionMode;
import de.akquinet.jbosscc.guttenbase.mapping.TableMapper;
import de.akquinet.jbosscc.guttenbase.meta.DatabaseMetaData;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author siraj
 */
public class TableRenameMapper extends Mapper implements TableMapper {
    private final Map<String, String> replacementsTables = new HashMap<>();
    private final CaseConversionMode _caseConversionMode;
    private final boolean _addSchema;

    public TableRenameMapper(final CaseConversionMode caseConversionMode, final boolean addSchema) {
        assert caseConversionMode != null : "caseConversionMode != null";
        _caseConversionMode = caseConversionMode;
        _addSchema = addSchema;

        //addReplacement("offices", "tab_offices");
        //addReplacement("orders", "tab_orders");
        // addReplacement("orderdetails", "tab_ordersdetails");
    }

    public TableRenameMapper() {
        this(CaseConversionMode.NONE, true);
    }

    @Override
    public TableMetaData map(TableMetaData source, DatabaseMetaData targetDatabaseMetaData) throws SQLException {

        final String defaultTableName = _caseConversionMode.convert(source.getTableName());
        final String tableName = replacementsTables.containsKey(defaultTableName)?
                replacementsTables.get(defaultTableName): defaultTableName;

        return targetDatabaseMetaData.getTableMetaData(tableName);
    }

    @Override
    public String mapTableName(TableMetaData source, DatabaseMetaData targetDatabaseMetaData) throws SQLException {

        String result = _caseConversionMode.convert(source.getTableName());
        final String schema = targetDatabaseMetaData.getSchema();
        final String tableName = replacementsTables.get(result);

        if(tableName != null) {
            result = _caseConversionMode.convert(tableName);
        }

        if ("".equals(schema.trim())|| !_addSchema)  {
            return result;
        } else {
            return schema + "." + result;
        }
    }

    @Override
    public TableRenameMapper addReplacement(final String sourceTable, final String targetTable) {
        replacementsTables.put(sourceTable, targetTable);

        return this;
    }

    @Override
    public String fullyQualifiedTableName(TableMetaData source, DatabaseMetaData targetDatabaseMetaData) throws SQLException {

        //final String schema = targetDatabaseMetaData.getSchemaPrefix();
        //return schema + mapTableName(source, targetDatabaseMetaData);
        return mapTableName(source, targetDatabaseMetaData);
    }
}
