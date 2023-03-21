import java.util.*;

class Node {
    private String attribute;
    private Map<String, Node> children;

    public Node(String attribute) {
        this.attribute = attribute;
        this.children = new HashMap<String, Node>();
    }

    public String getAttribute() {
        return attribute;
    }

    public Map<String, Node> getChildren() {
        return children;
    }

    public void addChild(String value, Node child) {
        children.put(value, child);
    }

    @Override
    public String toString() {
        return toString(0);
    }

    private String toString(int depth) {
        StringBuilder sb = new StringBuilder();
        String tb = "\t";
        for (int i = 0; i < depth; i++) {
            tb += "\t";
        }
        sb.append(attribute);
        sb.append(": ");
        sb.append("\n");
        for (String value : children.keySet()) {
            sb.append(tb + value + "->");
            sb.append(children.get(value).toString(depth + 1));
        }
        sb.append("\n");
        return sb.toString();
    }
}

public class Prgrm8 {
    public static void main(String[] args) {
        String[][] data = { { "sunny", "hot", "high", "weak", "no" },
                { "sunny", "hot", "high", "strong", "no" },
                { "overcast", "hot", "high", "weak", "yes" },
                { "rainy", "mild", "high", "weak", "yes" },
                { "rainy", "cool", "normal", "weak", "yes" },
                { "rainy", "cool", "normal", "strong", "no" },
                { "overcast", "cool", "normal", "strong", "yes" },
                { "sunny", "mild", "high", "weak", "no" },
                { "sunny", "cool", "normal", "weak", "yes" },
                { "rainy", "mild", "normal", "weak", "yes" },
                { "sunny", "mild", "normal", "strong", "yes" },
                { "overcast", "mild", "high", "strong", "yes" },
                { "overcast", "hot", "normal", "weak", "yes" },
                { "rainy", "mild", "high", "strong", "no" } };
        String[] attributes = { "outlook", "temperature", "humidity", "wind" };
        Node tree = buildDecisionTree(data, attributes);
        System.out.println(tree.toString());
        double accuracy = findAccuracy(tree, attributes);
        System.out.println("The acuuracy of the decision tree is.. " + accuracy * 100 + "%");
    }

    public static Node buildDecisionTree(String[][] data, String[] attributes) {
        if (data.length == 0) {
            return new Node("no data");
        }
        if (allSameClass(data)) {
            return new Node(data[0][data[0].length - 1]);
        }
        if (attributes.length == 0) {
            return new Node(majorityClass(data));
        }
        String bestAttribute = findBestAttribute(data, attributes);
        Node node = new Node(bestAttribute);
        String[] values = getAttributeValues(data, attributes, bestAttribute);
        for (String value : values) {
            String[][] subset = getSubset(data, bestAttribute, value, attributes);
            Node child = buildDecisionTree(subset, removeAttribute(attributes, bestAttribute));
            node.addChild(value, child);
        }
        return node;
    }

    public static boolean allSameClass(String[][] data) {
        String cls = data[0][data[0].length - 1];
        for (int i = 1; i < data.length; i++) {
            if (!data[i][data[i].length - 1].equals(cls)) {
                return false;
            }
        }
        return true;
    }

    public static String majorityClass(String[][] data) {
        Map<String, Integer> counts = new HashMap<String, Integer>();
        for (int i = 0; i < data.length; i++) {
            String cls = data[i][data[i].length - 1];
            if (counts.containsKey(cls)) {
                counts.put(cls, counts.get(cls) + 1);
            } else {
                counts.put(cls, 1);
            }
        }
        int maxCount = -1;
        String majorityClass = null;
        for (String cls : counts.keySet()) {
            int count = counts.get(cls);
            if (count > maxCount) {
                maxCount = count;
                majorityClass = cls;
            }
        }

        return majorityClass;
    }

    public static String findBestAttribute(String[][] data, String[] attributes) {
        double maxGain = -1;
        String bestAttribute = null;
        for (String attribute : attributes) {
            double gain = informationGain(data, attributes, attribute);
            if (gain > maxGain) {
                maxGain = gain;
                bestAttribute = attribute;
            }
        }
        return bestAttribute;
    }

    public static double informationGain(String[][] data, String[] attributes, String attribute) {
        double infoGain = entropy(data);
        String[] values = getAttributeValues(data, attributes, attribute);
        for (String value : values) {
            String[][] subset = getSubset(data, attribute, value, attributes);
            if (subset.length > 0) {
                infoGain -= ((double) subset.length / (double) data.length) * entropy(subset);
            }
        }
        return infoGain;
    }

    public static double entropy(String[][] data) {
        Map<String, Integer> counts = new HashMap<String, Integer>();
        for (int i = 0; i < data.length; i++) {
            String cls = data[i][data[i].length - 1];
            if (counts.containsKey(cls)) {
                counts.put(cls, counts.get(cls) + 1);
            } else {
                counts.put(cls, 1);
            }
        }
        double entropy = 0;
        for (String cls : counts.keySet()) {
            double p = (double) counts.get(cls) / (double) data.length;
            entropy -= p * Math.log(p) / Math.log(2);
        }
        return entropy;
    }

    public static String[] getAttributeValues(String[][] data, String[] attributes, String attribute) {
        Set<String> values = new HashSet<String>();
        int index = getAttributeIndex(attributes, attribute);
        for (int i = 0; i < data.length; i++) {
            values.add(data[i][index]);
        }
        return values.toArray(new String[values.size()]);
    }

    public static String[][] getSubset(String[][] data, String attribute, String value, String[] attributes) {
        List<String[]> subset = new ArrayList<String[]>();
        int index = getAttributeIndex(attributes, attribute);
        for (int i = 0; i < data.length; i++) {
            if (data[i][index].equals(value)) {
                subset.add(data[i]);
            }
        }
        return subset.toArray(new String[subset.size()][]);
    }

    public static int getAttributeIndex(String[] attributes, String attribute) {
        for (int i = 0; i < attributes.length; i++) {
            if (attributes[i].equals(attribute)) {
                return i;
            }
        }
        return -1;
    }

    public static String[] removeAttribute(String[] attributes, String attribute) {
        String[] newAttributes = new String[attributes.length];
        int i = 0;
        for (String oneAttribute : attributes) {
            if (oneAttribute == attribute) {
                newAttributes[i] = " ";
                i++;
                continue;
            }
            newAttributes[i] = oneAttribute;
            i++;
        }
        return newAttributes;
    }

    public static double findAccuracy(Node tree, String[] attributes) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter number of  data rows :");
        int n = sc.nextInt();
        int validCounter = 0;
        String row = "";
        for (int i = 0; i < n; i++) {
            System.out.println("Enter the data row " + (i + 1) + " with comma seperated values...");
            sc.nextLine();
            row = sc.next();
            String[] splittedrow = row.split(",");
            String resultClass = findClass(tree, splittedrow, attributes);
            if (splittedrow[splittedrow.length - 1].equals(resultClass)) {
                validCounter++;
            }
        }
        return validCounter * 1.0 / n;

    }

    public static String findClass(Node tree, String[] splittedRow, String[] attributes) {
        Node curNode = tree;
        String curAttribute;
        while (curNode.getAttribute() != "yes" && curNode.getAttribute() != "no") {
            curAttribute = curNode.getAttribute();
            int curIndex = getAttributeIndex(attributes, curAttribute);
            for (Map.Entry<String, Node> onechild : curNode.getChildren().entrySet()) {
                if (onechild.getKey().equals(splittedRow[curIndex])) {
                    curNode = onechild.getValue();

                    break;
                }
            }
        }
        String result = curNode.getAttribute();
        return result;

    }
}
