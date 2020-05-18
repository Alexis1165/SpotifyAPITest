package de.uniba.dsg.interfaces;

import java.util.List;

import de.uniba.dsg.models.Interpret;
import de.uniba.dsg.models.Song;

public interface ArtistApi {
    /**
     * TODO:
     * Method should return an artist modeled by the interpret model class
     * Method should be available at /artists/[artist-id], e.g., /artists/4gzpq5DPGxSnKTe4SA8HAU
     * Parameter name must be 'artist-id'
     * Handle requests with unknown artist IDs correctly
     */
    Interpret getArtist(String artistId);

    /**
     * TODO:
     * Method should return a collection of songs modeled by the song model class
     * Method should be available at /artists/[artist-id]/top-tracks, e.g., /artists/4gzpq5DPGxSnKTe4SA8HAU/top-tracks
     * Parameter name must be 'artist-id'
     * Handle requests with unknown artist IDs correctly
     * Always return top songs for Germany (DE)
     * Size of the track list must be <= 5
     */
    List<Song> getTopTracks(String artistId);

    /**
     * TODO:
     * Method should return a similar artist modeled by the interpret model class
     * Method should be available at /artists/[artist-id]/similar-artist, e.g., /artists/4gzpq5DPGxSnKTe4SA8HAU/similar-artist
     * Parameter name must be 'artist-id'
     * Subsequent calls should not always return the same similar artist
     * Handle requests with unknown artist IDs correctly
     */
    Interpret getSimilarArtist(String artistId);
}
