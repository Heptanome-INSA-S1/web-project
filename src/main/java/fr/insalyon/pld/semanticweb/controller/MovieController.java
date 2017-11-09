package fr.insalyon.pld.semanticweb.controller;

import fr.insalyon.pld.semanticweb.model.persistence.MovieModel;
import fr.insalyon.pld.semanticweb.repositories.entities.Movie;
import fr.insalyon.pld.semanticweb.repositories.mapper.MovieMapper;
import fr.insalyon.pld.semanticweb.repositories.services.MovieRepository;
import fr.insalyon.pld.semanticweb.repositories.SPARQLRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ResourceNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@Controller
public class MovieController extends AbstractController {

  @Autowired
  MovieRepository movieRepository;
  @Autowired
  MovieMapper movieMapper;

  @RequestMapping(value = "/movies/unique", produces = "application/json")
  public @ResponseBody
  MovieModel getMovie(@RequestParam String uuid) {
    return fetchUniqueFromRepository(uuid, movieRepository, movieMapper);
  }

  @RequestMapping("/movies")
  public @ResponseBody
  List<MovieModel> index(
      @RequestParam(value = "name", required = false) String query,
      @RequestParam(value = "short", required = false, defaultValue = "true") String isShort
  ) {
    return fetchFromRepository(query, isShort, movieRepository, movieMapper);
  }

}
