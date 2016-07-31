package com.justinoboyle.kitmaker.main;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class ResultList implements Serializable {

    private static final long serialVersionUID = -4732960618195087838L;

    private ArrayList<Result> results = new ArrayList<Result>();
    private String name;
    private Random random;

    public ResultList(ArrayList<Result> results, String name) {
        super();
        this.results = results;
        this.name = name;
        random = new Random();
    }

    public static ResultList fromString(String configName) {
        ArrayList<Result> results = new ArrayList<Result>();
        String s = KitMaker.instance.getConfig().getString("ResultLists." + configName);
        for (String ss : s.split(",")) {
            try {
                Result r = new Result(ss);
                results.add(r);
            } catch (Exception ex) {
                // Result is null, don't want to cancel it.
                System.out.println("[Warning] " + ss + " is not a Result.");
            }
        }
        return new ResultList(results, configName);
    }

    @Override
    public String toString() {
        return "ResultList [results=" + results + ", name=" + name + "]";
    }

    public ArrayList<Result> getResults() {
        return results;
    }

    public void setResults(ArrayList<Result> results) {
        this.results = results;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addResult(Result result) {
        results.add(result);
    }

    public void removeResult(Result result) {
        results.remove(result);
    }

    public boolean containsResult(Result result) {
        return results.contains(result);
    }

    public Result pickResult() {
        HashMap<String, String> rl = new HashMap<String, String>();
        int weight = 0;
        for (Result r : this.getResults()) {
            int i2 = r.getPercentage();
            for (int i = 0; i < i2; i++) {
                weight++;
                rl.put(weight + "", r.toString());
            }
        }
        int rand = random.nextInt(weight);

        String res = rl.get(rand + "");
        for (Result r : results) {
            String s = r.toString();
            if (s.equals(res)) {
                return r;
            }
        }
        return results.get(0);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((random == null) ? 0 : random.hashCode());
        result = prime * result + ((results == null) ? 0 : results.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ResultList other = (ResultList) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (random == null) {
            if (other.random != null)
                return false;
        } else if (!random.equals(other.random))
            return false;
        if (results == null) {
            if (other.results != null)
                return false;
        } else if (!results.equals(other.results))
            return false;
        return true;
    }

}
