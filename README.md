# movieparser

Task
The legal department has requested a list of all our movies to report it to the FSK (the German motion picture rating system organization). They want the list to be grouped and counted by age rating (e.g. FSK12, FSK16, etc.)
You are provided with a JSON file including 1000 assets (“Movies”, “Series”, “Seasons” and “Episodes”), on which you should conduct the following tasks:
1. Read in the file and return the list of movies requested by the legal department. The output should be a JSON file and each asset should only contain id, title and year.

To run directly:
java -jar movie.jar [input file path] [output file directory]

To compile the code:

1. cd code/
2. mvn clean install

To run the code:
From code/ folder
1. java -jar target/movie-0.0.1-SNAPSHOT-jar-with-dependencies.jar ../assets.json ../
