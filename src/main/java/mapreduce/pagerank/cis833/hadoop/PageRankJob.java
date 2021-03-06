package mapreduce.pagerank;

import mapreduce.pagerank.parse.PageLinksParseReducer;
import mapreduce.pagerank.parse.PageLinksParseMapper;
import mapreduce.pagerank.parse.XmlInputFormat;
import mapreduce.pagerank.calculate.RankCalculateMapper;
import mapreduce.pagerank.calculate.RankCalculateReducer;
import mapreduce.pagerank.rank.RankSortMapper;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class PageRankJob extends Configured implements Tool {

    private static NumberFormat nf = new DecimalFormat("00");

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.err.println("Usage: PageRank <input> <output>");
            System.exit(1);
        }
        System.exit(ToolRunner.run(new Configuration(), new PageRankJob(), args));
    }

    @Override
    public int run(String[] args) throws Exception {
        //Run the first MapReduce Job, parsing links from the large dump of wikipedia pages
        boolean isCompleted = runXmlParsing(args[0], args[1]+"/ranking/iter00");
        if (!isCompleted) return 1;
        //if (!isCompleted){
        String lastResultPath = null;
    
        //Run the second MapReduce Job, calculating new pageranks from existing values
        //Run this job several times, with each iteration the pagerank value will become more accurate
        for (int runs = 0; runs <= 9; runs++) {
            String inPath = args[1]+"/ranking/iter" + nf.format(runs);
            lastResultPath = args[1]+"/ranking/iter" + nf.format(runs + 1);

            isCompleted = runRankCalculator(inPath, lastResultPath);

            if (!isCompleted) return 1;
        }

        isCompleted = runRankSorter(lastResultPath, args[1]+"/result");

        if (!isCompleted) return 1;//}
        return 0;
    }


    //Parsing MapReduce Job 1
    public boolean runXmlParsing(String inputPath, String outputPath) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf = new Configuration();
        conf.set("dfs.replication", "1");
        conf.set("mapreduce.client.submit.file.replication", "1");
        conf.set(XmlInputFormat.START_TAG_KEY, "<page>");
        conf.set(XmlInputFormat.END_TAG_KEY, "</page>");

        Job xmlParser = Job.getInstance(conf, "xmlParser");
        
        xmlParser.setJarByClass(PageRankJob.class);

        // Input -> Mapper -> Map
        FileInputFormat.addInputPath(xmlParser, new Path(inputPath));
        xmlParser.setInputFormatClass(XmlInputFormat.class);
        xmlParser.setMapperClass(PageLinksParseMapper.class);
        xmlParser.setMapOutputKeyClass(Text.class);

        // Map -> Reducer -> Output
        FileOutputFormat.setOutputPath(xmlParser, new Path(outputPath));
        xmlParser.setOutputFormatClass(TextOutputFormat.class);
        xmlParser.setOutputKeyClass(Text.class);
        xmlParser.setOutputValueClass(Text.class);
        xmlParser.setReducerClass(PageLinksParseReducer.class);
        //xmlParser.setNumReduceTasks(0);

        return xmlParser.waitForCompletion(true);
    }

    //Calculation MapReduce Job 2
    private boolean runRankCalculator(String inputPath, String outputPath) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf = new Configuration();
        conf.set("dfs.replication", "1");
        conf.set("mapreduce.client.submit.file.replication", "1");

        Job rankCalculator = Job.getInstance(conf, "rankCalculator");
        rankCalculator.setJarByClass(PageRankJob.class);

        // Input -> Mapper -> Map
        rankCalculator.setOutputKeyClass(Text.class);
        rankCalculator.setOutputValueClass(Text.class);
        rankCalculator.setMapperClass(RankCalculateMapper.class);

        // Map -> Reducer -> Output
        FileInputFormat.setInputPaths(rankCalculator, new Path(inputPath));
        FileOutputFormat.setOutputPath(rankCalculator, new Path(outputPath));
        rankCalculator.setReducerClass(RankCalculateReducer.class);

        return rankCalculator.waitForCompletion(true);
    }

    //Sorting and sanitization Map Job 3
    private boolean runRankSorter(String inputPath, String outputPath) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf = new Configuration();
        conf.set("dfs.replication", "1");
        conf.set("mapreduce.client.submit.file.replication", "1");

        Job rankOrdering = Job.getInstance(conf, "rankSorter");
        rankOrdering.setJarByClass(PageRankJob.class);

        // Input -> Mapper -> Map
        rankOrdering.setOutputKeyClass(FloatWritable.class);
        rankOrdering.setOutputValueClass(Text.class);
        rankOrdering.setMapperClass(RankSortMapper.class);

        FileInputFormat.setInputPaths(rankOrdering, new Path(inputPath));
        FileOutputFormat.setOutputPath(rankOrdering, new Path(outputPath));
        rankOrdering.setInputFormatClass(TextInputFormat.class);
        rankOrdering.setOutputFormatClass(TextOutputFormat.class);

        return rankOrdering.waitForCompletion(true);
    }

}
