package com.example.busrouteservice.controller;

import com.example.busrouteservice.service.RouteService;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RouteController {

  private final RouteService routeService;

  public RouteController(RouteService routeService) {
    this.routeService = routeService;
    routeService.loadRoutes();
  }

  @GetMapping("/api/direct")
  public Map<String, Object> checkDirectRoute(@RequestParam int from, @RequestParam int to) {
    boolean direct = routeService.hasDirectRoute(from, to);
    Map<String, Object> response = new LinkedHashMap<>();
    response.put("from", from);
    response.put("to", to);
    response.put("direct", direct);
    return response;
  }
}
