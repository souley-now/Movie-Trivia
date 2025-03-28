package movies;

/**
 * Represents a Movie with name, critic rating, and audience rating.
 */
public class Movie {
	
	/**
	 * Name of movie.
	 */
	private String name;
	
	/**
	 * Critic rating.
	 */
	private int criticRating;
	
	/**
	 * Audience rating.
	 */
	private int audienceRating;

	/**
	 * Creates Movie with given name, given critic rating, and given audience rating.
	 * @param name of movie
	 * @param criticRating of movie
	 * @param audienceRating of movie
	 */
	public Movie (String name, int criticRating, int audienceRating) {
		this.name = name.toLowerCase().trim();
		this.criticRating = Math.max(0, Math.min(100, criticRating)); // Ensure rating is between 0 and 100
		this.audienceRating = Math.max(0, Math.min(100, audienceRating));
	}

	/**
	 * @return the name of the movie
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the criticRating of the movie
	 */
	public int getCriticRating() {
		return criticRating;
	}

	/**
	 * @return the audienceRating of the movie
	 */
	public int getAudienceRating() {
		return audienceRating;
	}

	/**
	 * @param criticRating the criticRating to set
	 */
	public void setCriticRating(int criticRating) {
		this.criticRating = Math.max(0, Math.min(100, criticRating));
	}

	/**
	 * @param audienceRating the audienceRating to set
	 */
	public void setAudienceRating(int audienceRating) {
		this.audienceRating = Math.max(0, Math.min(100, audienceRating));
	}
	
	/**
	 * Returns String containing the movie's name, the critic rating and the audience rating.
	 */
	@Override
	public String toString () {
		return "Name: " + name + " Critic Rating: " + this.criticRating + " Audience Rating: " + this.audienceRating; 
	}
	
}
