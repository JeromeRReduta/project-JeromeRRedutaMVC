#Refactored Search Engine, MVC version

##Project 2 - Search Engine
Now that we have somewhere to store our stems, we can build a system
that can search for specific stems. For this project, I had to build a
system that:

1. Given an inverted index snapshot, tracks how many of each stem is in each file
and stores the results in something I call a "stem counter."
2. Given a query file and a stem counter snapshot, searches the stem counter
and stores the results in something I call a "search result index."
3. Depending on user input, using one of the following searching strategies:  
&emsp;a. Exact search: A stem in the stem counter must be the same as a query stem  
&emsp;b. Partial search: A stem in the stem counter must be the same OR START WITH a query stem  
4. If requested, output inverted index and search result index in a "pretty"
JSON format.
5. Handle errors.
6. Parse command-line arguments to handle configurations, such as text input files
or text output files.

It also has to maintain the previous project's functionality:
1. Given a text file, converts the text into stems and stores them
into an inverted index.


##Inputs:
New flags:  
-counts (file): If this is present, we want to output the stem counter's data to (file).  
-queries (file): If this is present, we want to search the stem counter using the query file (file).  
-results (file): If this is present, we want to output the search result index's data to (file).  
-exact: If this is present, we want to use exact search. If it's not, we want to use partial search.  

We also want to maintain the old flags:
-text (file): If this is present, we want to process stems from (file) and store them into an inverted index.  
-index (file): If this is present, we want to output the inverted index's data to (file).  
 
##Outputs:
We can now output the following data upon request:  
SearchResultIndex data - a collection of sorted search results  
StemCounter data - a map that tracks how many stems each file has. Note that this is merely a view of the stem counter, and not the complete data as-is.  

We are also still able to output Project 1 data structures upon request:  
InvertedIndex data - a collection of all the stem / file name / list of positions triplets in the index

##Changes:

We've moved from separate views for each data structure to having a GenericTextFileView used
for everything. This is because after building on the project, we use the exact same logic
for every model, and will never need to add additional functionality.

We now use ModularConfigs instead of regular Configs. Modular Configs don't come with validation
and also follow a composite pattern, allowing you to create higher-order modular configs
made out of other modular configs. For example, the Project 2 Modular Config is made out of
3 modular configs: 1 for inverted index settings, 1 for stem counter settings, and 1 for 
search result index settings.

We moved a bunch of files around. We've organized most of the files into a models package,
a views package, a controllers package, and a data reading package.

##Snapshotting:

An crucial feature of this project is the ability for data models to take a copy of
their current state (i.e. a "snapshot"). This lets us keep data integrity
for the original models, and have a copy of the data we can mess with as much as we want.
The demerit is that making that copy is an expensive operation, so I would recommend using it
lightly.

##CRC Model

Right [here](https://lucid.app/lucidchart/a3c457f3-5b28-4172-9d3b-f2bf5515a6cf/edit?page=0_0&invitationId=inv_849033f1-ca31-489e-83a4-557c8ff5fdb3#).