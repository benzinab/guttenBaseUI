package de.akquinet.jbosscc.gbplugin.helper;

import javax.swing.*;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.LogRecord;

public class TextAreaHandler extends java.util.logging.Handler {

    private JTextArea textArea = new JTextArea(50, 50);

    @Override
    public void publish(final LogRecord record) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                StringWriter text = new StringWriter();
                PrintWriter out = new PrintWriter(text);
                out.println(textArea.getText());
                out.printf("[%s] [Thread-%d]: %s.%s -> %s", record.getLevel(),
                        record.getThreadID(), record.getSourceClassName(),
                        record.getSourceMethodName(), record.getMessage());
                textArea.setText(text.toString());
            }

        });
    }

    @Override
    public void flush() {

    }

    @Override
    public void close() throws SecurityException {

    }

    public JTextArea getTextArea() {
        return this.textArea;
    }

    //...
}