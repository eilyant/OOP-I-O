// -----------------------------------------------------
// Assignment (2)
// Question: (Movie Driver)
// Written by: (Eilya Nasertorabi 40183363)
// -----------------------------------------------------

/**
 * <p>Name and ID: Eilya Nasertorabi - 40183363</p>
 * <p>Course: COMP249</p>
 * <p>Assignment #: 2</p>
 * <p>Due Date: March27th</p>
 * 
 * <p>Description: This program manages a movie database, including initializing genre files, navigating through movie arrays, and handling movie records with serialization and deserialization. It allows for the categorization of movies by genre, providing functionality to navigate, add, and serialize movie data.</p>
 */
import java.io.*;
import java.util.*;

/**
 * Represents a movie with properties such as year, title, duration, genres,
 * rating, score, director, and actors. This class provides methods to access
 * and modify movie properties, and override methods for equality checks and
 * string representation.
 */
public class Movie implements Serializable {

	private int year;
	private String title;
	private int duration;
	private String[] genres;
	private String rating;
	private double score;
	private String director;
	private String actor1;
	private String actor2;
	private String actor3;

// Constructor
	/**
	 * Constructs a new Movie instance with specified details.
	 * 
	 * @param year     The release year of the movie.
	 * @param title    The title of the movie.
	 * @param duration The duration of the movie in minutes.
	 * @param genres   An array of genres the movie belongs to.
	 * @param rating   The MPAA rating of the movie.
	 * @param score    The score or rating given to the movie, typically out of 10.
	 * @param director The director of the movie.
	 * @param actor1   The lead actor of the movie.
	 * @param actor2   The supporting actor of the movie.
	 * @param actor3   Another supporting actor of the movie.
	 */

	public Movie(int year, String title, int duration, String[] genres, String rating, double score, String director,
			String actor1, String actor2, String actor3) {
		this.year = year;
		this.title = title;
		this.duration = duration;
		this.genres = genres;
		this.rating = rating;
		this.score = score;
		this.director = director;
		this.actor1 = actor1;
		this.actor2 = actor2;
		this.actor3 = actor3;
	}

// Accessor and Mutator Methods

	/**
	 * Returns the year of the movie.
	 * 
	 * @return The year the movie was released.
	 */
	public int getYear() {
		return year;
	}

	/**
	 * Sets the year of the movie.
	 * 
	 * @param year The year to set for the movie.
	 */
	public void setYear(int year) {
		this.year = year;
	}

	/**
	 * Returns the title of the movie.
	 * 
	 * @return The movie's title.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the title of the movie.
	 * 
	 * @param title The title to set for the movie.
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Returns the duration of the movie in minutes.
	 * 
	 * @return The duration of the movie.
	 */
	public int getDuration() {
		return duration;
	}

	/**
	 * Sets the duration of the movie in minutes.
	 * 
	 * @param duration The duration to set for the movie.
	 */
	public void setDuration(int duration) {
		this.duration = duration;
	}

	/**
	 * Returns the genres of the movie.
	 * 
	 * @return An array of genres the movie belongs to.
	 */
	public String[] getGenres() {
		return genres;
	}

	/**
	 * Sets the genres of the movie.
	 * 
	 * @param genres The genres to set for the movie.
	 */
	public void setGenres(String[] genres) {
		this.genres = genres;
	}

	/**
	 * Returns the MPAA rating of the movie.
	 * 
	 * @return The MPAA rating of the movie.
	 */
	public String getRating() {
		return rating;
	}

	/**
	 * Sets the MPAA rating of the movie.
	 * 
	 * @param rating The MPAA rating to set for the movie.
	 */
	public void setRating(String rating) {
		this.rating = rating;
	}

	/**
	 * Returns the score of the movie.
	 * 
	 * @return The score of the movie.
	 */
	public double getScore() {
		return score;
	}

	/**
	 * Sets the score of the movie.
	 * 
	 * @param score The score to set for the movie.
	 */
	public void setScore(double score) {
		this.score = score;
	}

	/**
	 * Returns the director of the movie.
	 * 
	 * @return The director of the movie.
	 */
	public String getDirector() {
		return director;
	}

	/**
	 * Sets the director of the movie.
	 * 
	 * @param director The director to set for the movie.
	 */
	public void setDirector(String director) {
		this.director = director;
	}

	/**
	 * Returns the name of the lead actor of the movie.
	 * 
	 * @return The name of the lead actor.
	 */
	public String getActor1() {
		return actor1;
	}

	/**
	 * Sets the name of the lead actor of the movie.
	 * 
	 * @param actor1 The name of the lead actor to set.
	 */
	public void setActor1(String actor1) {
		this.actor1 = actor1;
	}

	/**
	 * Returns the name of the supporting actor of the movie.
	 * 
	 * @return The name of the supporting actor.
	 */
	public String getActor2() {
		return actor2;
	}

	/**
	 * Sets the name of the supporting actor of the movie.
	 * 
	 * @param actor2 The name of the supporting actor to set.
	 */
	public void setActor2(String actor2) {
		this.actor2 = actor2;
	}

	/**
	 * Returns the name of another supporting actor of the movie.
	 * 
	 * @return The name of another supporting actor.
	 */
	public String getActor3() {
		return actor3;
	}

	/**
	 * Sets the name of another supporting actor of the movie.
	 * 
	 * @param actor3 The name of another supporting actor to set.
	 */
	public void setActor3(String actor3) {
		this.actor3 = actor3;
	}

	/**
	 * Compares this movie to another object to determine equality. Two movies are
	 * considered equal if all their properties are the same.
	 * 
	 * @param obj The object to compare this movie against.
	 * @return true if the given object represents a Movie equivalent to this movie,
	 *         false otherwise.
	 */
// Override equals method
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;

		Movie movie = (Movie) obj;

		if (year != movie.year)
			return false;
		if (duration != movie.duration)
			return false;
		if (Double.compare(movie.score, score) != 0)
			return false;
		if (!title.equals(movie.title))
			return false;
		if (!rating.equals(movie.rating))
			return false;
		if (!director.equals(movie.director))
			return false;
		if (!actor1.equals(movie.actor1))
			return false;
		if (!actor2.equals(movie.actor2))
			return false;
		if (!actor3.equals(movie.actor3))
			return false;
		// Check genres array equality
		if (genres.length != movie.genres.length)
			return false;
		for (int i = 0; i < genres.length; i++) {
			if (!genres[i].equals(movie.genres[i]))
				return false;
		}
		return true;
	}

	/**
	 * Returns a string representation of the movie, including all its properties.
	 * 
	 * @return A string representation of the movie.
	 */
// Override toString method
	@Override
	public String toString() {
		return "Movie{" + "year=" + year + ", title='" + title + '\'' + ", duration=" + duration + ", genres="
				+ Arrays.toString(genres) + ", rating='" + rating + '\'' + ", score=" + score + ", director='"
				+ director + '\'' + ", actor1='" + actor1 + '\'' + ", actor2='" + actor2 + '\'' + ", actor3='" + actor3
				+ '\'' + '}';
	}
}
