package de.uniba.dsg.models;

import java.util.List;

/**
 * TODO:
 * PlaylistRequest attributes should be
 * - title:String
 * - artistSeeds:List<String>, must be serialized as 'seeds'
 * - numberOfSongs:int, must be serialized as 'size'
 */
public class PlaylistRequest {
	private String title;		
	private List<String> artistSeeds;	
	private int numberOfSongs;
	
	public String getTitle(){
		return this.title;
	}
	
	public void setTitle(String title){
		this.title = title;
	}
	
	public List<String> getArtistSeeds(){
		return this.artistSeeds;
	}
	
	public void setArtistSeeds(List<String> artistSeeds){
		this.artistSeeds = artistSeeds;
	}
	
	public int getNumberOfSongs(){
		return this.numberOfSongs;
	}
	
	public void setNumberOfSongs(int numberOfSongs){
		this.numberOfSongs = numberOfSongs;
	}
}
