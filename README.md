# Composite-Tree Application

## About

This application is used to maintain the tree structure.  
At the init view of the application in browser the user see the root node with the zero value.  
From this node can be build a tree structure.  
Every node hold some value.  
User can edit the current node. To edit the node he must to click the right arrow icon which ist right to its value.  
Then three four icons in turn to add a new leaf node to current node (addition icon), to delete current node from the tree (trash icon), and the last to edit the node value. After click the last icon a editable field will appear ti insert the new value, which user can accept or cancel.  
Above the root node are three buttons, to expand the whole tree, to collapse the whole tree or to save the current tree in the database.   
When an error form the backend is occurred then it will be shown.

## Requirements, building and deploying the app

### Requirements

To develop/deploy this application required is:
- jdk8  
- node v10.16.3
- npm v6.9.0

### Build and run the app

Set environment variable JAVA_HOME    
To build this app go to root folder and run in console/terminal
- for windows: mvnw clean install
- for linux: sh mvnw clean install  

To run, after build, stay in the root folder run in console/terminal
- for windows: mvnw spring-boot:run -pl composite-tree-app
- for linux: sh mvnw spring-boot:run -pl composite-tree-app

Now the application is available under url http://localhost:8080/

### Build and start just frontend

To build the frontend go from root to the news-search-web folder and run in console/terminal
- npm install

To run, after build, stai in the folder and run in console/terminal
- npm start

Now the frontent is available under url http://localhost:4200/

## Modules, technology stack and REST Api

### Modules

Application has three maven modules:
- the project root module (composite-tree)
- the backend side of the app (composite-tree-app)
- the frontend side of the app (composite-tree-web) 
 
### Technology stack

- frontend: angular, angular material, jasmine and karma
- backend: java8, spring boot2, JUnit5 and maven
- database: HSQL (in memory mode)

### REST Api

The backend has five endpoints:
- POST /component/id/create-leaf create the new leaf node for the given node id
- GET /component/root get the root component
- PUT /component/id update the nodes value for the given node id
- PUT /component/root update the root component with the state from the frontend
- DELETE /component/id delete the node for the given node id
