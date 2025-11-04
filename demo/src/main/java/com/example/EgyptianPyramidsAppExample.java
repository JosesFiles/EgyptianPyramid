package com.example;

import java.util.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class EgyptianPyramidsAppExample {


  // I've used two arrays here for O(1) reading of the pharaohs and pyramids.
  // other structures or additional structures can be used
  protected Pharaoh[] pharaohArray;
  protected Pyramid[] pyramidArray;
  protected Set<Pyramid> viewedPyramids = new HashSet<>();

  public static void main(String[] args) {
    // create and start the app
    EgyptianPyramidsAppExample app = new EgyptianPyramidsAppExample();
    app.start();
  }

  // main loop for app
  public void start() {
    try (Scanner scan = new Scanner(System.in)) {
      Character command = '_';

      // loop until user quits
      while (command != 'q') {
        printMenu();
        System.out.print("Enter a command: ");
        command = menuGetCommand(scan);

        executeCommand(command);
      }
    }
  }

  // constructor to initialize the app and read commands
  public EgyptianPyramidsAppExample() {
    // read egyptian pharaohs
    String pharaohFile =
      "C:/Users/josec/OneDrive/Java/EgyptianPyramid/demo/src/main/java/com/example/pharaoh.json";
    JSONArray pharaohJSONArray = JSONFile.readArray(pharaohFile);

    // create and intialize the pharaoh array
    initializePharaoh(pharaohJSONArray);

    // read pyramids
    String pyramidFile =
      "C:/Users/josec/OneDrive/Java/EgyptianPyramid/demo/src/main/java/com/example/pyramid.json";
    JSONArray pyramidJSONArray = JSONFile.readArray(pyramidFile);

    // create and initialize the pyramid array
    initializePyramid(pyramidJSONArray);

  }

  // initialize the pharaoh array
  private void initializePharaoh(JSONArray pharaohJSONArray) {
    // create array and hash map
    pharaohArray = new Pharaoh[pharaohJSONArray.size()];

    // initalize the array
    for (int i = 0; i < pharaohJSONArray.size(); i++) {
      // get the object
      JSONObject o = (JSONObject) pharaohJSONArray.get(i);

      // parse the json object
      Integer id = toInteger(o, "id");
      String name = o.get("name").toString();
      Integer begin = toInteger(o, "begin");
      Integer end = toInteger(o, "end");
      Integer contribution = toInteger(o, "contribution");
      String hieroglyphic = o.get("hieroglyphic").toString();

      // add a new pharoah to array
      Pharaoh p = new Pharaoh(id, name, begin, end, contribution, hieroglyphic);
      pharaohArray[i] = p;
    }
  }

    // initialize the pyramid array
    private void initializePyramid(JSONArray pyramidJSONArray) {
      // create array and hash map
      pyramidArray = new Pyramid[pyramidJSONArray.size()];
  
      // initalize the array
      for (int i = 0; i < pyramidJSONArray.size(); i++) {
        // get the object
        JSONObject o = (JSONObject) pyramidJSONArray.get(i);
  
        // parse the json object
        Integer id = toInteger(o, "id");
        String name = o.get("name").toString();
        JSONArray contributorsJSONArray = (JSONArray) o.get("contributors");
        String[] contributors = new String[contributorsJSONArray.size()];
        for (int j = 0; j < contributorsJSONArray.size(); j++) {
          String c = contributorsJSONArray.get(j).toString();
          contributors[j] = c;
        }
  
        // add a new pyramid to array
        Pyramid p = new Pyramid(id, name, contributors);
        pyramidArray[i] = p;
      }
    }

  // get a integer from a json object, and parse it
  private Integer toInteger(JSONObject o, String key) {
    return ((Number) o.get(key)).intValue();
  }

  // get first character from input
  private static Character menuGetCommand(Scanner scan) {
    Character command = '_';

    String rawInput = scan.nextLine();

    if (rawInput.length() > 0) {
      rawInput = rawInput.toLowerCase();
      command = rawInput.charAt(0);
    }

    return command;
  }

  // print all pharaohs
  private void printAllPharaoh() {
    for (Pharaoh pharaoh : pharaohArray) {
      printMenuLine();
      pharaoh.print();
      printMenuLine();
    }
  }

  private Boolean executeCommand(Character command) {
    Boolean success = true;

    switch (command) {
      case '1' -> {
        printAllPharaoh();
      }
      case 'q' -> {
        System.out.println("Thank you for using Nassef's Egyptian Pyramid App!");
      }
      default -> {
        System.out.println("ERROR: Unknown commmand");
        success = false;
      }
    }

    return success;
  }

  private static void printMenuCommand(Character command, String desc) {
    System.out.printf("%s\t\t%s\n", command, desc);
  }

  private static void printMenuLine() {
    System.out.println(
      "--------------------------------------------------------------------------"
    );
  }

  public static void printMenu() {
    System.out.println("Nassef's Egyptian Pyramids App");
    System.out.println("-------------------------------------------------------------");
    System.out.println("Command\t\tDescription");
    System.out.println("-------\t\t-----------------------------------------------");
    printMenuCommand('1', "List all the pharaohs");
    printMenuCommand('2', "Displays a specific Egyptian pharaoh");
    printMenuCommand('3', "List all the pyramids");
    printMenuCommand('4', "Displays a specific pyramid");
    printMenuCommand('5', "Displays a list of requested pyramids.");
    printMenuCommand('q', "Quit");
    System.out.println("-------------------------------------------------------------");
}

  }

