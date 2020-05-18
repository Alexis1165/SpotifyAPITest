package de.uniba.dsg.jaxrs.resources;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import com.wrapper.spotify.exceptions.WebApiException;
import com.wrapper.spotify.methods.ArtistRequest;
import com.wrapper.spotify.models.Artist;
import com.wrapper.spotify.models.Track;

import de.uniba.dsg.SpotifyApi;
import de.uniba.dsg.interfaces.ArtistApi;
import de.uniba.dsg.jaxrs.exceptions.ClientRequestException;
import de.uniba.dsg.jaxrs.exceptions.RemoteApiException;
import de.uniba.dsg.jaxrs.exceptions.ResourceNotFoundException;
import de.uniba.dsg.models.ErrorMessage;
import de.uniba.dsg.models.Interpret;
import de.uniba.dsg.models.Song;

@Path("artists")
public class ArtistResourse implements ArtistApi{

	@Override
	@GET
	@Path("{artist-id}")
	public Interpret getArtist(@PathParam("artist-id") String artistId) {
		if (artistId.isEmpty()) {
			throw new ClientRequestException(new ErrorMessage("Required query parameter is missing: artist Id"));
		}

		ArtistRequest artistRequest = SpotifyApi.getInstance().getArtist(artistId).build();

		try {
			// get search results
			Artist artist = artistRequest.get();

			// no artist found
			if (artist == null) {
				throw new ResourceNotFoundException(new ErrorMessage(String.format("No matching artist found for query: %s", artistId)));
			}

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

	@Override
	@GET
	@Path("{artist-id}/top-tracks")	
	public List<Song> getTopTracks(@PathParam("artist-id") String artistId) {
		
		if (artistId.isEmpty()) {
			throw new ClientRequestException(new ErrorMessage("Required query parameter is missing: artist Id"));
		}

		try {
			List<Track> topTracks = SpotifyApi.getInstance().getTopTracksForArtist(artistId, "US").
									build().get();
			List<Song> ls = new ArrayList<>();
			Track song = null;
			String artists = "";			
			
			for (int i = 0 ; i < 5 ; ++i){
				song = topTracks.get(i);

				for (int j = 0 ; j < song.getArtists().size() ; ++j)
					artists += song.getArtists().get(j).getName() + ", ";

				artists = artists.substring(0, artists.length() - 2);

				Song s = new Song();
				s.setTitle(song.getName());
				s.setArtist(artists);
				s.setDuration(song.getDuration());

				ls.add(s);
				artists = "";
			}
			return ls;

		} catch (IOException | WebApiException e) {
			throw new RemoteApiException(new ErrorMessage(e.getMessage()));
		}
	}

	@Override
	@GET
	@Path("{artist-id}/similar-artist")	
	public Interpret getSimilarArtist(@PathParam("artist-id") String artistId) {
		if (artistId.isEmpty()) {
			throw new ClientRequestException(new ErrorMessage("Required query parameter is missing: artist Id"));
		}

		try {
			// get search results
			List<Artist> similarArtist = SpotifyApi.getInstance().getArtistRelatedArtists(artistId).build().get();
			Artist artist = similarArtist.get(0);

			// no artist found
			if (artist == null) {
				throw new ResourceNotFoundException(new ErrorMessage(String.format("No matching artist found for query: %s", artistId)));
			}

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
