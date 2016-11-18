package uk.ac.ncl.cs.csc8101.hadoop.calculate;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.Iterator;

public class RankCalculateReducer extends Reducer<Text, Text, Text, Text> {

    /**
     * The `reduce(...)` method is called for each <key, (Iterable of values)> pair in the grouped input.
     * Output values must be of the same type as input values and Input keys must not be altered.
     *
     * Specifically, this method should take the iterable of links to a page, along with their pagerank and number of links.
     * It should then use these to increase this page's rank by its share of the linking page's:
     *      thisPagerank +=  linkingPagerank> / count(linkingPageLinks)
     *
     * Note: remember pagerank's dampening factor.
     *
     * Note: Remember that the pagerank calculation MapReduce job will run multiple times, as the pagerank will get
     * more accurate with each iteration. You should preserve each page's list of links.
     *
     * @param page The individual page whose rank we are trying to capture
     * @param values The Iterable of other pages which link to this page, along with their pagerank and number of links
     * @param context The Reducer context object, to which key-value pairs are written
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    public void reduce(Text page, Iterable<Text> values, Context context) throws IOException, InterruptedException { //TODO: mess with
    	// d=0.85 is the normal damping factor
    	double d = 0.85;
        float sOtherPR = 0;
        String links = "";
        Iterator<Text> val = values.iterator();
        
        // For each page that links to the current
        while ((val).hasNext()) {
        	String pageStr = val.next().toString();
 
        	// If it is a links record, add it to the links array
            if (pageStr.startsWith("! ")) {
                links = pageStr.substring(2);
            } 
            // If it is a normal record however
            else {
            // Find the pagerank and number of links for the given page
            String[] sections = pageStr.split("\\t");
            float currentPageRank = Float.valueOf(sections[1]);
            int linkCount = Integer.valueOf(sections[2]);
 
            // Add the given pagerank to the running total for the other pages
            sOtherPR += currentPageRank / linkCount;
            }
        }
        
        // Calculate pagerank by applying the dampening to the sum
        double pageRank = (1-d) + d * sOtherPR ;

        // Add new pagerank to total
        context.write(page, new Text(pageRank + "\t" + links));
    }
}
