package de.akquinet.jbosscc.gbplugin.utils;

import de.akquinet.jbosscc.guttenbase.utils.LoggingScriptExecutorProgressIndicator;
import de.akquinet.jbosscc.guttenbase.utils.ScriptExecutorProgressIndicator;
import de.akquinet.jbosscc.guttenbase.utils.TimingProgressIndicator;
import org.apache.log4j.Logger;

public class UIScriptExecutorProgressIndicator implements ScriptExecutorProgressIndicator{

    private static final Logger LOG = Logger.getLogger(LoggingScriptExecutorProgressIndicator.class);

    private final TimingProgressIndicator timingDelegate = new TimingProgressIndicator();

    @Override
    public void initializeIndicator()
    {

        timingDelegate.initializeIndicator();

    }

    @Override
    public void startProcess(final int numberOfTables)
    {
        timingDelegate.startProcess(numberOfTables);
    }

    @Override
    public void startExecution()
    {
        timingDelegate.startExecution();
    }

    @Override
    public void endExecution(final int totalCopiedRows)
    {
        timingDelegate.endExecution(totalCopiedRows);
    }

    @Override
    public void endProcess()
    {
        timingDelegate.endProcess();
    }

    @Override
    public void warn(final String text)
    {
        timingDelegate.warn(text);
        LOG.warn(text);
    }

    @Override
    public void info(final String text)
    {
        timingDelegate.info(text);
        LOG.info(text);
    }

    @Override
    public void debug(final String text)
    {
        timingDelegate.debug(text);
        LOG.debug(text);
    }

    @Override
    public void finalizeIndicator()
    {
        timingDelegate.finalizeIndicator();
    }

    @Override
    public void updateTimers()
    {
        throw new UnsupportedOperationException();
    }

    public TimingProgressIndicator getTimingDelegate() {
        return timingDelegate;
    }
}