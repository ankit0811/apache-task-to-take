# apache-task-to-take

![Image of apache-task-to-take](https://github.com/ankit0811/apache-task-to-take/blob/master/src/main/resources/apache-task-to-take.gif)


The main idea behind this repo is to get a one stop page for all apache issues that you as a developer can contribute to.

Those who are curios to contribute to the open source community can now run this code 
to generate a simple html table with sort and search feature listing all the open ossues marked as 
`beginners task` or `first task` or similar label



## Code flow:

1. Hit guthub api to find all repositories associated with `Apache` org
2. Read the `config.properities` file to filter out repositories based on users config where the user can provide
    1. repo language `FILTER_REPO_LANGUAGE=java,python`
    2. repos to be ignored `IGNORE_REPO=sling`
    3. repos interested in (key words) `FILTER_REPO=`
    4. issue labels in (key words). `FILTER_LABEL=start,first,begin,contribution`
    
    _**Note**: Make sure to provide `username:password` in this config file as it is required to access git apis (to avoid rate limit)_
    
3. Using velocity template we then generate the [html file](beginner_table.html) in resources

## Steps to execute

#### Using terminal
1. Clone the repo.
```bash
git clone git@github.com:ankit0811/apache-task-to-take.git
```
2. Go to the root directory of this repo.
```bash
cd apache-task-to-take
```
3. Build the repo using the following command.
```bash
mvn clean install
```
4. Make sure to modify the file `config.properties.sample` to add your Git username and password
and then run the following command.
```bash
 java -Dconfig="config.properties.sample" -cp target/apache-task-to-take-1.0-SNAPSHOT-jar-with-dependencies.jar ProjectMain
```
5. This will generate the target html.
 The target location is configurable via `config.properties.sample` file.  

#### Using IDE 
1. Once you have checkout the repo, open the project in your choice of IDE

2. Make sure to modify the `config.properties` file in resources folder to add `username:password`

3. Run `ProjectMain.class`.
 This will generate the target html.
 The target location is configurable via `config.properties` file. 





  
