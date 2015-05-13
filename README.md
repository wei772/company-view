A project in a subject called "Team Management in Developing Information Systems" IDU3191.

To run the project with your own db configuration:
1) Go to WEB-INF folder
2) Add db-main-properties.xml
3) Configure it like db-default-properties.xml

And now to run the whole stuff:
mvn clean
mvn compile
mvn war:war
mvn jetty:run-war -Djetty.port=xxxx (not mandatory, default is 8080)