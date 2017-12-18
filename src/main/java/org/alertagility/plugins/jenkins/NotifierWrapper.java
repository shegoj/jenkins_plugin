package org.alertagility.plugins.jenkins;

import hudson.Util;
import hudson.model.Result;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.TaskListener;

import java.util.logging.Logger;

public class NotifierWrapper implements IAlertAgilityNotifier {

    private static final Logger logger = Logger.getLogger(NotifierWrapper.class.getName());

    AlertAgilityNotifier notifier;

    public NotifierWrapper(AlertAgilityNotifier notifier) {
        super();
        //logger.debug("NotifierWrapper Class instantiated");
        this.notifier = notifier;
    }

    private IAlertAgilityService getJenkinsSetting(AbstractBuild r) {
        AbstractProject<?, ?> project = r.getProject();
        String serverUrl = Util.fixEmpty(project.getProperty(AlertAgilityNotifier.AlertAgilityJobProperty.class).getServerUrl());
        String apiKey = Util.fixEmpty(project.getProperty(AlertAgilityNotifier.AlertAgilityJobProperty.class).getApiKey());
        String routingKey = Util.fixEmpty(project.getProperty(AlertAgilityNotifier.AlertAgilityJobProperty.class).getRoutingKey());
        return notifier.newIAlertAgilityService(serverUrl, apiKey, routingKey);
    }

    public void jobcompleted(AbstractBuild r, TaskListener listener) {
        AbstractProject<?, ?> project = r.getProject();
        //logger.debug("Project " + project.getName() + " running executing jobcompleted method");
        AlertAgilityNotifier.AlertAgilityJobProperty jobProperty = project.getProperty(AlertAgilityNotifier.AlertAgilityJobProperty.class);
        if (jobProperty == null) {
            logger.warning("Project " + project.getName() + " has no AlertAgility configuration.");
            return;
        }
        Result result = r.getResult();
        AbstractBuild<?, ?> previousBuild = project.getLastBuild();
        do {
            previousBuild = previousBuild.getPreviousCompletedBuild();
        } while (previousBuild != null && previousBuild.getResult() == Result.ABORTED);
        if ((result == Result.FAILURE && jobProperty.getNotifyFailure())
            || (result == Result.SUCCESS && jobProperty.getNotifySuccess())) {
            String status = getBuildStatus(r);
            String incident = getBuildIncident(r);
            listener.getLogger().println("Posting status '" + status + "' to AlertAgility  '" + incident + "'.");
            getJenkinsSetting(r).publish(status, getBuildMessage(r), incident);
        }
    }

    static String getBuildStatus(AbstractBuild r) {
        Result result = r.getResult();
        if (result == Result.SUCCESS) {
            return "INFO";
        } else if (result == Result.FAILURE) {
            return "CRITICAL";
        } else {
            return "WARNING";
        }
    }

    static String getBuildMessage(AbstractBuild r) {
        AbstractProject<?, ?> project = r.getProject();
        return project.getName() + " completed with status '" + r.getResult().toString() + "'";
    }

    static String getBuildIncident(AbstractBuild r) {
        AbstractProject<?, ?> project = r.getProject();
        return "Jenkins " + project.getLastBuild().toString();
    }

}
