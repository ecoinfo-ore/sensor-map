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

    - Schema 
    
    
    - Infos : Database Name  :
              Table Name     :
              LOGIN/PASSWORD : postgres/postgres
              ALOG           : password Hashed using MD5 

###### 2.2 Deploy services : SensorThings ( Frost + Postgres Database ) + Gost ( Dashboard ) + Sensor-map

```  
   02_deploy.sh
```


