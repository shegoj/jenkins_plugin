package org.alertagility.plugins.jenkins;

import hudson.Extension;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Descriptor;
import hudson.model.TaskListener;
import hudson.model.listeners.RunListener;
import hudson.tasks.Publisher;

import java.util.Map;
import java.util.logging.Logger;

@Extension
@SuppressWarnings("rawtypes")
public class AlertAgilityListener extends RunListener<AbstractBuild> {

    private static final Logger logger = Logger.getLogger(AlertAgilityListener.class.getName());

    public AlertAgilityListener() {
        super(AbstractBuild.class);
    }

    @Override
    public void onCompleted(AbstractBuild r, TaskListener listener) {
        getNotifier(r.getProject()).jobcompleted(r, listener);
        super.onCompleted(r, listener);
    }

    @SuppressWarnings("unchecked")
    IAlertAgilityNotifier getNotifier(AbstractProject project) {
        Map<Descriptor<Publisher>, Publisher> map = project.getPublishersList().toMap();
        for (Publisher publisher : map.values()) {
            logger.info ("value: " + publisher );
            if (publisher instanceof AlertAgilityNotifier) {
                ((AlertAgilityNotifier)publisher).update();
                return new NotifierWrapper((AlertAgilityNotifier) publisher);
            }
        }
        logger.info ("no match found!!");
        return null;
    }

}
