package de.akquinet.jbosscc.gbplugin.utils;

import com.intellij.ui.JBColor;
import de.akquinet.jbosscc.gbplugin.ui.migrate.progressview.ProgressView;
import de.akquinet.jbosscc.guttenbase.utils.ScriptExecutorProgressIndicator;
import de.akquinet.jbosscc.guttenbase.utils.TimingProgressIndicator;
import de.akquinet.jbosscc.guttenbase.utils.Util;
import org.apache.log4j.Logger;

public class UIScriptExecutorProgressIndicator implements ScriptExecutorProgressIndicator
{
    private final ProgressView progressView;
    private final TimingProgressIndicator _timingDelegate = new TimingProgressIndicator();
    private final StringBuilder _text = new StringBuilder();
    private static final Logger LOG = Logger.getLogger(UIScriptExecutorProgressIndicator.class);

    public UIScriptExecutorProgressIndicator(ProgressView progressView)
    {
        this.progressView = progressView;
    }

    @Override
    public void initializeIndicator()
    {
        _timingDelegate.initializeIndicator();

        progressView.getTotalTimeElapsed().setText("");
        progressView.getStatementTimeElapsed().setText("");
        progressView.getStatus().setText("Initializing...");
    }

    @Override
    public void startProcess(final int totalNumberOfProcesses)
    {
        _timingDelegate.startProcess(totalNumberOfProcesses);

        progressView.getTotalBar().setValue(0);
        progressView.getTotalBar().setMinimum(0);
        progressView.getTotalBar().setMaximum(totalNumberOfProcesses);
        progressView.getStatus().setText("Process starting...");
    }

    @Override
    public void startExecution()
    {
        _timingDelegate.startExecution();
    }

    @Override
    public void endExecution(final int numberOfItems)
    {
        _timingDelegate.endExecution(numberOfItems);

        updateTimers();
    }

    @Override
    public void endProcess()
    {
        _timingDelegate.endProcess();

        progressView.getTotalBar().setValue(_timingDelegate.getItemCounter());
        progressView.getStatus().setText("Process ending...");
        updateTimers();
    }

    @Override
    public void warn(final String text)
    {
        _timingDelegate.warn(text);
        _text.append("WARNING: ").append(text).append("\n");
        updateMessages();
        LOG.warn(text);
    }

    @Override
    public void info(final String text)
    {
        _timingDelegate.info(text);
        _text.append("Info: ").append(text).append("\n");
        updateMessages();
        LOG.info(text);
    }

    @Override
    public void debug(final String text)
    {
        _timingDelegate.debug(text);
        _text.append("Debug: ").append(text).append("\n");
        updateMessages();
        LOG.debug(text);
    }

    @Override
    public void finalizeIndicator()
    {
        _timingDelegate.finalizeIndicator();
        progressView.getStatus().setText("Done!");
        progressView.getStatus().setForeground(JBColor.GREEN);
    }

    @Override
    public final void updateTimers()
    {
        progressView.getTotalTimeElapsed().setText(Util.formatTime(_timingDelegate.getElapsedTotalTime()));
        progressView.getStatementTimeElapsed().setText(Util.formatTime(_timingDelegate.getElapsedExecutionTime()));
    }

    private void updateMessages()
    {
        progressView.getLogArea().setText(_text.toString());
    }

    public TimingProgressIndicator getTimingDelegate() {
        return _timingDelegate;
    }
}
