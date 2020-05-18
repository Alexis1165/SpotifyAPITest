package de.uniba.dsg.jaxws.resources;

import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import de.uniba.dsg.interfaces.ArtistApi;
import de.uniba.dsg.jaxws.MusicApiImpl;
import de.uniba.dsg.jaxws.exceptions.MusicRecommenderFault;
import de.uniba.dsg.models.ErrorMessage;
import de.uniba.dsg.models.Interpret;
import de.uniba.dsg.models.Song;

public class ArtistResource implements ArtistApi{

	@Override
	public Interpret getArtist(String artistId) {
		Client client = ClientBuilder.newClient();
		Response response = client
				.target(MusicApiImpl.restServerUri + "artists/{artist-id}")
				.resolveTemplate("artist-id", artistId)
				.request()
				.get();

		if (response.getStatus() == 200) {
			Interpret artist = response.readEntity(Interpret.class);
			return artist;
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

	@Override
	public List<Song> getTopTracks(String artistId) {
		
		Client client = ClientBuilder.newClient();
		Response response = client
				.target(MusicApiImpl.restServerUri + "artists/{artist-id}/top-tracks")
				.resolveTemplate("artist-id", artistId)
				.request()
				.get();

		if (response.getStatus() == 200) {
			List<Song> topTracks = response.readEntity(new GenericType<List<Song>>(){});
			return topTracks;
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

	@Override
	public Interpret getSimilarArtist(String artistId) {
		Client client = ClientBuilder.newClient();
		Response response = client
				.target(MusicApiImpl.restServerUri + "artists/{artist-id}/similar-artist")
				.resolveTemplate("artist-id", artistId)
				.request()
				.get();				

		if (response.getStatus() == 200) {
			Interpret artist = response.readEntity(Interpret.class);				
			return artist;
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
