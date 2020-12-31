package de.akquinet.jbosscc.gbplugin.helper;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

import javax.swing.*;

public class LoggingAppender extends AppenderSkeleton {

    private final JTextArea jTextA;

    public LoggingAppender(JTextArea jTextA) {
        this.jTextA = jTextA;
    }

    @Override
    protected void append(LoggingEvent loggingEvent) {

            jTextA.append(loggingEvent.getMessage().toString());
    }

    @Override
    public void close() {

    }

    @Override
    public boolean requiresLayout() {
        return false;
    }
}
