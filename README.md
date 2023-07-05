# Refactored Search Engine, MVC version

In college, I had to build a multi-threaded search engine
that stemmed input from either a url/url directory or a
text file/text files from a directory. It also supported
a web page and CSS using Java Servlet Pages. We received a
few template pages which were sometimes directly relevant (e.g.
we needed to write an object in JSON format, and SimpleJsonWriter
did exactly that) and sometimes not so much (e.g. SimpleTextIndex
gave us an idea of what a forward index looks like, but not how
an InvertedIndex works). From there we would build up to a fully-functioning,
multi-threaded, web-app supporting search engine.

I've learned a lot of new concepts and processes since graduating (e.g. SOLID and MVC architecture),
so I decided to put it to the test: I'd complete the template pages
again and then build a Search Engine with the skills I've gained.

This capstone project has 4 sub-projects:
1. Inverted Index
2. Partial Search
3. Multithreading
4. Web Crawler and Web-based Search Engine

For brevity's sake, I'll only do the first 2 projects, which already
produces a single-threaded Search Engine capable of search text files
and directories based on commandline input.

## Project 1 - Inverted Index
For this project, I had to build a system that:
1. Given a text file, gets all text and converts them into stems. If
given a directory, gets all text and converts text from all available
text files.
2. Holds an inverted index which stores mapping from word stems
to file names to positions within those files.
3. If requested, writes the inverted index contents in JSON format to
a given output file.
4. Parses command-line arguments to find the input text file, output file,
whether writing to the output file is requested.
5. Contains exception handling.

## Inputs:
flag-value pairs written in the command line
For example, the command line args ["-text", "inputFileName", "-index", "outputFileName"]
is converted to the following map:
-text = inputFileName,
-index = outputFileName

This signals the following things:
1. The input file has the filename: inputFileName
2. The output file has the filename: outputFileName
3. Because the input file is non-null, building the index is requested
4. Because the output file is non-null, writing to the output file is requested

## Outputs:
If requested, an output file with the inverted index's contents is generated.
If given the pair -index = (any value), an output file write is requested to the file with the name (any value)
If given this pair: -index = null, we will output to a default output file

## CRC model:
I made a CRC model. Link is [here](https://lucid.app/lucidchart/7fe9eb7c-befc-41dc-a8d5-ffd00daeb6d8/edit?viewport_loc=-4800%2C-3794%2C10175%2C4944%2C0_0&invitationId=inv_3850d397-fcf1-4a9d-9ffd-25499cddf566).

