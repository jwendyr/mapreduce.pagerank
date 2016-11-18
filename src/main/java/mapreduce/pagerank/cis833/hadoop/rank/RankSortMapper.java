package mapreduce.pagerank.rank;

import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class RankSortMapper extends Mapper<LongWritable, Text, FloatWritable, Text> {

    @Override
    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
    	// Get the page and split by tabs
    	String valueStr = value.toString();
    	String[] sections = valueStr.split("\\t");
    	
    	// Get the rank and the current page (ignoring the pages at the end)
    	float rank = Float.parseFloat(sections[1]);
    	String page = sections[0];
    	
    	// Output rank first
    	context.write(new FloatWritable(rank), new Text(page));
    }
    
}
