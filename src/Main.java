// -----------------------------------------------------
// Assignment (2)
// Question: (Main Driver)
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

import java.util.*;
import java.io.*;
//Custom checked exceptions

/**
 * This class contains methods for managing a movie database, including
 * initializing genre files, navigating through movie arrays, and handling movie
 * records with serialization and deserialization.
 */
public class Main {

	/**
	 * Initializes genre files and a manifest file listing all genres. Each genre
	 * will have its own CSV file created if it doesn't exist.
	 * 
	 * @throws IOException If an I/O error occurs writing to or creating the file.
	 */
	public static void initializeGenreFilesAndManifest() throws IOException {
		String[] genres = getValidGenres();
		try (BufferedWriter writer = new BufferedWriter(new FileWriter("part2_manifest.txt", false))) { // false to
																										// overwrite
			for (String genre : genres) {
				File genreFile = new File(genre + ".csv");
				if (!genreFile.exists()) {
					genreFile.createNewFile();
				}
				writer.write(genreFile.getName() + "\n");
			}
			writer.flush();
		}
	}

	private static int currentGenre = -1;
	private static String[] processedGenres = new String[100]; // Assuming you won't exceed 100 genres.
	private static int processedGenresCount = 0;
	private static String[] genres = { "Musical", "Comedy", "Animation", "Adventure", "Drama", "Crime", "Biography",
			"Horror", "Action", "Documentary", "Fantasy", "Mystery", "Sci-fi", "Family", "Romance", "Thriller",
			"Western" };

	private static Scanner scanner = new Scanner(System.in);
	private static int[] currentPositions; // To keep track of the current position in each genre array

	/**
	 * The main method that orchestrates the flow of the program including
	 * initializing genre files, processing movie records, and navigating movie
	 * arrays.
	 * 
	 * @param args Command line arguments (not used).
	 */
	public static void main(String[] args) {
		Movie[][] all_movies = do_part3("part3_manifest.txt"); // Placeholder for actual deserialization call

		navigateMovieArrays(all_movies);
		try {
			initializeGenreFilesAndManifest(); // Ensure all genres are initialized and listed in part2_manifest
			String part1_manifest = "part1_manifest.txt";
			try (BufferedWriter badRecordsWriter = new BufferedWriter(new FileWriter("bad_movie_records.txt", false))) { // false
																															// to
																															// overwrite
				String part2_manifest = do_part1(part1_manifest, badRecordsWriter);
				String part3_manifest = do_part2(part2_manifest);
				do_part3(part3_manifest);

			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Provides a menu to navigate through movie arrays by genre. Allows the user to
	 * select a movie genre and navigate through the movies within that genre.
	 * 
	 * @param all_movies A 2D array containing movie arrays categorized by genre.
	 */
	public static void navigateMovieArrays(Movie[][] all_movies) {
		// Initialization moved here for clarity; this should be done after all_movies
		// is populated
		currentPositions = new int[all_movies.length];

		boolean exit = false;
		while (!exit) {
			System.out.println("-------------------------------");
			System.out.println("Main Menu");
			System.out.println("-------------------------------");
			System.out.println("s - Select a movie array to navigate");
			System.out.println("n - Navigate " + (currentGenre >= 0 ? genres[currentGenre] : "genre") + " movies ("
					+ (currentGenre >= 0 ? all_movies[currentGenre].length : 0) + " records)");
			System.out.println("x - Exit");
			System.out.println("-------------------------------");
			System.out.print("Enter Your Choice: ");
			String choice = scanner.nextLine();

			switch (choice) {
			case "s":
				selectMovieArray(all_movies);
				break;
			case "n":
				if (currentGenre != -1) { // Ensure there's a genre selected
					navigateGenreMovies(all_movies[currentGenre], currentGenre);
				} else {
					System.out.println("Please select a genre first.");
				}
				break;
			case "x":
				exit = true;
				break;
			default:
				System.out.println("Invalid choice. Please enter 's' to select, 'n' to navigate, or 'x' to exit.");
			}
		}
	}

	/**
	 * Allows the user to navigate through movies within a selected genre, providing
	 * options to move forward or backward through the list.
	 * 
	 * @param genreMovies The array of movies within the selected genre.
	 * @param genreIndex  The index of the selected genre in the genres array.
	 */
	private static void navigateGenreMovies(Movie[] genreMovies, int genreIndex) {
		if (genreMovies.length == 0) {
			System.out.println("There are no movies in the selected genre.");
			return;
		}

		System.out.println("Navigating " + genres[genreIndex] + " movies (" + genreMovies.length + ")");
		boolean keepNavigating = true;

		while (keepNavigating) {
			System.out.println("Current movie: " + genreMovies[currentPositions[genreIndex]].toString()); // Assuming
																											// toString
																											// is
																											// overridden
																											// in Movie
			System.out.print("Enter Your Choice (0 to return to main menu): ");
			int n = scanner.nextInt();
			scanner.nextLine(); // Consume newline

			if (n == 0) {
				keepNavigating = false;
			} else if (n < 0) {
				// Navigate up
				int newPosition = Math.max(0, currentPositions[genreIndex] + n);
				if (newPosition == 0) {
					System.out.println("BOF has been reached");
				}
				currentPositions[genreIndex] = newPosition;
			} else {
				// Navigate down
				int newPosition = Math.min(genreMovies.length - 1, currentPositions[genreIndex] + n - 1);
				if (newPosition == genreMovies.length - 1) {
					System.out.println("EOF has been reached");
				}
				currentPositions[genreIndex] = newPosition;
			}
		}
	}

	/**
	 * Presents a submenu for selecting a movie array by genre for navigation.
	 * 
	 * @param all_movies A 2D array of movies categorized by genre.
	 */
	private static void selectMovieArray(Movie[][] all_movies) {
		System.out.println("------------------------------");
		System.out.println("Genre Sub-Menu");
		for (int i = 0; i < genres.length; i++) {
			// Make sure each genre aligns with an existing movie array before displaying
			System.out.println((i + 1) + " - " + genres[i] + " ("
					+ (i < all_movies.length ? all_movies[i].length : "N/A") + " movies)");
		}
		System.out.println((genres.length + 1) + " - Exit");
		System.out.println("------------------------------");
		System.out.print("Enter Your Choice: ");
		int genreChoice = scanner.nextInt() - 1;
		scanner.nextLine(); // Consume newline

		if (genreChoice >= 0 && genreChoice < genres.length) {
			if (genreChoice < all_movies.length) {
				currentGenre = genreChoice; // Correctly set the current genre for navigation
				navigateGenreMovies(all_movies[genreChoice], genreChoice);
			} else {
				// Handle case where there's no corresponding movie array for the selected genre
				System.out.println("No movies available for this genre.");
			}
		} else if (genreChoice == genres.length) {
			// Exit condition correctly handled by doing nothing
		} else {
			System.out.println("Invalid selection. Please select a number between 1 and " + (genres.length + 1) + ".");
		}

	}

	/**
	 * Processes part1 of the operation by initializing CSV files for all genres and
	 * distributing movies into their respective genre file.
	 * 
	 * @param part1_manifest   The filename of the part1 manifest.
	 * @param badRecordsWriter A BufferedWriter for logging bad movie records.
	 * @return The filename of the part2 manifest.
	 * @throws IOException If an I/O error occurs.
	 */
	// Method for Part 1
	public static String do_part1(String part1_manifest, BufferedWriter badRecordsWriter) throws IOException {

		// Initialize CSV files for all genres
		for (String genre : getValidGenres()) {
			File genreFile = new File(genre + ".csv");
			// Overwrite the file by simply creating a new FileWriter instance without
			// appending mode
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(genreFile, false))) {
				// Optionally write headers or leave it empty to just ensure the file is
				// recreated
			} catch (IOException e) {
				System.err.println("Error while creating/overwriting the file for genre: " + genre);
				e.printStackTrace();
			}
		}

		new BufferedWriter(new FileWriter("part2_manifest.txt")).close();
		try {
			// Read manifest file
			BufferedReader manifestReader = new BufferedReader(new FileReader(part1_manifest));
			String line;
			while ((line = manifestReader.readLine()) != null) {
				partitionMovies(line, badRecordsWriter);
			}
			manifestReader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return "part2_manifest.txt";
	}

	/**
	 * Retrieves an array of valid genres that the application recognizes. This
	 * method is used to ensure that only movies belonging to predefined genres are
	 * processed and categorized.
	 * 
	 * @return An array of Strings representing valid genres.
	 */
	private static String[] getValidGenres() {
		return new String[] { "Musical", "Comedy", "Animation", "Adventure", "Drama", "Crime", "Biography", "Horror",
				"Action", "Documentary", "Fantasy", "Mystery", "Sci-fi", "Family", "Romance", "Thriller", "Western" };
	}

	// Method to partition movies
	/**
	 * Partitions movies based on the genre by reading movie records from a given
	 * file, writing valid records into corresponding genre-specific CSV files, and
	 * logging invalid records into a bad records file.
	 * 
	 * @param inputFile        The name of the file containing movie records to be
	 *                         partitioned.
	 * @param badRecordsWriter A BufferedWriter to log records that fail validation.
	 * @throws IOException If an error occurs during file reading or writing.
	 */
	private static void partitionMovies(String inputFile, BufferedWriter badRecordsWriter) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(inputFile));
		String line;
		int lineNumber = 0; // Initialize line number counter

		while ((line = reader.readLine()) != null) {
			lineNumber++; // Increment line number for each line read
			try {
				Movie movie = parseMovie(line);

				// Assuming each movie has only one genre for simplicity
				String genre = movie.getGenres()[0];
				File genreFile = new File(genre + ".csv");

				// Check if this genre was already processed
				if (!isGenreProcessed(genre)) {
					// If not, mark it as processed
					processedGenres[processedGenresCount++] = genre;
					// Write to part2_manifest.txt
					try (BufferedWriter manifestWriter = new BufferedWriter(
							new FileWriter("part2_manifest.txt", true))) {
						manifestWriter.write(genreFile.getName() + "\n");
					}
				}

				// Write movie to genre-specific CSV file
				try (BufferedWriter genreWriter = new BufferedWriter(new FileWriter(genreFile, true))) {
					genreWriter.write(line + "\n");
				}
			} catch (SyntaxErrorException | SemanticErrorException e) {
				// Log the bad record and the exception message to bad_movie_records.txt
				badRecordsWriter.write("Line " + lineNumber + ": " + line + " - Error: " + e.getMessage() + "\n");
				badRecordsWriter.flush(); // Ensure that the data is written out immediately
			}
		}
		reader.close();
	}

	/**
	 * Updates the manifest for part2 by adding the genre file name if it hasn't
	 * been processed yet. This ensures that each genre is listed once in the
	 * manifest, corresponding to its CSV file.
	 * 
	 * @param genre The genre being processed.
	 */
	private static void updateManifestForPart2(String genre) {
		// Assuming you're tracking written genres with processedGenres[] and
		// processedGenresCount
		if (!isGenreProcessed(genre)) {
			processedGenres[processedGenresCount++] = genre;
			try (BufferedWriter manifestWriter = new BufferedWriter(new FileWriter("part2_manifest.txt", true))) {
				manifestWriter.write(genre + ".csv\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Checks if a genre has already been processed by looking it up in an array of
	 * processed genres. This method helps in avoiding duplicate entries in the
	 * manifest file.
	 * 
	 * @param genre The genre to check.
	 * @return true if the genre has already been processed; false otherwise.
	 */
	private static boolean isGenreProcessed(String genre) {
		for (int i = 0; i < processedGenresCount; i++) {
			if (processedGenres[i].equals(genre)) {
				return true;
			}
		}
		return false;
	}

	// Method to parse a movie record

	/**
	 * Parses a movie record from a string line. This method extracts movie details
	 * and constructs a Movie object if the record is valid. It throws exceptions if
	 * the record has syntax or semantic errors.
	 * 
	 * @param line A string representing a movie record.
	 * @return A Movie object constructed from the parsed record.
	 * @throws SyntaxErrorException   If the record has syntax errors.
	 * @throws SemanticErrorException If the record has semantic errors.
	 */
	private static Movie parseMovie(String line) throws SyntaxErrorException, SemanticErrorException {
		// Split the line into fields
		String[] fields = line.split(",");

		// Check if all fields are present
		if (fields.length != 10) {
			throw new SyntaxErrorException("ExcessFieldsException: Excess fields in the record");
		}

		// Extract fields
		int year;
		try {
			year = Integer.parseInt(fields[0].trim());
		} catch (NumberFormatException e) {
			throw new SemanticErrorException("BadYearException: Invalid year format");
		}
		String title = fields[1].trim();
		int duration;
		try {
			duration = Integer.parseInt(fields[2].trim());
		} catch (NumberFormatException e) {
			throw new SemanticErrorException("BadDurationException: Invalid duration format");
		}
		String[] genres = fields[3].trim().split(";");
		String rating = fields[4].trim();
		double score;
		try {
			score = Double.parseDouble(fields[5].trim());
		} catch (NumberFormatException e) {
			throw new SemanticErrorException("BadScoreException: Invalid score format");
		}
		String director = fields[6].trim();
		String actor1 = fields[7].trim();
		String actor2 = fields[8].trim();
		String actor3 = fields[9].trim();

		for (String genre : genres) {
			if (!isValidGenre(genre)) {
				throw new SemanticErrorException("BadGenreException: Invalid genre - " + genre);
			}
		}
		// Check for semantic errors
		if (year < 1990 || year > 1999) {
			throw new SemanticErrorException("BadYearException: Invalid year");
		}
		if (title.isEmpty()) {
			throw new SemanticErrorException("BadTitleException: Title cannot be empty");
		}
		if (duration < 30 || duration > 300) {
			throw new SemanticErrorException("BadDurationException: Invalid duration");
		}
		if (genres.length == 0) {
			throw new SemanticErrorException("BadGenreException: No genres specified");
		}
		if (rating.isEmpty() || (!rating.equals("G") && !rating.equals("Unrated") && !rating.equals("PG")
				&& !rating.equals("PG-13") && !rating.equals("NC-17") && !rating.equals("R"))) {
			throw new SemanticErrorException("BadRatingException: Invalid rating");
		}
		if (score > 10) {
			throw new SemanticErrorException("BadScoreException: Invalid score");
		}
		// Create and return movie object
		return new Movie(year, title, duration, genres, rating, score, director, actor1, actor2, actor3);
	}

	// Method to validate genre

	/**
	 * Validates if a given genre is recognized by the application.
	 * 
	 * @param genre The genre to validate.
	 * @return true if the genre is valid; false otherwise.
	 */
	private static boolean isValidGenre(String genre) {
		switch (genre) {
		case "Musical":
		case "Comedy":
		case "Animation":
		case "Adventure":
		case "Drama":
		case "Crime":
		case "Biography":
		case "Horror":
		case "Action":
		case "Documentary":
		case "Fantasy":
		case "Mystery":
		case "Sci-fi":
		case "Family":
		case "Romance":
		case "Thriller":
		case "Western":
			return true;
		default:
			return false;
		}
	}

	// Exception classes
	// Exception classes
	/**
	 * Represents syntax errors encountered during parsing movie records.
	 */
	static class SyntaxErrorException extends Exception {
		public SyntaxErrorException(String message) {
			super(message);
		}
	}

	/**
	 * Represents semantic errors encountered during parsing movie records.
	 */
	static class SemanticErrorException extends Exception {
		public SemanticErrorException(String message) {
			super(message);
		}
	}

	/**
	 * Processes part2 of the operation by loading movies from genre-specific files
	 * and serializing them into binary files.
	 * 
	 * @param part2_manifest The filename of the part2 manifest.
	 * @return The filename of the part3 manifest.
	 * @throws IOException If an I/O error occurs.
	 */
	// Method for Part 2
	public static String do_part2(String part2_manifest) throws IOException {
		// Clear or recreate part3_manifest.txt at the beginning
		new PrintWriter("part3_manifest.txt").close(); // This clears the file

		try (BufferedReader manifestReader = new BufferedReader(new FileReader(part2_manifest))) {
			String line;
			while ((line = manifestReader.readLine()) != null) {
				loadAndSerialize(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "part3_manifest.txt";
	}

	// Method to load movies from genre file and serialize into binary file
	private static void loadAndSerialize(String genreFileName) {
		try {
			// Ensure directory exists or is correctly referenced.
			File csvFile = new File(genreFileName);
			if (!csvFile.exists()) {
				System.out.println("CSV file does not exist: " + genreFileName);
				return;
			}

			Movie[] movies = loadMoviesFromFile(genreFileName);
			String binaryFileName = genreFileName.replace(".csv", ".ser");

			// System.out.println("Serializing: " + binaryFileName);
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(binaryFileName));
			objectOutputStream.writeObject(movies);
			objectOutputStream.close();

			BufferedWriter manifestWriter = new BufferedWriter(new FileWriter("part3_manifest.txt", true));
			manifestWriter.write(binaryFileName + "\n");
			manifestWriter.close();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	// Method to load movies from CSV file
	private static Movie[] loadMoviesFromFile(String fileName) throws IOException, ClassNotFoundException {
		BufferedReader reader = new BufferedReader(new FileReader(fileName));
		String line;
		int numLines = 0;
		while (reader.readLine() != null) {
			numLines++;
		}
		reader.close();

		Movie[] movies = new Movie[numLines];
		reader = new BufferedReader(new FileReader(fileName));
		int i = 0;
		while ((line = reader.readLine()) != null) {
			String[] fields = line.split(",");
			int year = Integer.parseInt(fields[0].trim());
			String title = fields[1].trim();
			int duration = Integer.parseInt(fields[2].trim());
			String[] genres = fields[3].trim().split(";");
			String rating = fields[4].trim();
			double score = Double.parseDouble(fields[5].trim());
			String director = fields[6].trim();
			String actor1 = fields[7].trim();
			String actor2 = fields[8].trim();
			String actor3 = fields[9].trim();

			movies[i] = new Movie(year, title, duration, genres, rating, score, director, actor1, actor2, actor3);
			i++;
		}
		reader.close();
		return movies;
	}

	/**
	 * Handles part3 of the process by deserializing movies from binary files listed
	 * in the part3 manifest and organizing them into a 2D array.
	 * 
	 * @param part3_manifest The filename of the part3 manifest.
	 * @return A 2D array of Movie objects, categorized by genre.
	 */
	public static Movie[][] do_part3(String part3_manifest) {
		Movie[][] all_movies = null;

		try {
			// First pass: Count the number of files listed in the manifest
			BufferedReader manifestReader = new BufferedReader(new FileReader(part3_manifest));
			int numFiles = 0;
			while (manifestReader.readLine() != null) {
				numFiles++;
			}
			manifestReader.close();

			// Initialize the array of movie arrays with the count
			all_movies = new Movie[numFiles][];

			// Second pass: Read the file names again and deserialize the movies
			manifestReader = new BufferedReader(new FileReader(part3_manifest));
			String line;
			int fileIndex = 0;
			while ((line = manifestReader.readLine()) != null) {
				all_movies[fileIndex] = deserializeMovies(line);
				fileIndex++;
			}
			manifestReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return all_movies;
	}

	/**
	 * Deserializes an array of Movie objects from a binary file. This method is
	 * used during part3 of the process to load movies into memory from serialized
	 * binary files.
	 * 
	 * @param binaryFileName The name of the binary file to deserialize.
	 * @return An array of Movie objects.
	 */
	private static Movie[] deserializeMovies(String binaryFileName) {
		Movie[] movies = null;
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(binaryFileName))) {
			// Assuming the file contains an array of Movie objects
			movies = (Movie[]) ois.readObject();
		} catch (IOException | ClassNotFoundException e) {
			System.err.println("Error reading or casting object from " + binaryFileName);
			e.printStackTrace();
		}
		return movies;
	}

}
