package com.example.busrouteservice.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RouteService {

  private static final Logger logger = LoggerFactory.getLogger(RouteService.class);

  private final Map<Integer, Set<Integer>> routes = new HashMap<>();

  @Value("${routes.file.path:src/main/resources/routes.txt}")
  private String filePath;

  public void loadRoutes() {
    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
      String line;
      while ((line = reader.readLine()) != null) {
        String[] parts = line.split(" ");
        int routeId = Integer.parseInt(parts[0]);
        Set<Integer> stops = new HashSet<>();
        for (int i = 1; i < parts.length; i++) {
          stops.add(Integer.parseInt(parts[i]));
        }
        routes.put(routeId, stops);
      }
      logger.info("Routes loaded successfully from {}", filePath);
    } catch (IOException e) {
      logger.error("Error loading routes from file: {}", filePath, e);
    }
  }

  public boolean hasDirectRoute(int from, int to) {
    for (Set<Integer> stops : routes.values()) {
      if (stops.contains(from) && stops.contains(to)) {
        return getIndex(stops, from) < getIndex(stops, to);
      }
    }
    return false;
  }

  private int getIndex(Set<Integer> stops, int stop) {
    int index = 0;
    for (Integer s : stops) {
      if (s.equals(stop)) {
        return index;
      }
      index++;
    }
    return -1;
  }
}
