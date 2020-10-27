package de.akquinet.jbosscc.guttenBasePlugin;

import com.intellij.database.psi.DbDataSource;
import com.intellij.openapi.ui.DialogWrapper;
import com.sun.istack.Nullable;

import javax.swing.*;
import java.awt.*;

public class Dialog extends DialogWrapper {
    private DbDataSource dbDataSource;


        public Dialog(DbDataSource dbDataSource) {
            super(true); // use current window as parent
            init();
            setTitle("Migrate Database");
            this.dbDataSource = dbDataSource;


        }

        @Nullable
        @Override
        protected JComponent createCenterPanel() {
            //return new GuttenWindow(dbDataSource).getContent();
            return new JPanel();
        }


}
