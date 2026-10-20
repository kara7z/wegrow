# WeGrow

Application web de gestion RH : départements et employés.
Projet Maven avec Spring (configuration XML), Spring Data JDBC, MariaDB et Payara 6.

## Stack

| Élément | Choix |
|---|---|
| Java | 21 |
| Packaging | WAR (`target/wegrow.war`) |
| Spring | 6.2.x — configuration **100 % XML**, pas de `@Configuration` maison |
| Data | Spring Data JDBC, `JdbcTemplate`, transactions déclaratives |
| Base de données | MariaDB (`wegrow`, schéma dans `src/main/resources/schema.sql`) |
| Serveur | Payara 6 (Jakarta EE 10), déploiement manuel en WAR |
| Tests | JUnit 5 (`mvn test`) |

Le chemin d'accès de l'application une fois déployée est **`/wegrow`**.

## Ports

| Port | Usage |
|---|---|
| 8081 | HTTP (l'app) — 8080 est occupé par Tomcat |
| 4849 | Admin `asadmin` et console web |
| 3306 | MariaDB |

## Démarrage rapide

```bash
# 1. base de données (service système, déjà lancé normalement)
mariadb -uroot -p'Admin1234@' wegrow < src/main/resources/schema.sql

# 2. serveur + déploiement
./wegrow.sh start          # démarre le domaine Payara
./wegrow.sh deploy         # mvn package + déploiement sur Payara
./wegrow.sh status         # servlets + GET /api/health
./wegrow.sh stop           # arrêt du serveur
./wegrow.sh log            # suivre server.log

# 3. faire tourner sans le serveur (console, pas de HTTP)
mvn compile exec:java
```

Attention : `mvn exec:java` seul ne compile pas. Toujours `mvn compile exec:java`
(sauf si vous venez de faire `mvn package`).

## API

| Méthode | URI | Effet | Codes |
|---|---|---|---|
| GET | `/api/health` | état app + base | 200 |
| GET | `/api/departements` | liste des départements `[{id, name, description, employeeCount}]`, triée par `name` | 200 |
| POST | `/api/departements` | crée un département (`name`, `description`), JSON ou form-data | 201, 409 (nom déjà pris), 400 (`name` manquant) |
| DELETE | `/api/departements/{id}` | supprime un département | 204, 404 (id inconnu), 409 (contient des employés) |

Exemples :

```bash
curl -s http://localhost:8081/wegrow/api/health

curl -s -w '\nHTTP %{http_code}\n' \
  -X POST http://localhost:8081/wegrow/api/departements \
  -H 'Content-Type: application/json' \
  -d '{"name":"IT","description":"IT department"}'
```

Les champs de requête et de réponse sont en anglais : `name`, `description`,
et `employeeCount` dans la liste. Le chemin `/api/departements` reste inchangé.

### Tester avec Postman

La collection est prête dans `postman/wegrow.postman_collection.json`
(health + création/409/400). Dans Postman : **Import → File** → choisir le fichier.

Pour créer via **Body → form-data**, ajouter deux champs de type **Text** :
`name` (obligatoire) et `description` (facultatif). Ne pas définir manuellement
`Content-Type` : Postman génère l'en-tête multipart avec sa boundary.

## Structure du projet

```
src/main/java/com/example/wegrow/
  controller/   HealthController, DepartmentController (REST @RestController)
  repository/   DepartmentRepository, EmployeeRepository (Spring Data JDBC)
  model/        Department, Employee, enums.ContractType
src/main/resources/
  applicationContext.xml       tout le contexte Spring (datasource, tx, JDBC, repositories)
  dispatcher-servlet.xml      MVC Spring : converters JSON + scan controllers
  application.properties      connexion MariaDB
  schema.sql                  tables departments / employees
src/main/webapp/WEB-INF/
  web.xml                     ContextLoaderListener + DispatcherServlet
  dispatcher-servlet.xml      connexion root -> servlet context
  beans.xml                   question CDI (servira plus tard)
src/test/java/com/example/    MainTest (JUnit)
```

## Déploiement Payara

Déployé manuellement (script) :

```bash
PAYARA="$HOME/Documents/servers/payara6"
$PAYARA/bin/asadmin --port 4849 start-domain
$PAYARA/bin/asadmin --port 4849 deploy --name wegrow --force target/wegrow.war
$PAYARA/bin/asadmin --port 4849 list-applications
```

Notes :

- Payara est installé dans `~/Documents/servers/payara6`, un dossier permanent
  qui reste disponible après redémarrage. Le script n'utilise plus `/tmp`.
  Pour une autre installation : `PAYARA_HOME=/chemin/payara6 ./wegrow.sh start`.
- Après redémarrage de la machine, lancer `./wegrow.sh start`.
  Après une nouvelle installation de Payara, lancer aussi `./wegrow.sh deploy`.
- `WEB-INF/beans.xml` (fichier vide) est **obligatoire** : sans lui Payara échoue au
  déploiement avec « CDI is not available ».
- La duplication de la config Spring sur deux fichiers est classique :
  `applicationContext.xml` = contexte racine (data/tx), la partie web (MVC +
  controllers) est dans `dispatcher-servlet.xml` chargée par le `DispatcherServlet`.
- `java -jar target/wegrow.war` ne fonctionne pas : un WAR n'est pas exécutable,
  il doit tourner dans un serveur (seul Spring Boot intègre un mini-serveur).

## Jira

Le suivi des user stories est sur le projet Jira `WEG`
(`https://kara-oussama.atlassian.net`, épic WEG-1). Chaque WS livre un endpoint
de l'API ci-dessus.
