
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class TestTask {

    private static final int ARRAY_DEPTH = 100;
    private static final List<dataEntity> list = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        InputStreamReader stream = new InputStreamReader(new BufferedInputStream(System.in));
        Scanner scanner = new Scanner(stream);

        for (int i = 0; i < ARRAY_DEPTH; i++) {
            if (scanner.next().equals("C")){
                list.add(new dataEntity(Integer.parseInt(scanner.next()),Integer.parseInt(scanner.next()),
                        Integer.parseInt(scanner.next()), Integer.parseInt(scanner.next()),Integer.parseInt(scanner.next()),
                        scanner.next(),convertStringToDate(scanner.next()),Integer.parseInt(scanner.next())));
                System.out.println(list.get(i));
            } else {
                runQuery(new dataEntity.queryEntity(scanner.next(),scanner.next(), scanner.next(), scanner.next(),scanner.next(),
                        scanner.next(),convertStringToDate(scanner.next()),convertStringToDate(scanner.next())),list);
                i--;
            }
        }
        stream.close();
        scanner.close();
    }

    public static void runQuery(dataEntity.queryEntity queryEntity, List<dataEntity> tempList){
        System.out.println(queryEntity);
        int counter = 0;
        int mapCounter = 0;
        Map<Integer,Integer> map = new TreeMap<>();
        for (TestTask.dataEntity dataEntity : tempList) {

            if (isNumeric(queryEntity.serviceID)){
                if (dataEntity.serviceID.equals(Integer.parseInt(queryEntity.serviceID))) {
                    counter++;
                    if (dataEntity.variationID.equals(Integer.parseInt(queryEntity.variationID))) {
                        counter++;
                    }
                }

            } else if(queryEntity.serviceID.equals("*")){
                counter += 2;
            }

            if (isNumeric(queryEntity.questionTypeID)){

                if (dataEntity.questionTypeID.equals(Integer.parseInt(queryEntity.questionTypeID))) {
                    counter++;
                    if (dataEntity.categoryID.equals(Integer.parseInt(queryEntity.categoryID))) {
                        counter++;
                    }
                    if (dataEntity.subCategoryID.equals(Integer.parseInt(queryEntity.subCategoryID))){
                        counter++;
                    }
                }

            } else if (queryEntity.questionTypeID.equals("*")) {
                counter += 3;

            }

            if (dataEntity.p_n.equals(queryEntity.p_n)) {
                counter++;
            }

            if (queryEntity.endDate != null) {
                if (dataEntity.date.compareTo(queryEntity.startDate) >= 0 && dataEntity.date.compareTo(queryEntity.endDate) <= 0) {
                    counter++;
                } else {
                    counter = 0;
                }
            }

            if (queryEntity.endDate == null) {
                if (dataEntity.date.compareTo(queryEntity.startDate) > 0) {
                    counter++;
                } else {
                    counter = 0;
                }
            }

            if (counter == 0){
                System.out.println("Didn't find any match for:");
                System.out.println(dataEntity);
            }

            map.put(mapCounter++, counter);


            counter = 0;
        }

        int maxValue = 0;
        for (Map.Entry<Integer, Integer> pair : map.entrySet()){
            if(maxValue < pair.getValue()){
                maxValue = pair.getValue();
            }
        }

        List <Integer> listMaxKeys = new ArrayList<>();
        List <Integer> listMaxValues = new ArrayList<>();
        for (Map.Entry<Integer, Integer> pair : map.entrySet()){
            if(pair.getValue().equals(maxValue)){
                listMaxKeys.add(pair.getKey());
                listMaxValues.add(pair.getValue());
            }
        }


        if(verifyElements(listMaxValues) && !listMaxValues.get(0).equals(new Integer(0))){
            int average = 0;
            for (Integer max : listMaxKeys) {
                average += list.get(max).time;
            }
            System.out.println("Average time: " + average/listMaxKeys.size());
        }
    }

    public static Date convertStringToDate(String data){
        Date date = null;
        try {
            date = new SimpleDateFormat("dd.M.yyyy", Locale.ENGLISH).parse(data);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static boolean verifyElements(List<Integer> listTest) {
        for (Integer s : listTest) {
            if (!s.equals(listTest.get(0)))
                return false;
        }
        return true;
    }


    public static class dataEntity{
        private final  String type = "C";
        private final  Integer serviceID;
        private final  Integer variationID;
        private final  Integer questionTypeID;
        private final  Integer categoryID;
        private final  Integer subCategoryID;
        private final  String p_n;
        private final  Date date;
        private final  Integer time;

        public dataEntity(int serviceID, int variationID, int questionTypeID, int categoryID, int subCategoryID,
                          String p_n, Date date, int time) {

            this.serviceID = serviceID;
            this.variationID = variationID;
            this.questionTypeID = questionTypeID;
            this.categoryID = categoryID;
            this.subCategoryID = subCategoryID;
            this.p_n = p_n;
            this.date = date;
            this.time = time;
        }

        @Override
        public String toString() {
            return "dataEntity{" +
                    "type='" + type + '\'' +
                    ", serviceID=" + serviceID +
                    ", variationID=" + variationID +
                    ", questionTypeID=" + questionTypeID +
                    ", categoryID=" + categoryID +
                    ", subCategoryID=" + subCategoryID +
                    ", p_n='" + p_n + '\'' +
                    ", date=" + date +
                    ", time=" + time +
                    '}';
        }

        public static class queryEntity{
            private final String type = "D";
            private final String serviceID;
            private final String variationID;
            private final String questionTypeID;
            private final String categoryID;
            private final String subCategoryID;
            private final String p_n;
            private final Date startDate;
            private final Date endDate;

            public queryEntity(String serviceID, String variationID, String questionTypeID, String categoryID, String subCategoryID,
                               String p_n, Date startDate, Date endDate) {

                if (serviceID.equals("*")){
                    variationID = serviceID;
                }

                if (questionTypeID.equals("*")){
                    categoryID = questionTypeID;
                    subCategoryID = questionTypeID;
                }

                this.serviceID = serviceID;
                this.variationID = variationID;
                this.questionTypeID = questionTypeID;
                this.categoryID = categoryID;
                this.subCategoryID = subCategoryID;
                this.p_n = p_n;
                this.startDate = startDate;
                this.endDate = endDate;
            }

            @Override
            public String toString() {
                return "queryEntity{" +
                        "type='" + type + '\'' +
                        ", serviceID=" + serviceID +
                        ", variationID=" + variationID +
                        ", questionTypeID=" + questionTypeID +
                        ", categoryID=" + categoryID +
                        ", subCategoryID=" + subCategoryID +
                        ", p_n='" + p_n + '\'' +
                        ", startDate=" + startDate +
                        ", endDate=" + endDate +
                        '}';
            }
        }
    }
}

//Input
//C 1 1 8 15 1 P 15.10.2012 83
//C 1 0 10 1 0 P 01.12.2012 65
//C 1 1 5 5 1 P 01.11.2012 117
//D 1 1 8 0 0 P 01.01.2012 01.12.2012
//C 3 0 10 2 0 N 02.10.2012 100
//D 1 0 * * * P 8.10.2012 20.11.2012
//D 3 0 10 0 0 P 01.12.2012 0.0.0