package application;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import javax.imageio.ImageIO;

import com.flickr4java.flickr.*;
import com.flickr4java.flickr.photos.*;

public class ImageManager {

	public static String getAPIKey(String key) throws Exception {
		String config = System.getProperty("user.dir") 
				+ System.getProperty("file.separator")+ "flickr-api-keys.txt"; 

		File file = new File(config); 
		BufferedReader br = new BufferedReader(new FileReader(file)); 

		String line;
		while ( (line = br.readLine()) != null ) {
			if (line.trim().startsWith(key)) {
				br.close();
				return line.substring(line.indexOf("=")+1).trim();
			}
		}
		br.close();
		throw new RuntimeException("Couldn't find " + key +" in config file "+file.getName());
	}

	public void getImages(int num, String query) {
		try {
			String apiKey = getAPIKey("apiKey");
			String sharedSecret = getAPIKey("sharedSecret");

			Flickr flickr = new Flickr(apiKey, sharedSecret, new REST());

			int resultsPerPage = num;
			int page = 0;

			PhotosInterface photos = flickr.getPhotosInterface();
			SearchParameters params = new SearchParameters();
			params.setSort(SearchParameters.RELEVANCE);
			params.setMedia("photos"); 
			params.setText(query);

			PhotoList<Photo> results = photos.search(params, resultsPerPage, page);

			for (Photo photo: results) {
				int toPad = results.indexOf(photo);
				String padded = String.format("%02d", toPad);
				try {
					BufferedImage image = photos.getImage(photo,Size.LARGE);
					if(image.getWidth()%2 != 0) {
						image = image.getSubimage(0, 0, image.getWidth()-1, image.getHeight());
					} if(image.getHeight()%2 != 0) {
						image = image.getSubimage(0, 0, image.getWidth(), image.getHeight()-1);
					}
					String filename = query + padded + ".jpg";
					File outputfile = new File(System.getProperty("user.dir") 
							+ System.getProperty("file.separator"),filename);
					ImageIO.write(image, "jpg", outputfile);
				} catch (FlickrException fe) {
					System.err.println("Ignoring image " +photo.getId() +": "+ fe.getMessage());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}