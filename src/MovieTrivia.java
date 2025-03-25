import file.MovieDB;
import java.util.ArrayList;
import movies.Actor;
import movies.Movie;

/**
 * Movie trivia class providing different methods for querying and updating a
 * movie database.
 */
public class MovieTrivia {

	/**
	 * Create instance of movie database
	 */
	MovieDB movieDB = new MovieDB();

	public static void main(String[] args) {

		// create instance of movie trivia class
		MovieTrivia mt = new MovieTrivia();

		// setup movie trivia class
		mt.setUp("moviedata.txt", "movieratings.csv");
	}

	/**
	 * Sets up the Movie Trivia class
	 * @param movieData    .txt file
	 * @param movieRatings .csv file
	 */
	public void setUp(String movieData, String movieRatings) {
		// load movie database files
		movieDB.setUp(movieData, movieRatings);

		// print all actors and movies
		this.printAllActors();
		this.printAllMovies();
	}

	/**
	 * Prints a list of all actors and the movies they acted in.
	 */
	public void printAllActors() {
		System.out.println(movieDB.getActorsInfo());
	}

	/**
	 * Prints a list of all movies and their ratings.
	 */
	public void printAllMovies() {
		System.out.println(movieDB.getMoviesInfo());
	}

	/**
	 * Inserts given actor and his/her movies into database
	 * @param actor is the actor name as a string
	 * @param movies is a String array of movie names that the actor has acted in
	 * @param actorsInfo is the ArrayList that is to be inserted into/updated
	 */
	public void insertActor(String actor, String[] movies, ArrayList<Actor> actorsInfo) {
		// Normalize actor name
		actor = actor.trim().toLowerCase();

		// Normalize movie names
		for (int i = 0; i < movies.length; i++) {
			movies[i] = movies[i].trim().toLowerCase();
		}

		// Check if actor already exists in actorsInfo
		for (Actor a : actorsInfo) {
			if (a.getName().equalsIgnoreCase(actor)) {
				// Update movies list
				for (String movie : movies) {
					if (!a.getMoviesCast().contains(movie)) {
						a.getMoviesCast().add(movie);
					}
				}
				return;
			}
		}

		// Create new actor object
		Actor newActor = new Actor(actor);

		// Add unique movies to actor object
		for (String movie : movies) {
			if (!newActor.getMoviesCast().contains(movie)) {
				newActor.getMoviesCast().add(movie);
			}
		}

		// Add actor to actorsInfo
		actorsInfo.add(newActor);
	}

	/**
	 * Inserts given ratings for given movie into database
	 * @param movie is the movie name as a string
	 * @param ratings is an int array with 2 elements: the critics rating at index 0 and the audience rating at index 1
	 * @param moviesInfo is the ArrayList that is to be inserted into updated
	 */
	public void insertRating(String movie, int[] ratings, ArrayList<Movie> moviesInfo) {
		// Trim and normalize movie name
		movie = movie.trim().toLowerCase();

		// Check if movie already exists in moviesInfo
		for (Movie m : moviesInfo) {
			if (m.getName().equalsIgnoreCase(movie)) {
				// Update ratings
				m.setCriticRating(Math.max(0, Math.min(100, ratings.length > 0 ? ratings[0] : 0)));
				m.setAudienceRating(Math.max(0, Math.min(100, ratings.length > 1 ? ratings[1] : 0)));
				return;
			}
		}

		// Add new movie if not found
		int criticRating = Math.max(0, Math.min(100, ratings.length > 0 ? ratings[0] : 0));
		int audienceRating = Math.max(0, Math.min(100, ratings.length > 1 ? ratings[1] : 0));
		moviesInfo.add(new Movie(movie, criticRating, audienceRating));
	}

	/**
	 * Given an actor, returns the list of all movies
	 * @param actor is the name of an actor as a String
	 * @param actorsInfo is the ArrayList to get the data from
	 * @return list of movies
	 */
	public ArrayList<String> selectWhereActorIs(String actor, ArrayList<Actor> actorsInfo) {
		// create list to store movies
		ArrayList<String> movies = new ArrayList<String>();

		// search for actor in actorsInfo
		for (Actor a : actorsInfo) {
			if (a.getName().equalsIgnoreCase(actor.trim())) {
				// add movies to list
				movies.addAll(a.getMoviesCast());
			}
		}

		// return list of movies
		return movies;
	}

	/**
	 * Given a movie, returns the list of all actors in that movie
	 * @param movie is the name of a movie as a String
	 * @param actorsInfo is the ArrayList to get the data from
	 * @return list of actors in that movie
	 */
	public ArrayList<String> selectWhereMovieIs(String movie, ArrayList<Actor> actorsInfo) {
		// Normalize movie name
		movie = movie.trim().toLowerCase();

		// Create list to store actors
		ArrayList<String> actors = new ArrayList<>();

		// Search for movie in actorsInfo
		for (Actor a : actorsInfo) {
			if (a.getMoviesCast().contains(movie)) {
				actors.add(a.getName());
			}
		}

		// Return list of actors
		return actors;
	}

	/**
	 * returns a list of movies that satisfy an inequality or equality, based on the
	 * comparison argument and the targeted rating argument
	 * @param comparison is either ‘=’, ‘>’, or ‘< ‘ and is passed in as a char
	 * @param targetRating is an integer
	 * @param isCritic is a boolean that represents whether we are interested in the critics rating or the audience rating. true = critic ratings, false = audience ratings.
	 * @param moviesInfo   is the ArrayList to get the data from
	 * @return a list of movies that satisfy an inequality or equality
	 */
	public ArrayList<String> selectWhereRatingIs(char comparison, int targetRating, boolean isCritic,
			ArrayList<Movie> moviesInfo) {
		// create list to store movies
		ArrayList<String> movies = new ArrayList<String>();

		// validate input
		if (targetRating < 0 || targetRating > 100 || (comparison != '=' && comparison != '>' && comparison != '<')) {
			return movies;
		}
		// search for movies in moviesInfo
		for (Movie m : moviesInfo) {
			// check if movie rating meets criteria
			if (isCritic) {
				if (comparison == '=' && m.getCriticRating() == targetRating) {
					movies.add(m.getName());
				} else if (comparison == '>' && m.getCriticRating() > targetRating) {
					movies.add(m.getName());
				} else if (comparison == '<' && m.getCriticRating() < targetRating) {
					movies.add(m.getName());
				}
			} else {
				if (comparison == '=' && m.getAudienceRating() == targetRating) {
					movies.add(m.getName());
				} else if (comparison == '>' && m.getAudienceRating() > targetRating) {
					movies.add(m.getName());
				} else if (comparison == '<' && m.getAudienceRating() < targetRating) {
					movies.add(m.getName());
				}
			}
		}

		// Return list of movies
		return movies;
	}

	/**
	 * Returns a list of all actors that the given actor has ever worked with in any movie except the actor herself/himself.
	 * @param actor      is the name of an actor as a String
	 * @param actorsInfo is the ArrayList to search through
	 * @return a list of all actors that the given actor has ever worked with
	 */
	public ArrayList<String> getCoActors(String actor, ArrayList<Actor> actorsInfo) {
		// create list to store co-actors
		ArrayList<String> coActors = new ArrayList<String>();

		// search for actor in actorsInfo
		for (Actor a : actorsInfo) {
			if (a.getName().equalsIgnoreCase(actor.trim())) {
				// search for co-actors
				for (String movie : a.getMoviesCast()) {
					for (Actor b : actorsInfo) {
						if (b.getMoviesCast().contains(movie) && !b.getName().equalsIgnoreCase(actor.trim())) {
							if (!coActors.contains(b.getName())) {
								coActors.add(b.getName());
							}
						}
					}
				}
			}
		}

		// return list of co-actors
		return coActors;
	}

	/**
	 * Returns a list of movie names where both actors were cast. You may think of how to use the 5 basic utility methods implemented previously.
	 * @param actor1 actor names as Strings
	 * @param actor2 actor names as Strings
	 * @param actorsInfo is the ArrayList to search through
	 * @return list of movie names where both actors were cast
	 */
	public ArrayList<String> getCommonMovie(String actor1, String actor2, ArrayList<Actor> actorsInfo) {

		// create list to store common movies
		ArrayList<String> commonMovies = new ArrayList<String>();

		// get movies for actor1
		ArrayList<String> moviesActor1 = selectWhereActorIs(actor1.trim(), actorsInfo);

		// get movies for actor2
		ArrayList<String> moviesActor2 = selectWhereActorIs(actor2.trim(), actorsInfo);

		// find common movies
		for (String movie : moviesActor1) {
			if (moviesActor2.contains(movie)) {
				commonMovies.add(movie);
			}
		}

		// return list of common movies
		return commonMovies;
	}

	/**
	 * @param moviesInfo is the ArrayList to search through
	 * @return a list of movie names that both critics and the audience have rated above 85 (>= 85).
	 */
	public ArrayList<String> goodMovies(ArrayList<Movie> moviesInfo) {
		// create list to store good movies
		ArrayList<String> goodMovies = new ArrayList<>();

		// Fix: Include movies with exactly 85 ratings
		for (Movie m : moviesInfo) {
			if (m.getCriticRating() >= 85 && m.getAudienceRating() >= 85) {
				goodMovies.add(m.getName());
			}
		}

		return goodMovies;
	}

	/**
	 * Given a pair of movies, this method returns a list of actors that acted in both movies.
	 * @param movie1 are the names of movies as Strings
	 * @param movie2 are the names of movies as Strings
	 * @param actorsInfo is the actor ArrayList
	 * @return a list of actors that acted in both movies
	 */

	public ArrayList<String> getCommonActors(String movie1, String movie2, ArrayList<Actor> actorsInfo) {
		// Normalize movie names
		movie1 = movie1.trim().toLowerCase();
		movie2 = movie2.trim().toLowerCase();

		// Create list to store common actors
		ArrayList<String> commonActors = new ArrayList<>();

		// Search for common actors
		for (Actor a : actorsInfo) {
			if (a.getMoviesCast().contains(movie1) && a.getMoviesCast().contains(movie2)) {
				commonActors.add(a.getName());
			}
		}

		// Return list of common actors
		return commonActors;
	}

	/**
	 * Given the moviesInfo DB, this static method returns the mean value of the critics ratings and the audience ratings.
	 * @param moviesInfo is the ArrayList to search through
	 * @return the mean values as a double array, where the 1st item (index 0) is the mean of all critics ratings and the 2nd item (index 1) is the mean of all audience ratings
	 */
	public static double[] getMean(ArrayList<Movie> moviesInfo) {
	    // Check if moviesInfo is null or empty
	    if (moviesInfo == null || moviesInfo.isEmpty()) {
	        return new double[] { 0.0, 0.0 };
	    }

	    // Create variables to store total ratings
	    double totalCriticRating = 0.0;
	    double totalAudienceRating = 0.0;

	    // Calculate total ratings
	    for (Movie m : moviesInfo) {
	        totalCriticRating += m.getCriticRating();
	        totalAudienceRating += m.getAudienceRating();
	    }

	    // Calculate mean ratings
	    double meanCriticRating = totalCriticRating / moviesInfo.size();
	    double meanAudienceRating = totalAudienceRating / moviesInfo.size();

	    // Return mean ratings
	    return new double[] { meanCriticRating, meanAudienceRating };
	}

}
