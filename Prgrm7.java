import java.util.*;
class fpGrowthAlgorithm {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        System.out.println("enter the no of transactions to be recorded : ");
        int t_no = sc.nextInt();
        System.out.println("Enter the transactions with comma seperated items :");
        String[] transactions = new String[t_no];
        for (int i = 0; i < t_no; i++) {
            transactions[i] = sc.next();
        }
        System.out.println("Enter the minimum support:");
        int minSupport = sc.nextInt();
        List<List<String>> transactionList = generateTransactionList(transactions);
        List<String> uniqueItems = generateUniqueItems(transactionList);
        HashMap<String, Integer> hm = generateHashMap(transactionList, uniqueItems);
        List<String> minSupportSatisfiedUniqueItems = uniqueItems
                .stream()
                .filter(s -> hm.get(s) >= minSupport)
                .toList();
        FPTree fpTree = generateFPTree(transactionList, hm, minSupport);
        fpTree.printFullTree();
        HashMap<String, List<List<String>>> conditionalPatternBase = generateConditionalPatternBase(
                minSupportSatisfiedUniqueItems, fpTree);
        HashMap<String, HashMap<List<String>, Integer>> frequentPatterns = new HashMap<String, HashMap<List<String>, Integer>>();
        for (String item : minSupportSatisfiedUniqueItems) {
            List<List<String>> itemTransactionList = conditionalPatternBase.get(item);
            FPTree itemFPTree = generateFPTree(itemTransactionList, hm, minSupport);
            HashMap<List<String>, Integer> tmpHm = new HashMap<List<String>, Integer>();
            List<FPNode> itemNodes = itemFPTree.getNodesWithName(item);
            for (FPNode itemNode : itemNodes) {
                List<String> itemAncestorNames = itemFPTree.getAncestorNames(itemNode);
                int itemFreq = itemNode.getFrequency();
                String itemName = itemNode.getItem();
                List<List<String>> subsets = generateSubSets(itemAncestorNames);
                for (List<String> subset : subsets) {
                    subset.add(itemName);
                    int oldFreq = 0;
                    if (tmpHm.containsKey(subset)) {
                        oldFreq = tmpHm.get(subset);
                    }
                    tmpHm.put(subset, itemFreq + oldFreq);
                }
            }
            frequentPatterns.put(item, tmpHm);
            List<List<String>> patternsToUpdate = new ArrayList<>();
            HashMap<List<String>, Integer> oneFreqMap = frequentPatterns.get(item);
            for (Map.Entry<List<String>, Integer> newhm : oneFreqMap.entrySet()) {
                if (newhm.getValue() < minSupport) {
                    patternsToUpdate.add(newhm.getKey());
                }
            }
            for (List<String> onePattern : patternsToUpdate) {
                frequentPatterns.get(item).remove(onePattern);                
            }
        }
        printFrequentPattern(frequentPatterns);
        sc.close();
    }
    public static List<List<String>> generateTransactionList(String[] transactions) {
        List<List<String>> transactionList = new ArrayList<>();
        int t_no = transactions.length;
        for (int ptr = 0; ptr < t_no; ptr++) {
            String[] splitted = transactions[ptr].split(",");
            List<String> oneTransaction = new ArrayList<String>();
            for (String item : splitted) {
                oneTransaction.add(item);
            }
            transactionList.add((oneTransaction));
        }
        return transactionList;
    }
    public static List<String> generateUniqueItems(List<List<String>> transactionList) {
        List<String> uniqueItems = new ArrayList<>();
        int t_no = transactionList.size();
        for (int ptr = 0; ptr < t_no; ptr++) {
            List<String> transaction = transactionList.get(ptr);
            for (String item : transaction) {
                if (uniqueItems.contains(item) == false) uniqueItems.add(item);
            }
        }
        return uniqueItems;
    }
    public static HashMap<String, Integer> generateHashMap(List<List<String>> transactionList, List<String> uniqueItems) {
        HashMap<String, Integer> hm = new HashMap<>();
        for (String item : uniqueItems) {
            hm.put(item, 0);
        }
        for (Map.Entry<String, Integer> item : hm.entrySet()) {
            int count = 0;
            for (List<String> onetrans : transactionList) {
                if (onetrans.contains(item.getKey())) count++;
            }
            hm.replace(item.getKey(), count);
        }
        return hm;
    }
    private static void printFrequentPattern(HashMap<String, HashMap<List<String>, Integer>> frequentPatterns) {
        System.out.println("\n\nPrinting Frequent Patterns");
        for (Map.Entry<String, HashMap<List<String>, Integer>> entry : frequentPatterns.entrySet()) {
            System.out.println("Item:: " + entry.getKey() + " Frequent Patterns");
            for (Map.Entry<List<String>, Integer> entry2 : entry.getValue().entrySet()) {
                System.out.print("\t");
                System.out.print(entry2.getKey() + ":: ");
                System.out.println(entry2.getValue());
            }
        }
    }
    private static List<List<String>> generateSubSets(List<String> set) {
        List<List<String>> ans = new ArrayList<List<String>>();
        recursiveSubSetsGenerator(ans, set, new ArrayList<String>(), 0);
        return ans;
    }
    private static void recursiveSubSetsGenerator(List<List<String>> ans, List<String> set, ArrayList<String> output,int i) {
        if (i == set.size()) {
            if (output.size() == 0) return;
            ans.add(output);
            return;
        }
        recursiveSubSetsGenerator(ans, set, new ArrayList<>(output), i + 1);
        output.add(set.get(i));
        recursiveSubSetsGenerator(ans, set, new ArrayList<>(output), i + 1);
    }
    private static HashMap<String, List<List<String>>> generateConditionalPatternBase(List<String> uniqueItems,FPTree fpTree) {
        HashMap<String, List<List<String>>> ans = new HashMap<String, List<List<String>>>();
        for (String item : uniqueItems) {
            List<FPNode> nodes = fpTree.getNodesWithName(item);
            List<List<String>> currentItemTransactionList = new ArrayList<List<String>>();
            for (FPNode node : nodes) {
                int freq = node.getFrequency();
                List<String> ancestorNames = fpTree.getAncestorNames(node);
                ancestorNames.add(node.getItem());
                for (int i = 0; i < freq; i++) {
                    currentItemTransactionList.add(ancestorNames);
                }
            }
            ans.put(item, currentItemTransactionList);
        }
        return ans;
    }
    private static FPTree generateFPTree(List<List<String>> transactionList, HashMap<String, Integer> hm, int minSupport) {
        FPNode root = new FPNode("Null", 0, null);
        for (List<String> transaction : transactionList) {
            List<String> sortedTransaction = transaction
                    .stream()
                    .filter(s -> hm.get(s) >= minSupport)
                    .sorted(new Comparator<String>() {
                        public int compare(String a, String b) {
                            return hm.get(b) - hm.get(a);
                        }
                    })
                    .toList();
            FPNode curr = root;
            for (String item : sortedTransaction) {
                FPNode candidate = curr.getChildWithName(item);
                if (candidate != null) {
                    candidate.incrementFrequency();
                } 
                else {
                    candidate = new FPNode(item, 1, curr);
                    curr.addChild(candidate);
                }
                curr = candidate;
            }
        }
        return new FPTree(root);
    }
}
class FPNode {
    private String item;
    private int frequency;
    private FPNode parent;
    private List<FPNode> children;
    public FPNode(String item, int frequency, FPNode parent) {
        this.item = item;
        this.frequency = frequency;
        this.parent = parent;
        this.children = new ArrayList<>();
    }
    public FPNode getChildWithName(String name) {
        for (FPNode child : children) {
            if (child == null) continue;
            if (child.getItem().equals(name)) return child;         
        }
        return null;
    }
    public String getItem() {
        return item;
    }

    public int getFrequency() {
        return frequency;
    }

    public FPNode getParent() {
        return parent;
    }

    public List<FPNode> getChildren() {
        return children;
    }

    public void incrementFrequency() {
        frequency++;
    }

    public void addChild(FPNode child) {
        children.add(child);
    }
}
class FPTree {
    private FPNode root;
    public List<FPNode> getNodesWithName(String name) {
        List<FPNode> ans = new ArrayList<FPNode>();
        LinkedList<FPNode> queue = new LinkedList<>();
        queue.addLast(root);
        while (queue.size() != 0) {
            FPNode curr = queue.removeFirst();
            if (curr.getItem().equals(name)) {
                ans.add(curr);
            } else {
                List<FPNode> children = curr.getChildren();
                for (FPNode child : children) {
                    queue.addLast(child);
                }
            }
        }
        return ans;
    }
    public FPTree(FPNode root) {
        this.root = root;
    }
    public FPNode getRoot() {
        return root;
    }
    public void printFullTree() {
        printTreeFromNode(root, "");
    }
    private void printTreeFromNode(FPNode node, String indent) {
        System.out.println(indent + node.getItem() + "::" + node.getFrequency());
        for (FPNode child : node.getChildren()) {
            printTreeFromNode(child, indent + "\t");
        }
    }
    public List<String> getAncestorNames(FPNode node) {
        List<String> ans = new ArrayList<String>();
        FPNode curr = node.getParent();
        while (!curr.getItem().equals("Null")) {
            ans.add(curr.getItem());
            curr = curr.getParent();
        }
        return ans;
    }
}
