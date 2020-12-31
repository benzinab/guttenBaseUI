package de.akquinet.jbosscc.gbplugin.data.gbactions;

import de.akquinet.jbosscc.gbplugin.data.nodes.MyDataNode;
import de.akquinet.jbosscc.gbplugin.data.nodes.RenameType;
import de.akquinet.jbosscc.gbplugin.mapping.Mapper;

public class RenameGBAction extends GBAction{

    private String regExp;

    private String replace;

    private RenameType renameType;

    private Mapper mapper;


    public RenameGBAction(String name, String regExp, String replace, RenameType renameType) {
        super(name, GBActionType.RENAME);
        this.regExp = regExp;
        this.replace = replace;
        this.renameType = renameType;
    }

    public RenameGBAction(String name, String description, String regExp, String replace, RenameType renameType) {
        super(name, GBActionType.RENAME, description);
        this.regExp = regExp;
        this.replace = replace;
        this.renameType = renameType;
    }

    public RenameGBAction(RenameGBAction gbAction, MyDataNode node) {
        super(gbAction, node);
        regExp = gbAction.getRegExp();
        replace = gbAction.getReplace();
        renameType = gbAction.getRenameType();
    }

    @Override
    public boolean matches(MyDataNode node){
        return node.getName().matches(regExp);
    }

    @Override
    public void execute() {
        String result = replace;
        if (renameType != null) {
            switch (renameType) {
            case REPLACE:
                break;
            case ADD_PREFIX:
                result += getSource().getName();
                break;
            case ADD_SUFFIX:
                result = getSource().getName() + replace;
            }
        }
        mapper.addReplacement(getSource().getName(), result);
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

    public void setMapper(Mapper mapper) {
        this.mapper = mapper;
    }
}
