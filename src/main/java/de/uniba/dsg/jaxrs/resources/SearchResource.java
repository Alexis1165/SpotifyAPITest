package de.uniba.dsg.jaxrs.resources;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import com.wrapper.spotify.exceptions.WebApiException;
import com.wrapper.spotify.methods.ArtistSearchRequest;
import com.wrapper.spotify.models.Artist;
import com.wrapper.spotify.models.Page;
import de.uniba.dsg.SpotifyApi;
import de.uniba.dsg.interfaces.SearchApi;
import de.uniba.dsg.jaxrs.exceptions.ClientRequestException;
import de.uniba.dsg.jaxrs.exceptions.RemoteApiException;
import de.uniba.dsg.jaxrs.exceptions.ResourceNotFoundException;
import de.uniba.dsg.models.ErrorMessage;
import de.uniba.dsg.models.Interpret;

@Path("search")
public class SearchResource implements SearchApi {

	@Override
	@GET
	public Interpret searchArtist(@QueryParam("artist") String artistName) {
		if (artistName == null) {
			throw new ClientRequestException(new ErrorMessage("Required query parameter is missing: artist"));
		}

		ArtistSearchRequest artistRequest = SpotifyApi.getInstance().searchArtists(artistName).limit(1).build();

		try {
			// get search results
			Page<Artist> artistSearchResult = artistRequest.get();
			List<Artist> artists = artistSearchResult.getItems();

			// no artist found
			if (artists.isEmpty()) {
				throw new ResourceNotFoundException(new ErrorMessage(String.format("No matching artist found for query: %s", artistName)));
			}

			Artist artist = artists.get(0);
			Interpret result = new Interpret();
			result.setId(artist.getId());
			result.setName(artist.getName());
			result.setPopularity(artist.getPopularity());
			result.setGenres(artist.getGenres());

			return result;
		} catch (WebApiException | IOException e) {
			throw new RemoteApiException(new ErrorMessage(e.getMessage()));
		}
	}
}
