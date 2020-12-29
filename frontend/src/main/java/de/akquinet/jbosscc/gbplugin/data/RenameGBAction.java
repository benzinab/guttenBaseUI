package de.akquinet.jbosscc.gbplugin.data;

import de.akquinet.jbosscc.gbplugin.data.nodes.MyDataNode;
import de.akquinet.jbosscc.gbplugin.data.nodes.RenameType;
import de.akquinet.jbosscc.gbplugin.mappers.ColumnRenameMapper;

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

    public RenameGBAction(RenameGBAction gbAction, MyDataNode node) {
        super(gbAction, node);
        regExp = gbAction.getRegExp();
        replace = gbAction.getReplace();
    }

    @Override
    public boolean matches(MyDataNode node){
        return node.getName().matches(regExp);
    }

    @Override
    public void execute(ColumnRenameMapper renameMapper) {
        renameMapper.addReplacement(getSource().getName(), replace);
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
