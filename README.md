# AlertAGility plugin for Jenkins

The project was based on the Slack plugin: https://github.com/giancarlogarcia/jenkins-victorops-plugin


# screenshots

![screengrab1](images/3.png?raw=true "screengrab1")
![screengrab1](images/4.png?raw=true "screengrab1")

# How to compile

1. Install maven on your machine and ensure its working correctly
2. To test with installing Jenkins, run  mvn hpi:run -Djetty.port=8090. This installs the plugin onn local Jenkins app
3. When ready run mvn package, this create .hpi in target/ directory
4. Place the plugin .hpi file in the {jenkins-home}/plugins/ directory
5. Restart Jenkins
6. Configure the global plugin settings: "Manage Jenkins" > "Configure System" > "AlertAgility Settings"
6. Add a routing key to your Jenkins job and **don't forget to add the AlertAgility post-build action**



# ToDo
1. Remove all hardcoded string in the Java code
2. Add AlertAgility icon to the config page
3. Add form validation to the config page
4. Improve interface look&feel
5. Improve the Readme file
     
