import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

//test fork pull
@SuppressWarnings("deprecation")
public class Main {

	static String key;
	static String places = "/maps/api/place/textsearch/xml";
	static String details = "/maps/api/place/details/xml";
	
	public static void main(String[] args) {
		
		File in = new File(args[0]);
		File out = new File(args[1]);
		key = args[2];
		
		Map<String, String> results = new  HashMap<String, String>();
		HttpClient client  = new DefaultHttpClient();
		List lines = null;
		Set temp = new HashSet<String>();
		StringBuilder resultbuilder = new StringBuilder();
		
		try {
			 lines =  FileUtils.readLines(in,"UTF-8");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(int i=0;i<lines.size();i++)
		{
			String term = ((String) lines.get(i)).trim();
			
			if(term!="")// for empty lines no need 
			{
				URIBuilder builder = new URIBuilder().setScheme("https").setHost("maps.googleapis.com").setPath(places);
	
		        builder.addParameter("query", term);
		        builder.addParameter("sensor", "true");
		        builder.addParameter("key", key);
	
		        HttpUriRequest request;
				try {
					request = new HttpGet(builder.build());
				
	
			        HttpResponse execute = client.execute(request);
		
			        String response = EntityUtils.toString(execute.getEntity());
			       // Thread.sleep(10000);
			        temp.clear();
			        GooglePlaces_SaxParser ms = new GooglePlaces_SaxParser(response, false);
			       // for(int k=0;k<ms.references.size();k++)
			        if(ms.references.size()>0)
			        {
			            builder = new URIBuilder().setScheme("https").setHost("maps.googleapis.com").setPath(details);
	
			 	        builder.addParameter("reference", (String) ms.references.get(0));
			 	        builder.addParameter("sensor", "true");
			 	        builder.addParameter("key", key);
			 	        request = new HttpGet(builder.build());
			 	      // Thread.sleep(10000);
			 	        execute = client.execute(request);
			 	        String responsedetail = EntityUtils.toString(execute.getEntity());
			 	        GooglePlaces_SaxParser msdetail = new GooglePlaces_SaxParser(responsedetail, true);
			 	        
			 	        temp.add((String)msdetail.adr.get(0)+ " -- " + (String)msdetail.adr.get(1));
			 	        //String joined = Joiner.on("\t").join(msdetail.adr);
			 	        //System.out.println(joined);
			 	        
			 	        
			        }
			        Object[] res =  temp.toArray();
			        for(int k=0;k<res.length;k++)
			        {
			        	resultbuilder.append(term + " -- " + (String)res[k] + "\n");
			        	
			        	System.out.println(term + " -- " + (String)res[k]);
			        }
			        
		        
				} catch (Exception e) {
					
					e.printStackTrace();
				}
			}
		}
	    
		// writing results to output file
		try {
			FileUtils.writeStringToFile(out, resultbuilder.toString());
		} catch (IOException e) {
	
			e.printStackTrace();
		}
			
		
		
		
		
		
	}

}
