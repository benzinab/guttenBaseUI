package de.akquinet.jbosscc.gbplugin.data;

import de.akquinet.jbosscc.gbplugin.data.nodes.MyDataNode;
import de.akquinet.jbosscc.gbplugin.data.nodes.RenameType;
import de.akquinet.jbosscc.gbplugin.mappers.ColumnRenameMapper;
import de.akquinet.jbosscc.guttenbase.hints.ColumnMapperHint;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnMapper;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;

public class RenameGBAction extends GBAction{

    private String regExp;

    private String replace;

    private RenameType renameType;


    public RenameGBAction(String name, String regExp, String replace, RenameType renameType) {
        super(name, GBActionType.COLUMN_RENAME_ACTION);
        this.regExp = regExp;
        this.replace = replace;
        this.renameType = renameType;
    }

    public RenameGBAction(String name, String description, String regExp, String replace, RenameType renameType) {
        super(name, GBActionType.COLUMN_RENAME_ACTION, description);
        this.regExp = regExp;
        this.replace = replace;
        this.renameType = renameType;
    }

    @Override
    public boolean matches(MyDataNode node){
        return true;
    }

    @Override
    public void execute(ConnectorRepository repository) {
        repository.addConnectorHint("target", new ColumnMapperHint() {
            @Override
            public ColumnMapper getValue() {
                return new ColumnRenameMapper()
                        .addReplacement(getSource().getName(), replace);
            }
        });
    }

    public String getRegExp() {
        return regExp;
    }

    public void setRegExp(String regExp) {
        this.regExp = regExp;
    }

    public String getReplace() {
        return replace;
    }

    public void setReplace(String replace) {
        this.replace = replace;
    }

    public RenameType getRenameType() {
        return renameType;
    }

    public void setRenameType(RenameType renameType) {
        this.renameType = renameType;
    }
}
