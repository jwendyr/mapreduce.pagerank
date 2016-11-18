# mapreduce.pagerank
CIS833

    Configuration conf = new Configuration();
    conf.set("dfs.replication", "1");
    conf.set("mapreduce.client.submit.file.replication", "1");
    Job job = Job.getInstance(conf, "word count");

hadoop fs -copyToLocal output/HadoopPageRank/result/part-r-00000
Step 0. Clone this repository with the following command:

git clone https://github.com/jwendyr/mapreduce.pagerank.git

Step 1. Code your solution...

Step 2. Build your solution by executing the command

mvn clean package

from the root of your project.

Step 3. Once the build has finished running, transfer two files to your VM (make sure it is running):

scp -i CSC8101-2014-15-student.pem target/csc8101-hadoop-assignment-1.0-SNAPSHOT-jar-with-dependencies.jar ubuntu@<your-VM-ip>:/home/ubunutu

scp -i CSC8101-2014-15-student.pem example-input.xml ubuntu@<your-VM-ip>:/home/ubunutu/data/hadoop_pagerank

//Step 4. From your VM, as the user hduser, start hadoop as described here

//Step 5. From your VM, as the user hduser, copy the example-input.xml into hdfs with the following command:

hadoop fs -copyFromLocal example-wiki.xml input/HadoopPageRank/wiki

Step 6. Make sure that your the job's output directory in hdfs is clear:

hadoop fs -rm -r output/HadoopPageRank/*

Step 7. Run the hadoop jar with the following command:

hadoop jar pagerank.jar uk.ac.ncl.cs.csc8101.hadoop.PageRankJob

Step 8. When the hadoop execution has completed, you may get your results out from hdfs with the following command:

hadoop fs -copyToLocal output/HadoopPageRank/result/part-r-00000 .

Step 9. When your code seems to be working satisfactorily, you should unzip the large xml file with the following command:

bzip2 -dk nlwiki-latest-pages-articles.xml.bz2

Note: This may take some time.

Step 10. Copy the unzipped xml document to the input/HadoopPageRank/wiki directory in hdfs as in step 5.

Step 11. Make sure the output/HadoopPageRank directory in hdfs is clear as in step 6.

Step 12. Re-run the hadoop job as in step 7.

Submission

Your project src directory, zipped up.
The output of the command tail -n 100 part-r-0000 as a text file.
