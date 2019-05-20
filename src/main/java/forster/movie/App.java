package forster.movie;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class App {

	public static void main(String[] args) {
		try {
			// the path to the input file ie,. assets.json
			String inputPath = args[0];
			// the path to the output directory
			String outputPath = args[1];
			parseFile(inputPath, outputPath);
		} catch (ArrayIndexOutOfBoundsException ex) {
			System.out.println("Not enough parameters. arg1 = input file path, arg2 = output directory");
		}
		
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void parseFile(String inputPath, String outputPath) {
		JSONArray array = null;
		// JSON parser object to parse read file
		JSONParser jsonParser = new JSONParser();
		try {
			array = (JSONArray) jsonParser.parse(new FileReader(inputPath));
		} catch (IOException | ParseException e) {
			System.out.println("Error parsing input file. Please ensure the input file is valid");
		}
		if (array != null) {
			Map<String, List<Map>> ageMap = groupByAge(array);
			JSONArray result = new JSONArray();
			// create the final output format
			for (Entry entry : ageMap.entrySet()) {
				Map<String, Object> movie = new HashMap<>();
				movie.put("groupId", entry.getKey());
				movie.put("count", ((List) entry.getValue()).size());
				movie.put("data", entry.getValue());
				result.add(movie);
			}
			try (FileWriter file = new FileWriter(outputPath + "result.json")) {
				file.write(result.toJSONString());
				file.flush();
			} catch (IOException e) {
				System.out.println("Error writing output. Please ensure the output file path is valid");
			}
		} else {
			System.out.println("Invalid Input file format.");
		}

	}

	/**
	 * Groups items by age group rating and keeps only asset_id, title and
	 * production_year for Movies.
	 *
	 * @param array
	 *            the array of TV shows, episodes, Movies etc.
	 * @return the map
	 */
	private static Map<String, List<Map>> groupByAge(JSONArray array) {
		Map<String, List<Map>> ageMap = new HashMap<>();
		for (Object item : array) {
			JSONObject asset = (JSONObject) item;
			if (((String) asset.get("object_class")).equals("Movie")) {
				Map<String, Object> details = new HashMap<>();
				details.put("asset_id", (Long) asset.get("asset_id"));
				details.put("title", (String) asset.get("title"));
				details.put("production_year", (Long) asset.get("production_year"));
				List<String> ageGroup = (List<String>) asset.get("fsk_level_list_facet");
				for (Object age : ageGroup) {
					List<Map> list = null;
					if (!ageMap.containsKey(age)) {
						list = new ArrayList<>();
					} else {
						list = ageMap.get(age);
					}
					list.add(details);
					ageMap.put((String) age, list);
				}
			}
		}
		return ageMap;

	}
}
