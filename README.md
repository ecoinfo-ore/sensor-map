# sensorMap ( v 0.9 )
### *Database to SensorThings Mapping Tool*

**Requirements :**

-    `JAVA 8`  

-    ` MAVEN 3.3.9 + `
   
-    `Postgres | mySql ( in progress ) `

-    `Docker`

---

#### 1. [Compile SensorMap Server](https://github.com/ecoinfo-ore/sensor-map/blob/master/01_compile.sh)  

```java
   ./01_compile.sh
```
###### *The command will launch the script [compile.sh](https://github.com/ecoinfo-ore/sensor-map/blob/master/sensor-map-server/compile.sh) that will compile the project [SensorMap-Server](https://github.com/ecoinfo-ore/sensor-map/tree/master/sensor-map-server/sensor-map)*
-------

#### 2. Deploy :

###### 2.1 [Sensor-Auth](https://github.com/ecoinfo-ore/sensor-map/blob/master/02_deploy_sensor_auth.sh) : Postgres DB used by sensorMap server to secure user access ( [Dockerized Postgres Database](https://hub.docker.com/repository/docker/rac021/sensor-auth) ) :

```java
   ./02_deploy_sensor_auth.sh
```

 - Schema :
 
         - Database Name                           :  sensorusers
         - Table    Name                           :  users
         - Postgres User Access ( LOGIN/PASSWORD ) :  postgres/postgres 
         - URL                                     :  jdbc:postgresql://localhost:2346/sensorusers
 
 - Note : **Passwords** in the table **users** are stored in **MD5 Hash** ( **admin/admin** , **public/public** , **stg01/stg01** )

 ![SensorMap-DB](https://user-images.githubusercontent.com/37690220/78401513-32ee3580-75f9-11ea-8c52-b5211d24d4a8.png)
    
    
###### 2.2 [Sensor_Infra](https://github.com/ecoinfo-ore/sensor-map/blob/master/03_deploy_infra.sh) : SensorThings ( Frost + Postgres Database ) + Gost ( Dashboard ) + SensorMap Server  ( [docker-compose.yaml](https://github.com/ecoinfo-ore/sensor-map/blob/master/sensorThings-impl/FROST/docker-compose.yaml) )

```java 
   ./03_deploy_infra.sh
```

###### 2.3 List Services :
 
    - SensorThings FROST Server   :  http://localhost:8181/FROST-Server 
    - SensorThings FROST DB       :  jdbc:postgresql://localhost:2345/sensorthings ( sensorthings/ChangeMe )
    
    - SensorMap-Server            :  http://localhost:8080/rest/resources (  Login/Pwd  :  admin/admin     )
    - SensorMap-DB Auth           :  jdbc:postgresql://localhost:2346/sensorusers  (  postgres/postgres    )
    
    - Gost         ( Dashboard )  :  http://localhost:8282/
    - SensorMap-Ui                :  Just launch the file << sensor-map-ui/index.html >> on browser 
        
    - PgAdmin4                    :  http://localhost:8383/  (   Email/Pwd : admin@admin.com / admin       )    
    
-------

### **3. U.I**

#### *3.1 [Frost Server](https://github.com/FraunhoferIOSB/FROST-Server)* : ( [Settings](https://github.com/FraunhoferIOSB/FROST-Server/blob/master/docs/settings.md) ) - ( [Documentation](https://github.com/FraunhoferIOSB/FROST-Server/tree/master/docs) ) - ( [context.xml](https://github.com/FraunhoferIOSB/FROST-Server/blob/master/FROST-Server.HTTP/src/main/webapp/META-INF/context.xml) )

<img width="500" alt="FROST_SERVER" src="https://user-images.githubusercontent.com/37690220/78457856-9cc81700-76ad-11ea-8af8-475b8227c4a9.jpg">

#### *3.2 GOST Dashboard*

![GOST](https://user-images.githubusercontent.com/37690220/78457327-b0717e80-76a9-11ea-863c-495550e9818e.jpg)

#### *3.3 SensorMap UI*

![sensorMap-Ui](https://user-images.githubusercontent.com/37690220/78455300-caa55f80-769d-11ea-81cf-f655738bfa2f.jpg)

-------

#### 4. [SensorThings UML Model](https://developers.sensorup.com/docs/#introduction) :

<img width="720" alt="SensorThings_API_Part_I_UML" src="https://user-images.githubusercontent.com/37690220/78402076-464dd080-75fa-11ea-857e-a8f4b8462349.png">

-------

#### 5. Improvement + Upcoming features

   - Migrate SensorMap-Ui + Gost Dashboeard    ( AngularJS ) to Web components 
   - Integrate Keycloak   ( for Authentication )
   - Secure SensorThings  ( FROST ) : Access   ( Keycloak ? ) Write ( Disabling DELETE, PUT PATCH ? )
   - More Configuration   ( SSL, BATCH ... )
   - Add Endpoint to automate playing multiple mapping files
   - Add Support MySQL 
   - Integrate @iot.id ( Supported by FROST Implementation )
   - Manage LOGS properly


