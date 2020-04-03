# sensor-map ( DEV )
### *Database to SensorThings Mapper Tool*

**Requirements :**

-    `JAVA 8`  

-    ` MAVEN 3.3.9 + `
   
-    `Postgres | mySql `

-    `Docker`

---

#### 1. Compile SensorMap Server 

```
   01_compile.sh
```

#### 2. Deploy :

###### 2.1 Postgres Database ( used by sensorMap server to secure user access ) :

```
   docker run --rm --name sensor-auth -p 2345:5432 rac021/sensor-auth 
```

 - Schema :
 
         - Database Name  :  sensorusers
         - Table Name     :  users
         - LOGIN/PASSWORD :  postgres/postgres
         - ALOG           :  Password Stored using MD5 Hash
 
 
 ![SensorMap-DB](https://user-images.githubusercontent.com/37690220/78401513-32ee3580-75f9-11ea-8c52-b5211d24d4a8.png)
    
    
###### 2.2 Deploy services : SensorThings ( Frost + Postgres Database ) + Gost ( Dashboard ) + Sensor-map

```  
   02_deploy.sh
```

###### 2.3 List Services :
 
         - SensorMap-Server            :  ---
         - SensorThings ( FROST     )  :  ---
         - Gost         ( Dashboard )  :  ---
         - SensorMap-Ui                :  ---   

-------

#### 3. SensorMap UI :

![sensorMap-Ui](https://user-images.githubusercontent.com/37690220/78403329-79915f00-75fc-11ea-8d14-c5c01fe3ef23.jpg)

-------

#### 4. SensorThings UML Model :

<img width="720" alt="SensorThings_API_Part_I_UML" src="https://user-images.githubusercontent.com/37690220/78402076-464dd080-75fa-11ea-857e-a8f4b8462349.png">

