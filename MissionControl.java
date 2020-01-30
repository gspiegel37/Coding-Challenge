//Gavin Spiegel

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class MissionControl {

    static ArrayList<String> alerts = new ArrayList<>();
    static ArrayList<Integer> alerts_sat = new ArrayList<>();

    public static void main(String[] args) throws FileNotFoundException {

        //File text = new File(file extension);
        //Scanner scan = new Scanner(text);

        Scanner scan = new Scanner(System.in);

        ArrayList<String> red_battery_sat1 = new ArrayList<>();
        ArrayList<String> red_thermostat_sat1 = new ArrayList<>();

        ArrayList<String> red_battery_sat2 = new ArrayList<>();
        ArrayList<String> red_thermostat_sat2 = new ArrayList<>();

        String line;
        boolean first = true;
        boolean second = true;
        int sat1 = 0;
        int sat2 = 0;

        //This loop will first determine any reading violations. Later I will check for five minute intervals on each
        // satellite.
        while (!(line = scan.nextLine()).isEmpty()) {

            String[] data = line.split("\\|");

            if (first) {

                sat1 = Integer.parseInt(data[1]);
                first = false;

            }

            if (Double.parseDouble(data[6]) < Double.parseDouble(data[5])) {

                if (Integer.parseInt(data[1]) == sat1) {

                    red_battery_sat1.add(data[0]);

                } else {

                    if (second) {

                        sat2 = Integer.parseInt(data[1]);
                        second = false;

                    }

                    red_battery_sat2.add(data[0]);

                }

            } else if (Double.parseDouble(data[6]) > Double.parseDouble(data[2])) {

                if (Integer.parseInt(data[1]) == sat1) {

                    red_thermostat_sat1.add(data[0]);

                } else {

                    if (second) {

                        sat2 = Integer.parseInt(data[1]);
                        second = false;

                    }

                    red_thermostat_sat2.add(data[0]);

                }

            }

        }

        addAlerts(red_battery_sat1, sat1, "BATT");
        addAlerts(red_battery_sat2, sat2, "BATT");
        addAlerts(red_thermostat_sat1, sat1, "TSTAT");
        addAlerts(red_thermostat_sat2, sat2, "TSTAT");

        String s = formatString(alerts_sat, alerts);
        System.out.print(s);

    }

    //This method creates a double from the time so calculations can be done to find 5 minute intervals.
    public static double stringConvert(String timeStamp) {

        String[] p = timeStamp.split("[:.\\s]");

        String s = p[1] + "." + p[2] + p[3] + p[4];

        return Double.parseDouble(s);

    }

    //This will check if the violations for each satellite were within five minutes and would warrant an alert.
    public static void addAlerts(ArrayList<String> sat_list, int sat, String type) {

        for (int i = 2; i < sat_list.size(); i++) {

            double before = stringConvert(sat_list.get(i-2));
            double after = stringConvert(sat_list.get(i));


            if (after > before) {

                if (after - before <= 0.5) {

                    alerts.add(sat_list.get(i-2));
                    alerts.add(type);
                    alerts_sat.add(sat);

                }

            } else {
                //This else is for checking times in different days together.

                if ((after + 24) - before <= 0.5) {

                    alerts.add(sat_list.get(i-2));
                    alerts.add(type);
                    alerts_sat.add(sat);

                }

            }

        }

    }

    //This method formats the output into JSON.
    public static String formatString(ArrayList<Integer> sat_num, ArrayList<String> info) {

        String s = "[";

        for (int i = 0; i < sat_num.size(); i++) {

            s += "\n\t{\n\t\t\"satelliteID\": " + sat_num.get(i) + ",\n\t\t\"severity\": ";

            if (info.get(2 * i + 1) == "BATT") {

                s +=  "\"RED LOW\',\n\t\t\"component\": " + info.get(2 * i + 1) + ",\n\t\t\"timestamp\": \"" +
                        formatTime(info.get(2 * i)) + "\"\n\t}";

            } else {

                s +=  "\"RED HIGH\",\n\t\t\"component\": \"" + info.get(2 * i + 1) + "\",\n\t\t\"timestamp\": \"" +
                        formatTime(info.get(2 * i)) + "\"\n\t}";

            }

            if (i != sat_num.size()-1) {
                s += ",";
            }

        }

        s += "\n]";

        return s;

    }

    //This method formats the timestamp for output.
    public static String formatTime(String timeStamp) {

        String[] p = timeStamp.split("[\\s]");

        String s = p[0].substring(0,4) + "-" + p[0].substring(4,6) + "-" + p[0].substring(6,8) + "T" + p[1] + "Z";

        return s;

    }


}
