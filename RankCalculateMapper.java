package uk.ac.ncl.cs.csc8101.hadoop.calculate;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class RankCalculateMapper extends Mapper<LongWritable, Text, Text, Text> {
    /**
     * The `map(...)` method is executed against each item in the input split. A key-value pair is
     * mapped to another, intermediate, key-value pair.
     *
     * Specifically, this method should take Text objects in the form
     *      `"[page]    [initialPagerank]    outLinkA,outLinkB,outLinkC..."`
     * and store a new key-value pair mapping linked pages to this page's name, rank and total number of links:
     *      `"[otherPage]   [thisPage]    [thisPagesRank]    [thisTotalNumberOfLinks]"
     *
     * Note: Remember that the pagerank calculation MapReduce job will run multiple times, as the pagerank will get
     * more accurate with each iteration. You should preserve each page's list of links.
     *
     * @param key the key associated with each item output from {@link uk.ac.ncl.cs.csc8101.hadoop.parse.PageLinksParseReducer PageLinksParseReducer}
     * @param value the text value "[page]  [initialPagerank]   outLinkA,outLinkB,outLinkC..."
     * @param context Mapper context object, to which key-value pairs are written
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException { //TODO: mess with
    	
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
