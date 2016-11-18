package mapreduce.pagerank.calculate;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.Iterator;

public class RankCalculateReducer extends Reducer<Text, Text, Text, Text> {

    @Override
    public void reduce(Text page, Iterable<Text> values, Context context) throws IOException, InterruptedException {
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
