package de.uniba.dsg.jaxws.resources;

import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.uniba.dsg.interfaces.AlbumApi;
import de.uniba.dsg.jaxws.MusicApiImpl;
import de.uniba.dsg.jaxws.exceptions.MusicRecommenderFault;
import de.uniba.dsg.models.ErrorMessage;
import de.uniba.dsg.models.Release;

public class AlbumResource implements AlbumApi{

	@Override
	public List<Release> getNewReleases(String country, int size) {
		
		if (country.length() == 0) country = "DE";
		if (size == 0) size = 5;
		
		Client client = ClientBuilder.newClient();
		Response response = client.target(MusicApiImpl.restServerUri)
				.path("/albums/new-releases").queryParam("country", country).queryParam("size", size)
				.request(MediaType.APPLICATION_JSON_TYPE)
				.get();
		
		if (response.getStatus() == 200) {
			List<Release> newReleases = response.readEntity(new GenericType<List<Release>>(){});			
			return newReleases;
		} else if (response.getStatus() == 400) {
			String cause = response.readEntity(ErrorMessage.class).getMessage();
			throw new MusicRecommenderFault("A client side error occurred", cause);
		} else if (response.getStatus() == 404) {
			String cause = response.readEntity(ErrorMessage.class).getMessage();
			throw new MusicRecommenderFault("The requested resource was not found", cause);
		} else if (response.getStatus() == 500) {
			String cause = response.readEntity(ErrorMessage.class).getMessage();
			throw new MusicRecommenderFault("An internal server error occurred", cause);
		} else {
			String cause = response.readEntity(ErrorMessage.class).getMessage();
			throw new MusicRecommenderFault("An unknown remote server error occurred", cause);
		}			
	}

}
