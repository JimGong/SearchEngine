import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;

// TODO This should be the "InvertedIndex" class (use "Refactor" functionality in Eclipse)
public class WordMap {

	// TODO Use final (rename it to "index")
	private TreeMap<String, TreeMap<String, TreeSet<Integer>>> wordMap;

	public WordMap() {
		wordMap = new TreeMap<>();
	}
	
	/* TODO
	   Consider adding helpful methods like hasWord(String word), hasPath(String word, String path), hasPosition(...)
	*/

	public void add(String word, String file, int position) {
		if (wordMap.containsKey(word) == false) {
			// TODO Rename to "positions"
			TreeSet<Integer> positionSet = new TreeSet<Integer>();
			positionSet.add(position);
			
			// TODO Rename to "fileMap"
			TreeMap<String, TreeSet<Integer>> newSubMap = new TreeMap<String, TreeSet<Integer>>();
			newSubMap.put(file, positionSet);
			wordMap.put(word, newSubMap);
		}
		else if (wordMap.containsKey(word) == true) {
			// TODO Rethink your variable names
			
			TreeMap<String, TreeSet<Integer>> myVal = wordMap.get(word);
			// 1. create new subMap, add new file and position (file doesn't
			// exist)

			// 2. get old subMap, add new position (file exist)
			if (fileExist(myVal, file) == false) {
				TreeSet<Integer> newSet = new TreeSet<Integer>();
				newSet.add(position);
				myVal.put(file, newSet);
				wordMap.put(word, myVal);
			}
			else {
				TreeSet<Integer> set = myVal.get(file);
				set.add(position);
				myVal.put(file, set);
				wordMap.put(word, myVal);
			}
		}
	}

	private boolean fileExist(Map<String, TreeSet<Integer>> myVal,
			String newFile) {
		if (myVal.containsKey(newFile)) {
			return true;
		}
		else {
			return false;
		}
	}

	private String indent(int times) throws IOException {
		return times > 0 ? String.format("%" + (times * 2) + "s", " ") : "";
	}

	private String quote(String text) {
		return "\"" + text + "\"";
	}

	public void printWordMap(Path output) {
		BufferedWriter bw;
		FileWriter fw;
		
		// TODO Use try-with-resources
		// TODO https://github.com/cs212/lectures/tree/fall2015/Files%20and%20Exceptions
		
		try {
			fw = new FileWriter(output.toFile());
			bw = new BufferedWriter(fw);

			bw.write("{");
			if (!wordMap.isEmpty()) {
				Entry<String, TreeMap<String, TreeSet<Integer>>> first = wordMap
						.firstEntry();

				output_Outside(first, bw);

				for (Entry<String, TreeMap<String, TreeSet<Integer>>> entry : wordMap
						.tailMap(first.getKey(), false).entrySet()) {
					bw.write(",");
					output_Outside(entry, bw);
				}
			}
			bw.write("\n}");
			bw.close(); // TODO Improper way to close resources

		} catch (IOException e) {
			// TODO Not a user-friendly exception message.
			System.out.println("### IOException in printWordMap() ####");
			// e.printStackTrace();
		}

	}

	private void output_Outside(
			Entry<String, TreeMap<String, TreeSet<Integer>>> entry,
			BufferedWriter bw) {

		try {
			// TODO Call bw.newLine() to add a newline character in a system-independent way
			
			bw.write("\n" + indent(1) + quote(entry.getKey()));
			bw.write(": {");
			TreeMap<String, TreeSet<Integer>> subMap = entry.getValue();
			if (!subMap.isEmpty()) {
				Entry<String, TreeSet<Integer>> subFirst = subMap.firstEntry();

				output_Mid(subFirst, bw);
				for (Entry<String, TreeSet<Integer>> subEntry : subMap
						.tailMap(subFirst.getKey(), false).entrySet()) {
					bw.write(",");
					output_Mid(subEntry, bw);
				}

			}
			bw.write("\n" + indent(1) + "}");

		} catch (IOException e) {
			// TODO Fix :)
			// TODO Private methods almost always throws the exceptions to a public method
			System.out.println("#### IOExcepetion in output_OutSide() ####");
			e.printStackTrace();
		}

	}

	private void output_Mid(Entry<String, TreeSet<Integer>> entry,
			BufferedWriter bw) {

		try {
			bw.write("\n" + indent(2));
			bw.write(quote(entry.getKey()));
			bw.write(": [");
			TreeSet<Integer> subTreeSet = entry.getValue();
			output_Inside(subTreeSet, bw);
			bw.write("\n" + indent(2) + "]");

		} catch (IOException e) {
			System.out.println("#### IOException in output_Mid() ####");
			e.printStackTrace();
		}

	}

	private void output_Inside(TreeSet<Integer> elements, BufferedWriter bw) {

		try {
			if (!elements.isEmpty()) {
				Integer first = elements.first();

				bw.write("\n" + indent(3) + first);

				for (int integer : elements.tailSet(first, false)) {
					bw.write(",\n");
					bw.write(indent(3) + integer);
				}
			}
		} catch (IOException e) {
			System.out.println("#### IOException in output_Inside() ####");
			e.printStackTrace();
		}
	}

}
