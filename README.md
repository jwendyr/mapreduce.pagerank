#mapreduce.pagerank
##CIS833
###beocat

    Configuration conf = new Configuration();
    conf.set("dfs.replication", "1");
    conf.set("mapreduce.client.submit.file.replication", "1");
    Job job = Job.getInstance(conf, "word count");

**Step 1**. Clone this repository with the following command:

    rm -rf mapreduce.pagerank/
    git clone https://github.com/jwendyr/mapreduce.pagerank.git

Cloning into 'mapreduce.pagerank'...
remote: Counting objects: 234, done.
remote: Compressing objects: 100% (137/137), done.
remote: Total 234 (delta 67), reused 0 (delta 0), pack-reused 0
Receiving objects: 100% (234/234), 25.03 MiB | 10.08 MiB/s, done.
Resolving deltas: 100% (67/67), done.
Checking connectivity... done.

**Step 2**. Build your solution by executing the command from the root of your project.

    cd mapreduce.pagerank/
    mvn clean package

**Step 3**. From your VM, as the user hduser, copy the input file into hdfs with the following command:
    
    hadoop fs -rm -r input/HadoopPageRank/wiki/*
    //hadoop fs -rm -r input/HadoopPageRank/wiki/wiki.xml

    //cd ..
    //hadoop fs -copyFromLocal example-wiki.xml input/HadoopPageRank/wiki/wiki.xml

    unzip scowiki-20090929-one-page-per-line.zip
    hadoop fs -copyFromLocal scowiki-20090929-one-page-per-line input/HadoopPageRank/wiki/wiki.xml

    unzip afwiki-20091002-one-page-per-line.zip
    hadoop fs -copyFromLocal afwiki-20091002-one-page-per-line input/HadoopPageRank/wiki/wiki.xml

    ls /homes/dcaragea/WikiProject
    hadoop fs -copyFromLocal /homes/dcaragea/WikiProject/* input/HadoopPageRank/wiki
    hadoop fs -ls input/HadoopPageRank/wiki

**Step 4**. Make sure that your the job's output directory in hdfs is clear:
    
    hadoop fs -rm -r output/HadoopPageRank/*


**Step 5**. Run the hadoop jar with the following command:
    
    cd target/
    yarn jar cis833.hadoop-1.0-SNAPSHOT.jar mapreduce.pagerank.PageRankJob

**Step 6**. When the hadoop execution has completed, you may get your results out from hdfs with the following command:

    hadoop fs -copyToLocal output/HadoopPageRank/result/part-r-00000 1
    tail -n 100 1

5.6457105       Unitit_States
5.8568          Scots_leid
5.949724        Northren_Ireland
7.286911        Europe
7.5826597       Inglis_leid
10.499083       Scotland
10.91714        New_Zealand
12.002874       Ingland
13.5375185      Unitit_Kinrick
14.023736       Republic_o_Ireland


    hadoop fs -copyToLocal output/HadoopPageRank/result/part-r-00000 2
    tail -n 100 2

17.83002        Verenigde_Koninkryk
17.880821       Gregoriaanse_kalender
18.177961       Italië
21.940887       Engels
22.262438       Rome
27.93743        Duitsland
28.428913       provinsie_Soria
33.871284       Frankryk
39.76103        Verenigde_State_van_Amerika
55.79539        Suid-Afrika

    hadoop fs -copyToLocal output/HadoopPageRank/result/part-r-00000 3
    tail -n 100 3

761.6818        United_Kingdom
775.1779        Italy
902.53705       Canada
969.6055        France
972.393         Insect
1021.9603       Arthropod
1053.8143       England
1062.101        Germany
1086.3003       India
1447.3062       Animal
2645.2258       United_States

    cd ..

**Step 7**. When your code seems to be working satisfactorily, try large file with the following command:

    hadoop fs -copyFromLocal /homes/dcaragea/WikiProject/* input/HadoopPageRank/wiki

16/11/18 05:05:45 INFO hdfs.DFSClient: Exception in createBlockOutputStream
java.io.IOException: Bad connect ack with firstBadLink as 10.5.42.12:50010
        at org.apache.hadoop.hdfs.DFSOutputStream$DataStreamer.createBlockOutputStream(DFSOutputStream.java:1614)
        at org.apache.hadoop.hdfs.DFSOutputStream$DataStreamer.nextBlockOutputStream(DFSOutputStream.java:1512)
        at org.apache.hadoop.hdfs.DFSOutputStream$DataStreamer.run(DFSOutputStream.java:668)
16/11/18 05:05:45 INFO hdfs.DFSClient: Abandoning BP-130976981-10.5.3.30-1463579955828:blk_1073791070_85266
16/11/18 05:05:45 INFO hdfs.DFSClient: Excluding datanode DatanodeInfoWithStorage[10.5.42.12:50010,DS-48aefdc4-724c-4e80-803e-c6e92b5bf2a0,DISK]

Note: This may take some time.

**Step 8**. Spark

    rm -rf spark.pagerank/
    git clone https://github.com/jwendyr/spark.pagerank.git
    cd spark.pagerank/
    mvn clean package
    unzip scowiki-20090929-one-page-per-line.zip
    hadoop fs -copyFromLocal scowiki-20090929-one-page-per-line input/HadoopPageRank/wiki/wiki.xml
    hadoop fs -rm -r output/HadoopPageRank/*
    cd target/
    spark-submit --class com.spark.cis833.extra.SparkPageRank --master yarn cis833.extra-0.0.1-SNAPSHOT.jar graph/1 10
    hadoop fs -copyToLocal output/HadoopPageRank/result/part-r-00000 1
    tail -n 100 1
