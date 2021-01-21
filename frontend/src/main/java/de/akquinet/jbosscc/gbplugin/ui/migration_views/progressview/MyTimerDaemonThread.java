package de.akquinet.jbosscc.gbplugin.ui.migration_views.progressview;

import de.akquinet.jbosscc.guttenbase.utils.ProgressIndicator;

public class MyTimerDaemonThread extends Thread
{
        private boolean _active = true;
        private final ProgressView progressView;
        private final ProgressIndicator[] _progressIndicators;

        public MyTimerDaemonThread(final ProgressView progressView, final ProgressIndicator... progressIndicators)
        {
            super("GB-Timer-Daemon");
            setDaemon(true);

            _progressIndicators = progressIndicators;
            this.progressView = progressView;
        }

        @Override
        public void run()
        {

            while (_active)
            {
                try
                {
                    Thread.sleep(800L);
                }
                catch (final InterruptedException ignored)
                {}

                for (final ProgressIndicator progressIndicator : _progressIndicators)
                {
                    progressIndicator.updateTimers();
                }
            }
        }

        public boolean isActive()
        {
            return _active;
        }

        public void setActive(final boolean active)
        {
            _active = active;
        }
}