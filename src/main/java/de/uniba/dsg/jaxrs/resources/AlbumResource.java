package de.uniba.dsg.jaxrs.resources;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import com.wrapper.spotify.exceptions.WebApiException;
import com.wrapper.spotify.methods.NewReleasesRequest;
import com.wrapper.spotify.models.Album;
import com.wrapper.spotify.models.NewReleases;
import com.wrapper.spotify.models.SimpleAlbum;
import com.wrapper.spotify.models.SimpleArtist;

import de.uniba.dsg.SpotifyApi;
import de.uniba.dsg.interfaces.AlbumApi;
import de.uniba.dsg.jaxrs.exceptions.RemoteApiException;
import de.uniba.dsg.models.ErrorMessage;
import de.uniba.dsg.models.Release;

@Path("albums")
public class AlbumResource implements AlbumApi{

	@Override
	@GET
	@Path("new-releases")
	public List<Release> getNewReleases(@QueryParam("country") @DefaultValue("DE") String country, 
										@QueryParam("size") @DefaultValue("5") int size) {				
		try {			
			List <Release> newReleasesList = new ArrayList<Release>();

			NewReleasesRequest newReleasesRequest = SpotifyApi.getInstance().
					getNewReleases().limit(size).offset(1).country(country).build();
						
			NewReleases newReleases = newReleasesRequest.get();
			List<SimpleAlbum> albumsList = newReleases.getAlbums().getItems();
			
			for (int i = 0 ; i < size ; ++i){
				String artistCommaSeparated = "";
				Release r = new Release();
				SimpleAlbum album = albumsList.get(i);
								
				Album albumRequest = SpotifyApi.getInstance().
						getAlbum(album.getId()).build().get();				
				
				List<SimpleArtist> artists = albumRequest.getArtists();
				
				for (int j = 0 ; j < artists.size() ; ++j)
					artistCommaSeparated += artists.get(j).getName() + ", ";

				artistCommaSeparated = artistCommaSeparated.substring(0, artistCommaSeparated.length() - 2);
				
				r.setArtist(artistCommaSeparated);
				r.setTitle(albumRequest.getName());
				newReleasesList.add(r);
			}
			
			return newReleasesList;
		} catch (IOException | WebApiException e) {
			throw new RemoteApiException(new ErrorMessage(e.getMessage()));
		}
		
	}

}
