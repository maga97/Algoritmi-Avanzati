package it.unipd.advancedalgorithms.graph;

import java.nio.file.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.HashMap;

public class GraphReader {
  public static Graph getGraph(String path) {
    try {
      Iterator<String> iterator = Files.lines(Paths.get(path)).iterator();
      String[] dimension = iterator.next().split(" ");
      List<Edge> edgeList = new ArrayList<Edge>(Integer.parseInt(dimension[1]));
      HashMap<Integer, LinkedList<Edge>> map = new HashMap<>();
      HashMap<Pair<Integer, Integer>, Integer> mappa = new HashMap<>();
      while (iterator.hasNext()) {
        String[] info = iterator.next().split(" ");
        int src = Integer.parseInt(info[0]);
        int dest = Integer.parseInt(info[1]);
        int weight = Integer.parseInt(info[2]);
        Pair<Integer,Integer> key = new Pair<Integer,Integer>(src, dest);
        if(mappa.putIfAbsent(key, weight) != null) {
          Integer currentWeight = mappa.get(key);
          if(currentWeight > weight) {
            mappa.put(key, weight);
          }
        }
      }
      for (Pair<Integer, Integer> p : mappa.keySet()) {
        LinkedList<Edge> listaSrc = map.getOrDefault(p.getValue0(), new LinkedList<Edge>());
        listaSrc.add(new Edge(p.getValue0(), p.getValue1(), mappa.get(p)));
        map.put(p.getValue0(), listaSrc);
        LinkedList<Edge> listaDest = map.getOrDefault(p.getValue1(), new LinkedList<Edge>());
        listaDest.add(new Edge(p.getValue1(), p.getValue0(), mappa.get(p)));
        map.put(p.getValue1(), listaDest);
        edgeList.add(new Edge(p.getValue0(), p.getValue1(), mappa.get(p)));
      }

      return new Graph(edgeList, map);

    } catch (Exception e) {
      e.printStackTrace();
    }

    return new Graph();
  }
}