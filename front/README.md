# Yoga

This project was generated with [Angular CLI](https://github.com/angular/angular-cli) version 14.1.0.
- [ ] [Prerequis](https://gitlab.com/-/experiment/new_project_readme_content:02dabd3766913935689dcab8513bbde9?https://gitlab.com/MENDEL-BA/clinic_api_v3/-/settings/integrations)
```
Angular >=14
Node >=16
```
## Start the project

Git clone:

> git clone https://github.com/OpenClassrooms-Student-Center/P5-Full-Stack-testing

Aller dans le dossier:

> cd yoga

Installer les dependences:

> npm install

Lancer le Front-end:

> npm run start;

## Run application Test Front-end:
# Avec Jest
```
cd existing_repo
git checkout develop
```

Génération du Rapport de Couverture de Code (Coverage)
Pour générer le rapport de couverture de code, exécutez la commande suivante :
```
bash
Copy code
npm test -- --coverage
```
Les rapports de couverture seront générés dans le répertoire coverage/ à la racine du projet.
Ouvrez le fichier index.html dans ce répertoire pour visualiser les résultats.

# Avec Cyspress
Lancer le projet en mode developpement
```
cd existing_repo/frontend
npm start
```
Dans un autre terminal, exécutez la commande suivante pour ouvrir l'interface graphique de Cypress :
```
npm run cypress:open
```
Exécution des Tests en headless (Sans Interface Graphique)
Pour exécuter les tests en mode headless, utilisez la commande suivante :
```
npm run cypress:run
```

### Postman collection

For Postman import the collection

> ressources/postman/yoga.postman_collection.json 

by following the documentation: 

https://learning.postman.com/docs/getting-started/importing-and-exporting-data/#importing-data-into-postman


### MySQL

SQL script for creating the schema is available `ressources/sql/script.sql`

By default the admin account is:
- login: yoga@studio.com
- password: test!1234


### Test

#### E2E

Lancer les e2e test:

> npm run e2e

Genrerer le coverage report (you should launch e2e test before):

> npm run e2e:coverage

Report disponible:

> front/coverage/lcov-report/index.html
