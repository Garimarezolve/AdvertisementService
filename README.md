# ResolveChallange

#PreRequesties

1.) You must have installed postgres database in your machine to run this project.
2.) You must have java8+ installed on your system.

#project-setup

1.) Create database as [geofence_db] in postgres.
2.) Update postgres username and password in application.properties file.
    spring.datasource.username=[${your_username}]
    spring.datasource.password=[${your_password}]


[There are total 2 micro services created under ResolveChallange]

#geofence-service 
[It is running on port 8080]
1.)This service basically creates new geofence by providing the lat,long and radius.
2.)It gives you the list of geofences available in the system.
3.) It delete/update the geofences in the system.

#advertising-service
[It is running on port 8090]

1.) This service create new advertising based on lat and lang.
2.) It updates/delete the advertising based on unique advertisingId.
3.) It gives you the list of advertsing whose geo(lat and long) are inside the geo fence radius.

# how to run services

1.) To start the application import the service in any of your favourite IDE.
2.) In the main application i.e for #geofence-service go to GeofenceServiceApplication.java & for advertising-service
    go to AdvertisementServiceApplication.java and run the application.
    Or you can run it using below steps:-
       step-1 : mvn clean install  (it will create a jar inside your targer folder)
       step-2 : Go to target folder of each service and run [java -jar service-name.jar]

# How to access service document 

To access the swagger UI, go to http://localhost:${port}/swagger-ui/index.html


#External dependency

Please ensure you have configured lambok in your IDE.

