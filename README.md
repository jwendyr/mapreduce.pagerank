# mapreduce.pagerank
CIS833
#beocat

    Configuration conf = new Configuration();
    conf.set("dfs.replication", "1");
    conf.set("mapreduce.client.submit.file.replication", "1");
    Job job = Job.getInstance(conf, "word count");

Step 1. Clone this repository with the following command:

rm -rf mapreduce.pagerank/
git clone https://github.com/jwendyr/mapreduce.pagerank.git

Cloning into 'mapreduce.pagerank'...
remote: Counting objects: 234, done.
remote: Compressing objects: 100% (137/137), done.
remote: Total 234 (delta 67), reused 0 (delta 0), pack-reused 0
Receiving objects: 100% (234/234), 25.03 MiB | 10.08 MiB/s, done.
Resolving deltas: 100% (67/67), done.
Checking connectivity... done.

Step 2. Build your solution by executing the command from the root of your project.

cd mapreduce.pagerank/
mvn clean package

Step 3. From your VM, as the user hduser, copy the input file into hdfs with the following command:
hadoop fs -rm -r input/HadoopPageRank/wiki/*
//hadoop fs -rm -r input/HadoopPageRank/wiki/wiki.xml

//hadoop fs -copyFromLocal example-wiki.xml input/HadoopPageRank/wiki/wiki.xml
unzip afwiki-20091002-one-page-per-line.zip
hadoop fs -copyFromLocal afwiki-20091002-one-page-per-line input/HadoopPageRank/wiki/wiki.xml

unzip scowiki-20090929-one-page-per-line.zip
hadoop fs -copyFromLocal scowiki-20090929-one-page-per-line input/HadoopPageRank/wiki/wiki.xml

unzip afwiki-20091002-one-page-per-line.zip
hadoop fs -copyFromLocal afwiki-20091002-one-page-per-line input/HadoopPageRank/wiki/wiki.xml

Step 4. Make sure that your the job's output directory in hdfs is clear:
hadoop fs -rm -r output/HadoopPageRank/*

Step 5. Run the hadoop jar with the following command:
cd target/
yarn jar cis833.hadoop-1.0-SNAPSHOT.jar mapreduce.pagerank.PageRankJob

Step 6. When the hadoop execution has completed, you may get your results out from hdfs with the following command:
hadoop fs -copyToLocal output/HadoopPageRank/result/part-r-00000 1
hadoop fs -copyToLocal output/HadoopPageRank/result/part-r-00000 2
hadoop fs -copyToLocal output/HadoopPageRank/result/part-r-00000 3
tail -n 100 part-r-00000
tail -n 100 1
tail -n 100 2
tail -n 100 3
cd ..

Step 7. When your code seems to be working satisfactorily, you should unzip the large xml file with the following command:
bzip2 -dk nlwiki-latest-pages-articles.xml.bz2
Note: This may take some time.

Step 8. Copy the unzipped xml document to the input/HadoopPageRank/wiki directory in hdfs as in step 3.

Step 9. Make sure the output/HadoopPageRank directory in hdfs is clear as in step 4.
Step 10. Re-run the hadoop job as in step 5.
