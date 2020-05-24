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
    
    _**Note**: Make sure to provide username and password in this config file as the same is required to access git apis (to avoid rate limit)_
    
3. Using velocity template we then generate the [html file](https://github.com/ankit0811/apache-task-to-take/blob/master/src/main/resources/sample_table_template.html) in resources




  
