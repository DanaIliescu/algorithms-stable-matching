import java.io.File;
import java.io.PrintWriter;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        File input = new File("data/sm-worst-5-in.txt");
        int numberOfPpl = 0;
        boolean hasReachedPreferences = false;
        List<List<Integer>> menPreferences = new ArrayList<List<Integer>>();
        List<List<Integer>> womenPreferences = new ArrayList<List<Integer>>();
        HashMap<Integer, Integer> matches = new HashMap<>();
        HashMap<Integer, String> men = new HashMap<>();
        HashMap<Integer, String> women = new HashMap<>();

        try {
            Scanner sc = new Scanner(input);
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.length() > 0) {
                    if (!hasReachedPreferences) {
                        if (line.charAt(0) == 'n') {
                            numberOfPpl = Integer.parseInt(line.split("=")[1]);
                            continue;
                        }
                        if (line.charAt(0) != '#') {
                            int personId = Integer.parseInt(line.split(" ")[0]);
                            String personName = line.split(" ")[1];
                            if (personId % 2 == 1) {
                                men.put(personId, personName);
                            } else {
                                women.put(personId, personName);
                            }
                        }
                    } else {
                        if (Integer.parseInt(line.split(":")[0]) % 2 == 1) {
                            List<Integer> manPreferences = new ArrayList<Integer>();
                            for (int i = 0; i < numberOfPpl; i++) {
                                manPreferences.add(Integer.parseInt(line.split(": ")[1].split(" ")[i]));
                            }
                            menPreferences.add(manPreferences);
                        } else {
                            List<Integer> womanPreferences = new ArrayList<Integer>();
                            for (int i = 0; i < numberOfPpl; i++) {
                                womanPreferences.add(Integer.parseInt(line.split(": ")[1].split(" ")[i]));
                            }
                            womenPreferences.add(womanPreferences);
                        }
                    }
                } else {
                    hasReachedPreferences = true;
                }
            }
            sc.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 0; i < menPreferences.size(); i++) {
            int manId = 2 * i + 1;

            for (int j = 0; j < menPreferences.get(i).size(); j++) {
                if (isFree(manId, matches)) {
                    int preferredWomanId = menPreferences.get(i).get(j);
                    if (isFree(preferredWomanId, matches)) {
                        matches.put(manId, preferredWomanId);
                        matches.put(preferredWomanId, manId);
                        break;
                    } else {
                        int spouse = matches.get(preferredWomanId);
                        int preferredWomanIndex = (preferredWomanId - 2) / 2;
                        if (womenPreferences.get(preferredWomanIndex).indexOf(spouse)
                                > womenPreferences.get(preferredWomanIndex).indexOf(manId)) {
                            matches.remove(preferredWomanId);
                            matches.remove(spouse);
                            matches.put(manId, preferredWomanId);
                            matches.put(preferredWomanId, manId);
                            i = -1;
                            break;
                        }
                    }
                }
            }
        }

        try {
            String inputFileName = input.getName();
            String outputFileName = "myData/" + inputFileName.replace("-in", "-out");
            PrintWriter writer = new PrintWriter(outputFileName, "UTF-8");
            for (Integer id : matches.keySet()) {
                int key = id;
                int value = matches.get(id);
                if (id % 2 == 1) {
                    String man = men.get(key);
                    String woman = women.get(value);
                    writer.println(man + " -- " + woman);
                }
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean isFree(int personId, HashMap<Integer, Integer> matches) {
        return !matches.containsKey(personId);
    }
}

/*
        for (Integer id : men.keySet()) {
            int key = id;
            String value = men.get(id);
            System.out.println(key + " -- " + value);
        }

        for (Integer id : women.keySet()) {
            int key = id;
            String value = women.get(id);
            System.out.println(key + " -- " + value);
        }

        for (Integer id : matches.keySet()) {
            int key = id;
            int value = matches.get(id);
            System.out.println(key + " -- " + value);
        }
*/
