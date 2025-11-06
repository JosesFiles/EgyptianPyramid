package com.example;

import java.util.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class EgyptianPyramidsAppExample {


  // I've used two arrays here for O(1) reading of the pharaohs and pyramids.
  // other structures or additional structures can be used
  protected Pharaoh[] pharaohArray;
  protected Pyramid[] pyramidArray;
  protected Set<Integer> viewedPyramids = new HashSet<>();

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
        
        if (command != 'q') {
          System.out.println("Press Enter to continue...");
          scan.nextLine();
        }
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
    String s = o.get(key).toString();
    Integer result = Integer.parseInt(s);
    return result;

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

  // display a specific Egyptian pharaoh
private void displaySpecificPharaoh() {
    Scanner scan = new Scanner(System.in);
    System.out.print("Enter Pharaoh ID: ");

    // read input from user
    int id = Integer.parseInt(scan.nextLine());

    // find the pharaoh in the array
    Pharaoh found = null;
    for (Pharaoh p : pharaohArray) {
        if (p.getId() == id) {
            found = p;
            break;
        }
    }

    // display result
    if (found != null) {
        printMenuLine();
        found.print();
        printMenuLine();
    } else {
        System.out.println("Pharaoh not found. Please try again.");
    }
}

private void printAllPharaoh() {
    printMenuLine();
    if (pharaohArray == null || pharaohArray.length == 0) {
        System.out.println("No pharaohs available.");
    } else {
        for (Pharaoh p : pharaohArray) {
            if (p != null) {
                p.print();
            }
        }
    }
    printMenuLine();
}

// print all pyramids and their contributors
private void printAllPyramids() {
    for (Pyramid pyramid : pyramidArray) {
        printMenuLine();
        System.out.println("Pyramid ID: " + pyramid.getId());
        System.out.println("Pyramid Name: " + pyramid.getName());
        System.out.println("Contributors:");

        // list all contributors by name
        String[] contributors = pyramid.getContributors();
        for (String c : contributors) {
            System.out.println("  - " + c);
        }

        printMenuLine();
    }
}
// display information for a specific pyramid by ID


private void displaySpecificPyramid() {
    Scanner scan = new Scanner(System.in);
    System.out.print("Enter Pyramid ID: ");
    int id = Integer.parseInt(scan.nextLine());

    // find the pyramid with this ID
    Pyramid foundPyramid = null;
    for (Pyramid p : pyramidArray) {
        if (p.getId() == id) {
            foundPyramid = p;
            break;
        }
    }

    if (foundPyramid == null) {
        System.out.println("Pyramid not found. Please try again.");
        return;
    }

    printMenuLine();
    System.out.println("Pyramid ID: " + foundPyramid.getId());
    System.out.println("Pyramid Name: " + foundPyramid.getName());
    System.out.println("Contributors:");

    // list contributors
    String[] contributors = foundPyramid.getContributors();
    for (String c : contributors) {
        // try to find matching pharaoh for gold info
        Pharaoh pharaoh = findPharaohByName(c);
        if (pharaoh != null) {
            System.out.println("  - " + pharaoh.getName() + " (" + pharaoh.getContribution() + " gold)");
        } else {
            System.out.println("  - " + c);
        }
    }

    printMenuLine();

    // remember this pyramid ID for command 5 (requested pyramids)
    viewedPyramids.add(id);
}
// show all pyramids that have been viewed (no duplicates)
private void listRequestedPyramids() {
    if (viewedPyramids.isEmpty()) {
        System.out.println("No pyramids have been requested yet.");
        return;
    }

    System.out.println("List of Requested Pyramids:");
    printMenuLine();

    for (Integer id : viewedPyramids) {
        Pyramid p = findPyramidById(id);
        if (p != null) {
            System.out.println("Pyramid ID: " + p.getId());
            System.out.println("Pyramid Name: " + p.getName());
            printMenuLine();
        }
    }
}


  private Boolean executeCommand(Character command) {
    Boolean success = true;

    switch (command) {
      case '1' -> {
        printAllPharaoh();
      }
      case '2' -> {
        displaySpecificPharaoh();
      }
      case '3' -> {
        printAllPyramids();
      }
      case '4' -> {
        displaySpecificPyramid();
      }
      case '5' -> {
        listRequestedPyramids();
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

  private Pharaoh findPharaohByName(String name) {
    for (Pharaoh p : pharaohArray) {
        if (p.getName().equalsIgnoreCase(name)) {
            return p;
        }
    }
    return null;
}
// helper method to find a pyramid by its ID number
private Pyramid findPyramidById(int id) {
    for (Pyramid p : pyramidArray) {
        if (p.getId() == id) {
            return p;
        }
    }
    return null;
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

