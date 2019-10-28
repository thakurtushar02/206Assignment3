package application.create;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import javax.imageio.ImageIO;
import com.flickr4java.flickr.*;
import com.flickr4java.flickr.photos.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

/**
 * This class downloads images from Flickr using an API Key.
 * @author Jacinta
 *
 */
public class ImageManager {

	/**
	 * List of unused image files
	 */
	ObservableList<File> oToDelete = FXCollections.observableArrayList();
	
	/**
	 * This method gets the API Key from the text file where the API Key is stored for further use when getting the images.
	 * The method throws a Runtime Exception if it cannot find an API Key.
	 * @param key
	 * @return
	 * @throws Exception
	 */
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

	/**
	 * This method gets the specified amount of images from Flickr relating to the search term and saves them as jpg files.
	 * It also makes sure the images does not have an odd pixel dimension to prepare it for ffmpeg video creation.
	 * @param num
	 * @param query
	 */
	public void getImages(String query) {
		//Code sourced from 206_FlickrExample from ACP
		try {
			String apiKey = getAPIKey("apiKey");
			String sharedSecret = getAPIKey("sharedSecret");

			Flickr flickr = new Flickr(apiKey, sharedSecret, new REST());

			// Define the search parameters
			int resultsPerPage = 10;
			int page = 0;

			PhotosInterface photos = flickr.getPhotosInterface();
			SearchParameters params = new SearchParameters();
			params.setSort(SearchParameters.RELEVANCE);
			params.setMedia("photos"); 
			params.setText(query);
			
			PhotoList<Photo> results = photos.search(params, resultsPerPage, page); // get photo search results

			for (Photo photo: results) {
				int toPad = results.indexOf(photo);
				try {
					BufferedImage image = photos.getImage(photo,Size.LARGE);
					// Resize image if it has odd dimensions
					if(image.getWidth()%2 != 0) {
						image = image.getSubimage(0, 0, image.getWidth()-1, image.getHeight());
					} if(image.getHeight()%2 != 0) {
						image = image.getSubimage(0, 0, image.getWidth(), image.getHeight()-1);
					}
					// Save image 
					String filename = "." + query + toPad + ".jpg";
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
	
	/**
	 * Makes gridpane of images for user to choose from
	 * @param term
	 * @return
	 */
	public GridPane getImagePane(String term) {
		GridPane imgPane = new GridPane();
		
		for(int i = 0; i < 10; i++) {
			File file = new File("." + term + i + ".jpg");
			Image im = new Image(file.toURI().toString());
			oToDelete.add(file);
			
			// GUI configuration
			ImageView imv = new ImageView(im);
			BorderPane bp = new BorderPane(imv);
			HBox imBox = new HBox(bp);
			
			imBox.setMinHeight(220);
			imBox.setMinWidth(220);
			
			imv.setPreserveRatio(true);
			imv.setFitHeight(200);
			imv.setFitWidth(200);
			
			imv.setOnMouseEntered(arg0 -> { //Enlarges on hover
				imv.setFitHeight(210);
				imv.setFitWidth(210);
			});
			
			imv.setOnMouseExited(arg0 -> { //Return to normal
				imv.setFitHeight(200);
				imv.setFitWidth(200);
			});
			
			imv.setOnMouseClicked(arg0 -> { //Apply a border around image to show it has been selected
				if(oToDelete.contains(file)) {
					bp.getStyleClass().add("border");
					oToDelete.remove(file);
				} else {
					bp.getStyleClass().clear();
					oToDelete.add(file);
				}
			});
			
			imgPane.add(imBox, i%5, i/5);
		}
		
		imgPane.setPadding(new Insets(10,10,10,10));
		imgPane.setHgap(10);
		imgPane.setVgap(10);
		
		return imgPane;
	}
	/**
	 * Returns the list file paths of unselected images
	 * @return oToDelete - List of unselected images to delete
	 */
	public ObservableList<File> getToDelete(){
		return oToDelete;
	}
}