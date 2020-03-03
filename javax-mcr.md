class: inverse, center, middle

# Microservice alkalmazás felépítése Spring Boot keretrendszerrel Docker környezetben

---

class: inverse, center, middle

# Bevezetés

---

## Referenciák

* Craig Walls: Spring in Action, Fifth Edition (Manning)
* Kevin Hoffman: Beyond the Twelve-Factor App (O'Reilly)
* [Spring Boot Reference Documentation](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)

---

class: inverse, center, middle

# Bevezetés a Spring Framework és Spring Boot használatába

---

## Spring Framework céljai

* Keretrendszer nagyvállalati alkalmazásfejlesztésre
* Keretrendszer: komponensek hívása, életciklusa
* Nagyvállalati alkalmazás: Java SE által nem támogatott tulajdonságok
    * Spring Framework nem magában ad választ, hanem integrál egy vagy több megoldást

---

class: split-50

## Nagyvállalati alkalmazás

* Komponensek életciklus kezelése és kapcsolatok
* Távoli elérés
* Többszálúság
* Perzisztencia
* Tranzakció-kezelés
* Aszinkron üzenetkezelés
* Ütemezés
* Integráció
* Auditálhatóság
* Konfigurálhatóság
* Naplózás, monitorozás és beavatkozás
* Biztonság
* Tesztelhetőség

---

## Spring Framework <br /> tulajdonságai

* Komponensek, melyeket konténerként tartalmaz (konténer: Application Context)
* Konténer vezérli a komponensek életciklusát (pl. példányosítás)
* Konténer felügyeli a komponensek közötti kapcsolatot (Dependency Injection, Inversion of Control)
* Komponensek és kapcsolataik leírása több módon: XML, annotáció, Java kód
* Pehelysúlyú, non invasive (POJO komponensek)
* Aspektusorientált programozás támogatása
* 3rd party library-k integrálása az egységes modellbe
* Glue kód
* Boilerplate kódok eliminálása
* Fejlesztők az üzleti problémák <br /> megoldására koncentráljanak

---

## Application Context

![Application Context](images/spring-app-context.png)

---

## Háromrétegű webes alkalmazás

* Nem kizárólag erre, de ez a fő felhasználási terület
* Rétegek
    * Repository
    * Service
    * Controller
* Hangsúlyos része a Spring MVC webes alkalmazások írására, HTTP felé egy absztrakció
* HTTP kezelését web konténerre bízza, <br /> pl. Tomcat, Jetty, stb.

---

## Rétegek

![Rétegek](images/spring-app-context-layers.png)

---

## Spring Boot

* Autoconfiguration: classpath-on lévő osztályok, 3rd party library-k, környezeti változók és
  egyéb körülmények alapján komponensek automatikus létrehozása és konfigurálása
* Intelligens alapértékek, convention over configuration, konfiguráció nélkül is működjön
    * Saját konfiguráció írása csak akkor, ha az alapértelmezettől el szeretnénk térni
    * Automatically, automagically
* Self-contained: az alkalmazás tartalmazza a web konténert is (pl. Tomcat)
* Nagyvállalati üzemeltethetőség: Actuatorok
    * Pl. monitorozás, konfiguráció, beavatkozás, naplózás állítása, stb.
* Gyors kezdés: Spring Initializr [https://start.spring.io/](https://start.spring.io/)
* Starter projektek: függőségek, előre beállított verziószámokkal (tesztelt)

---

## Könyvtárstruktúra

```
├── .gitignore
├── .mvn
├── HELP.md
├── mvnw
├── mvnw.cmd
├── pom.xml
└── src
    ├── main
    │   ├── java
    │   │   └── training
    │   │       └── employees
    │   │           └── EmployeesApplication.java
    │   └── resources
    │       ├── application.properties
    │       ├── static
    │       └── templates
    └── test
        └── java
            └── training
                └── employees
                    └── EmployeesApplicationTests.java
```

---

## pom.xml

* Parent: `org.springframework.boot:spring-boot-starter-parent`
    * Innen öröklődnek a függőségek, verziókkal együtt
* Starter: `org.springframework.boot:spring-boot-starter-web`
    * Jackson, Tomcat, Hibernate Validator
* Teszt támogatás: `org.springframework.boot:spring-boot-starter-test`
    * JUnit 5, Mockito, AssertJ, Hamcrest, [XMLUnit](https://www.xmlunit.org/), [JSONassert](https://github.com/skyscreamer/JSONassert), [JsonPath](https://github.com/json-path/JsonPath)

---

class: inverse, center, middle

# Spring Beanek

---

## Spring Beanek

* Spring bean: tud róla a Spring konténer
* Spring példányosítja
* Spring állítja be a függőségeit
* POJO
* Rétegekbe rendezve
* Alapértelmezetten singleton, egy példányban jön létre

---

## Application

* Alkalmazás belépési pontja `main()` metódussal
* `@SpringBootApplication`:
    * `@EnableAutoConfiguration`: autoconfiguration bekapcsolása
    * `@SpringBootConfiguration`: `@Configuration`: maga az osztály is tartalmazhasson további konfigurációkat
    * `@ComponentScan`: `@Component`, `@Repository`, `@Service`, `@Controller`
    annotációval ellátott

```java
@SpringBootApplication
public class EmployeesApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmployeesApplication.class, args);
	}

}
```

---

## Controller komponensek

* Spring MVC része
* Felhasználóhoz legközelebb lévő réteg, felelős a felhasználóval való
  kapcsolattartásért
    * Adatmegjelenítés és adatbekérés
* POJO
* Annotációk erős használata
* Nem feltétlenül van Servlet API függősége
* Metódusok neve, paraméterezése flexibilis
* Gyakran REST végpontok kialakítására használjuk

---

## Annotációk

* `@Controller`: megtalálja a component scan, Spring MVC felismeri
* `@RequestMapping` milyen URL-en hallgat
    * Ant-szerű megadási mód (pl. `/admin/*.html`)
    * Megadható a HTTP metódus a `method` paraméterrel
* `@ResponseBody` visszatérési értékét azonnal a HTTP válaszba kell írni (valamilyen szerializáció után)

---

## Controller

```java
@Controller
public class EmployeesController {

    @RequestMapping("/")
    @ResponseBody
    public String helloWorld() {
        return "Hello World!";
    }
}
```

---

## Service

```java
@Service
public class EmployeesService {

    public String helloWorld() {
        return "Hello World at " + LocalDateTime.now();
    }
}
```

---

## Kapcsolatok

* Dependency injection
* Definiáljuk a függőséget, a konténer állítja be
* Függőségek definiálása:
    * Attribútum
    * Konstruktor
    * Metódus
* Legjobb gyakorlat: kötelező függőség konstruktorban
* Ha csak egy konstruktor, automatikusan megtörténik a dependency injection
* Egyéb esetben `@Autowired` annotáció

---

## Függőség a controllerben

```java
@Controller
public class EmployeesController {

    public EmployeesService employeesService;

    public EmployeesController(EmployeesService employeesService) {
        this.employeesService = employeesService;
    }

    @RequestMapping("/")
    @ResponseBody
    public String helloWorld() {
        return employeesService.helloWorld();
    }
}
```

---

class: inverse, center, middle

# Konfiguráció Javaban

---

## Java konfiguráció

* Ekkor nem kell a `@Service` annotáció: non-invasive
* `@Configuration` által ellátott osztályban, itt `EmployeesApplication`
* Legjobb gyakorlat: saját bean-ek component scannel, 3rd party library-k Java konfiggal
* Legjobb gyakorlat: rétegenként külön `@Configuration` annotációval ellátott osztály

```java
@Bean
public EmployeesService employeesService() {
	return new EmployeesService();
}
```

```java
@Service
public class EmployeesService {

    public String helloWorld() {
        return "Hello Dev Tools at " + LocalDateTime.now();
    }
}
```

---

class: inverse, center, middle

# Build és futtatás

---

## Build és futtatás parancssorból

* Build parancssorból Mavennel

```
mvn clean package
```

* `spring-boot-maven-plugin`: átcsomagolás, beágyazza a web konténert
* `employees-0.0.1-SNAPSHOT.jar.original` és `employees-0.0.1-SNAPSHOT.jar`

* Futtatás parancssorból Mavennel

```
mvn spring-boot:run
```

* Futtatás parancssorból

```
java -jar employees-0.0.1-SNAPSHOT.jar
```

---

class: inverse, center, middle

# Build és futtatás Gradle használatával

---

## Gradle használata

* A [start.spring.io](https://start.spring.io) támogatja a Gradle alapú projekt generálását
* A [io.spring.dependency-management](https://docs.spring.io/dependency-management-plugin/docs/current/reference/html/) lehetővé tesz Maven-szerű
  függőségkezelést - csak a verziókat deklarálja, de a függőséget explicit kell megadni
* A [org.springframework.boot](https://docs.spring.io/spring-boot/docs/current/gradle-plugin/reference/html/#reacting-to-other-plugins-dependency-management) képes a jar és war előállítására, figyelembe véve az előző plugint
* Generál Gradle wrappert is - ha nincs Gradle telepítve az adott gépre
* JUnit 5 függőség

---

## Gradle taskok

```
gradle build

gradle -i build

gradle bootRun
```

A `-i` kapcsoló INFO szintű naplózást állít

---

class: inverse, center, middle

# Unit és integrációs tesztek

---

## Unit tesztelés

* JUnit 5
* Non invasive - POJO-ként tesztelhető
* AssertJ, Hamcrest clsspath-on

```java
@Test
void testSayHello() {
    EmployeesService employeesService = new EmployeesService();
    assertThat(employeesService.sayHello()).startsWith("Hello");
}
```

---

## Unit tesztelés függőséggel

* Mockito classpath-on

```java
@ExtendWith({MockitoExtension.class})
public class EmployeesControllerTest {

    @Mock
    EmployeesService employeesService;

    @InjectMocks
    EmployeesController employeesController;

    @Test
    void testSayHello() {
        when(employeesService.sayHello()).thenReturn("Hello Mock");
        assertThat(employeesController.helloWorld()).startsWith("Hello Mock");
    }
}
```

---

## Integrációs tesztek

* Üres teszt a konfiguráció ellenőrzésére, elindul-e az application context
* `@SpringBootTest` annotáció: tartalmazza a `@ExtendWith({SpringExtension.class})` annotációt
* Tesztesetek között cache-eli az application contextet
* Bean-ek injektálhatóak az `@Autowired` annotációval

```java
@SpringBootTest
public class EmployeesControllerIT {

    @Autowired
    EmployeesController employeesController;

    @Test
    void testSayHello() {
        String message = employeesController.helloWorld();
        assertThat(message).startsWith("Hello");
    }
}
```

---

class: inverse, center, middle

# Developer Tools

---

## Developer Tools

* Felülírt property-k (Property Defaults): pl. template cache kikapcsolása
* Automatikus újraindítás
* Globális, felhasználónkénti beállítások (Global Settings)
* Távoli alkalmazások (Remote Applications)


```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-devtools</artifactId>
</dependency>
```

---

## Automatikus újraindítás

* Ha változik valami a classpath-on
* IDE függő
    * Eclipse-nél mentésre
    * IDEA-nál _Build / Rebuild Project_ (`Ctrl + F9` billentyűzetkombináció)
* Két osztálybetöltő, az egyik a saját kód, másik a függőségek - változás esetén csak az elsőt tölti újra, függőség változása esetén manuálisan kell újraindítani
* Újraindítja a web konténert is

---

## LiveReload

* Böngésző plugin szükséges hozzá
* A Spring Boot elindít egy LiveReload szervert
* LiveReload plugin felépít egy WebSocket kapcsolatot
* Ha változik valami, újratöltés van
* Pl. statikus állomány esetén (`src/main/resources/static/*.html`)
* IDE függő
    * Eclipse-nél mentésre
    * IDEA-nál _Build / Rebuild Project_ (`Ctrl + F9` billentyűzetkombináció)

---

## Global settings

* `$HOME/.config/spring-boot` könyvtárban pl. `spring-boot-devtools.properties` állomány

---

## Remote Applications

* El lehet indítani becsomagolt alkalmazáson is a DevToolst (`spring-boot-maven-plugin` konfigurálásával)
* Remote Client Applicationt kell elindítani, mely lokális gépen indul, és csatlakozik a távoli alkalmazáshoz
* Remote Update: ha valami változik a classpath-on, feltölti
* Ne használjuk éles környezetben

---

class: inverse, center, middle

# Twelve factor app

---

## Twelve-factor app

* [Twelve-factor app](https://12factor.net/) egy manifesztó, metodológia felhőbe telepíthető alkalmazások fejlesztésére
* Heroku platform fejlesztőinek ajánlása
* Előtérben a cloud, PaaS, continuous deployment
* PaaS: elrejti az infrastruktúra részleteit
    * Pl. Google App Engine, Redhat Open Shift, Pivotal Cloud Foundry, Heroku, AppHarbor, Amazon AWS, stb.

---

## Cloud native

* Jelző olyan szervezetekre, melyek képesek az automatizálás előnyeit kihasználva gyorsabban megbízható és skálázható alkalmazásokat szállítani
* Pivotal, többek között a Spring mögött álló cég
* Előtérben a continuous delivery, DevOps, microservices
* Alkalmazás jellemzői
    * PaaS-on fut (cloud)
    * Elastic: automatikus horizontális skálázódás

---

## Twelve-factor app ajánlások

* Verziókezelés: "One codebase tracked in revision control, many deploys"
* Függőségek: "Explicitly declare and isolate dependencies"
* Konfiguráció: "Store config in the environment"
* Háttérszolgáltatások: "Treat backing services as attached resources"
* Build, release, futtatás: "Strictly separate build and run stages"
* Folyamatok: "Execute the app as one or more stateless processes"
* Port hozzárendelés: "Export services via port binding"
* Párhuzamosság: "Scale out via the process model"
* Disposability: "Maximize robustness with fast startup and graceful shutdown"
* Éles és fejlesztői környezet hasonlósága: "Keep development, staging, and production as similar as possible"
* Naplózás: "Treat logs as event streams"
* Felügyeleti folyamatok: "Run admin/management tasks as one-off processes"

---

## Beyond the Twelve-Factor App

* One Codebase, One Application
* API first
* Dependency Management
* Design, Build, Release, Run
* Configuration, Credentials and Code
* Logs
* Disposability
* Backing services
* Environment Parity
* Administrative Processes
* Port Binding
* Stateless Processes
* Concurrency
* Telemetry
* Authentication and Authorization

---

class: inverse, center, middle

# Bevezetés a Docker használatába

---

## Docker

* Operációs rendszer szintű virtualizáció
* Jól elkülönített környezetek, saját fájlrendszerrel és telepített szoftverekkel
* Jól meghatározott módon kommunikálhatnak egymással
* Kevésbé erőforrásigényes, mint a virtualizáció

---

## Docker

<img src="images/container-what-is-container.png" alt="Docker" width="500" />

---

## Docker felhasználási módja

* Saját fejlesztői környezetben reprodukálható erőforrások
  * Adatbázis (relációs/NoSQL), cache, kapcsolódó rendszerek (kifejezettem microservice környezetben)
* Saját fejlesztői környezettől való izoláció
* Docker image tartalmazza a teljes környezetet, függőségeket is
* Portabilitás (különböző környezeten futattható, pl. saját gép, privát vagy publikus felhő)

---

## További Docker komponensek

* Docker Hub - publikus szolgáltatás image-ek megosztására
* Docker Compose - több konténer egyidejű kezelése
* Docker Swarm - natív cluster támogatás
* Docker Machine - távoli Docker környezetek üzemeltetéséhez

---

## Docker fogalmak

![Image és container](images/docker-containers.jpg)

---

## Docker konténerek

```
docker version
docker run hello-world
docker run -p 8080:80 nginx
docker run -d -p 8080:80 nginx
docker ps
docker stop 517e15770697
docker run -d -p 8080:80 --name nginx nginx
docker stop nginx
docker ps -a
docker start nginx
docker logs -f nginx
docker stop nginx
docker rm nginx
```

---

## Műveletek image-ekkel

```
docker images
docker rmi nginx
```

---

## Linux elindítása, bejelentkezés

```
docker run  --name myubuntu -d ubuntu tail -f /dev/null
docker exec -it myubuntu bash
```

---

class: inverse, center, middle

# Java alkalmazások Dockerrel

---

## Java telepítése Ubuntura

```
apt-get update
apt-get install wget
wget -qO - https://adoptopenjdk.jfrog.io/adoptopenjdk/api/gpg/key/public | apt-key add -
apt-get install software-properties-common
add-apt-repository --yes https://adoptopenjdk.jfrog.io/adoptopenjdk/deb/
apt-get update
apt-get install adoptopenjdk-13-hotspot
```

---

## Saját image összeállítása

`Dockerfile`

```
FROM adoptopenjdk:13-jre-hotspot
RUN mkdir /opt/app
COPY target/employees-0.0.1-SNAPSHOT.jar /opt/app/employees.jar
CMD ["java", "-jar", "/opt/app/employees.jar"]
```

```
docker build -t employees .
docker run -d -p 8080:8080 employees
```

---

## docker-maven-plugin

* Fabric8
* Alternatíva: Spotify dockerfile-maven, Google [JIB Maven plugin](https://github.com/GoogleContainerTools/jib)

---

## Plugin

```xml
<plugin>
    <groupId>io.fabric8</groupId>
    <artifactId>docker-maven-plugin</artifactId>
    <version>0.32.0</version>
    <!-- ... -->    
</plugin>
```

---

## Plugin konfiguráció

```xml
<configuration>
  <verbose>true</verbose>
    <images>
        <image>
            <name>vicziani/employees</name>
            <build>
                <dockerFileDir>${project.basedir}/src/main/docker/</dockerFileDir>
                <assembly>
                    <descriptorRef>artifact</descriptorRef>
                </assembly>
                <tags>
                    <tag>latest</tag>
                    <tag>${project.version}</tag>
                </tags>
            </build>
            <run>
                <ports>8080:8080</ports>
            </run>
        </image>
    </images>
</configuration>
```

---

## Dockerfile

```
FROM adoptopenjdk:13-jre-hotspot
RUN mkdir /opt/app
ADD maven/${project.artifactId}-${project.version}.jar /opt/app/employees.jar
CMD ["java", "-jar", "/opt/app/employees.jar"]
```

Property placeholder

---

## Parancsok

```
mvn package docker:build
mvn docker:start
mvn docker:stop
```

A `docker:stop` törli is a konténert

---

## 12Factor hivatkozás: <br /> Disposability

* Nagyon gyorsan induljanak és álljanak le
* Graceful shutdown
* Ne legyen inkonzisztens adat
* Batch folyamatoknál: megszakíthatóvá, újraindíthatóvá (reentrant)
    * Tranzakciókezeléssel
    * Idempotencia


---

class: inverse, center, middle

# Docker layers

---

## Layers

![Docker layers](docker-layers.png)

`docker image inspect employees`

---

## Legjobb gyakorlat

* Külön változó részeket külön layerbe tenni
* Operációs rendszer, JDK, libraries, alkalmazás saját fejlesztésű része külön layerbe kerüljön

---

## Manuálisan

* Build közben megoldani, hogy a spring-boot-maven-plugin által összecsomagolt jar fájlt ki kell csomagolni
  * `BOOT-INF/lib` - függőségek
  * `META-INF` - leíró állományok
  * `BOOT-INF/classes` - alkalmazás saját fájljai
* Odamásolni az image-be a könyvtárokat

```
FROM adoptopenjdk:13-jre-hotspot
ARG DEPENDENCY=target/dependency
COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY ${DEPENDENCY}/META-INF /app/META-INF
COPY ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java","-cp","app:app/lib/*","training.employees.EmployeesApplication"]
```

---

## Spring támogatás

* Spring 2.3.0.M2-től (Milestone esetén szükség van a spring-milestones repo-ra)
    * [Bejelentés](https://spring.io/blog/2020/01/27/creating-docker-images-with-spring-boot-2-3-0-m1)
* Layered JAR-s
* Buildpacks

---

## Layered JAR-s

* A JAR felépítése legyen layered
* Ki kell csomagolni
* Létrehozni a Docker image-t

---

## Layered JAR

```xml
<plugin>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-maven-plugin</artifactId>
    <configuration>
    	<layout>LAYERED_JAR</layout>
    </configuration>
</plugin>
```

---

## Kicsomagolás

```
java -Djarmode=layertools -jar target/employees-0.0.1-SNAPSHOT.jar list

java -Djarmode=layertools -jar target/employees-0.0.1-SNAPSHOT.jar extract
```

---

## Dockerfile

```
FROM adoptopenjdk:13-jre-hotspot as builder
WORKDIR application
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} application.jar
RUN java -Djarmode=layertools -jar application.jar extract

FROM adoptopenjdk:13-jre-hotspot
WORKDIR application
COPY --from=builder application/dependencies/ ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/resources/ ./
COPY --from=builder application/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
```

---

## Buildpacks

* Dockerfile-hoz képest magasabb absztrakciós szint (Cloud Foundry vagy Heroku)
* Image készítése közvetlen Maven vagy Grade-ből
* Alapesetben Java 11, spring-boot-maven-plugin konfigurálandó `BP_JAVA_VERSION` értéke `13.0.2`

```
mvn spring-boot:build-image
docker run -d -p 8080:8080 --name employees employees:0.0.1-SNAPSHOT
docker logs -f employees
```

---

## 12Factor hivatkozás: <br /> Dependencies

* Az alkalmazás nem függhet az őt futtató környezetre telepített semmilyen csomagtól
* Függőségeket explicit deklarálni kell
* Nem a függőségek közé soroljuk a háttérszolgáltatásokat, mint pl. adatbázis, mail szerver, cache szerver, stb.
* Docker és Maven/Gradle segít
* Egybe kell csomagolni a függőségekkel, hiszen a futtató környezetben szükség van rá
* Függőségek ritkábban változnak: Docker layers
* Vigyázni az ismételhetőségre: ne használjunk intervallumokat!

---

class: inverse, center, middle

# Feltöltés Git repository-ba

---

## Feltöltés Git repository-ba

* Új GitHub repository létrehozás a webes felületen

```
git init
git add .
git commit -m "First commit"
git remote add origin https://github.com/username/employees.git
git push -u origin master
```

---

## 12Factor hivatkozás: <br /> One Codebase, One Application

* Egy alkalmazás, egy repository
* A többi függőségként definiálandó
* Gyakori megsértés:
    * Modularizált fejlesztésnél tűnhet ez jó ötletnek a modulokat külön repository-ban tartani: nagyon megbonyolítja a buildet
    * Külön repository, de ugyanazon üzleti domainen dolgozó különböző alkalmazás darabok
    * Egy repository, különböző alkalmazások
* A különböző környezetekre telepített példányoknál alapvető igény, hogy tudjuk, hogy mely verzióból készült (felületen, logban látható legyen)

---

## Alkalmazások és csapatok <br /> kapcsolata

* Figyelni a Conway törvényre:  "azok a szervezetek, amelyek rendszereket terveznek, ... kénytelenek olyan terveket készíteni, amelyek saját kommunikációs struktúrájuk másolatai"
* Egy codebase, több team - ellentmond a microservice elképzelésnek
* Lehetséges megosztás:
    * Library - függőségként felvenni
    * Microservice

---

class: inverse, center, middle

# REST webszolgáltatások GET művelet

---

## RESTful webszolgáltatások tulajdonságai

* Roy Fielding: Architectural Styles and the Design of Network-based Software Architectures, 2000
* Representational state transfer
* Alkalmazás erőforrások gyűjteménye, melyeken CRUD műveleteket lehet végezni
* HTTP protokoll erőteljes használata: URI, metódusok, státuszkódok
* JSON formátum használata
* Egyszerűség, skálázhatóság, platformfüggetlenség

---

## Annotációk

* `@RestController`, mintha minden metóduson `@ResponseBody` annotáció
    * Alapértelmezetten JSON formátumba
* `@RequestMapping` annotation helyett `@GetMapping`, `@PostMapping`, stb.

---

## Controller

```java
@RestController
@RequestMapping("/api/employees")
public class EmployeesController {

    public EmployeesService employeesService;

    public EmployeesController(EmployeesService employeesService) {
        this.employeesService = employeesService;
    }

    @GetMapping
    public List<EmployeeDto> listEmployees() {
        return employeesService.listEmployees();
    }
}
```

---

## Architektúra

![Architektúra](images/rest-architecture.png)

---

## Lombok

* Boilerplate kódok generálására, pl. getter/setter, konstruktor, `toString()`, equals/hashcode, logger, stb.
* Annotation processor
* IntelliJ IDEA támogatás: plugin és _Enable annotation processing_
* `@Data` annotáció
    * `@ToString`, `@EqualsAndHashCode`, `@Getter` minden attribútumon, `@Setter` nem final attribútumon és `@RequiredArgsConstructor`
* `@NoArgsConstructor`

---

## Példa a Lombok használatára

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    private long id;

    private String name;

    public Employee(String name) {
        this.name = name;
    }
}
```

---

## ModelMapper

* Object mapping
* Hasonló struktúrájú osztályú példányok konvertálására (pl. entitások és DTO-k között)
* Reflection alapú, intelligens alapértékekkel
* Fluent mapping API speciális esetek kezelésére

---

## Példa a ModelMapper <br /> használatára

```java
Employee entity = // load
EmployeeDto dto = modelMapper.map(e, EmployeeDto.class);

List<EmployeeDto> dtos = employees.stream()
  .map(entity -> modelMapper.map(entity, EmployeeDto.class))
  .collect(Collectors.toList());
```

---

## URL paraméterek kezelése

* `@RequestParam` annotációval
* Kötelező, kivéve a `required = "false"` attribútum megadásakor
* Automatikus típuskonverzió


```java
public List<EmployeeDto> listEmployees(Optional<String> prefix) {
       return employeesService.listEmployees(prefix);
}
```

Elérhető a `/api/employees?prefix=Jack` címen

---

## URL részletek kezelése

* Osztályon lévő `@RequestMapping` és `@GetMapping` összeadódik

```java
@GetMapping("/{id}")
public EmployeeDto findEmployeeById(@PathVariable("id") long id) {
    return employeesService.findEmployeeById(id);
}
```

Elérhető a `/api/employees/1` címen

---

## 12Factor hivatkozás: API first

* Contract first alapjain
* Laza csatolás
* Webes és mobil GUI és az üzleti logika is ide tartozik
* Dokumentálva és tesztelve legyen
* (API Blueprint)[https://apiblueprint.org/]: Markdown alapú formátum API dokumentálására
* OpenAPI, Swagger

---

## 12Factor hivatkozás: <br /> Stateless processes

* Csak egy kérés időtartamáig van állapot
  * Nem baj, ha elveszik
  * Nem kell cluster-ezni
  * Kevesebb erőforrás
  * Nincs párhuzamossági probléma
* Kérések közötti állapot: backing services
* Cache: inkább backing service, ne nőjön szignifikánsan az alkalmazás memóriaigénye
* Shared nothing

---

## Konkurrencia

* Ha állapotmentesen dolgozunk, nem okoz problémát
* Horizontális skálázás
* Backing service szintre kerül

---
class: inverse, center, middle

# REST webszolgáltatások POST és DELETE művelet

---

## POST és PUT művelet

* PUT idempotens

---

## Controller POST művelettel

* `@RequestBody` annotáció - deszerializáció, alapból JSON-ből Jacksonnel

```java
@PostMapping
public EmployeeDto createEmployee(@RequestBody CreateEmployeeCommand command) {
    return employeesService.createEmployee(command);
}

@PostMapping("/{id}")
public EmployeeDto updateEmployee(@PathVariable("id") long id,
        @RequestBody UpdateEmployeeCommand command) {
    return employeesService.updateEmployee(id, command);
}
```

---

## Controller DELETE művelettel

```java
@DeleteMapping("/{id}")
public void deleteEmployee(@PathVariable("id") long id) {
    employeesService.deleteEmployee(id);
}
```

---

class: inverse, center, middle

# Státuszkódok és hibakezelés

---

## Státuszkód állítása controller metódusból

* `ResponseEntity` visszatérési típus: státuszkód, header, body, stb.

```java
@GetMapping("/{id}")
public ResponseEntity findEmployeeById(@PathVariable("id") long id) {
    try {
        return ResponseEntity.ok(employeesService.findEmployeeById(id));
    }
    catch (IllegalArgumentException iae) {
        return ResponseEntity.notFound().build();
    }
}
```

---

## 201 - CREATED státuszkód

```java
@PostMapping
@ResponseStatus(HttpStatus.CREATED)
public EmployeeDto createEmployee(@RequestBody CreateEmployeeCommand command) {
    return employeesService.createEmployee(command);
}
```

---

## 204 - NO CONTENT státuszkód

```java
@DeleteMapping("/{id}")
public ResponseEntity deleteEmployee(@PathVariable("id") long id) {
    employeesService.deleteEmployee(id);
    return ResponseEntity.noContent().build();
}
```

---

## Hibakezelés

* Servlet szabvány szerint `web.xml` állományban
* Exceptionre tehető `@ResponseStatus` annotáció
* Globálisan `ExceptionResolver` osztályokkal
* `@ExceptionHandler` annotációval ellátott metódus a controllerben
* `@ControllerAdvice` annotációvan ellátott globális `@ExceptionHandler` annotációval ellátott metódus

---

## ExceptionHandler

```java
@ExceptionHandler({IllegalArgumentException.class})
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public void handleNotFound() {
    System.out.println("Employee not found");
}
```

---

class: inverse, center, middle

# Integrációs tesztelés

---

## Web réteg tesztelése

* Elindítható csak a Spring MVC réteg: `@SpringBootTest` helyett `@WebMvcTest` annotáció használata
* Service réteg mockolható Mockitoval, `@MockBean` annotációval
* `MockMvc` injektálható
    * Kérések összeállítására (path variable, paraméterek, header, stb.)
    * Válasz ellenőrzésére (státuszkód, header, tartalom)
    * Válasz naplózására
    * Válasz akár Stringként, JSON dokumentumként (jsonPath)
* Nem indít valódi konténert, a Servlet API-t mockolja
* JSON szerializáció

---

## Web réteg tesztelése példa

```java
@Test
void testListEmployees() throws Exception {
    when(employeesService.listEmployees(any())).thenReturn(List.of(
            new EmployeeDto(1L, "John Doe"),
            new EmployeeDto(2L, "Jane Doe")
    ));

    mockMvc.perform(get("/api/employees"))
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(jsonPath("$[0].name", equalTo("John Doe")));
}
```

---

## Teljes alkalmazás tesztelése <br /> konténer nélkül

* `@SpringBootTest` és `@AutoConfigureMockMvc` annotáció

```java
@Test
void testListEmployees() throws Exception {
    mockMvc.perform(get("/api/employees"))
            .andExpect(status().isOk())
            .andDo(print())
            .andExpect(jsonPath("$[0].name", equalTo("John Doe")));
}
```

---

## Teljes alkalmazás tesztelése <br /> konténerrel

* `@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)`
* Port `@LocalServerPort` annotációval injektálható
* Injektálható `TestRestTemplate` - url és port előre beállítva
* JSON szerializáció és deszerializáció

---

## Teljes alkalmazás tesztelése <br /> konténerrel példa

```java
@Test
void testListEmployees() {
    List<EmployeeDto> employees = restTemplate.getForObject("/api/employees", List.class);
    assertThat(employees)
            .extracting(EmployeeDto::getName)
            .containsExactly("John Doe", "Jane Doe");
}
```

---

class: inverse, center, middle

# Content negotiation

---

## Content negotiation

* Mechanizmus, mely lehetővé teszi a kliens számára, hogy az erőforrás megjelenítési
  formái közül válasszon, pl.
    * JSON vagy XML (`Accept` fejléc és Mime Type)
    * GIF vagy JPEG
    * Nyelv (`Accept-Language` fejléc alapján)

---

## Content negotiation Spring Boot támogatás

* Controllerben

```java
@RequestMapping(value = "/api/employees",
  produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
```

* `pom.xml`-ben

```xml
<dependency>
  <groupId>org.glassfish.jaxb</groupId>
  <artifactId>jaxb-runtime</artifactId>
</dependency>
```

* Dto-ban `@XmlRootElement`

---

## Lista esetén

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "employees")
@XmlAccessorType(XmlAccessType.FIELD)
public class EmployeesDto {

    @XmlElement(name = "employee")
    private List<EmployeeDto> employees;
}
```

---

class: inverse, center, middle

# Validáció

---

## Validáció

* Bean Validation 2.0 (JSR 380) támogatás
* Ne réteghez legyen kötve, hanem az adatot hordozó beanhez
* Attribútumokra annotáció
* Beépített annotációk
* Saját annotáció implementálható
* Megadható metódus paraméterekre és visszatérési értékre is

---

## Beépített annotációk

* `@AssertFalse`, `@AssertTrue`
* `@Null`, `@NotNull`
* `@Size`
* `@Max`, `@Min`, `@Positive`, `@PositiveOrZero`, `@Negative`, `@NegativeOrZero`
* `@DecimalMax`, `@DecimalMin`
* `@Digits`
* `@Future`, `@Past`, `@PastOrPresent`, `@FutureOrPresent`
* `@Pattern`
* `@Email`
* `@NotEmpty`, `@NotBlank`

---

## Validáció controlleren

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateEmployeeCommand {

    @NotNull
    private String name;
}
```

```java
@PostMapping
public EmployeeDto createEmployee(@Valid @RequestBody CreateEmployeeCommand command) {
    return employeesService.createEmployee(command);
}
```

---

class: inverse, center, middle

# Spring Boot konfiguráció

---

## Externalized Configuration

* Konfiguráció alkalmazáson kívül szervezése, hogy ugyanazon alkalmazás több környezetben is tudjon futni
* Spring `Environment` absztrakcióra épül, `PropertySource` implementációk egy hierarchiája,
  melyek különböző helyekről töltenek be property-ket
* Majdnem húsz forrása a property-knek, fontosabbak (az elől szereplők felülírják a később szereplőket)
    * Parancssori paraméterek
    * Operációs rendszer környezeti változók
    * `application.properties` állomány a jar fájlon kívül (`/config` könyvtár, vagy közvetlenül a jar mellett)
    * `application.properties` állomány a jar fájlon belül
* Az `application.properties` fájlban más property-kre lehet hivatkozni placeholderrel
* YAML formátum is használható

---

## Konfiguráció beolvasása

* Injektálható a `@Value` annotáció használatával
* Injektálható `Environment` példánytól lekérdezhető

```java
@Value("${employees.max-number}")
private int maxNumber;
```

```java
int maxNumber = Integer.parseInt(environment.getProperty("employees.max-number"));
```

---

## ConfigurationProperties

```java
@Component
@ConfigurationProperties(prefix = "employees")
@Data
public class EmployeesConfiguration {

    private int maxNumber;
}
```

* Használható a `@Validated` Spring annotáció, majd használható a Bean Validation
* A property-ket definiálni lehet a `META-INF/additional-spring-configuration-metadata.json` állományban, ekkor felismeri az IDE

---

## Előre definiált property-k

* Százas nagyságrendben
* [Spring Boot Reference Documentation - Appendix A: Common Application properties](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#common-application-properties)

---

## Port

* `server.port`

---

## 12Factor hivatkozás: Port binding

* Konfigurálható legyen a port, ahol elindul
* Két alkalmazás ne legyen telepítve ugyanarra a web konténerre, alkalmazásszerverre

---

## Profile-ok használata

* Alapvető cél: különböző környezetben különböző beanek példányosodnak
    * Egyszerre több profile is aktiválható
* Lehet a property-knek különböző értéket adni profile-onként
* Gyakran használják környezetek megnevezésére, pl. `dev`, `stage`, `prod`, stb.
    * Twelve-factor app szerint rossz minta, kerülni kell a környezetek nevesítését

---

## Konfiguráció Dockerrel

* Fabric8 docker-maven-plugin

```xml
<run>
	<env>
    <SERVER_PORT>8081</SERVER_PORT>
		<EMPLOYEES_MAX_NUMBER>5</EMPLOYEES_MAX_NUMBER>
	</env>
	<ports>8080:8081</ports>
</run>
```

---

## 12Factor hivatkozás: Configuration

* Környezetenként eltérő értékek
* Pl. backing service-ek elérhetőségei
* Ide tartoznak a jelszavak, titkos kulcsok, melyeket különös figyelemmel kell kezelni
* Konfigurációs paraméterek a környezet részét képezzék, és ne az alkalmazás részét
* Konfigurációs paraméterek környezeti változókból jöjjenek
* Kerüljük az alkalmazásban a környezetek nevesítését
* Nem kerülhetnek a kód mellé a verziókezelőbe (csak a fejlesztőkörnyezet default beállításai)
* Verziókezelve legyen, ki, mikor mit módosított
* Lásd még Spring Cloud Config

---

class: inverse, center, middle

# Spring Boot naplózás

---

## Naplózás

* Spring belül a Commons Loggingot használja
* Előre be van konfigurálva a Java Util Logging, Log4J2, és Logback
* Alapesetben konzolra ír
* Naplózás szintje, és fájlba írás is állítható az `application.properties` állományban

---

## Best practice

* SLF4J használata
* Lombok használata
* Paraméterezett üzenet

```java
private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(LogExample.class);
```

```java
@Slf4j
```

```
log.debug("Employee has created");
log.debug("Employee has been created with name {}", employee.getName());
```

---

## Konfiguráció

* `application.properties`: szint, fájl
* Használható logger library specifikus konfigurációs fájl (pl. `logback.xml`)
* Twelve-factor app: naplózás konzolra

```
logging.level.training = debug
```

---

## 12Factor hivatkozás: Naplózás

* Time ordered event stream
* Nem az alkalmazás feladata a napló irányítása a megfelelő helyre, vagy a napló tárolása, kezelése, archiválása, görgetése, stb.
* Írjon konzolra
* Központi szolgáltatás: pl. ELK, Splunk, hiszen az alkalmazás node-ok bármikor eltűnhetnek

---

class: inverse, center, middle

# Spring JdbcTemplate

---

## Spring JdbcTemplate

* JDBC túl bőbeszédű
* Elavult kivételkezelés
  * Egy osztály, üzenet alapján megkülönböztethető
  * Checked
* Boilerplate kódok eliminálására template-ek
* Adatbáziskezelés SQL-lel

---

## Architektúra

![Architektúra](images/db-architecture.png)

---

## JDBC használata

* `org.springframework.boot:spring-boot-starter-jdbc` függőség
* Embedded adatbázis támogatás, automatikus `DataSource` konfiguráció
    * Pl H2: `com.h2database:h2`
    * Developer Tools esetén  elérhető webes konzol a `/h2-console` címen
* Injektálható `JdbcTemplate`
* Service delegál a Repository felé

---

## Insert, update és delete

```java
jdbcTemplate.update("insert into employees(emp_name) values (?)");

jdbcTemplate.update("update employees set emp_name = ? where id = ?", name, id);

jdbcTemplate.update("delete from employees where id = ?", id);
```

Generált id lekérése:

```java
KeyHolder keyHolder = new GeneratedKeyHolder();

jdbcTemplate.update(
        con -> {
            PreparedStatement ps =
                    con.prepareStatement("insert into employees(emp_name) values (?)",
                            Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, employee.getName());
            return ps;
        }, keyHolder);

employee.setId(keyHolder.getKey().longValue());
```

---

## Select

```java
List<Employee> employees = jdbcTemplate.query("select id, emp_name from employees",
  new EmployeeRowMapper());

Employee employee = jdbcTemplate.queryForObject("select id, emp_name from employees where id = ?",
                    new EmployeeRowMapper(),
                    id);
```

```java
private static class EmployeeRowMapper implements RowMapper<Employee> {
        @Override
        public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
            long id = rs.getLong("id");
            String name = rs.getString("emp_name");
            Employee employee = new Employee(id, name);
            return employee;
        }
    }
```

---

## Séma inicializálás

```java
@Component
public class DbInitializer implements CommandLineRunner {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        jdbcTemplate.execute("create table employees " +
          "(id bigint auto_increment, emp_name varchar(255), constraint pk_employee primary key (id))");

        jdbcTemplate.execute("insert into employees(emp_name) values ('John Doe');");
        jdbcTemplate.execute("insert into employees(emp_name) values ('Jack Doe');");
    }
}
```

---

class: inverse, center, middle

# Spring Data JPA

---

## Spring Data JPA

* Egyszerűbbé teszi a perzisztens réteg implementálását
* Tipikusan CRUD műveletek támogatására, olyan gyakori igények megvalósításával, mint a rendezés és a lapozás
* Interfész alapján repository implementáció generálás
* Query by example
* Ismétlődő fejlesztési feladatok redukálása, *boilerplate* kódok csökkentése

---

## Spring Data JPA használatba <br /> vétele

* `org.springframework.boot:spring-boot-starter-data-jpa` függőség
* Entitás létrehozása
* `JpaRepository` kiterjesztése
* `@Transactional` alkalmazása a service rétegben
* `application.properties`

```
spring.jpa.show-sql=true
```

---

## JpaRepository

* `save(S)`, `saveAll(Iterable<S>)`, `saveAndFlush(S)`
* `findById(Long)`, `findOne(Example<S>)`, `findAll()` különböző paraméterezésű metódusai (lapozás, `Example`), `findAllById(Iterable<ID>)`
* `getOne(ID)` (nem `Optional` példánnyal tér vissza)
* `exists(Example<S>)`, `existsById(ID)`
* `count()`, `count(Example<S>)`
* `deleteById(ID)`, `delete(S)`, `deleteAll()` üres és `Iterable` paraméterezéssel, `deleteAllInBatch()`, `deleteInBatch(Iterable<S>)`
* `flush()`

---

## Entitás

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "emp_name")
    private String name;

    public Employee(String name) {
        this.name = name;
    }
}
```

---

## Repository

```java
public interface EmployeesRepository extends JpaRepository<Employee, Long> {

    @Query("select e from Employee e where upper(e.name) like :name")
    List<Employee> findAll(String name);

}
```

---

class: inverse, center, middle

# MariaDB

---

## MariaDB indítása Dockerrel

```
docker run \
  -d \
  -e MYSQL_DATABASE=employees \
  -e MYSQL_USER=employees \
  -e MYSQL_PASSWORD=employees \
  -e MYSQL_ALLOW_EMPTY_PASSWORD=yes \  
  -p 3306:3306 \
  --name employees-mariadb \
  mariadb
```

---

## Inicializálás

* Driver: `org.mariadb.jdbc:mariadb-java-client`
* `application.properties` konfiguráció

```
spring.datasource.url=jdbc:mariadb://localhost/employees
spring.datasource.username=employees
spring.datasource.password=employees

spring.jpa.hibernate.ddl-auto=create-drop
```

---

## 12Factor hivatkozás: Backing services

* Adatbázis (akár relációs, akár NoSQL), üzenetküldő middleware-ek, directory és email szerverek, elosztott cache, Big Data eszközök, stb.
* Microservice környezetben egy alkalmazás lehet egy másik alkalmazás backing service-e
* Automatizált telepítés
* Infrastructure as Code, Ansible, Chef, Puppet
* Eléréseik, autentikációs paraméterek környezeti paraméterként publikálódnak az alkalmazás felé
* Fájlrendszer nem tekinthető megfelelő háttérszolgáltatásnak
* Beágyazható háttérszolgáltatások
* Redeploy nélkül megoldható legyen a kapcsolódás
* Circuit breaker: ha nem működik a szolgáltatás, megszűnteti egy időre a hozzáférést

---

class: inverse, center, middle

# Integrációs tesztelés

---

## JPA repository tesztelése

* JPA repository-k tesztelésére
* `@DataJpaTest` annotáció, csak a repository réteget indítja el
    * Embedded adatbázis
    * Tesztbe injektálható: JPA repository,  `DataSource`, `JdbcTemplate`, `EntityManager`
* Minden teszt metódus saját tranzakcióban, végén rollback
* Service réteg már nem kerül elindításra
* Tesztelni:
    * Entitáson lévő annotációkat
    * Névkonvenció alapján generált metódusokat
    * Custom query

---

## DataJpaTest

```java
@DataJpaTest
public class EmployeesRepositoryIT {

    @Autowired
    EmployeesRepository employeesRepository;

    @Test
    void testPersist() {
        Employee employee = new Employee("John Doe");
        employeesRepository.save(employee);
        List<Employee> employees = employeesRepository.findAllByPrefix("%");
        assertThat(employees).extracting(Employee::getName).containsExactly("John Doe");
    }

}
```

---

## @SpringBootTest használata

* Teljes alkalmazás tesztelése
* Valós adatbázis szükséges hozzá, gondoskodni kell az elindításáról
* Séma inicializáció és adatfeltöltés szükséges

---

## Tesztek H2 adatbázisban

* `src\test\resources\application.properties` fájlban a teszteléshez használt DataSource

```
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.url=jdbc:h2:mem:db;DB_CLOSE_DELAY=-1
spring.datasource.username=sa
spring.datasource.password=sa
```

---

## Séma inicializáció

* `spring.jpa.hibernate.ddl-auto` `create-drop` alapesetben, teszt lefutása végén eldobja a sémát
    * `create`-re állítva megmaradnak a táblák és adatok
* Ha van `schema.sql` a classpath-on, azt futtatja le
* Flyway vagy Liquibase használata

---

## Adatfeltöltés

* `data.sql` a classpath-on
* `@Sql` annotáció használata a teszten
* Programozott módon
    * Teszt osztályban `@BeforeEach` vagy `@AfterEach` annotációkkal megjelölt metódusokban
    * Publikus API-n keresztül
    * Injektált controller, service, repository, stb. használatával
    * Közvetlen hozzáférés az adatbázishoz (pl. `JdbcTemplate`)

---

## Tesztek egymásra hatása

* Csak külön adatokon dolgozunk - nehéz lehet a kivitelezése
* Teszt eset maga előtt vagy után rendet tesz
* Állapot
    * Teljes séma törlése, séma inicializáció
    * Adatbázis import
    * Csak (bizonyos) táblák ürítése

---

class: inverse, center, middle

# Alkalmazás futtatása Dockerben MariaDB-vel

---

## Architektúra

![Alkalmazás futtatása Dockerben](images/docker-mysql-arch.png)

---

## Alkalmazás futtatása Dockerben

```
docker run \
    -d \    
    -e SPRING_DATASOURCE_URL=jdbc:mariadb://employees-mysql/employees \
    -e SPRING_DATASOURCE_USERNAME=employees \
    -e SPRING_DATASOURCE_PASSWORD=employees \
    -p 8080:8080 \
    --link employees-mysql:employees-mysql \
    --name employees \
    vicziani/employees
```

---

class: inverse, center, middle

# Alkalmazás futtatása Dockerben MariaDB-vel Fabric8 Docker Maven Pluginnel

---

## Adatbázis

```xml
<image>
	<name>mariadb</name>
	<alias>employees-mariadb</alias>
	<run>
		<env>
			<MYSQL_DATABASE>employees</MYSQL_DATABASE>
			<MYSQL_USER>employees</MYSQL_USER>
			<MYSQL_PASSWORD>employees</MYSQL_PASSWORD>
			<MYSQL_ALLOW_EMPTY_PASSWORD>yes</MYSQL_ALLOW_EMPTY_PASSWORD>
		</env>
		<ports>3306:3306</ports>
	</run>
</image>
```

---

## Wait

```
FROM adoptopenjdk:13-jre-hotspot
RUN  apt update \
     && apt-get install wget \
     && apt-get install -y netcat \
     && wget https://raw.githubusercontent.com/vishnubob/wait-for-it/master/wait-for-it.sh \
     && chmod +x ./wait-for-it.sh
RUN mkdir /opt/app
ADD maven/${project.artifactId}-${project.version}.jar /opt/app/employees.jar
CMD ["./wait-for-it.sh", "-t", "180", "employees-mariadb:3306", "--", "java", "-jar", "/opt/app/employees.jar"]
```

---

## Alkalmazás

```xml
<image>

  <!--- ... -->

	<run>
		<env>
			<SPRING_DATASOURCE_URL>jdbc:mariadb://employees-mariadb/employees</SPRING_DATASOURCE_URL>
		</env>
		<ports>8080:8080</ports>
		<links>
			<link>employees-mariadb:employees-mariadb</link>
		</links>
		<dependsOn>
			<container>employees-mariadb</container>
		</dependsOn>
	</run>
</image>
```

---

class: inverse, center, middle

# Teljes alkalmazás futtatása docker compose-zal

---

## docker-compose.yml

```yaml
version: '3'

services:
  employees-mysql:
    image: mariadb
    restart: always
    ports:
      - '3306:3306'
    environment:
      MYSQL_DATABASE: employees
      MYSQL_ALLOW_EMPTY_PASSWORD: 'yes' # aposztrófok nélkül boolean true-ként értelmezi
      MYSQL_USER: employees
      MYSQL_PASSWORD: employees

```

---

## docker-compose.yml folytatás

```yaml
  employees-app:
    image: vicziani/employees
    ports:
      - "8080:8080"
    restart: always
    depends_on:
      - employees-mysql
    environment:
      SPRING_DATASOURCE_URL: 'jdbc:mariadb://employees-mysql:3306/employees'
    command: ["./wait-for-it.sh", "-t", "120", "employees-mysql:3306", "--", "java", "-jar", "/opt/app/employees.jar"]
```

---

class: inverse, center, middle

# Integrációs tesztelés adatbázis előkészítéssel

---

## pom.xml

```xml
<profile>
	<id>startdb</id>
	<properties>
		<docker.filter>mariadb</docker.filter>
	</properties>
	<build>
		<plugins>
			<plugin>
				<groupId>io.fabric8</groupId>
				<artifactId>docker-maven-plugin</artifactId>
				<executions>
					<execution>
						<id>start</id>
						<phase>pre-integration-test</phase>
						<goals>
							<goal>start</goal>
						</goals>
					</execution>
					<execution>
						<id>stop</id>
						<phase>post-integration-test</phase>
						<goals>
							<goal>stop</goal>
						</goals>
					</execution>
				</executions>
			</plugin>
		</plugins>
	</build>
</profile>
```

---

class: inverse, center, middle

# Séma inicializálás Flyway-jel

---

## Séma inicializálás

* Adatbázis séma létrehozása (táblák, stb.)
* Változások megadása
* Metadata table alapján  

---

## Elvárások

* SQL/XML leírás
* Platform függetlenség
* Lightweight
* Visszaállás korábbi verzióra
* Indítás paranccssorból, alkalmazásból
* Cluster támogatás
* Placeholder támogatás
* Modularizáció
* Több séma támogatása

---

## Flyway függőség

```xml
<dependency>
  <groupId>org.flywaydb</groupId>
  <artifactId>flyway-core</artifactId>
</dependency>
```

---

## Migration

`src/resources/db/migration/V1__employees.sql` állomány

```sql
create table employees (id bigint auto_increment, emp_name varchar(255), primary key (id));

insert into employees (emp_name) values ('John Doe');
insert into employees (emp_name) values ('Jack Doe');
```

`flyway_schema_history` tábla

---

class: inverse, center, middle

# MongoDB

---

## MongoDB elindítása

```
docker run -d -p27017:27017 --name employees-mongo mongo
```

Konzol

```
docker exec -it employees-mongo mongo employees
```

---

## Alkalmazás előkészítése

`application.properties` fájban:

```
spring.data.mongodb.database = employees
```

`pom.xml` függőség

```xml
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-data-mongodb</artifactId>
</dependency>
```

---

## Entitás

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("employees")
public class Employee {

    @MongoId
    private String id;

    private String name;

    public Employee(String name) {
        this.name = name;
    }
}
```

`ObjectId` 12-bájtos generált azonosító, `String`-ben tárolható

---

## Repository

```java
public interface EmployeesRepository extends MongoRepository<Employee, Long> {

    @Query("{ 'name': { $regex: ?0, $options: 'i'} }")
    List<Employee> findAll(String name);

}
```

---

## Konzol

```javascript
db.employees.insert({"name": "John Doe"})

db.employees.find()
```

---

class: inverse, center, middle

# Authentication and Authorization

---

## 12Factor hivatkozás: Authentication and Authorization

* Security-vel az elejétől foglalkozni kell
* Endpoint védelem
* Audit naplózás
* RBAC - role based access controll
* OAuth2

---

class: inverse, center, middle

# OAuth 2.0 Keycloak szerverrel

---

## OAuth 2.0

* Nyílt szabány erőforrás-hozzáférés kezelésére
* Elválik, hogy a felhasználó mit is akar igénybe venni, és az, hogy hol jelentkezik be
* Google, Facebook vagy saját szerver
* Szereplők
  * Resource owner: aki hozzáfér az erőforráshoz, a szolgáltatáshoz, humán esetben a felhasználó (de lehet alkalmazás is)
  * Client: a szoftver, ami hozzá akar férni a felhasználó adataihoz
  * Authorization Server: ahol a felhasználó adatai tárolva vannak, és ahol be tud lépni
  * Resource Server: ahol a felhasználó igénybe veszi az erőforrásokat, a szolgáltatást

---

## OAuth 2.0 forgatókönyvek

* Grant Type:
  * Authorization Code: klasszikus mód, ahogy egy webes alkalmazásba lépünk Facebook vagy a Google segítségével
  * Implicit: mobil alkalmazások, vagy csak böngészőben futó alkalmazások használják
  * Resource Owner Password Credentials: ezt olyan megbízható alkalmazások használják, melyek maguk kérik be a jelszót
  * Client Credentials: ebben az esetben nem a felhasználó kerül azonosításra, hanem az alkalmazás önmaga

---

## Authorization Code forgatókönyv

* A felhasználó elmegy az alkalmazás oldalára
* Az átirányít a Authorization Serverre (pl. Google vagy Facebook), megadva a saját azonosítóját (client id), hogy hozzá szeretne férni a felhasználó adataihoz
* Az Authorization Serveren a felhasználó bejelentkezik
* Az Authorization Serveren a felhasználó jogosultságot ad az alkalmazásnak, hogy hozzáférjen a felhasználó adataihoz
* Az Authorization Server visszairányítja a felhasználót az alkalmazás oldalára, url paraméterként átadva neki egy úgynevezett authorization code-ot
* Az alkalmazás megkapja az authorization code-ot, és ezt, valamint a saját azonosítóját (client id), a saját, alkalmazáshoz tartozó “jelszavát” (client secret) felhasználva lekéri az Authorization Servertől a felhasználóhoz tartozó
* Az alkalmazás visszakapja a tokent, mely hordozza a felhasználó adatait

---

## Token

![JW* szabványok](images/jwasterix.png)

---

## Keycloak

* Keycloak indítása Dockerben
* Létre kell hozni egy Realm-et (`EmployeesRealm`)
* Létre kell hozni egy klienst, amihez meg kell adni annak azonosítóját, és hogy milyen url-en érhető el (`employees-app`)
* Létre kell hozni a szerepköröket (`employees_app_user`)
* Létre kell hozni egy felhasználót, beállítani a jelszavát, valamint hozzáadni a szerepkört (`johndoe`)

```
docker run -e KEYCLOAK_USER=root -e KEYCLOAK_PASSWORD=root -p 8081:8080 --name keycloak jboss/keycloak
```

---

## Keycloak tesztelés

* `http://localhost:8081/auth/realms/EmployeesRealm/.well-known/openid-configuration`
* Token lekérése (https://jws.io címen ellenőrizhető)

```
curl -s --data "grant_type=password&client_id=employees-app&username=johndoe&password=johndoe" http://localhost:8081/auth/realms/EmployeesRealm/protocol/openid-connect/token | jq
```

* `http://localhost:8081/auth/realms/EmployeesRealm/protocol/openid-connect/certs` címen lekérhető a tanúsítvány

---

## Spring támogatás

* Spring Security OAuth deprecated
* Spring Security 5.2 majdnem teljes támogatás
    * Authorization Server nélkül

```xml
<dependency>
	<groupId>org.springframework.security</groupId>
	<artifactId>spring-security-oauth2-resource-server</artifactId>
	<version>${spring-security.version}</version>
</dependency>

<dependency>
	<groupId>org.springframework.security</groupId>
	<artifactId>spring-security-oauth2-jose</artifactId>
	<version>${spring-security.version}</version>
</dependency>
```

---

## Konfiguráció

* `application.properties`:

```
spring.security.oauth2.resourceserver.jwt.jwk-set-uri = http://localhost:8081/auth/realms/EmployeesRealm/protocol/openid-connect/certs
```

---

## Kérés

```
GET http://localhost:8080/api/employees
Accept: application/json
Authorization: bearer eyJ...
```

---

## További konfiguráció

* Szerepkörök: `JwtAuthenticationConverter`, `realm_access/roles`
* Felhasználónév megadása: `NimbusJwtDecoder` `ClaimSetConverter`, `PREFERRED_USERNAME` a `SUB` helyére

---

class: inverse, center, middle

# RestTemplate

---

## Architektúra

![RestTemplate architektúra](images/resttemplate.png)

---

## Forrás

```java
@Service
@Slf4j
public class EventStoreGateway {

    private RestTemplate restTemplate;

    @Value("${employees.eventstore.url}")
    private String url;

    public EventStoreGateway(RestTemplateBuilder builder) {
        restTemplate = builder.build();
    }

    public void sendEvent(CreateEventCommand event) {
        log.debug("Send event to eventstore");
        restTemplate.postForObject(url, event, String.class);
    }
}
```

---

class: inverse, center, middle

# JMS üzenet küldése

---

## Message Oriented Middleware

* Rendszerek közötti üzenetküldés
* Megbízható üzenetküldés: store and forward
* Következő esetekben alkalmazható hatékonyan
  * Hívott fél megbízhatatlan
  * Kommunikációs csatorna megbízhatatlan
  * Hívott fél lassan válaszol
  * Terheléselosztás
  * Heterogén rendszerek
* Lazán kapcsolt rendszerek: nem kell ismerni a <br /> címzettet

---

## JMS

* Szabványos Java API MOM-ekhez való hozzáféréshez
* Java EE része, de Java SE-ben is használható
* JMS provider
  * IBM MQ, Apache ActiveMQ (ActiveMQ 5 "Classic", ActiveMQ Artemis), RabbitMQ
* Hozzáférés JMS API-n keresztül

---

## Architektúra

![JMS architektúra](images/artemismq.png)

---

## Spring üzenetküldés konfiguráció

`pom.xml`

```xml
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-artemis</artifactId>
</dependency>
```

* Ekkor a localhosthoz, default porton (`61616`) kapcsolódik
* Felülbírálható a `spring.artemis.host` és `spring.artemis.port` paraméterekkel

```
spring.artemis.mode = native
```

---

## Üzenetküldés

* Injektálható `JmsTemplate` segítségével

```java
public class EventStoreGateway {

    private JmsTemplate jmsTemplate;

    public EventStoreGateway(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void sendEvent(CreateEventCommand event) {
        log.debug("Send event to eventstore");
        jmsTemplate.convertAndSend("eventsQueue", event);
    }
}
```

---

## Konvertálás

* Alapesetben a `SimpleMessageConverter` aktív
    * `String` -> `TextMessage`
    * `byte[]` -> `BytesMessage`
    * `Map` -> `MapMessage`
    * `Serializable` -> `ObjectMessage`
* `MarshallingMessageConverter` (JAXB), vagy `MappingJackson2MessageConverter` (JSON)


```java
@Bean
public MessageConverter messageConverter(ObjectMapper objectMapper){
	MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
	converter.setTypeIdPropertyName("_typeId");
	return converter;
}
```

* A cél a `_typeId` értékéből (header-ben utazik) találja ki, hogy milyen osztállyá kell alakítani (unmarshal)

---

## Típus megadása

* Alapesetben a típus értéke fully qualified classname - lehet, hogy a cél oldalon nem mond semmit

```java
MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
converter.setTypeIdPropertyName("_typeId");
converter.setTypeIdMappings(Map.of("EmployeeHasCreatedMessage", EmployeeHasCreatedMessage.class));
```

---

class: inverse, center, middle

# JMS üzenet fogadása

---

# JMS üzenet fogadása

```java
@JmsListener(destination = "eventsQueue")
public void processMessage(CreateEventCommand command) {
    eventsService.createEvent(command, "JMS");
}
```

---

class: inverse, center, middle

# Actuator

---

## Actuator

* Monitorozás, beavatkozás és metrikák
* HTTP és JMX végpontok

---

## Actuator alapok

```xml
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

* `http://localhost:8080/actuator` címen elérhető az enabled és exposed endpontok listája
* Logban: `o.s.b.a.e.web.EndpointLinksResolver: Exposing 2 endpoint(s) beneath base path '/actuator'`
* További actuator végpontok bekapcsolása: `management.endpoints.web.exposure.include` konfigurációval
* Mind be van kapcsolva, kivéve a shutdown
* Saját fejleszthető
* Biztonságossá kell tenni

```
management.endpoint.shutdown.enabled = true
```

* Összes expose: `management.endpoints.web.exposure.include = *`

---

## Health

```javascript
{"status":"UP"}
```

`management.endpoint.health.show-details = always`

* Létező JDBC DataSource, MongoDB, JMS providers, stb.
* Saját fejleszthető (`implements HealthIndicator`)

---

## Health details

```javascript
{
  "status": "UP",
  "components": {
    "db": {
      "status": "UP",
      "details": {
        "database": "H2",
        "result": 1,
        "validationQuery": "SELECT 1"
      }
    },
    "diskSpace": {
      "status": "UP",
      "details": {
        "total": 1000202039296,
        "free": 680306184192,
        "threshold": 10485760
      }
    },
    "ping": {
      "status": "UP"
    }
  }
}
```

---

## JVM belső működés

* Heap dump: `/heapdump` (bináris állomány)
* Thread dump: `/threaddump`

---

## Spring belső működés

* Beans: `/beans`
* Conditions: `/conditions`
    * Autoconfiguration feltételek teljesültek-e vagy sem - ettől függ, milyen beanek kerültek létrehozásra
* HTTP mappings: `/mappings`
    * HTTP mappings és a hozzá tartozó kiszolgáló metódusok
* Configuration properties: `/configprops`

---

## Trace

* Ha van `HttpTraceRepository` a classpath-on
* Fejlesztői környezetben: `InMemoryHttpTraceRepository`
* Éles környezetben: Zipkin vagy Spring Cloud Sleuth
* Megjelenik a `/httptrace` endpoint

---

## Kapcsolódó szolgáltatások és library-k

* `/caches` - Cache
* `/scheduledtasks` - Ütemezett feladatok
* `/flyway` - Flyway
* `/liquibase` - Liquibase
* `/integrationgraph` - Spring Integration
* `/sessions` - Spring Session
* `/jolokia` - Jolokia (JMX http-n keresztül)
* `/prometheus`

---

## Info

* `info` prefixszel megadott property-k belekerülnek

```
info.appname = employees
```

```javascript
{"appname":"employees"}
```

---

## Property

* `/env` végpont - property source-ok alapján felsorolva
* `/env/info.appname` - értéke, látszik, hogy melyik property source-ból jött
* Spring Cloud Config esetén `POST`-ra módosítani is lehet (Spring Cloud Config Server használja)

---

## JMX

* `spring.jmx.enabled` hatására management endpointok exportálása MBean-ként
* Kapcsolódás pl. JConsole-lal
* JMX over HTTP beállítása Jolokiával

```
<dependency>
    <groupId>org.jolokia</groupId>
    <artifactId>jolokia-core</artifactId>
</dependency>
```

* JavaScript, Java API
* Kliens pl. a [Jmx4Perl](https://metacpan.org/pod/jmx4perl)
* Jmx4Perl Docker konténerben

```
docker run --rm -it jolokia/jmx4perl jmx4perl http://host.docker.internal:8080/actuator/jolokia \
  read java.lang:type=Memory HeapMemoryUsage
```

---

## 12Factor hivatkozás: Admin processes

* Felügyeleti, üzemeltetési folyamatok
* Ne ad-hoc szkriptek
* Alkalmazással együtt kerüljenek verziókezelésre, buildelésre és kiadásra
* Preferálja a REPL (read–eval–print loop) használatát
    * Tipikusan command line
* Megosztó, könnyen el lehet rontani
* Tipikusan máshogy kéne megoldani:
    * Adatbázis migráció
    * Ütemezett folyamatok
    * Egyszer lefutó kódok
    * Command line-ban elvégezhető feladatok
* Megoldások:
    * Flyway, Liquibase
    * Magas szintű ütemező (pl. Quartz)
    * REST-en, MQ-n meghívható kódrészek
    * Új microservice

---

class: inverse, center, middle

# Git információk megjelenítése

---

## Git információk megjelenítése

```xml
<plugin>
	<groupId>pl.project13.maven</groupId>
	<artifactId>git-commit-id-plugin</artifactId>
</plugin>
```

A `target/classes` könyvtárban `git.properties` fájl

```javascript
{
  "appname": "employees",
  "git": {
    "branch": "master",
    "commit": {
      "id": "d63acd0",
      "time": "2020-02-04T11:12:58Z"
    }
  }
}
```

---

## 12Factor hivatkozás: One Codebase, One Application

* A különböző környezetekre telepített példányoknál alapvető igény, hogy tudjuk, hogy mely verzióból készült (felületen, logban látható legyen)

---

class: inverse, center, middle

# Naplózás

---

# Naplózás lekérdezése és beállítása

* `/loggers`
* `/logfile`

```
### Get logger
GET http://localhost:8080/actuator/loggers/training.employees

### Set logger
POST http://localhost:8080/actuator/loggers/training.employees
Content-Type: application/json

{
  "configuredLevel": "INFO"
}
```

---

class: inverse, center, middle

# Metrics

---

## Metrics

* `/metrics` végponton
* [Micrometer](https://micrometer.io/) - application metrics facade (mint az SLF4J a naplózáshoz)
* Több, mint 15 monitoring eszközhöz való csatlakozás (Elastic, Ganglia, Graphite, New Relic, stb.)

---

## Gyűjtött értékek

* JVM
    * Memória
    * GC
    * Szálak
    * Betöltött osztályok
* CPU
* File descriptors
* Uptime
* Tomcat (`server.tomcat.mbeanregistry.enabled` értéke legyen `true`)
* Library-k: Spring MVC, WebFlux, Jersey, HTTP Client, Cache, DataSource, Hibernate, RabbitMQ
* Stb.

---

## Saját metrics

```java
Counter.builder(EMPLOYEES_CREATED_COUNTER_NAME)
        .baseUnit("employees")
        .description("Number of created employees")
        .register(meterRegistry);

meterRegistry.counter(EMPLOYEES_CREATED_COUNTER_NAME).increment();
```

A `/metrics/employees.created` címen elérhető

---

## 12Factor hivatkozás: Telemetry

* Adatok különböző kategóriákba sorolhatóak:
  * Application performance monitoring
  * Domain specifikus értékek
  * Health, logs
* Új konténerek születnek és szűnnek meg
* Központi eszköz

---

class: inverse, center, middle

# Metrics Graphite monitoring eszközzel

---

## Graphite indítás

```
docker run \
  -d \
  --name graphite \
  --restart=always \
  -p 80:80 -p 2003-2004:2003-2004 -p 2023-2024:2023-2024 -p 8125:8125/udp -p 8126:8126 \
  graphiteapp/graphite-statsd
```

Felhasználó/jelszó: `root`/`root`

---

## Graphite integráció

```xml
<dependency>
	<groupId>io.micrometer</groupId>
	<artifactId>micrometer-registry-graphite</artifactId>
	<version>1.3.3</version>
</dependency>
```

```
management.metrics.export.graphite.step = 10s
```

---

class: inverse, center, middle

# Metrics Prometheus monitoring eszközzel

---

## Prometheus architektúra

* Prometheus kérdez le a megadott rendszerességgel
* yml konfiguráció, `prometheus.yml`

```
scrape_configs:
  - job_name: 'spring'
    metrics_path: '/actuator/prometheus'
    scrape_interval: 20s
    static_configs:
      - targets: ['host.docker.internal:8080']
```

---

## Prometheus indítása

Tegyük fel, hogy a `prometheus.yml` a `D:\data\prometheus` könyvtárban van

```
docker run -p 9090:9090 -v D:\data\prometheus:/etc/prometheus --name prom prom/prometheus
```

---

## Spring Boot alkalmazás konfigurálása

* `io.micrometer:micrometer-registry-prometheus` függőség
* `/actuator/prometheus` endpoint

```xml
<dependency>
	<groupId>io.micrometer</groupId>
	<artifactId>micrometer-registry-prometheus</artifactId>
	<version>1.3.3</version>
</dependency>
```

---

class: inverse, center, middle

# Audit events

---

## Audit events

* Pl. bejelentkezéssel kapcsolatos események
* Saját események vehetőek fel
* Kell egy `AuditEventRepository` implementáció, beépített: `InMemoryAuditEventRepository`
* Megjelenik az `/auditevents` endpoint

```java
applicationEventPublisher.publishEvent(new AuditApplicationEvent("anonymous",
        "employee_has_been_created", Map.of("name", command.getName())));
```

---

class: inverse, center, middle

# Continuous Delivery Jenkins Pipeline-nal

---

## Continuous Integration

*	Extreme Programming
*	Termék átadásának gyorsítására, integrációs idő csökkentésére
*	Revision control, branching csökkentése, gyakori commit, commit-onként build
*	Build folyamat automatizálása, idejének csökkentése
*	Tesztelés automatizálása, az éles (production) környezethez hasonló környezetben
*	A build eredménye mindenki számára hozzáférhető – „eltört build” fogalma
*	A build eredményének azonnali publikálása: hibák mielőbbi megtalálása

---

## Continuous Integration előnyei

*	Integrációs problémák mielőbbi feltárása és javítása
*	Hibás teszt esetén könnyű visszaállás
*	Nem forduló kód mielőbbi feltárása és javítása
*	Konfliktusok mielőbbi feltárása és javítása
*	Minden módosítás azonnali unit tesztelése
*	Verziók azonnali elérhetősége
*	Fejlesztőknek szóló rövidebb visszajelzés

---

## Continuous Delivery

Olyan megközelítés, melynek használatával a fejlesztés rövid ciklusokban történik, biztosítva
hogy a szoftver bármelyik pillanatban kiadható

* Minden egyes változás (commit) potenciális release
* Build automatikus és megismételhető formában
* Több lépésből áll: fordítás, csomagolás, tesztelés (statikus és dinamikus), telepítés különböző környezetekre
* Deployment pipeline foglalja magába a lépéseket
* Ugyanaz az artifact megy végig a pipeline-on

---

## Continuous Deployment

---

## Jenkins

* Automation server
* Open source
* Build, deploy, automatic
* Nagyon sok plugin
* Jenkins nodes: master és agents
* [Dockerben futtatható](https://github.com/jenkinsci/docker/blob/master/README.md)

---

## Pipeline

* Pluginek
* Continuous delivery pipeline megvalósításához
* Pipelines "as code"
* DSL: `Jenkinsfile`
* Stage-ek, melyek step-ekből állnak

---

## Pull szemantika

* CD túl sokmindenhez hozzáfér
* A CD vége csak az artifact előállítás, de nem a telepítés
* Környezet deklaratív leírás alapján előállítható/frissíthető legyen
* Ha új környezetet kell előállítani, nem kell hozzá CD

---

## Példa pipeline

```groovy
pipeline {
   agent any

   tools {
      jdk 'jdk-13'
   }

   stages {
      stage('package') {
         steps {
            git 'http://gitlab.training360.com/trainers/employees-ci'

            sh "./mvnw clean package"
         }
      }
      stage('test') {
         steps {
            sh "./mvnw verify"
         }
      }
   }
}
```

---

## Jenkins pipeline grafikusan

![Jenkins pipeline](images/jenkins-pipeline.png)

---

## Jenkins előkészítése

`training360/jenkins-jdk13` repository: előretelepített AdoptOpenJDK 13

```
docker run -p 8082:8080 -p 50000:50000 --name jenkins jenkins/jenkins:lts

docker run -p 8082:8080 -p 50000:50000 --name jenkins training360/jenkins-jdk13
```

* Unlock Jenkins (password a logból)
* Selet Plugins to install (Git, Pipeline, Pipeline: Stage View)
* Create Admin User
* Instance Configuration

---

## Java13 beállítása

* Jenkins kezelése/Global Tool Configuration
* Add JDK
    * Name: `jdk-13`
    * JAVA_HOME: `/usr/lib/jvm/adoptopenjdk-13-hotspot-amd64/`

---

## Job létrehozása

* Új Item
* Projektnév megadása, pl. `employees`
* Pipeline
* Pipeline/Definition Pipeline script from SCM
* Git
* Repository URL kitöltése, pl. `https://github.com/Training360/employees`

---

## Maven wrapper

```
git update-index --chmod=+x mvnw
```

* Letölti a Mavent
* Maven letölti a függőségeket

---

## 12Factor hivatkozás: Design, Build, Release and Run

* Futtatható alkalmazás készítése
* Forráskód + build = verziózott immutable artifact
* Verziózott artifact + környezeti konfiguráció: egyedi azonosítóval rendelkező immutable release
* Kezeli a függőségeket
* Visszaállás egy előző release-re
* Build, release, futtatás élesen elválik

---

## 12Factor hivatkozás: Environment Parity

* Ne legyen olyan, hogy "nálam működik"
* Különbségek
    * Időbeli eltolódás (lassú release)
    * Személyi különbségek (fejlesztő nem lát rá az éles rendszerre), egy gombos deploy
    * Eszközbeli különbségek (pl. pehelysúlyú, embedded megoldások, pl. H2)
* Docker segít
