# sensor-map ( DEV )
### *Database to SensorThings Mapper Tool*

**Requirements :**

-    `JAVA 8`  

-    ` MAVEN 3.3.9 + `
   
-    `Postgres | mySql `

-    `Docker`

---

#### 1. Compile Jaxy 
```
   01_compile.sh
```
#### 2. Deploys SensorThings ( FROST + GOST Dashboard ) + Jaxy W.S 

###### 2.1 Deploy Database (used by sensor-map to secure user access )

```
   docker run --rm --name sensor-auth -p 2345:5432 rac021/sensor-auth 
```
###### 2.2 Deploy SensorThings ( Frost ) + Gost Dashboard + Sensor-map

```  
   02_deploy.sh
```


