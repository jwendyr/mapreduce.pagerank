# mapreduce.pagerank
CIS833

    Configuration conf = new Configuration();
    conf.set("dfs.replication", "1");
    conf.set("mapreduce.client.submit.file.replication", "1");
    Job job = Job.getInstance(conf, "word count");
