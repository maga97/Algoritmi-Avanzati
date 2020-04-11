package it.unipd.advancedalgorithms;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.opencsv.CSVWriter;

import it.unipd.advancedalgorithms.graph.*;
import it.unipd.advancedalgorithms.algorithms.*;

public class Prova {
  public static void main(final String[] args) throws Exception {
    final List<String[]> kruskalUnionFindTimes = new ArrayList<String[]>();
    final List<String[]> kruskalTimes = new ArrayList<>();
    final List<String[]> primTimes = new ArrayList<>();
    try (Stream<Path> paths = Files.walk(Paths.get("datasets"))) {
      paths.filter(Files::isRegularFile).forEach(file -> {
        String f = file.getFileName().toString();
        final Graph g = GraphReader.getGraph("datasets/" + f);
        Integer numberVertex = g.getnVertex();

        // Benchmark Prim
        Long time = System.nanoTime();
        int prim = Prim.solve(g, 1);
        Long temp = (System.nanoTime() - time) / 1000000;
        primTimes.add(new String[] { f, numberVertex.toString(), temp.toString() });

        // Benchmark Kruskal
        time = System.nanoTime();
        int kruskal = Kruskal.MST(g);
        temp = (System.nanoTime() - time) / 1000000;
        kruskalTimes.add(new String[] { f, numberVertex.toString(), temp.toString() });

        // Benchmark Kruskal with UnionFind
        time = System.nanoTime();
        int kruskalUF = KruskalUnionFind.KruskalMST(g);
        temp = (System.nanoTime() - time) / 1000000;
        kruskalUnionFindTimes.add(new String[] { f, numberVertex.toString(), temp.toString() });
        Path path = Paths.get("DatasetsOutput/" + file.getFileName().toString().replace("input", "output"));
        int outputres = 0;
        try {
          outputres = Integer.parseInt(Files.lines(path).iterator().next());// print each line
        } catch (IOException ex) {
          ex.printStackTrace();// handle exception here
        }
        System.out.println("prim output: " + prim);
        System.out.println("kruskal output: " + kruskal);
        System.out.println("kruskal union find output: " + kruskalUF);
        System.out.println("real output: " + outputres);
        System.out.println("");

      });
    } catch (final Exception e) {
    }

    printFile("kruskal.csv", kruskalTimes);
    printFile("prim.csv", primTimes);
    printFile("unionkruskal.csv", kruskalUnionFindTimes);
  }

  private static void printFile(final String filename, final List<String[]> entries) throws IOException {
    Files.deleteIfExists(Paths.get(filename));
    try (FileOutputStream fos = new FileOutputStream(filename);
        OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
        CSVWriter writer = new CSVWriter(osw, CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER,
            CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END)) {
      writer.writeAll(entries);
    }
  }
}