package mapreduce.pagerank.calculate;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class RankCalculateMapper extends Mapper<LongWritable, Text, Text, Text> {

    @Override
    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
    	
    	// Gets the string of the value 
    	String valueStr = value.toString();
    	String[] sections = valueStr.split("\\t");

    	// Gets the mapped page
        String mappedPage = sections[0];
        
        // Gets the [thisPage]	[thisPagesRank]
        String mappedPageStr = sections[0] + "\t" + sections[1] + "\t";
        
        // Ignore if page contains no links
        if(sections.length < 3 || sections[2] == "")
        {
        	context.write(new Text(mappedPage), new Text("! "));
        	return;
        }

        // Gets the linked pages and [thisTotalNumberOfLinks]
        String linkedPages = sections[2];
        String[] pages = linkedPages.split(",");
        int total = pages.length;
 
        // For each linked to page, store [thisPage]    [thisPagesRank]    [thisTotalNumberOfLinks]
        for (String page : pages) {
            context.write(new Text(page), new Text(mappedPageStr + total));
        }
 
        // Adds original links for preservation
        context.write(new Text(mappedPage), new Text("! " + linkedPages));
    
    }
}
