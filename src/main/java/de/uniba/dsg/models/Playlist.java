package de.uniba.dsg.models;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO:
 * Playlist attributes should be
 * - title:String
 * - size:int
 * - tracks:List<Song>
 */
public class Playlist {
	private String title;
	private int size;
	private List<Song> tracks;
	
	public Playlist(){
		this.tracks = new ArrayList<Song>();
	}
	
	public String getTitle(){
		return this.title;
	}
	
	public void setTitle(String title){
		this.title = title;
	}
	
	public int getSize(){
		return this.size;
	}
	
	public void setSize(int size){
		this.size = size;
	}
	
	public List<Song> getTracks(){
		return this.tracks;
	}
	
	public void setTracks(List<Song> tracks){
		this.tracks = tracks;
	}
}