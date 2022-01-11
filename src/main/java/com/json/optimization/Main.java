package com.json.optimization;

import com.json.optimization.ann.NeuronNetwork;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    public static final int NEW_RECORD = 18;
    public static final int NEW_TASKS = 11;
    public static final int NEW_POSITION = 5;

    public static final int JSON_DATA = 5;
    public static final int STOP_DATA = 2;
    public static final int TASKS_DATA = 6;
    public static final int POSITION_DATA = 5;

    public static void main(String[] args) throws IOException, InvalidFormatException {

//        NeuronNetwork neuronNetwork = new NeuronNetwork(1., 1., 0.);
//        neuronNetwork.calculateData();

//        String[] names = {"before.json.xlsx", "after.json.xlsx"};
//        ArrayList<JsonObject> data = getDataFromFiles(names);
    }

    private static ArrayList<JsonObject> getDataFromFiles(String[] names) throws IOException, InvalidFormatException {
        double startTime = System.currentTimeMillis();

        String path = "C:\\Users\\georg\\IdeaProjects\\JsonOptim\\json\\";
        ArrayList<JsonObject> dataArr = new ArrayList<>();

        for (String name : names) {
            Map<Integer, List<String>> subMap = openFile(path + name);
            parseData(subMap, dataArr);
        }

        System.out.println(dataArr.size() + " | " + dataArr.get(1).getTasks().get(0).getDriverMark());//.get(10).getQuantity());

        System.out.println((System.currentTimeMillis() - startTime) / 1000 + " s");

        HashMap<String, Integer> testMap = new HashMap<>();

        for (int i = 1; i < dataArr.size(); i++) {
            String point = dataArr.get(i).getPoint();

            if (testMap.containsKey(point))
                testMap.put(point, testMap.get(point) + 1);
            else
                testMap.put(point, 1);
        }

        System.out.println(testMap);

        return dataArr;
    }

    private static void parseData(Map<Integer, List<String>> map, ArrayList<JsonObject> dataArr) {
        JsonObject data = new JsonObject();
        boolean isRecord = false;

        for (int i = 1; i < map.size(); i++) {
            if (map.get(i).size() == NEW_RECORD) {
                if (isRecord)
                    dataArr.add(data);

                isRecord = true;
                data = new JsonObject();

                addDataToData(data, map.get(i), NEW_RECORD);
            }
            else if (map.get(i).size() == NEW_TASKS) {
                addDataToData(data, map.get(i), NEW_TASKS);
            }
            else if (map.get(i).size() == NEW_POSITION) {
                data.getTasks().get(data.getTasks().size() - 1).setTask(
                        map.get(i).get(0),
                        Double.parseDouble(map.get(i).get(1)),
                        Double.parseDouble(map.get(i).get(2)),
                        Double.parseDouble(map.get(i).get(3)),
                        Double.parseDouble(map.get(i).get(4))
                );
            }
        }

        dataArr.add(data);
    }

    private static void addDataToData(JsonObject data, List<String> strings, int type) {
        if (type == NEW_RECORD) {
            data.setPoint(strings.get(0));
            data.setPointLat(Double.parseDouble(strings.get(1)));
            data.setPointLon(Double.parseDouble(strings.get(2)));
            data.setStopLat(Double.parseDouble(strings.get(3)));
            data.setStopLon(Double.parseDouble(strings.get(4)));

            data.setStop(strings.get(5), Integer.parseInt(strings.get(6)));
        }

        data.setTasks(
                strings.get(type == NEW_RECORD? 7 : 0),
                strings.get(type == NEW_RECORD? 8 : 1),
                Double.parseDouble(strings.get(type == NEW_RECORD? 9 : 2)),
                Double.parseDouble(strings.get(type == NEW_RECORD? 10 : 3)),
                Double.parseDouble(strings.get(type == NEW_RECORD? 11 : 4)),
                Double.parseDouble(strings.get(type == NEW_RECORD? 12 : 5))
        );

        data.getTasks().get(data.getTasks().size() - 1).setTask(
                strings.get(type == NEW_RECORD? 13 : 6),
                Double.parseDouble(strings.get(type == NEW_RECORD? 14 : 7)),
                Double.parseDouble(strings.get(type == NEW_RECORD? 15 : 8)),
                Double.parseDouble(strings.get(type == NEW_RECORD? 16 : 9)),
                Double.parseDouble(strings.get(type == NEW_RECORD? 17 : 10))
        );
    }

    public static Map<Integer, List<String>> openFile(String filePath) throws IOException, InvalidFormatException {
        File file = new File(filePath);
        FileInputStream fis = new FileInputStream(file);
        Workbook workbook = new XSSFWorkbook(file);
        Sheet sheet = workbook.getSheetAt(0);

        Map<Integer, List<String>> data = new HashMap<>();
        int i = 0;
        for (Row row : sheet) {
            data.put(i, new ArrayList<String>());
            for (Cell cell : row) {
                switch (cell.getCellType()) {
                    case STRING:
                        data.get(i).add(cell.getRichStringCellValue().getString());
                        break;
                    case NUMERIC:
                        if (DateUtil.isCellDateFormatted(cell)) {
                            data.get(i).add(cell.getDateCellValue() + "");
                        } else {
                            data.get(i).add(cell.getNumericCellValue() + "");
                        }
                        break;
                    case BOOLEAN:
                        data.get(i).add(cell.getBooleanCellValue() + "");
                        break;
                    case FORMULA:
                        data.get(i).add(cell.getCellFormula() + "");
                        break;
                    default: data.get(i).add(" ");
                }
            }
            i++;
        }

        return data;
    }
}

// DoubleMatrix2D inputs;
//inputs

//        SigmoidNetwork sigmoidNetwork = new SigmoidNetwork(3, 3, 2);
//        double[] inputs = {1, 0, 1};
//        double[] outputs = sigmoidNetwork.feedForward(inputs);
//
//        for (int i = 0; i < outputs.length; i++) {
//            System.out.println(outputs[i]);
//        }
//
//        System.out.println(1 / (1 + Math.exp(-1.23)));

//        PerceptronNetwork net = new PerceptronNetwork(2, 3, 2);
//        int[] inputs = {1, 1};
//        int[] outputs = net.feedForward(inputs);
//
//        for (int i = 0; i < outputs.length; i++) {
//            System.out.println(outputs[i]);
//        }
