package org.alertagility.plugins.jenkins;

import hudson.model.AbstractBuild;
import hudson.model.TaskListener;

public interface IAlertAgilityNotifier {

    void jobcompleted (AbstractBuild r, TaskListener listener);

}
