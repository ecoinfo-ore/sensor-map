# sensorMap ( v 0.9 )
### *Database to SensorThings Mapper Tool*

**Requirements :**

-    `JAVA 8`  

-    ` MAVEN 3.3.9 + `
   
-    `Postgres | mySql ( in progress ) `

-    `Docker`

---

#### 1. [Compile SensorMap Server](https://github.com/ecoinfo-ore/sensor-map/blob/master/01_compile.sh)  

```
   ./01_compile.sh
```
###### *The command will launch the script [compile.sh](https://github.com/ecoinfo-ore/sensor-map/blob/master/sensor-map-server/compile.sh) that will compile the project [SensorMap-Server](https://github.com/ecoinfo-ore/sensor-map/tree/master/sensor-map-server/sensor-map)*
-------

#### 2. Deploy :

###### 2.1 [Dockerized Postgres Database](https://hub.docker.com/repository/docker/rac021/sensor-auth) ( used by sensorMap server to secure user access ) :

```
   docker run --rm --name sensor-auth -p 2345:5432 rac021/sensor-auth 
```

 - Schema :
 
         - Database Name                           :  sensorusers
         - Table    Name                           :  users
         - Postgres User Access ( LOGIN/PASSWORD ) :  postgres/postgres 
 
 - Note : **Passwords** in the table **users** are stored in **MD5 Hash** ( **admin/admin** , **public/public** , **stg01/stg01** )

 ![SensorMap-DB](https://user-images.githubusercontent.com/37690220/78401513-32ee3580-75f9-11ea-8c52-b5211d24d4a8.png)
    
    
###### 2.2 [Deploy services](https://github.com/ecoinfo-ore/sensor-map/blob/master/02_deploy.sh) : [SensorThings ( Frost + Postgres Database ) + Gost ( Dashboard ) + SensorMap Server](https://github.com/ecoinfo-ore/sensor-map/blob/master/sensorThings-impl/FROST/docker-compose.yaml)

```  
   ./02_deploy.sh
```

###### 2.3 List Services :
 
     - SensorThings Server ( FROST     )  :  http://localhost:8181/FROST-Server 
     - SensorThings Postgres Database     :  jdbc:postgresql://localhost:2345/sensorthings ( postgres/postgres )
     - PgAdmin4                           :  http://localhost:8383/  (  Email/Pwd : admin@admin.com / admin    )
     - Gost         ( Dashboard )         :  http://localhost:8282/
     - SensorMap-Server                   :  http://localhost:8080/rest/resources (  Login/Pwd  :  admin/admin )
     - SensorMap-Ui                       :  Just launch the file << sensor-map-ui/index.html >> on browser 
         
-------

#### 3. SensorMap UI :

![sensorMap-Ui](https://user-images.githubusercontent.com/37690220/78403329-79915f00-75fc-11ea-8d14-c5c01fe3ef23.jpg)

-------

#### 4. [SensorThings UML Model](https://developers.sensorup.com/docs/#introduction) :

<img width="720" alt="SensorThings_API_Part_I_UML" src="https://user-images.githubusercontent.com/37690220/78402076-464dd080-75fa-11ea-857e-a8f4b8462349.png">

