package de.akquinet.jbosscc.gbplugin.mapping;

import de.akquinet.jbosscc.guttenbase.mapping.ColumnMapper;
import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author siraj
 */
public class ColumnRenameMapper extends Mapper implements ColumnMapper {
    private final Map<String, String> replacementsColumns = new HashMap<>();
    @Override
    public ColumnMapperResult map(ColumnMetaData source, TableMetaData targetTableMetaData) throws SQLException {

        final String defaultColumnName = source.getColumnName();

        final String columnName = replacementsColumns.containsKey(defaultColumnName)?
                "test": defaultColumnName;

        final ColumnMetaData columnMetaData2 = targetTableMetaData.getColumnMetaData(columnName);
        return new ColumnMapperResult(Arrays.asList(columnMetaData2));
    }

    @Override
    public String mapColumnName(ColumnMetaData source, TableMetaData targetTableMetaData) throws SQLException {
        if (source == null) {
            return "ERROR";
        }
        String result = source.getColumnName();
        final String columnName = replacementsColumns.get(result);

        if(columnName == null)
            return result;
        else
            return columnName;
    }

    @Override
    public ColumnRenameMapper addReplacement(final String sourceColumn, final String targetColumn) {
        replacementsColumns.put(sourceColumn, targetColumn);
        return this;
    }
}
