import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.*;
import file.MovieDB;
import java.util.ArrayList;

class MovieTriviaTest {

	// instance of movie trivia object to test
	MovieTrivia mt;
	// instance of movieDB object
	MovieDB movieDB;

	@BeforeEach
	void setUp() throws Exception {
		// initialize movie trivia object
		mt = new MovieTrivia();

		// set up movie trivia object
		mt.setUp("moviedata.txt", "movieratings.csv");

		// get instance of movieDB object from movie trivia object
		movieDB = mt.movieDB;
	}

	@Test
	void testSetUp() {
		assertEquals(6, movieDB.getActorsInfo().size(),
				"actorsInfo should contain 6 actors after reading moviedata.txt.");
		assertEquals(7, movieDB.getMoviesInfo().size(),
				"moviesInfo should contain 7 movies after reading movieratings.csv.");

		assertEquals("meryl streep", movieDB.getActorsInfo().get(0).getName(),
				"\"meryl streep\" should be the name of the first actor in actorsInfo.");
		assertEquals(3, movieDB.getActorsInfo().get(0).getMoviesCast().size(),
				"The first actor listed in actorsInfo should have 3 movies in their moviesCasted list.");
		assertEquals("doubt", movieDB.getActorsInfo().get(0).getMoviesCast().get(0),
				"\"doubt\" should be the name of the first movie in the moviesCasted list of the first actor listed in actorsInfo.");

		assertEquals("doubt", movieDB.getMoviesInfo().get(0).getName(),
				"\"doubt\" should be the name of the first movie in moviesInfo.");
		assertEquals(79, movieDB.getMoviesInfo().get(0).getCriticRating(),
				"The critics rating for the first movie in moviesInfo is incorrect.");
		assertEquals(78, movieDB.getMoviesInfo().get(0).getAudienceRating(),
				"The audience rating for the first movie in moviesInfo is incorrect.");
	}

	@Test
	void testInsertActor() {

		// try to insert new actor with new movies
		mt.insertActor("test1", new String[] { "testmovie1", "testmovie2" }, movieDB.getActorsInfo());
		assertEquals(7, movieDB.getActorsInfo().size(),
				"After inserting an actor, the size of actorsInfo should have increased by 1.");
		assertEquals("test1", movieDB.getActorsInfo().get(movieDB.getActorsInfo().size() - 1).getName(),
				"After inserting actor \"test1\", the name of the last actor in actorsInfo should be \"test1\".");
		assertEquals(2, movieDB.getActorsInfo().get(movieDB.getActorsInfo().size() - 1).getMoviesCast().size(),
				"Actor \"test1\" should have 2 movies in their moviesCasted list.");
		assertEquals("testmovie1",
				movieDB.getActorsInfo().get(movieDB.getActorsInfo().size() - 1).getMoviesCast().get(0),
				"\"testmovie1\" should be the first movie in test1's moviesCasted list.");

		// try to insert existing actor with new movies
		mt.insertActor("   Meryl STReep      ", new String[] { "   DOUBT      ", "     Something New     " },
				movieDB.getActorsInfo());
		assertEquals(7, movieDB.getActorsInfo().size(),
				"Since \"meryl streep\" is already in actorsInfo, inserting \"   Meryl STReep      \" again should not increase the size of actorsInfo.");

		// look up and inspect movies for existing actor
		// note, this requires the use of properly implemented selectWhereActorIs method
		// you can comment out these two lines until you have a selectWhereActorIs
		// method
		assertEquals(4, mt.selectWhereActorIs("meryl streep", movieDB.getActorsInfo()).size(),
				"After inserting Meryl Streep again with 2 movies, only one of which is not on the list yet, the number of movies \"meryl streep\" appeared in should be 4.");
		assertTrue(mt.selectWhereActorIs("meryl streep", movieDB.getActorsInfo()).contains("something new"),
				"After inserting Meryl Streep again with a new Movie \"     Something New     \", \"somenthing new\" should appear as one of the movies she has appeared in.");

		// Additional test case: Insert actor with no movies
		mt.insertActor("Souley", new String[] {}, movieDB.getActorsInfo());
		assertEquals(8, movieDB.getActorsInfo().size(),
				"After inserting an actor with no movies, the size of actorsInfo should have increased by 1.");
		assertEquals("souley", movieDB.getActorsInfo().get(movieDB.getActorsInfo().size() - 1).getName(),
				"After inserting actor \"Souley\", the name of the last actor in actorsInfo should be \"souley\".");
		assertEquals(0, movieDB.getActorsInfo().get(movieDB.getActorsInfo().size() - 1).getMoviesCast().size(),
				"Actor \"Souley\" should have no movies in their moviesCasted list.");

		// Additional test case: Insert actor with duplicate movie names
		mt.insertActor("DuplicateActor", new String[] { "duplicateMovie", "duplicateMovie" }, movieDB.getActorsInfo());
		assertEquals(9, movieDB.getActorsInfo().size(),
				"After inserting an actor with duplicate movies, the size of actorsInfo should increase by 1.");
		assertEquals(1, movieDB.getActorsInfo().get(movieDB.getActorsInfo().size() - 1).getMoviesCast().size(),
				"Actor \"DuplicateActor\" should have only 1 unique movie in their moviesCasted list.");

		// Additional test case: Insert actor with mixed-case name and movie names
		mt.insertActor("MixedCaseActor", new String[] { "MixedCaseMovie" }, movieDB.getActorsInfo());
		assertEquals(10, movieDB.getActorsInfo().size(),
				"After inserting an actor with mixed-case name and movie, the size of actorsInfo should increase by 1.");
		assertEquals("mixedcaseactor", movieDB.getActorsInfo().get(movieDB.getActorsInfo().size() - 1).getName(),
				"The actor's name should be normalized to lowercase.");

		// Additional test case: Insert actor with special characters in name
		mt.insertActor("Actor@123", new String[] { "movie1", "movie2" }, movieDB.getActorsInfo());
		assertEquals(11, movieDB.getActorsInfo().size(),
				"After inserting an actor with special characters in the name, the size of actorsInfo should increase by 1.");
		assertEquals("actor@123", movieDB.getActorsInfo().get(movieDB.getActorsInfo().size() - 1).getName(),
				"The actor's name should be normalized to lowercase and retain special characters.");

		// Additional test case: Insert actor with empty name
		mt.insertActor("", new String[] { "movie1" }, movieDB.getActorsInfo());
		assertEquals(12, movieDB.getActorsInfo().size(),
				"After inserting an actor with an empty name, the size of actorsInfo should increase by 1.");
		assertEquals("", movieDB.getActorsInfo().get(movieDB.getActorsInfo().size() - 1).getName(),
				"The actor's name should be an empty string.");
	}

	@Test
	void testInsertRating() {

		// try to insert new ratings for new movie
		mt.insertRating("testmovie", new int[] { 79, 80 }, movieDB.getMoviesInfo());
		assertEquals(8, movieDB.getMoviesInfo().size(),
				"After inserting ratings for a movie that is not in moviesInfo yet, the size of moviesInfo should increase by 1.");
		assertEquals("testmovie", movieDB.getMoviesInfo().get(movieDB.getMoviesInfo().size() - 1).getName(),
				"After inserting a rating for \"testmovie\", the name of the last movie in moviessInfo should be \"testmovie\".");
		assertEquals(79, movieDB.getMoviesInfo().get(movieDB.getMoviesInfo().size() - 1).getCriticRating(),
				"The critics rating for \"testmovie\" is incorrect.");
		assertEquals(80, movieDB.getMoviesInfo().get(movieDB.getMoviesInfo().size() - 1).getAudienceRating(),
				"The audience rating for \"testmovie\" is incorrect.");

		// try to insert new ratings for existing movie
		mt.insertRating("doubt", new int[] { 100, 100 }, movieDB.getMoviesInfo());
		assertEquals(8, movieDB.getMoviesInfo().size(),
				"Since \"doubt\" is already in moviesInfo, inserting ratings for it should not increase the size of moviesInfo.");

		// look up and inspect movies based on newly inserted ratings
		// note, this requires the use of properly implemented selectWhereRatingIs
		// method
		// you can comment out these two lines until you have a selectWhereRatingIs
		// method
		assertEquals(1, mt.selectWhereRatingIs('>', 99, true, movieDB.getMoviesInfo()).size(),
				"After inserting a critic rating of 100 for \"doubt\", there should be 1 movie in moviesInfo with a critic rating greater than 99.");
		assertTrue(mt.selectWhereRatingIs('>', 99, true, movieDB.getMoviesInfo()).contains("doubt"),
				"After inserting the rating for \"doubt\", \"doubt\" should appear as a movie with critic rating greater than 99.");

		// Additional test case: Insert ratings for a movie with invalid ratings
		mt.insertRating("invalidmovie", new int[] { -10, 200 }, movieDB.getMoviesInfo());
		assertEquals(9, movieDB.getMoviesInfo().size(),
				"After inserting ratings for a movie with invalid ratings, the size of moviesInfo should increase by 1.");
		assertEquals(0, movieDB.getMoviesInfo().get(movieDB.getMoviesInfo().size() - 1).getCriticRating(),
				"The critics rating for \"invalidmovie\" should default to 0 for invalid input.");
		assertEquals(100, movieDB.getMoviesInfo().get(movieDB.getMoviesInfo().size() - 1).getAudienceRating(),
				"The audience rating for \"invalidmovie\" should default to 100 for invalid input.");

		// Additional test case: Insert ratings for a movie with no ratings provided
		mt.insertRating("noratingsmovie", new int[] {}, movieDB.getMoviesInfo());
		assertEquals(10, movieDB.getMoviesInfo().size(),
				"After inserting a movie with no ratings provided, the size of moviesInfo should increase by 1.");
		assertEquals(0, movieDB.getMoviesInfo().get(movieDB.getMoviesInfo().size() - 1).getCriticRating(),
				"The critics rating for \"noratingsmovie\" should default to 0 when no ratings are provided.");
		assertEquals(0, movieDB.getMoviesInfo().get(movieDB.getMoviesInfo().size() - 1).getAudienceRating(),
				"The audience rating for \"noratingsmovie\" should default to 0 when no ratings are provided.");

		// Additional test case: Insert ratings for a movie with only one rating
		// provided
		mt.insertRating("singleRatingMovie", new int[] { 85 }, movieDB.getMoviesInfo());
		assertEquals(11, movieDB.getMoviesInfo().size(),
				"After inserting a movie with only one rating provided, the size of moviesInfo should increase by 1.");
		assertEquals(85, movieDB.getMoviesInfo().get(movieDB.getMoviesInfo().size() - 1).getCriticRating(),
				"The critics rating for \"singleRatingMovie\" should be 85.");
		assertEquals(0, movieDB.getMoviesInfo().get(movieDB.getMoviesInfo().size() - 1).getAudienceRating(),
				"The audience rating for \"singleRatingMovie\" should default to 0.");

		// Additional test case: Insert ratings for a movie with both ratings as zero
		mt.insertRating("zeroRatingsMovie", new int[] { 0, 0 }, movieDB.getMoviesInfo());
		assertEquals(12, movieDB.getMoviesInfo().size(),
				"After inserting a movie with both ratings as zero, the size of moviesInfo should increase by 1.");
		assertEquals(0, movieDB.getMoviesInfo().get(movieDB.getMoviesInfo().size() - 1).getCriticRating(),
				"The critics rating for \"zeroRatingsMovie\" should be 0.");
		assertEquals(0, movieDB.getMoviesInfo().get(movieDB.getMoviesInfo().size() - 1).getAudienceRating(),
				"The audience rating for \"zeroRatingsMovie\" should be 0.");

		// Additional test case: Insert ratings for a movie with negative audience
		// rating
		mt.insertRating("negativeAudienceMovie", new int[] { 70, -50 }, movieDB.getMoviesInfo());
		assertEquals(13, movieDB.getMoviesInfo().size(),
				"After inserting a movie with a negative audience rating, the size of moviesInfo should increase by 1.");
		assertEquals(70, movieDB.getMoviesInfo().get(movieDB.getMoviesInfo().size() - 1).getCriticRating(),
				"The critics rating for \"negativeAudienceMovie\" should be 70.");
		assertEquals(0, movieDB.getMoviesInfo().get(movieDB.getMoviesInfo().size() - 1).getAudienceRating(),
				"The audience rating for \"negativeAudienceMovie\" should default to 0 for invalid input.");

		// Additional test case: Insert ratings for a movie with very high ratings
		mt.insertRating("highRatingsMovie", new int[] { 150, 200 }, movieDB.getMoviesInfo());
		assertEquals(14, movieDB.getMoviesInfo().size(),
				"After inserting a movie with very high ratings, the size of moviesInfo should increase by 1.");
		assertEquals(100, movieDB.getMoviesInfo().get(movieDB.getMoviesInfo().size() - 1).getCriticRating(),
				"The critics rating for \"highRatingsMovie\" should default to 100 for values exceeding the maximum.");
		assertEquals(100, movieDB.getMoviesInfo().get(movieDB.getMoviesInfo().size() - 1).getAudienceRating(),
				"The audience rating for \"highRatingsMovie\" should default to 100 for values exceeding the maximum.");
	}

	@Test
	void testSelectWhereActorIs() {
		assertEquals(3, mt.selectWhereActorIs("meryl streep", movieDB.getActorsInfo()).size(),
				"The number of movies \"meryl streep\" has appeared in should be 3.");
		assertEquals("doubt", mt.selectWhereActorIs("meryl streep", movieDB.getActorsInfo()).get(0),
				"\"doubt\" should show up as first in the list of movies \"meryl streep\" has appeared in.");

		// Additional test case: Actor with no movies
		assertEquals(0, mt.selectWhereActorIs("Souley", movieDB.getActorsInfo()).size(),
				"Actor \"Souley\" should have no movies listed.");

		// Additional test case: Actor with mixed-case name
		assertEquals(3, mt.selectWhereActorIs("MERYL STREEP", movieDB.getActorsInfo()).size(),
				"The number of movies \"MERYL STREEP\" has appeared in should be 3, ignoring case.");

		// Additional test case: Actor with special characters in name
		assertEquals(0, mt.selectWhereActorIs("Actor@123", movieDB.getActorsInfo()).size(),
				"Actor \"Actor@123\" should have no movies listed.");

		// Additional test case: Actor with trailing spaces in name
		assertEquals(3, mt.selectWhereActorIs("meryl streep   ", movieDB.getActorsInfo()).size(),
				"The number of movies \"meryl streep   \" has appeared in should be 3, ignoring trailing spaces.");
	}

	@Test
	void testSelectWhereMovieIs() {
		assertEquals(2, mt.selectWhereMovieIs("doubt", movieDB.getActorsInfo()).size(),
				"There should be 2 actors in \"doubt\".");
		assertEquals(true, mt.selectWhereMovieIs("doubt", movieDB.getActorsInfo()).contains("meryl streep"),
				"\"meryl streep\" should be an actor who appeared in \"doubt\".");
		assertEquals(true, mt.selectWhereMovieIs("doubt", movieDB.getActorsInfo()).contains("amy adams"),
				"\"amy adams\" should be an actor who appeared in \"doubt\".");

		// Additional test case: Movie with no actors
		assertEquals(0, mt.selectWhereMovieIs("nonexistentmovie", movieDB.getActorsInfo()).size(),
				"There should be no actors in a nonexistent movie.");

		// Additional test case: Movie with mixed-case name
		assertEquals(2, mt.selectWhereMovieIs("DOUBT", movieDB.getActorsInfo()).size(),
				"There should be 2 actors in \"DOUBT\", ignoring case.");

		// Additional test case: Movie with special characters in name
		assertEquals(0, mt.selectWhereMovieIs("movie@123", movieDB.getActorsInfo()).size(),
				"There should be no actors in a movie with special characters in its name.");

		// Additional test case: Movie with leading spaces in name
		assertEquals(2, mt.selectWhereMovieIs("   doubt", movieDB.getActorsInfo()).size(),
				"There should be 2 actors in \"   doubt\", ignoring leading spaces.");
	}

	@Test
	void testSelectWhereRatingIs() {
		assertEquals(6, mt.selectWhereRatingIs('>', 0, true, movieDB.getMoviesInfo()).size(),
				"There should be 6 movies where critics rating is greater than 0.");
		assertEquals(0, mt.selectWhereRatingIs('=', 65, false, movieDB.getMoviesInfo()).size(),
				"There should be no movie where audience rating is equal to 65.");
		assertEquals(2, mt.selectWhereRatingIs('<', 30, true, movieDB.getMoviesInfo()).size(),
				"There should be 2 movies where critics rating is less than 30.");

		// Additional test case: No movies matching the criteria
		assertEquals(0, mt.selectWhereRatingIs('>', 100, true, movieDB.getMoviesInfo()).size(),
				"There should be no movies where critics rating is greater than 100.");

		// Additional test case: Movies with audience rating less than 50
		assertEquals(2, mt.selectWhereRatingIs('<', 50, false, movieDB.getMoviesInfo()).size(),
				"There should be 2 movies where audience rating is less than 50.");

		// Additional test case: Movies with critic rating equal to 79
		assertEquals(1, mt.selectWhereRatingIs('=', 79, true, movieDB.getMoviesInfo()).size(),
				"There should be 1 movie where critics rating is equal to 79.");

		// Additional test case: Movies with audience rating greater than or equal to 80
		assertEquals(4, mt.selectWhereRatingIs('>', 80, false, movieDB.getMoviesInfo()).size(),
				"There should be 4 movies where audience rating is greater than or equal to 80.");
	}

	@Test
	void testGetCoActors() {
		assertEquals(2, mt.getCoActors("meryl streep", movieDB.getActorsInfo()).size(),
				"\"meryl streep\" should have 2 co-actors.");
		assertTrue(mt.getCoActors("meryl streep", movieDB.getActorsInfo()).contains("tom hanks"),
				"\"tom hanks\" was a co-actor of \"meryl streep\".");
		assertTrue(mt.getCoActors("meryl streep", movieDB.getActorsInfo()).contains("amy adams"),
				"\"amy adams\" was a co-actor of \"meryl streep\".");

		// Additional test case: Actor with no co-actors
		assertEquals(0, mt.getCoActors("Souley", movieDB.getActorsInfo()).size(),
				"Actor \"Souley\" should have no co-actors.");

		// Additional test case: Actor with mixed-case name
		assertTrue(mt.getCoActors("MERYL STREEP", movieDB.getActorsInfo()).contains("tom hanks"),
				"\"tom hanks\" should be a co-actor of \"MERYL STREEP\", ignoring case.");

		// Additional test case: Actor with special characters in name
		assertEquals(0, mt.getCoActors("Actor@123", movieDB.getActorsInfo()).size(),
				"Actor \"Actor@123\" should have no co-actors.");

		// Additional test case: Actor with leading spaces in name
		assertTrue(mt.getCoActors("   meryl streep", movieDB.getActorsInfo()).contains("amy adams"),
				"\"amy adams\" should be a co-actor of \"   meryl streep\", ignoring leading spaces.");
	}

	@Test
	void testGetCommonMovie() {
		assertEquals(1, mt.getCommonMovie("meryl streep", "tom hanks", movieDB.getActorsInfo()).size(),
				"\"tom hanks\" and \"meryl streep\" should have 1 movie in common.");
		assertTrue(mt.getCommonMovie("meryl streep", "tom hanks", movieDB.getActorsInfo()).contains("the post"),
				"\"the post\" should be a common movie between \"tom hanks\" and \"meryl streep\".");

		// Additional test case: Actors with no common movies
		assertEquals(0, mt.getCommonMovie("meryl streep", "nonexistentactor", movieDB.getActorsInfo()).size(),
				"\"meryl streep\" and \"nonexistentactor\" should have no common movies.");

		// Additional test case: Actors with mixed-case names
		assertTrue(mt.getCommonMovie("MERYL STREEP", "TOM HANKS", movieDB.getActorsInfo()).contains("the post"),
				"\"the post\" should be a common movie between \"MERYL STREEP\" and \"TOM HANKS\", ignoring case.");

		// Additional test case: Actors with special characters in names
		assertEquals(0, mt.getCommonMovie("Actor@123", "Actor@456", movieDB.getActorsInfo()).size(),
				"\"Actor@123\" and \"Actor@456\" should have no common movies.");

		// Additional test case: Actors with leading and trailing spaces in names
		assertTrue(mt.getCommonMovie("   meryl streep", "tom hanks   ", movieDB.getActorsInfo()).contains("the post"),
				"\"the post\" should be a common movie between \"   meryl streep\" and \"tom hanks   \", ignoring spaces.");
	}

	@Test
	void testGoodMovies() {
		assertEquals(3, mt.goodMovies(movieDB.getMoviesInfo()).size(),
				"There should be 3 movies that are considered good movies, movies with both critics and audience rating that are greater than or equal to 85.");
		assertTrue(mt.goodMovies(movieDB.getMoviesInfo()).contains("jaws"),
				"\"jaws\" should be considered a good movie, since it's critics and audience ratings are both greater than or equal to 85.");

		// Additional test case: No good movies
		assertEquals(0, mt.goodMovies(new ArrayList<>(movieDB.getMoviesInfo().subList(0, 2))).size(),
				"There should be no good movies in the subset of movies with low ratings.");

		// Additional test case: All movies are good
		assertEquals(3, mt.goodMovies(movieDB.getMoviesInfo()).size(),
				"All movies with ratings >= 85 should be considered good movies.");

		// Create a test movie with exactly 85 ratings for both critic and audience
		ArrayList<Movie> testMovies = new ArrayList<>();
		testMovies.add(new Movie("testMovie", 85, 85));
		assertEquals(1, mt.goodMovies(testMovies).size(),
				"There should be 1 good movie with exactly 85 ratings.");

		// Additional test case: Movies with mixed ratings (one >= 85, one < 85)
		assertEquals(0, mt.goodMovies(new ArrayList<>(movieDB.getMoviesInfo().subList(1, 2))).size(),
				"There should be no good movies when one rating is >= 85 and the other is < 85.");
	}

	@Test
	void testGetCommonActors() {
		assertEquals(1, mt.getCommonActors("doubt", "the post", movieDB.getActorsInfo()).size(),
				"There should be one actor that appeared in both \"doubt\" and \"the post\".");
		assertTrue(mt.getCommonActors("doubt", "the post", movieDB.getActorsInfo()).contains("meryl streep"),
				"The actor that appeared in both \"doubt\" and \"the post\" should be \"meryl streep\".");

		// Additional test case: Movies with no common actors
		assertEquals(0, mt.getCommonActors("doubt", "nonexistentmovie", movieDB.getActorsInfo()).size(),
				"There should be no common actors between \"doubt\" and \"nonexistentmovie\".");

		// Additional test case: Movies with mixed-case names
		assertTrue(mt.getCommonActors("DOUBT", "THE POST", movieDB.getActorsInfo()).contains("meryl streep"),
				"\"meryl streep\" should be a common actor between \"DOUBT\" and \"THE POST\", ignoring case.");

		// Additional test case: Movies with special characters in names
		assertEquals(0, mt.getCommonActors("movie@123", "movie@456", movieDB.getActorsInfo()).size(),
				"There should be no common actors between \"movie@123\" and \"movie@456\".");

		// Additional test case: Movies with leading and trailing spaces in names
		assertTrue(mt.getCommonActors("   doubt", "the post   ", movieDB.getActorsInfo()).contains("meryl streep"),
				"\"meryl streep\" should be a common actor between \"   doubt\" and \"the post   \", ignoring spaces.");
	}

	@Test
	void testGetMean() {
		assertEquals(67.9, mt.getMean(movieDB.getMoviesInfo())[0], 0.1,
				"The mean of all critics ratings is incorrect.");

		// Additional test case: Mean calculation with no movies
		assertEquals(0.0, mt.getMean(new ArrayList<>(movieDB.getMoviesInfo().subList(0, 0)))[0], 0.1,
				"The mean of critics ratings should be 0 when there are no movies.");

		// Additional test case: Mean calculation with identical ratings
		assertEquals(79.0, mt.getMean(new ArrayList<>(movieDB.getMoviesInfo().subList(0, 1)))[0], 0.1,
				"The mean of critics ratings should match the single movie's rating when only one movie is considered.");

		// Create a test movie with zero ratings
		ArrayList<Movie> zeroRatedMovies = new ArrayList<>();
		zeroRatedMovies.add(new Movie("zeroMovie", 0, 0));
		assertEquals(0.0, mt.getMean(zeroRatedMovies)[0], 0.1,
				"The mean of critics ratings should be 0 when all ratings are zero.");

		// Additional test case: Mean calculation with mixed ratings
		assertEquals(39.5, mt.getMean(new ArrayList<>(movieDB.getMoviesInfo().subList(3, 5)))[0], 0.1,
				"The mean of critics ratings should be correctly calculated for mixed ratings.");
	}
}
