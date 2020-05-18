package de.uniba.dsg.jaxrs.resources;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.uniba.dsg.interfaces.PlaylistApi;
import de.uniba.dsg.jaxws.MusicApiImpl;
import de.uniba.dsg.models.Interpret;
import de.uniba.dsg.models.Playlist;
import de.uniba.dsg.models.PlaylistRequest;
import de.uniba.dsg.models.Song;

@Path("playlists")
public class PlaylistResource implements PlaylistApi{

	@Override
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createPlaylist(PlaylistRequest request) {
		
		Playlist createdPlaylist = new Playlist();
		createdPlaylist.setTitle(request.getTitle());
		List<String> seeds = Arrays.asList(request.getArtistSeeds().get(0).split("\\s*,\\s*"));
		List<Song> playlistTracks = new ArrayList<Song>();
		
		if (request.getNumberOfSongs() == 0)
			createdPlaylist.setSize(10);
		
		else createdPlaylist.setSize(request.getNumberOfSongs());
		
		for (int i = 0 ; i < seeds.size() && playlistTracks.size() == createdPlaylist.getSize() ; ++i){
			List<Song> topTracks = new ArrayList<Song>();
			Response response = GetTopTracks(seeds.get(i));			
			topTracks = response.readEntity(new GenericType<List<Song>>(){});
			playlistTracks.add(topTracks.get(0));
		}
		
		if (playlistTracks.size() < createdPlaylist.getSize()){						
			for (int i = 0 ; i < seeds.size() ; ++i){
				String artistSeeds = seeds.get(i);
				List<Song> topTracks = new ArrayList<Song>();
				Response response = GetSimilarArtist(artistSeeds);
				Interpret artist = response.readEntity(Interpret.class);				
				artistSeeds = artist.getId();
				response = GetTopTracks(artistSeeds);
				topTracks = response.readEntity(new GenericType<List<Song>>(){});
				playlistTracks.add(topTracks.get(0));
			}			
		}
		
		if (playlistTracks.size() < createdPlaylist.getSize()){						
			int seedCounter = 0;
			int trackCounter = 1;
			
			while (playlistTracks.size() < createdPlaylist.getSize() && 
					trackCounter != playlistTracks.size() && 
					seedCounter < playlistTracks.size() ){
				String artistSeeds = seeds.get(seedCounter);
				List<Song> topTracks = new ArrayList<Song>();
				Response response = GetSimilarArtist(artistSeeds);
				Interpret artist = response.readEntity(Interpret.class);				
				artistSeeds = artist.getId();
				response = GetTopTracks(artistSeeds);
				topTracks = response.readEntity(new GenericType<List<Song>>(){});
				playlistTracks.add(topTracks.get(trackCounter));				
				
				if (trackCounter == topTracks.size() - 1) {
					++seedCounter;
					trackCounter = 1;
				}	
				
				++trackCounter;
			}
		}
		
		createdPlaylist.setTracks(playlistTracks);		
		return Response.ok().entity(createdPlaylist).build();
	}
	
	public Response GetTopTracks(String artistSeeds){
		Client client = ClientBuilder.newClient();
		Response response = client
				.target(MusicApiImpl.restServerUri + "artists/{artist-id}/top-tracks")
				.resolveTemplate("artist-id", artistSeeds)
				.request()
				.get();

		return response;
	}
	
	public Response GetSimilarArtist(String artistSeeds){
		Client client = ClientBuilder.newClient();
		Response response = client
				.target(MusicApiImpl.restServerUri + "artists/{artist-id}/similar-artist")
				.resolveTemplate("artist-id", artistSeeds)
				.request()
				.get();				
		
		return response;
	}
	
}
