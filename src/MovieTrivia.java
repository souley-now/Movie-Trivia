import java.util.ArrayList;

import file.MovieDB;
import movies.Actor;
import movies.Movie;

/**
 * Movie trivia class providing different methods for querying and updating a movie database.
 */
public class MovieTrivia {
	
	/**
	 * Create instance of movie database
	 */
	MovieDB movieDB = new MovieDB();
	
	
	public static void main(String[] args) {
		
		//create instance of movie trivia class
		MovieTrivia mt = new MovieTrivia();
		
		//setup movie trivia class
		mt.setUp("moviedata.txt", "movieratings.csv");
	}
	
	/**
	 * Sets up the Movie Trivia class
	 * @param movieData .txt file
	 * @param movieRatings .csv file
	 */
	public void setUp(String movieData, String movieRatings) {
		//load movie database files
		movieDB.setUp(movieData, movieRatings);
		
		//print all actors and movies
		this.printAllActors();
		this.printAllMovies();		
	}
	
	/**
	 * Prints a list of all actors and the movies they acted in.
	 */
	public void printAllActors () {
		System.out.println(movieDB.getActorsInfo());
	}
	
	/**
	 * Prints a list of all movies and their ratings.
	 */
	public void printAllMovies () {
		System.out.println(movieDB.getMoviesInfo());
	}
	
	
	// TODO add additional methods as specified in the instructions PDF
	public void insertActor (String actor, String [] movies, ArrayList <Actor> actorsInfo)
	{
		//create new actor object
		Actor newActor = new Actor(actor);
		
		//add movies to actor object
		for (String movie : movies)
		{
			newActor.getMoviesCast().add(movie);
		}
		
		//add actor to actorsInfo
		actorsInfo.add(newActor);
	}
	public void insertRating (String movie, int [] ratings, ArrayList <Movie> moviesInfo)
	{
		//create new movie object
		Movie newMovie = new Movie(movie, ratings[0], ratings[1]);
		
		//add movie to moviesInfo
		moviesInfo.add(newMovie);

	}

	public ArrayList <String> selectWhereActorIs (String actor, ArrayList <Actor> actorsInfo)
	{
		//create list to store movies
		ArrayList <String> movies = new ArrayList <String> ();
		
		//search for actor in actorsInfo
		for (Actor a : actorsInfo)
		{
			if (a.getName().equals(actor))
			{
				//add movies to list
				movies.addAll(a.getMoviesCast());
			}
		}
		
		//return list of movies
		return movies;
	}

	public ArrayList <String> selectWhereMovieIs (String movie, ArrayList <Actor> actorsInfo) {
		//create list to store actors
		ArrayList <String> actors = new ArrayList <String> ();
		
		//search for movie in actorsInfo
		for (Actor a : actorsInfo)
		{
			if (a.getMoviesCast().contains(movie))
			{
				//add actor to list
				actors.add(a.getName());
			}
		}
		
		//return list of actors
		return actors;
	}
	public ArrayList <String> selectWhereRatingIs (char comparison, int targetRating, boolean isCritic,ArrayList <Movie> moviesInfo)
	{
		//create list to store movies
		ArrayList <String> movies = new ArrayList <String> ();
		
		//search for movies in moviesInfo
		for (Movie m : moviesInfo)
		{
			//check if movie rating meets criteria
			if (isCritic)
			{
				if (comparison == '=' && m.getCriticRating() == targetRating)
				{
					movies.add(m.getName());
				}
				else if (comparison == '>' && m.getCriticRating() > targetRating)
				{
					movies.add(m.getName());
				}
				else if (comparison == '<' && m.getCriticRating() < targetRating)
				{
					movies.add(m.getName());
				}
			}
			else
			{
				if (comparison == '=' && m.getAudienceRating() == targetRating)
				{
					movies.add(m.getName());
				}
				else if (comparison == '>' && m.getAudienceRating() > targetRating)
				{
					movies.add(m.getName());
				}
				else if (comparison == '<' && m.getAudienceRating() < targetRating)
				{
					movies.add(m.getName());
				}
			}
		}
		
		//return list of movies
		return movies;
	}

	public ArrayList <String> getCoActors (String actor, ArrayList <Actor> actorsInfo)
	{
		//create list to store co-actors
		ArrayList <String> coActors = new ArrayList <String> ();
		
		//search for actor in actorsInfo
		for (Actor a : actorsInfo)
		{
			if (a.getName().equals(actor))
			{
				//search for co-actors
				for (String movie : a.getMoviesCast())
				{
					for (Actor b : actorsInfo)
					{
						if (b.getMoviesCast().contains(movie) && !b.getName().equals(actor))
						{
							coActors.add(b.getName());
						}
					}
				}
			}
		}
		
		//return list of co-actors
		return coActors;
	}
	public ArrayList <String> getCommonMovie (String actor1, String actor2, ArrayList <Actor>
actorsInfo)
	{
		//create list to store common movies
		ArrayList <String> commonMovies = new ArrayList <String> ();
		
		//search for actor1 in actorsInfo
		for (Actor a : actorsInfo)
		{
			if (a.getName().equals(actor1))
			{
				//search for common movies
				for (String movie : a.getMoviesCast())
				{
					if (actorsInfo.get(actorsInfo.indexOf(a)).getMoviesCast().contains(movie))
					{
						commonMovies.add(movie);
					}
				}
			}
		}
		
		//return list of common movies
		return commonMovies;
	}
	
	public ArrayList <String> goodMovies (ArrayList <Movie> moviesInfo)
	{
		//create list to store good movies
		ArrayList <String> goodMovies = new ArrayList <String> ();
		
		//search for good movies
		for (Movie m : moviesInfo)
		{
			if (m.getCriticRating() > 85 && m.getAudienceRating() > 85)
			{
				goodMovies.add(m.getName());
			}
		}
		
		//return list of good movies
		return goodMovies;
	}

	public static double [] getMean (ArrayList <Movie> moviesInfo)
	{
		//create array to store mean ratings
		double [] mean = new double [2];
		
		//calculate mean ratings
		for (Movie m : moviesInfo)
		{
			mean[0] += m.getCriticRating();
			mean[1] += m.getAudienceRating();
		}
		
		mean[0] /= moviesInfo.size();
		mean[1] /= moviesInfo.size();
		
		//return mean ratings
		return mean;
	}


}
