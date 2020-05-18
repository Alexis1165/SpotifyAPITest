package de.uniba.dsg.models;

/**
 * TODO:
 * Release attributes should be
 * - title:String
 * - artist:String (possibly multiple artists concatenated with ", ")
 */
public class Release {
	private String title;
	private String artist;
	
	public String getTitle(){
		return this.title;
	}
	
	public void setTitle(String title){
		this.title = title;
	}
	
	public String getArtist(){
		return this.artist;
	}
	
	public void setArtist(String artist){
		this.artist = artist;
	}
}
