package org.alertagility.plugins.jenkins;

public interface IAlertAgilityService {

    boolean publish(String status, String message);

    boolean publish(String status, String message, String incident);

}
