Training Data Maker
===================

I created this application to make creating train data for a machine learning application easier. The application needed to be trained to classify websites into one of many categories based on the text in the page. 

The trainer needed to be fed some textual content for data in each category. This application downloads the pages in the text file, strips out all the HTML and then puts it in the output directory with a name which is the md5 hash of the url.

Usage:

java -jar jarfile.jar file_with_list_of_urls.txt output/dir/that/will/have/the/pages
