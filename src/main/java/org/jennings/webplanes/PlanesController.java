// http://websats.westus2.cloudapp.azure.com/webplanes/planes?num=600&var=400&rnd=true

package org.jennings.webplanes;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PlanesController {
	
	String IGNOREQUOTEDSTRINGS=",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";

	@Value("${num.days.data}")
	private Integer NUM_DAYS_DATA;
	
	@Value("${data.folder}")
	private String DATA_FOLDER;	
	

	@GetMapping("/planes")
	public Planes planes(@RequestParam(value = "num", defaultValue = "1000") Integer num,
			@RequestParam(value = "var", defaultValue = "0") Integer var,
			@RequestParam(value = "rnd", defaultValue = "false") Boolean rnd,
			@RequestParam(value = "gaussian", defaultValue = "true") Boolean gaussian) {
		Planes planes = new Planes();
		
		Random random = new Random();
		
		//System.out.println(NUM_DAYS_DATA);
		
		Long ts = System.currentTimeMillis();
        Calendar rightNow = Calendar.getInstance();
		
		long dayNum = System.currentTimeMillis()/68400/1000;

		
		int hr = rightNow.get(Calendar.HOUR_OF_DAY);
		int min = rightNow.get(Calendar.MINUTE);
		int sec = rightNow.get(Calendar.SECOND);
		
		int secOfDay = 3600 * hr + 60 * min + sec;
		
		String day = String.valueOf(dayNum % NUM_DAYS_DATA);
		Integer folderNum = secOfDay/1000;
		String folder = String.valueOf(folderNum);
		if (folderNum < 10) folder = "0" + folder;
		
		Integer fileNum = secOfDay - folderNum*1000; 
		String file = String.valueOf(fileNum);
		
		if (fileNum < 10) {
			file = "00" + file;
		} else if (fileNum < 100) {
			file = "0" + file;
		}
		
		String secfile = day + "/" + folder + "/" + file;
		
		
		String filename = DATA_FOLDER + secfile;
		
		try {
			
			FileReader fin = new FileReader(filename);
			BufferedReader br = new BufferedReader(fin);
			String lin = br.readLine();
			
			// Read all lines into ArrayList
		    ArrayList<String> lines = new ArrayList<>();
		    while (lin != null) {
		    	lines.add(lin);
				lin = br.readLine();
		    }
		    br.close();
			
		    // Shuffle order of lines
		    if (rnd) Collections.shuffle(lines);
		    
		    Integer numToReturn = num;
		    if (gaussian) {
		    	numToReturn = (int) (random.nextGaussian()*(double)var+(double)num);
		    } else {
		    	numToReturn = num - var + random.nextInt(var*2 + 1);
		    }
			
			Integer cnt = 0;
			
			for (String line : lines) {
				if (cnt > numToReturn) break;
				String[] parts = line.split(IGNOREQUOTEDSTRINGS);
				
				Integer id = Integer.parseInt(parts[0]);
				Double speed = Double.parseDouble(parts[2]);
				Double dist = Double.parseDouble(parts[3]);
				Double bearing = Double.parseDouble(parts[4]);
				Integer rtid = Integer.parseInt(parts[5]);
				String orig = parts[6];
				String dest = parts[7];
				Integer secsToDep = Integer.parseInt(parts[8]);
				Double lon = Double.parseDouble(parts[9]);
				Double lat = Double.parseDouble(parts[10]);
				Plane plane = new Plane(id,ts,speed, dist, bearing, rtid, orig,dest, secsToDep, lon, lat);
				cnt += 1;
				planes.add(plane);

			}

			br.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	

		
		return planes;
	}
}
